package com.horizonteeventos.dao.impl;

import com.horizonteeventos.config.Conexion;
import com.horizonteeventos.dao.CoordinacionDAO;
import com.horizonteeventos.modelo.EventoCoordinacion;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/** Consultas JDBC de solo lectura para Coordinacion. */
public class CoordinacionDAOImpl implements CoordinacionDAO {
    private static final String CONSULTA_BASE =
            "SELECT e.id_evento, e.fecha_evento, e.hora_inicio, e.hora_fin, "
            + "CONCAT(c.nombre, ' ', c.apellido) AS cliente, x.nombre_espacio, "
            + "s.nombre_estado, e.cantidad_invitados, e.asistencia_real "
            + "FROM eventos e JOIN clientes c ON c.id_cliente = e.id_cliente "
            + "JOIN espacios x ON x.id_espacio = e.id_espacio "
            + "JOIN estado_del_evento s ON s.id_estado = e.id_estado ";

    @Override
    public List<EventoCoordinacion> listarEventos() throws SQLException {
        String sql = CONSULTA_BASE + "ORDER BY e.fecha_evento, e.hora_inicio";
        List<EventoCoordinacion> eventos = new ArrayList<>();
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) eventos.add(mapear(resultado));
        }
        return eventos;
    }

    @Override
    public List<EventoCoordinacion> listarEventosPorFecha(LocalDate fecha) throws SQLException {
        String sql = CONSULTA_BASE + "WHERE e.fecha_evento = ? ORDER BY e.hora_inicio";
        List<EventoCoordinacion> eventos = new ArrayList<>();
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setDate(1, Date.valueOf(fecha));
            try (ResultSet resultado = consulta.executeQuery()) {
                while (resultado.next()) eventos.add(mapear(resultado));
            }
        }
        return eventos;
    }

    @Override
    public List<EventoCoordinacion> listarEventosConfirmados() throws SQLException {
        String sql = CONSULTA_BASE + "WHERE s.nombre_estado IN ('Aceptado', 'Señado', 'Pagado', 'Confirmado') "
                + "ORDER BY e.fecha_evento, e.hora_inicio";
        List<EventoCoordinacion> eventos = new ArrayList<>();
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) eventos.add(mapear(resultado));
        }
        return eventos;
    }

    @Override
    public List<Object[]> listarPersonalAsignado() throws SQLException {
        String sql = "SELECT * FROM ("
                + "SELECT a.id_evento, ev.fecha_evento, CONCAT(em.nombre, ' ', em.apellido) AS empleado, "
                + "a.funcion, a.tarea, a.hora_inicio_asignada, a.hora_fin_asignada, a.estado "
                + "FROM asignaciones_personal a JOIN eventos ev ON ev.id_evento = a.id_evento "
                + "JOIN empleados em ON em.id_empleado = a.id_empleado "
                + "UNION ALL "
                + "SELECT ee.id_evento, ev.fecha_evento, CONCAT(em.nombre, ' ', em.apellido) AS empleado, "
                + "COALESCE(ee.rol_en_evento, 'Personal') AS funcion, "
                + "CONCAT('Turno general: ', COALESCE(ee.rol_en_evento, 'Personal')) AS tarea, "
                + "ev.hora_inicio AS hora_inicio_asignada, ev.hora_fin AS hora_fin_asignada, "
                + "'Pendiente de detalle' AS estado "
                + "FROM evento_empleados ee JOIN eventos ev ON ev.id_evento = ee.id_evento "
                + "JOIN empleados em ON em.id_empleado = ee.id_empleado "
                + "WHERE NOT EXISTS (SELECT 1 FROM asignaciones_personal a "
                + "WHERE a.id_evento = ee.id_evento AND a.id_empleado = ee.id_empleado)"
                + ") recursos ORDER BY fecha_evento, id_evento, empleado";
        return consultarFilas(sql);
    }

    @Override
    public List<Object[]> listarServiciosContratados() throws SQLException {
        String sql = "SELECT es.id_evento, ev.fecha_evento, s.nombre_servicio, "
                + "COALESCE(t.razon_social, 'Servicio propio') AS proveedor, es.cantidad "
                + "FROM evento_servicios es JOIN eventos ev ON ev.id_evento = es.id_evento "
                + "JOIN servicios s ON s.id_servicio = es.id_servicio "
                + "LEFT JOIN terceros t ON t.id_tercero = s.id_tercero "
                + "ORDER BY ev.fecha_evento, es.id_evento, s.nombre_servicio";
        return consultarFilas(sql);
    }

    @Override
    public List<Object[]> listarModificaciones() throws SQLException {
        return consultarFilas("SELECT id_modificacion, id_evento, fecha_cambio, descripcion_cambio "
                + "FROM historial_modificaciones ORDER BY fecha_cambio DESC");
    }

    @Override
    public List<Object[]> listarIncidencias() throws SQLException {
        return consultarFilas("SELECT id_incidencia, id_evento, fecha_reporte, descripcion, reportado_por "
                + "FROM incidencias ORDER BY fecha_reporte DESC");
    }

    @Override
    public boolean registrarIncidencia(int idEvento, String descripcion, String reportadoPor) throws SQLException {
        String sql = "INSERT INTO incidencias (id_evento, descripcion, costo_estimado, reportado_por) "
                + "VALUES (?, ?, 0, ?)";
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setInt(1, idEvento);
            consulta.setString(2, descripcion.trim());
            consulta.setString(3, reportadoPor);
            return consulta.executeUpdate() == 1;
        }
    }

    private static List<Object[]> consultarFilas(String sql) throws SQLException {
        List<Object[]> filas = new ArrayList<>();
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {
            int columnas = resultado.getMetaData().getColumnCount();
            while (resultado.next()) {
                Object[] fila = new Object[columnas];
                for (int i = 0; i < columnas; i++) fila[i] = resultado.getObject(i + 1);
                filas.add(fila);
            }
        }
        return filas;
    }

    private static EventoCoordinacion mapear(ResultSet resultado) throws SQLException {
        int asistencia = resultado.getInt("asistencia_real");
        Integer asistenciaReal = resultado.wasNull() ? null : asistencia;
        return new EventoCoordinacion(resultado.getInt("id_evento"),
                resultado.getDate("fecha_evento").toLocalDate(),
                resultado.getTime("hora_inicio").toLocalTime(),
                resultado.getTime("hora_fin").toLocalTime(),
                resultado.getString("cliente"), resultado.getString("nombre_espacio"),
                resultado.getString("nombre_estado"), resultado.getInt("cantidad_invitados"), asistenciaReal);
    }
}

package com.horizonteeventos.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.horizonteeventos.config.Conexion;
import com.horizonteeventos.dao.PersonalDAO;
import com.horizonteeventos.modelo.AsignacionPersonal;

/** Acceso JDBC limitado a los turnos del empleado autenticado. */
public class PersonalDAOImpl implements PersonalDAO {
    @Override
    public List<Object[]> listarIncidenciasAsignadas(int idEmpleado) throws SQLException {
        String sql = "SELECT DISTINCT i.id_incidencia, i.id_evento, i.fecha_reporte, "
                + "i.descripcion, i.reportado_por FROM incidencias i "
                + "WHERE EXISTS (SELECT 1 FROM asignaciones_personal a "
                + "WHERE a.id_evento = i.id_evento AND a.id_empleado = ?) "
                + "OR EXISTS (SELECT 1 FROM evento_empleados ee "
                + "WHERE ee.id_evento = i.id_evento AND ee.id_empleado = ?) "
                + "ORDER BY i.fecha_reporte DESC";
        List<Object[]> incidencias = new ArrayList<>();
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setInt(1, idEmpleado);
            consulta.setInt(2, idEmpleado);
            try (ResultSet resultado = consulta.executeQuery()) {
                while (resultado.next()) {
                    incidencias.add(new Object[] {resultado.getInt("id_incidencia"),
                            resultado.getInt("id_evento"), resultado.getTimestamp("fecha_reporte"),
                            resultado.getString("descripcion"), resultado.getString("reportado_por")});
                }
            }
        }
        return incidencias;
    }

    @Override
    public boolean registrarIncidencia(int idEvento, int idEmpleado, String descripcion,
            String reportadoPor) throws SQLException {
        String sql = "INSERT INTO incidencias (id_evento, descripcion, costo_estimado, reportado_por) "
                + "SELECT ?, ?, 0, ? WHERE EXISTS (SELECT 1 FROM asignaciones_personal "
                + "WHERE id_evento = ? AND id_empleado = ?) OR EXISTS "
                + "(SELECT 1 FROM evento_empleados WHERE id_evento = ? AND id_empleado = ?)";
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setInt(1, idEvento);
            consulta.setString(2, descripcion.trim());
            consulta.setString(3, reportadoPor);
            consulta.setInt(4, idEvento);
            consulta.setInt(5, idEmpleado);
            consulta.setInt(6, idEvento);
            consulta.setInt(7, idEmpleado);
            return consulta.executeUpdate() == 1;
        }
    }

    @Override
    public List<AsignacionPersonal> listarAsignaciones(int idEmpleado) throws SQLException {
        String sql = "SELECT * FROM ("
                + "SELECT a.id_asignacion, e.id_evento, e.fecha_evento, a.hora_inicio_asignada, "
                + "a.hora_fin_asignada, a.funcion, a.tarea, a.hora_inicio_real, a.hora_fin_real, "
                + "a.estado, a.observaciones FROM asignaciones_personal a "
                + "JOIN eventos e ON e.id_evento = a.id_evento WHERE a.id_empleado = ? "
                + "UNION ALL "
                + "SELECT -ee.id_evento AS id_asignacion, e.id_evento, e.fecha_evento, e.hora_inicio, e.hora_fin, "
                + "COALESCE(ee.rol_en_evento, 'Personal') AS funcion, "
                + "CONCAT('Turno general: ', COALESCE(ee.rol_en_evento, 'Personal')) AS tarea, "
                + "NULL AS hora_inicio_real, NULL AS hora_fin_real, 'Pendiente de detalle' AS estado, "
                + "'Coordinación debe detallar la tarea y el turno.' AS observaciones "
                + "FROM evento_empleados ee JOIN eventos e ON e.id_evento = ee.id_evento "
                + "WHERE ee.id_empleado = ? AND NOT EXISTS (SELECT 1 FROM asignaciones_personal a "
                + "WHERE a.id_empleado = ee.id_empleado AND a.id_evento = ee.id_evento)"
                + ") asignaciones ORDER BY fecha_evento, hora_inicio_asignada";
        List<AsignacionPersonal> asignaciones = new ArrayList<>();
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setInt(1, idEmpleado);
            consulta.setInt(2, idEmpleado);
            try (ResultSet resultado = consulta.executeQuery()) {
                while (resultado.next()) {
                    Time inicioReal = resultado.getTime("hora_inicio_real");
                    Time finReal = resultado.getTime("hora_fin_real");
                    asignaciones.add(new AsignacionPersonal(
                            resultado.getInt("id_asignacion"), resultado.getInt("id_evento"),
                            resultado.getDate("fecha_evento").toLocalDate(),
                            resultado.getTime("hora_inicio_asignada").toLocalTime(),
                            resultado.getTime("hora_fin_asignada").toLocalTime(),
                            resultado.getString("funcion"), resultado.getString("tarea"),
                            inicioReal == null ? null : inicioReal.toLocalTime(),
                            finReal == null ? null : finReal.toLocalTime(),
                            resultado.getString("estado"), resultado.getString("observaciones")));
                }
            }
        }
        return asignaciones;
    }

    @Override
    public boolean registrarTrabajo(int idAsignacion, int idEmpleado, java.time.LocalTime inicioReal,
            java.time.LocalTime finReal, String observaciones) throws SQLException {
        if (idAsignacion < 0) return false;
        String sql = "UPDATE asignaciones_personal SET hora_inicio_real = ?, hora_fin_real = ?, "
                + "observaciones = ?, estado = 'Completada' "
                + "WHERE id_asignacion = ? AND id_empleado = ?";
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            if (inicioReal == null) {
				consulta.setNull(1, Types.TIME);
			} else {
				consulta.setTime(1, Time.valueOf(inicioReal));
			}
            if (finReal == null) {
				consulta.setNull(2, Types.TIME);
			} else {
				consulta.setTime(2, Time.valueOf(finReal));
			}
            consulta.setString(3, observaciones);
            consulta.setInt(4, idAsignacion);
            consulta.setInt(5, idEmpleado);
            return consulta.executeUpdate() > 0;
        }
    }
}

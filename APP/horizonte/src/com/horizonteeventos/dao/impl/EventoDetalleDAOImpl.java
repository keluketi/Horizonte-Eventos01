package com.horizonteeventos.dao.impl;

import com.horizonteeventos.config.Conexion;
import com.horizonteeventos.dao.EventoDetalleDAO;
import com.horizonteeventos.modelo.DetalleEvento;
import com.horizonteeventos.modelo.ServicioEventoDetalle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/** Implementa la consulta detallada con PreparedStatement. */
public class EventoDetalleDAOImpl implements EventoDetalleDAO {
    @Override
    public Optional<DetalleEvento> buscarPorId(int idEvento) throws SQLException {
        String consultaEvento = "SELECT e.id_evento, COALESCE(t.nombre, 'Sin tipo') AS tipo, "
                + "CONCAT(c.nombre, ' ', c.apellido) AS cliente, c.dni_cuit, c.email, c.telefono, "
                + "s.nombre_espacio, ee.nombre_estado, e.fecha_evento, e.hora_inicio, e.hora_fin, "
                + "e.cantidad_invitados, e.asistencia_real, p.nombre_paquete, p.descripcion AS descripcion_paquete, "
                + "p.precio_fijo, e.observaciones "
                + "FROM eventos e JOIN clientes c ON c.id_cliente = e.id_cliente "
                + "JOIN espacios s ON s.id_espacio = e.id_espacio "
                + "JOIN estado_del_evento ee ON ee.id_estado = e.id_estado "
                + "LEFT JOIN tipos_evento t ON t.id_tipo_evento = e.id_tipo_evento "
                + "LEFT JOIN paquetes p ON p.id_paquete = e.id_paquete WHERE e.id_evento = ?";
        String consultaServicios = "SELECT s.nombre_servicio, COALESCE(t.razon_social, 'Servicio propio') AS proveedor, "
                + "es.cantidad, es.precio_acordado FROM evento_servicios es "
                + "JOIN servicios s ON s.id_servicio = es.id_servicio "
                + "LEFT JOIN terceros t ON t.id_tercero = s.id_tercero "
                + "WHERE es.id_evento = ? ORDER BY s.nombre_servicio";

        try (Connection conexion = Conexion.getConnection()) {
            DetalleEvento detalle;
            try (PreparedStatement psEvento = conexion.prepareStatement(consultaEvento)) {
                psEvento.setInt(1, idEvento);
                try (ResultSet rs = psEvento.executeQuery()) {
                    if (!rs.next()) return Optional.empty();
                    detalle = new DetalleEvento();
                detalle.setId(rs.getInt("id_evento"));
                detalle.setTipo(rs.getString("tipo"));
                detalle.setCliente(rs.getString("cliente"));
                detalle.setDocumentoCliente(rs.getString("dni_cuit"));
                detalle.setEmailCliente(rs.getString("email"));
                detalle.setTelefonoCliente(rs.getString("telefono"));
                detalle.setEspacio(rs.getString("nombre_espacio"));
                detalle.setEstado(rs.getString("nombre_estado"));
                detalle.setFecha(rs.getDate("fecha_evento").toLocalDate());
                detalle.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                detalle.setHoraFin(rs.getTime("hora_fin").toLocalTime());
                detalle.setInvitados(rs.getInt("cantidad_invitados"));
                int asistencia = rs.getInt("asistencia_real");
                detalle.setAsistenciaReal(rs.wasNull() ? null : asistencia);
                detalle.setPaquete(rs.getString("nombre_paquete"));
                detalle.setDescripcionPaquete(rs.getString("descripcion_paquete"));
                detalle.setPrecioPaquete(rs.getBigDecimal("precio_fijo"));
                detalle.setObservaciones(rs.getString("observaciones"));
                }
            }
            try (PreparedStatement psServicios = conexion.prepareStatement(consultaServicios)) {
                psServicios.setInt(1, idEvento);
                try (ResultSet servicios = psServicios.executeQuery()) {
                    while (servicios.next()) {
                        detalle.getServicios().add(new ServicioEventoDetalle(
                                servicios.getString("nombre_servicio"),
                                servicios.getString("proveedor"),
                                servicios.getInt("cantidad"),
                                servicios.getBigDecimal("precio_acordado")));
                    }
                }
            }
            return Optional.of(detalle);
        }
    }
}

package com.horizonteeventos.dao.impl;

import com.horizonteeventos.config.Conexion;
import com.horizonteeventos.dao.FacturacionSimulacionDAO;
import com.horizonteeventos.modelo.DatosFacturacionEvento;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/** Consultas de solo lectura para la simulación financiera de un evento. */
public class FacturacionSimulacionDAOImpl implements FacturacionSimulacionDAO {
    @Override
    public DatosFacturacionEvento cargarPorEvento(int idEvento) throws SQLException {
        DatosFacturacionEvento datos = new DatosFacturacionEvento();
        String sqlPresupuestos = "SELECT fecha_emision, monto_total, monto_sena, estado_aprobacion "
                + "FROM presupuestos WHERE id_evento = ? "
                + "ORDER BY CASE WHEN estado_aprobacion = 'Aceptado' THEN 0 ELSE 1 END, "
                + "fecha_emision DESC, id_presupuesto DESC";
        String sqlPagos = "SELECT p.id_pago, p.fecha_pago, p.monto, p.medio_pago, "
                + "ee.nombre_estado AS estado_evento, p.observaciones FROM pagos p "
                + "JOIN eventos e ON e.id_evento = p.id_evento "
                + "JOIN estado_del_evento ee ON ee.id_estado = e.id_estado "
                + "WHERE p.id_evento = ? ORDER BY p.fecha_pago, p.id_pago";
        String sqlComprobantes = "SELECT nro_factura, tipo_comprobante, monto_abonado, fecha_emision "
                + "FROM facturas WHERE id_evento = ? ORDER BY fecha_emision, id_factura";

        try (Connection conexion = Conexion.getConnection()) {
            try (PreparedStatement consulta = conexion.prepareStatement(sqlPresupuestos)) {
                consulta.setInt(1, idEvento);
                try (ResultSet resultado = consulta.executeQuery()) {
                    boolean primerRegistro = true;
                    while (resultado.next()) {
                        String estado = resultado.getString("estado_aprobacion");
                        Date fecha = resultado.getDate("fecha_emision");
                        if (primerRegistro) {
                            datos.setEstadoPresupuesto(estado);
                            datos.setFechaPresupuesto(fecha == null ? null : fecha.toLocalDate());
                            primerRegistro = false;
                        }
                        if ("Aceptado".equalsIgnoreCase(estado) && datos.getPresupuestoAprobado() == null) {
                            datos.setPresupuestoAprobado(resultado.getBigDecimal("monto_total"));
                            datos.setMontoSena(resultado.getBigDecimal("monto_sena"));
                            datos.setFechaPresupuesto(fecha == null ? null : fecha.toLocalDate());
                        }
                    }
                }
            }
            try (PreparedStatement consulta = conexion.prepareStatement(sqlPagos)) {
                consulta.setInt(1, idEvento);
                try (ResultSet resultado = consulta.executeQuery()) {
                    while (resultado.next()) {
                        Timestamp fecha = resultado.getTimestamp("fecha_pago");
                        datos.getPagos().add(new DatosFacturacionEvento.Pago(
                                resultado.getInt("id_pago"), fecha == null ? null : fecha.toLocalDateTime(),
                                resultado.getBigDecimal("monto"), resultado.getString("medio_pago"),
                                resultado.getString("estado_evento"), resultado.getString("observaciones")));
                    }
                }
            }
            try (PreparedStatement consulta = conexion.prepareStatement(sqlComprobantes)) {
                consulta.setInt(1, idEvento);
                try (ResultSet resultado = consulta.executeQuery()) {
                    while (resultado.next()) {
                        Timestamp fecha = resultado.getTimestamp("fecha_emision");
                        datos.getComprobantes().add(new DatosFacturacionEvento.Comprobante(
                                resultado.getString("nro_factura"), resultado.getString("tipo_comprobante"),
                                resultado.getBigDecimal("monto_abonado"),
                                fecha == null ? null : fecha.toLocalDateTime()));
                    }
                }
            }
        }
        return datos;
    }
}

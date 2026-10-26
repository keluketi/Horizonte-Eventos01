package com.horizonteeventos.dao.impl;

import com.horizonteeventos.config.Conexion;
import com.horizonteeventos.dao.PagoDAO;
import com.horizonteeventos.modelo.Pago;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/** Persistencia transaccional de pagos simulados. */
public class PagoDAOImpl implements PagoDAO {
    @Override
    public BigDecimal calcularTotalPagado(int idEvento) throws SQLException {
        String sql = "SELECT COALESCE(SUM(monto), 0) FROM pagos WHERE id_evento = ?";
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setInt(1, idEvento);
            try (ResultSet resultado = consulta.executeQuery()) {
                resultado.next();
                return resultado.getBigDecimal(1);
            }
        }
    }

    @Override
    public List<Pago> listarPorEvento(int idEvento) throws SQLException {
        String sql = "SELECT id_pago, id_evento, monto, medio_pago, fecha_pago, referencia, observaciones "
                   + "FROM pagos WHERE id_evento = ? ORDER BY fecha_pago, id_pago";
        List<Pago> pagos = new ArrayList<>();
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setInt(1, idEvento);
            try (ResultSet resultado = consulta.executeQuery()) {
                while (resultado.next()) {
                    Pago pago = new Pago();
                    pago.setIdPago(resultado.getInt("id_pago"));
                    pago.setIdEvento(resultado.getInt("id_evento"));
                    pago.setMonto(resultado.getBigDecimal("monto"));
                    pago.setMedioPago(resultado.getString("medio_pago"));
                    Timestamp fecha = resultado.getTimestamp("fecha_pago");
                    if (fecha != null) pago.setFechaPago(fecha.toLocalDateTime());
                    pago.setReferencia(resultado.getString("referencia"));
                    pago.setObservaciones(resultado.getString("observaciones"));
                    pagos.add(pago);
                }
            }
        }
        return pagos;
    }

    @Override
    public void registrarYActualizarEstado(Pago pago, int idCliente) throws SQLException {
        if (pago.getMonto() == null || pago.getMonto().signum() <= 0) {
            throw new SQLException("El importe debe ser mayor que cero.");
        }
        try (Connection conexion = Conexion.getConnection()) {
            boolean autocommitAnterior = conexion.getAutoCommit();
            conexion.setAutoCommit(false);
            try {
                String estadoActual;
                BigDecimal total;
                BigDecimal sena;
                String consultaEvento = "SELECT ee.nombre_estado, p.monto_total, p.monto_sena "
                        + "FROM eventos e JOIN estado_del_evento ee ON ee.id_estado = e.id_estado "
                        + "JOIN presupuestos p ON p.id_presupuesto = (SELECT MAX(p2.id_presupuesto) "
                        + "FROM presupuestos p2 WHERE p2.id_evento = e.id_evento "
                        + "AND p2.estado_aprobacion = 'Aceptado') "
                        + "WHERE e.id_evento = ? AND e.id_cliente = ? FOR UPDATE";
                try (PreparedStatement consulta = conexion.prepareStatement(consultaEvento)) {
                    consulta.setInt(1, pago.getIdEvento());
                    consulta.setInt(2, idCliente);
                    try (ResultSet resultado = consulta.executeQuery()) {
                        if (!resultado.next()) {
                            throw new SQLException("El evento no pertenece a este cliente o no tiene presupuesto aceptado.");
                        }
                        estadoActual = resultado.getString("nombre_estado");
                        total = resultado.getBigDecimal("monto_total");
                        sena = resultado.getBigDecimal("monto_sena");
                    }
                }
                boolean esSena = "Seña".equalsIgnoreCase(pago.getTipoPago());
                boolean esPagoFinal = "Pago final".equalsIgnoreCase(pago.getTipoPago());
                if (!esSena && !esPagoFinal) throw new SQLException("Tipo de pago no reconocido.");
                boolean estadoAceptado = "Aceptado".equalsIgnoreCase(estadoActual)
                        || "Confirmado".equalsIgnoreCase(estadoActual);
                boolean estadoSenado = "Señado".equalsIgnoreCase(estadoActual);
                if ((esSena && !estadoAceptado) || (esPagoFinal && !estadoSenado)) {
                    throw new SQLException("El tipo de pago no corresponde al estado actual del evento.");
                }
                if (total == null || sena == null || sena.signum() <= 0 || sena.compareTo(total) > 0) {
                    throw new SQLException("El presupuesto no tiene una seña válida configurada.");
                }
                BigDecimal pagado;
                try (PreparedStatement consulta = conexion.prepareStatement(
                        "SELECT COALESCE(SUM(monto), 0) FROM pagos WHERE id_evento = ?")) {
                    consulta.setInt(1, pago.getIdEvento());
                    try (ResultSet resultado = consulta.executeQuery()) {
                        resultado.next();
                        pagado = resultado.getBigDecimal(1);
                    }
                }
                BigDecimal importeEsperado = esSena ? sena.subtract(pagado) : total.subtract(pagado);
                if (importeEsperado.signum() <= 0 || pago.getMonto().compareTo(importeEsperado) != 0) {
                    throw new SQLException("El importe cambió o no coincide con el saldo pendiente. Actualiza el historial e intenta de nuevo.");
                }
                BigDecimal acumulado = pagado.add(pago.getMonto());
                String nuevoEstado = acumulado.compareTo(total) >= 0 ? "Pagado"
                        : acumulado.compareTo(sena) >= 0 ? "Señado" : "Aceptado";
                int idNuevoEstado = obtenerIdEstado(conexion, nuevoEstado);

                String insertar = "INSERT INTO pagos (id_evento, monto, medio_pago, referencia, observaciones) "
                        + "VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement consulta = conexion.prepareStatement(insertar)) {
                    consulta.setInt(1, pago.getIdEvento());
                    consulta.setBigDecimal(2, pago.getMonto());
                    consulta.setString(3, pago.getMedioPago());
                    consulta.setString(4, pago.getReferencia());
                    consulta.setString(5, pago.getObservaciones());
                    consulta.executeUpdate();
                }
                try (PreparedStatement actualizacion = conexion.prepareStatement(
                        "UPDATE eventos SET id_estado = ? WHERE id_evento = ? AND id_cliente = ?")) {
                    actualizacion.setInt(1, idNuevoEstado);
                    actualizacion.setInt(2, pago.getIdEvento());
                    actualizacion.setInt(3, idCliente);
                    if (actualizacion.executeUpdate() != 1) {
                        throw new SQLException("No se pudo actualizar el estado del evento.");
                    }
                }
                conexion.commit();
            } catch (SQLException error) {
                conexion.rollback();
                throw error;
            } finally {
                conexion.setAutoCommit(autocommitAnterior);
            }
        }
    }

    private int obtenerIdEstado(Connection conexion, String nombre) throws SQLException {
        String sql = "SELECT id_estado FROM estado_del_evento WHERE LOWER(nombre_estado) = LOWER(?)";
        try (PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, nombre);
            try (ResultSet resultado = consulta.executeQuery()) {
                if (!resultado.next()) throw new SQLException("No existe el estado '" + nombre + "'. Ejecuta database/06_flujo_presupuestos_pagos.sql.");
                return resultado.getInt("id_estado");
            }
        }
    }
}
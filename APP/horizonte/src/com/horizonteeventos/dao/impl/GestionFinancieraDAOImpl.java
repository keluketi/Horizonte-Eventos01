package com.horizonteeventos.dao.impl;

import com.horizonteeventos.config.Conexion;
import com.horizonteeventos.dao.GestionFinancieraDAO;
import com.horizonteeventos.modelo.EventoConSaldo;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Persistencia transaccional de aprobación de presupuestos y pagos. */
public class GestionFinancieraDAOImpl implements GestionFinancieraDAO {
    @Override
    public boolean aprobarPresupuesto(int idPresupuesto, BigDecimal montoSena) throws SQLException {
        if (montoSena == null || montoSena.signum() <= 0) {
            throw new SQLException("La seña debe ser mayor que cero.");
        }
        try (Connection conexion = Conexion.getConnection()) {
            boolean autoCommitAnterior = conexion.getAutoCommit();
            conexion.setAutoCommit(false);
            try {
                int idEvento;
                BigDecimal total;
                String estadoPresupuesto;
                String sql = "SELECT id_evento, monto_total, estado_aprobacion FROM presupuestos "
                        + "WHERE id_presupuesto = ? FOR UPDATE";
                try (PreparedStatement consulta = conexion.prepareStatement(sql)) {
                    consulta.setInt(1, idPresupuesto);
                    try (ResultSet resultado = consulta.executeQuery()) {
                        if (!resultado.next()) throw new SQLException("No se encontró el presupuesto seleccionado.");
                        idEvento = resultado.getInt("id_evento");
                        total = resultado.getBigDecimal("monto_total");
                        estadoPresupuesto = resultado.getString("estado_aprobacion");
                    }
                }
                if (!"Pendiente".equalsIgnoreCase(estadoPresupuesto)
                        && !"Aceptado".equalsIgnoreCase(estadoPresupuesto)) {
                    throw new SQLException("Solo se puede aprobar un presupuesto pendiente o definir la seña de uno aceptado.");
                }
                if (total == null || montoSena.compareTo(total) > 0) {
                    throw new SQLException("La seña no puede superar el total del presupuesto.");
                }
                String sqlPagos = "SELECT COALESCE(SUM(monto), 0) AS pagado FROM pagos WHERE id_evento = ?";
                try (PreparedStatement consulta = conexion.prepareStatement(sqlPagos)) {
                    consulta.setInt(1, idEvento);
                    try (ResultSet resultado = consulta.executeQuery()) {
                        resultado.next();
                        if (resultado.getBigDecimal("pagado").signum() > 0) {
                            throw new SQLException("No se puede cambiar la seña después de registrar pagos.");
                        }
                    }
                }
                int idAceptado = obtenerIdEstado(conexion, "Aceptado");
                String actualizarPresupuesto = "UPDATE presupuestos SET estado_aprobacion = 'Aceptado', monto_sena = ? "
                        + "WHERE id_presupuesto = ?";
                try (PreparedStatement actualizacion = conexion.prepareStatement(actualizarPresupuesto)) {
                    actualizacion.setBigDecimal(1, montoSena);
                    actualizacion.setInt(2, idPresupuesto);
                    actualizacion.executeUpdate();
                }
                try (PreparedStatement actualizacion = conexion.prepareStatement(
                        "UPDATE eventos SET id_estado = ? WHERE id_evento = ?")) {
                    actualizacion.setInt(1, idAceptado);
                    actualizacion.setInt(2, idEvento);
                    actualizacion.executeUpdate();
                }
                conexion.commit();
                return true;
            } catch (SQLException ex) {
                conexion.rollback();
                throw ex;
            } finally {
                conexion.setAutoCommit(autoCommitAnterior);
            }
        }
    }

    @Override
    public boolean rechazarPresupuesto(int idPresupuesto) throws SQLException {
        String sql = "UPDATE presupuestos SET estado_aprobacion = 'Rechazado' "
                + "WHERE id_presupuesto = ? AND estado_aprobacion = 'Pendiente'";
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setInt(1, idPresupuesto);
            return consulta.executeUpdate() == 1;
        }
    }

    @Override
    public List<EventoConSaldo> listarEventosConSaldo() throws SQLException {
        String sql = "SELECT e.id_evento, CONCAT(c.nombre, ' ', c.apellido) AS cliente, "
                + "s.nombre_estado, p.monto_total, p.monto_sena, COALESCE(SUM(pg.monto), 0) AS pagado "
                + "FROM eventos e JOIN clientes c ON c.id_cliente = e.id_cliente "
                + "JOIN estado_del_evento s ON s.id_estado = e.id_estado "
                + "JOIN presupuestos p ON p.id_presupuesto = (SELECT MAX(p2.id_presupuesto) FROM presupuestos p2 "
                + "WHERE p2.id_evento = e.id_evento AND p2.estado_aprobacion = 'Aceptado') "
                + "LEFT JOIN pagos pg ON pg.id_evento = e.id_evento "
                + "WHERE s.nombre_estado IN ('Aceptado', 'Señado', 'Confirmado') AND p.monto_sena > 0 "
                + "GROUP BY e.id_evento, c.nombre, c.apellido, s.nombre_estado, p.monto_total, p.monto_sena, e.fecha_evento "
                + "HAVING COALESCE(SUM(pg.monto), 0) < p.monto_total ORDER BY e.fecha_evento, e.id_evento";
        List<EventoConSaldo> eventos = new ArrayList<>();
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                eventos.add(new EventoConSaldo(resultado.getInt("id_evento"),
                        resultado.getString("cliente"), resultado.getString("nombre_estado"),
                        resultado.getBigDecimal("monto_total"), resultado.getBigDecimal("monto_sena"),
                        resultado.getBigDecimal("pagado")));
            }
        }
        return eventos;
    }

    @Override
    public boolean registrarPago(int idEvento, BigDecimal monto, String medio,
            String referencia, String observaciones) throws SQLException {
        if (monto == null || monto.signum() <= 0) throw new SQLException("El pago debe ser mayor que cero.");
        try (Connection conexion = Conexion.getConnection()) {
            boolean autoCommitAnterior = conexion.getAutoCommit();
            conexion.setAutoCommit(false);
            try {
                BigDecimal total;
                BigDecimal sena;
                String estadoActual;
                String consultaEvento = "SELECT s.nombre_estado, p.monto_total, p.monto_sena "
                        + "FROM eventos e JOIN estado_del_evento s ON s.id_estado = e.id_estado "
                        + "JOIN presupuestos p ON p.id_presupuesto = (SELECT MAX(p2.id_presupuesto) "
                        + "FROM presupuestos p2 WHERE p2.id_evento = e.id_evento "
                        + "AND p2.estado_aprobacion = 'Aceptado') WHERE e.id_evento = ? FOR UPDATE";
                try (PreparedStatement consulta = conexion.prepareStatement(consultaEvento)) {
                    consulta.setInt(1, idEvento);
                    try (ResultSet resultado = consulta.executeQuery()) {
                        if (!resultado.next()) throw new SQLException("El evento no tiene un presupuesto aprobado.");
                        estadoActual = resultado.getString("nombre_estado");
                        total = resultado.getBigDecimal("monto_total");
                        sena = resultado.getBigDecimal("monto_sena");
                    }
                }
                if (!"Aceptado".equalsIgnoreCase(estadoActual)
                        && !"Señado".equalsIgnoreCase(estadoActual)
                        && !"Confirmado".equalsIgnoreCase(estadoActual)) {
                    throw new SQLException("Solo se pueden registrar pagos para eventos aceptados o señados.");
                }
                if (sena == null || sena.signum() <= 0 || sena.compareTo(total) > 0) {
                    throw new SQLException("Primero se debe definir una seña válida para el presupuesto.");
                }
                BigDecimal pagado;
                try (PreparedStatement consulta = conexion.prepareStatement(
                        "SELECT COALESCE(SUM(monto), 0) AS pagado FROM pagos WHERE id_evento = ?")) {
                    consulta.setInt(1, idEvento);
                    try (ResultSet resultado = consulta.executeQuery()) {
                        resultado.next();
                        pagado = resultado.getBigDecimal("pagado");
                    }
                }
                BigDecimal nuevoAcumulado = pagado.add(monto);
                if (nuevoAcumulado.compareTo(total) > 0) {
                    throw new SQLException("El pago supera el saldo pendiente de $ " + total.subtract(pagado).toPlainString() + ".");
                }
                String insertar = "INSERT INTO pagos (id_evento, monto, medio_pago, referencia, observaciones) "
                        + "VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement consulta = conexion.prepareStatement(insertar)) {
                    consulta.setInt(1, idEvento);
                    consulta.setBigDecimal(2, monto);
                    consulta.setString(3, medio);
                    if (referencia == null || referencia.trim().isEmpty()) consulta.setNull(4, java.sql.Types.VARCHAR);
                    else consulta.setString(4, referencia.trim());
                    if (observaciones == null || observaciones.trim().isEmpty()) consulta.setNull(5, java.sql.Types.VARCHAR);
                    else consulta.setString(5, observaciones.trim());
                    consulta.executeUpdate();
                }
                String nuevoEstado = nuevoAcumulado.compareTo(total) >= 0 ? "Pagado"
                        : nuevoAcumulado.compareTo(sena) >= 0 ? "Señado" : "Aceptado";
                int idNuevoEstado = obtenerIdEstado(conexion, nuevoEstado);
                try (PreparedStatement actualizacion = conexion.prepareStatement(
                        "UPDATE eventos SET id_estado = ? WHERE id_evento = ?")) {
                    actualizacion.setInt(1, idNuevoEstado);
                    actualizacion.setInt(2, idEvento);
                    actualizacion.executeUpdate();
                }
                conexion.commit();
                return true;
            } catch (SQLException ex) {
                conexion.rollback();
                throw ex;
            } finally {
                conexion.setAutoCommit(autoCommitAnterior);
            }
        }
    }

    private int obtenerIdEstado(Connection conexion, String nombre) throws SQLException {
        try (PreparedStatement consulta = conexion.prepareStatement(
                "SELECT id_estado FROM estado_del_evento WHERE LOWER(nombre_estado) = LOWER(?)")) {
            consulta.setString(1, nombre);
            try (ResultSet resultado = consulta.executeQuery()) {
                if (!resultado.next()) throw new SQLException("Falta el estado '" + nombre + "'. Ejecuta la actualización SQL del flujo financiero.");
                return resultado.getInt("id_estado");
            }
        }
    }
}

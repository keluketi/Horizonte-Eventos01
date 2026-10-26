package com.horizonteeventos.dao.impl;

import com.horizonteeventos.config.Conexion;
import com.horizonteeventos.dao.EventoPagoDAO;
import com.horizonteeventos.modelo.EventoPagoResumen;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Lista únicamente eventos con presupuesto aprobado que pertenecen al cliente. */
public class EventoPagoDAOImpl implements EventoPagoDAO {
    private static final String SELECT_HISTORIAL =
            "SELECT e.id_evento, COALESCE(t.nombre, 'Sin tipo') AS tipo, e.fecha_evento, "
          + "s.nombre_espacio, ee.nombre_estado, p.monto_total, p.monto_sena, "
          + "COALESCE(SUM(pg.monto), 0) AS pagado "
          + "FROM eventos e "
          + "JOIN estado_del_evento ee ON ee.id_estado = e.id_estado "
          + "JOIN espacios s ON s.id_espacio = e.id_espacio "
          + "LEFT JOIN tipos_evento t ON t.id_tipo_evento = e.id_tipo_evento "
          + "JOIN presupuestos p ON p.id_presupuesto = ("
          + "SELECT MAX(p2.id_presupuesto) FROM presupuestos p2 "
          + "WHERE p2.id_evento = e.id_evento AND p2.estado_aprobacion = 'Aceptado') "
          + "LEFT JOIN pagos pg ON pg.id_evento = e.id_evento "
          + "WHERE e.id_cliente = ? AND ee.nombre_estado IN "
          + "('Aceptado', 'Señado', 'Pagado', 'Confirmado') "
          + "GROUP BY e.id_evento, t.nombre, e.fecha_evento, s.nombre_espacio, "
          + "ee.nombre_estado, p.monto_total, p.monto_sena "
          + "ORDER BY e.fecha_evento DESC, e.id_evento DESC";

    @Override
    public List<EventoPagoResumen> listarHistorial(int idCliente) throws SQLException {
        List<EventoPagoResumen> eventos = new ArrayList<>();
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(SELECT_HISTORIAL)) {
            consulta.setInt(1, idCliente);
            try (ResultSet resultado = consulta.executeQuery()) {
                while (resultado.next()) {
                    eventos.add(mapear(resultado));
                }
            }
        }
        return eventos;
    }

    @Override
    public Optional<EventoPagoResumen> buscarEvento(int idCliente, int idEvento)
            throws SQLException {
        for (EventoPagoResumen evento : listarHistorial(idCliente)) {
            if (evento.getIdEvento() == idEvento) return Optional.of(evento);
        }
        return Optional.empty();
    }

    private EventoPagoResumen mapear(ResultSet resultado) throws SQLException {
        java.sql.Date fecha = resultado.getDate("fecha_evento");
        return new EventoPagoResumen(resultado.getInt("id_evento"),
                resultado.getString("tipo"), fecha == null ? null : fecha.toLocalDate(),
                resultado.getString("nombre_espacio"), resultado.getString("nombre_estado"),
                resultado.getBigDecimal("monto_total"), resultado.getBigDecimal("monto_sena"),
                resultado.getBigDecimal("pagado"));
    }
}
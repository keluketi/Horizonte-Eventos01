package com.horizonteeventos.dao.impl;

import com.horizonteeventos.config.Conexion;
import com.horizonteeventos.dao.EventoDAO;
import com.horizonteeventos.modelo.Evento;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Implementacion JDBC de EventoDAO. Usa PreparedStatement en todas las consultas. */
public class EventoDAOImpl implements EventoDAO {
    private static final String COLUMNAS =
            "id_evento, id_cliente, id_espacio, id_paquete, id_estado, "
            + "fecha_evento, hora_inicio, hora_fin, cantidad_invitados";

    @Override
    public int insertar(Evento evento) throws SQLException {
        String sql = "INSERT INTO eventos (id_cliente, id_espacio, id_paquete, "
                + "id_estado, fecha_evento, hora_inicio, hora_fin, cantidad_invitados) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(
                     sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            consulta.setInt(1, evento.getIdCliente());
            consulta.setInt(2, evento.getIdEspacio());
            setIdPaquete(consulta, 3, evento.getIdPaquete());
            consulta.setInt(4, evento.getIdEstado());
            consulta.setDate(5, Date.valueOf(evento.getFechaEvento()));
            consulta.setTime(6, Time.valueOf(evento.getHoraInicio()));
            consulta.setTime(7, Time.valueOf(evento.getHoraFin()));
            consulta.setInt(8, evento.getCantidadInvitados());
            consulta.executeUpdate();

            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) {
                    throw new SQLException("La base de datos no devolvio el id del evento.");
                }
                int idGenerado = claves.getInt(1);
                evento.setIdEvento(idGenerado);
                return idGenerado;
            }
        }
    }

    @Override
    public Optional<Evento> buscarPorId(int idEvento) throws SQLException {
        String sql = "SELECT " + COLUMNAS + " FROM eventos WHERE id_evento = ?";
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setInt(1, idEvento);
            try (ResultSet resultado = consulta.executeQuery()) {
                return resultado.next() ? Optional.of(mapear(resultado)) : Optional.empty();
            }
        }
    }

    @Override
    public List<Evento> listarTodos() throws SQLException {
        String sql = "SELECT " + COLUMNAS + " FROM eventos ORDER BY fecha_evento, hora_inicio";
        List<Evento> eventos = new ArrayList<>();
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                eventos.add(mapear(resultado));
            }
        }
        return eventos;
    }

    @Override
    public boolean actualizar(Evento evento) throws SQLException {
        String sql = "UPDATE eventos SET id_cliente = ?, id_espacio = ?, id_paquete = ?, "
                + "id_estado = ?, fecha_evento = ?, hora_inicio = ?, hora_fin = ?, "
                + "cantidad_invitados = ? WHERE id_evento = ?";
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setInt(1, evento.getIdCliente());
            consulta.setInt(2, evento.getIdEspacio());
            setIdPaquete(consulta, 3, evento.getIdPaquete());
            consulta.setInt(4, evento.getIdEstado());
            consulta.setDate(5, Date.valueOf(evento.getFechaEvento()));
            consulta.setTime(6, Time.valueOf(evento.getHoraInicio()));
            consulta.setTime(7, Time.valueOf(evento.getHoraFin()));
            consulta.setInt(8, evento.getCantidadInvitados());
            consulta.setInt(9, evento.getIdEvento());
            return consulta.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int idEvento) throws SQLException {
        String sql = "DELETE FROM eventos WHERE id_evento = ?";
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setInt(1, idEvento);
            return consulta.executeUpdate() > 0;
        }
    }

    @Override
    public List<Evento> obtenerEventosPorFecha(LocalDate fecha) throws SQLException {
        String sql = "SELECT " + COLUMNAS
                + " FROM eventos WHERE fecha_evento = ? ORDER BY hora_inicio";
        List<Evento> eventos = new ArrayList<>();
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setDate(1, Date.valueOf(fecha));
            try (ResultSet resultado = consulta.executeQuery()) {
                while (resultado.next()) {
                    eventos.add(mapear(resultado));
                }
            }
        }
        return eventos;
    }

    private static Evento mapear(ResultSet resultado) throws SQLException {
        int idPaqueteLeido = resultado.getInt("id_paquete");
        Integer idPaquete = resultado.wasNull() ? null : idPaqueteLeido;
        return new Evento(
                resultado.getInt("id_evento"),
                resultado.getInt("id_cliente"),
                resultado.getInt("id_espacio"),
                idPaquete,
                resultado.getInt("id_estado"),
                resultado.getDate("fecha_evento").toLocalDate(),
                resultado.getTime("hora_inicio").toLocalTime(),
                resultado.getTime("hora_fin").toLocalTime(),
                resultado.getInt("cantidad_invitados"));
    }

    private static void setIdPaquete(PreparedStatement consulta, int posicion,
                                     Integer idPaquete) throws SQLException {
        if (idPaquete == null) {
            consulta.setNull(posicion, Types.INTEGER);
        } else {
            consulta.setInt(posicion, idPaquete);
        }
    }
}

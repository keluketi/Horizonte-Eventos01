package com.horizonteeventos.dao.impl;

import com.horizonteeventos.config.Conexion;
import com.horizonteeventos.dao.EventoDAO;
import com.horizonteeventos.modelo.Evento;
import com.horizonteeventos.modelo.ServicioContratado;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;

/** Implementacion JDBC de EventoDAO. Usa PreparedStatement en todas las consultas. */
public class EventoDAOImpl implements EventoDAO {
    private static final String COLUMNAS =
            "id_evento, id_cliente, id_tipo_evento, id_espacio, id_paquete, id_estado, "
            + "fecha_evento, hora_inicio, hora_fin, cantidad_invitados, observaciones";

    @Override
    public int insertar(Evento evento) throws SQLException {
        try (Connection conexion = Conexion.getConnection()) {
            return insertarEvento(conexion, evento);
        }
    }

    @Override
    public int insertarConServicios(Evento evento, List<ServicioContratado> servicios) throws SQLException {
        try (Connection conexion = Conexion.getConnection()) {
            boolean autoCommitAnterior = conexion.getAutoCommit();
            conexion.setAutoCommit(false);
            try {
                int idEvento = insertarEvento(conexion, evento);
                String sql = "INSERT INTO evento_servicios "
                        + "(id_evento, id_servicio, cantidad, precio_acordado) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = conexion.prepareStatement(sql)) {
                    for (ServicioContratado servicio : servicios) {
                        ps.setInt(1, idEvento);
                        ps.setInt(2, servicio.getIdServicio());
                        ps.setInt(3, servicio.getCantidad());
                        ps.setBigDecimal(4, servicio.getPrecioAcordado());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
                insertarPresupuestoPendiente(conexion, evento, servicios);
                conexion.commit();
                return idEvento;
            } catch (SQLException ex) {
                conexion.rollback();
                throw ex;
            } finally {
                conexion.setAutoCommit(autoCommitAnterior);
            }
        }
    }

    /** Cada evento nuevo recibe una propuesta pendiente calculada con el paquete y sus servicios. */
    private void insertarPresupuestoPendiente(Connection conexion, Evento evento,
            List<ServicioContratado> servicios) throws SQLException {
        BigDecimal total = BigDecimal.ZERO;
        if (evento.getIdPaquete() != null) {
            String sqlPaquete = "SELECT precio_fijo FROM paquetes WHERE id_paquete = ?";
            try (PreparedStatement consulta = conexion.prepareStatement(sqlPaquete)) {
                consulta.setInt(1, evento.getIdPaquete());
                try (ResultSet resultado = consulta.executeQuery()) {
                    if (resultado.next()) total = resultado.getBigDecimal("precio_fijo");
                }
            }
        }
        if (servicios != null) {
            for (ServicioContratado servicio : servicios) {
                if (servicio.getPrecioAcordado() != null) {
                    total = total.add(servicio.getPrecioAcordado()
                            .multiply(BigDecimal.valueOf(servicio.getCantidad())));
                }
            }
        }
        String sql = "INSERT INTO presupuestos "
                + "(id_evento, fecha_emision, fecha_vencimiento, monto_total, estado_aprobacion) "
                + "VALUES (?, CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 15 DAY), ?, 'Pendiente')";
        try (PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setInt(1, evento.getIdEvento());
            consulta.setBigDecimal(2, total);
            consulta.executeUpdate();
        }
    }

    private int insertarEvento(Connection conexion, Evento evento) throws SQLException {
        String sql = "INSERT INTO eventos (id_cliente, id_tipo_evento, id_espacio, id_paquete, "
                + "id_estado, fecha_evento, hora_inicio, hora_fin, cantidad_invitados, observaciones) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, evento.getIdCliente());
            setIdTipoEvento(ps, 2, evento.getIdTipoEvento());
            ps.setInt(3, evento.getIdEspacio());
            setIdPaquete(ps, 4, evento.getIdPaquete());
            ps.setInt(5, evento.getIdEstado());
            ps.setDate(6, Date.valueOf(evento.getFechaEvento()));
            ps.setTime(7, Time.valueOf(evento.getHoraInicio()));
            ps.setTime(8, Time.valueOf(evento.getHoraFin()));
            ps.setInt(9, evento.getCantidadInvitados());
            ps.setString(10, evento.getObservaciones());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) throw new SQLException("La base no devolvio el ID del evento.");
                int id = keys.getInt(1);
                evento.setIdEvento(id);
                return id;
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
        String sql = "UPDATE eventos SET id_cliente = ?, id_tipo_evento = ?, id_espacio = ?, id_paquete = ?, "
                + "id_estado = ?, fecha_evento = ?, hora_inicio = ?, hora_fin = ?, "
                + "cantidad_invitados = ?, observaciones = ? WHERE id_evento = ?";
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setInt(1, evento.getIdCliente());
            setIdTipoEvento(consulta, 2, evento.getIdTipoEvento());
            consulta.setInt(3, evento.getIdEspacio());
            setIdPaquete(consulta, 4, evento.getIdPaquete());
            consulta.setInt(5, evento.getIdEstado());
            consulta.setDate(6, Date.valueOf(evento.getFechaEvento()));
            consulta.setTime(7, Time.valueOf(evento.getHoraInicio()));
            consulta.setTime(8, Time.valueOf(evento.getHoraFin()));
            consulta.setInt(9, evento.getCantidadInvitados());
            consulta.setString(10, evento.getObservaciones());
            consulta.setInt(11, evento.getIdEvento());
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

    @Override
    public List<Evento> obtenerEventosEntreFechas(LocalDate desde, LocalDate hasta) throws SQLException {
        String sql = "SELECT " + COLUMNAS + " FROM eventos e WHERE fecha_evento BETWEEN ? AND ? "
                + "AND NOT EXISTS (SELECT 1 FROM estado_del_evento estado "
                + "WHERE estado.id_estado = e.id_estado AND LOWER(estado.nombre_estado) = 'cancelado') "
                + "ORDER BY fecha_evento, hora_inicio";
        List<Evento> eventos = new ArrayList<>();
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setDate(1, Date.valueOf(desde));
            consulta.setDate(2, Date.valueOf(hasta));
            try (ResultSet resultado = consulta.executeQuery()) {
                while (resultado.next()) eventos.add(mapear(resultado));
            }
        }
        return eventos;
    }

    /** Devuelve el pico de eventos simultaneos al incluir el horario propuesto. */
    @Override
    public int maximoSimultaneosAlAgregar(LocalDate fecha, LocalTime inicio, LocalTime fin) throws SQLException {
        String sql = "SELECT e.hora_inicio, e.hora_fin FROM eventos e WHERE e.fecha_evento = ? "
                + "AND e.hora_inicio < ? AND e.hora_fin > ? "
                + "AND NOT EXISTS (SELECT 1 FROM estado_del_evento estado "
                + "WHERE estado.id_estado = e.id_estado AND LOWER(estado.nombre_estado) = 'cancelado')";
        List<PuntoHorario> puntos = new ArrayList<>();
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setDate(1, Date.valueOf(fecha));
            consulta.setTime(2, Time.valueOf(fin));
            consulta.setTime(3, Time.valueOf(inicio));
            try (ResultSet resultado = consulta.executeQuery()) {
                while (resultado.next()) {
                    LocalTime inicioExistente = resultado.getTime(1).toLocalTime();
                    LocalTime finExistente = resultado.getTime(2).toLocalTime();
                    LocalTime inicioSolapado = inicioExistente.isAfter(inicio) ? inicioExistente : inicio;
                    LocalTime finSolapado = finExistente.isBefore(fin) ? finExistente : fin;
                    puntos.add(new PuntoHorario(inicioSolapado, 1));
                    puntos.add(new PuntoHorario(finSolapado, -1));
                }
            }
        }
        puntos.sort(Comparator.comparing((PuntoHorario punto) -> punto.hora)
                .thenComparingInt(punto -> punto.cambio));
        int activos = 1; // El evento que se esta intentando crear.
        int maximo = activos;
        for (int i = 0; i < puntos.size();) {
            LocalTime horaActual = puntos.get(i).hora;
            int cambioTotal = 0;
            while (i < puntos.size() && puntos.get(i).hora.equals(horaActual)) {
                cambioTotal += puntos.get(i).cambio;
                i++;
            }
            activos += cambioTotal;
            maximo = Math.max(maximo, activos);
        }
        return maximo;
    }

    private static final class PuntoHorario {
        private final LocalTime hora;
        private final int cambio;
        private PuntoHorario(LocalTime hora, int cambio) { this.hora = hora; this.cambio = cambio; }
    }

    /** Comprueba si no hay otro evento activo que se cruce en el mismo salon y fecha. */
    @Override
    public boolean estaDisponible(int idEspacio, LocalDate fecha, LocalTime inicio,
                                  LocalTime fin) throws SQLException {
        String sql = "SELECT COUNT(*) FROM eventos "
                + "WHERE id_espacio = ? AND fecha_evento = ? "
                + "AND NOT EXISTS (SELECT 1 FROM estado_del_evento estado "
                + "WHERE estado.id_estado = eventos.id_estado AND estado.nombre_estado = 'Cancelado') "
                + "AND hora_inicio < ? AND hora_fin > ?";
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setInt(1, idEspacio);
            consulta.setDate(2, Date.valueOf(fecha));
            consulta.setTime(3, Time.valueOf(fin));
            consulta.setTime(4, Time.valueOf(inicio));
            try (ResultSet resultado = consulta.executeQuery()) {
                resultado.next();
                return resultado.getInt(1) == 0;
            }
        }
    }

    private static Evento mapear(ResultSet resultado) throws SQLException {
        int idPaqueteLeido = resultado.getInt("id_paquete");
        Integer idPaquete = resultado.wasNull() ? null : idPaqueteLeido;
        return new Evento(
                resultado.getInt("id_evento"),
                resultado.getInt("id_cliente"),
                tipoEvento(resultado),
                resultado.getInt("id_espacio"),
                idPaquete,
                resultado.getInt("id_estado"),
                resultado.getDate("fecha_evento").toLocalDate(),
                resultado.getTime("hora_inicio").toLocalTime(),
                resultado.getTime("hora_fin").toLocalTime(),
                resultado.getInt("cantidad_invitados"),
                resultado.getString("observaciones"));
    }

    private static void setIdPaquete(PreparedStatement consulta, int posicion,
                                     Integer idPaquete) throws SQLException {
        if (idPaquete == null) {
            consulta.setNull(posicion, Types.INTEGER);
        } else {
            consulta.setInt(posicion, idPaquete);
        }
    }

    private static Integer tipoEvento(ResultSet resultado) throws SQLException {
        int id = resultado.getInt("id_tipo_evento");
        return resultado.wasNull() ? null : id;
    }

    private static void setIdTipoEvento(PreparedStatement consulta, int posicion,
                                        Integer idTipoEvento) throws SQLException {
        if (idTipoEvento == null || idTipoEvento == 0) consulta.setNull(posicion, Types.INTEGER);
        else consulta.setInt(posicion, idTipoEvento);
    }
}

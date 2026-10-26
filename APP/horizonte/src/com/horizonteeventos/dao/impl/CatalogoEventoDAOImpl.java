package com.horizonteeventos.dao.impl;

import com.horizonteeventos.config.Conexion;
import com.horizonteeventos.dao.CatalogoEventoDAO;
import com.horizonteeventos.modelo.Espacio;
import com.horizonteeventos.modelo.EstadoEvento;
import com.horizonteeventos.modelo.Paquete;
import com.horizonteeventos.modelo.Servicio;
import com.horizonteeventos.modelo.ServicioContratado;
import com.horizonteeventos.modelo.TipoEvento;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CatalogoEventoDAOImpl implements CatalogoEventoDAO {
    @Override
    public List<Espacio> listarEspacios() throws SQLException {
        List<Espacio> lista = new ArrayList<>();
        String sql = "SELECT id_espacio, nombre_espacio, capacidad_maxima FROM espacios ORDER BY nombre_espacio";
        try (Connection c = Conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(new Espacio(rs.getInt("id_espacio"),
                    rs.getString("nombre_espacio"), rs.getInt("capacidad_maxima")));
        }
        return lista;
    }

    @Override
    public List<Paquete> listarPaquetes() throws SQLException {
        List<Paquete> lista = new ArrayList<>();
        String sql = "SELECT id_paquete, nombre_paquete, descripcion, precio_fijo "
                + "FROM paquetes ORDER BY nombre_paquete";
        try (Connection c = Conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(new Paquete(rs.getInt("id_paquete"),
                    rs.getString("nombre_paquete"), rs.getString("descripcion"), rs.getBigDecimal("precio_fijo")));
        }
        return lista;
    }

    @Override
    public List<EstadoEvento> listarEstados() throws SQLException {
        List<EstadoEvento> lista = new ArrayList<>();
        String sql = "SELECT id_estado, nombre_estado FROM estado_del_evento ORDER BY id_estado";
        try (Connection c = Conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(new EstadoEvento(rs.getInt("id_estado"), rs.getString("nombre_estado")));
        }
        return lista;
    }

    @Override
    public List<TipoEvento> listarTiposEvento() throws SQLException {
        List<TipoEvento> lista = new ArrayList<>();
        String sql = "SELECT id_tipo_evento, nombre FROM tipos_evento WHERE activo = 1 ORDER BY nombre";
        try (Connection c = Conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(new TipoEvento(rs.getInt("id_tipo_evento"), rs.getString("nombre")));
        }
        return lista;
    }

    @Override
    public List<Servicio> listarServicios() throws SQLException {
        List<Servicio> lista = new ArrayList<>();
        String sql = "SELECT id_servicio, nombre_servicio, costo_unitario, cantidad_disponible "
                + "FROM servicios ORDER BY nombre_servicio";
        try (Connection c = Conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(new Servicio(rs.getInt("id_servicio"),
                    rs.getString("nombre_servicio"), rs.getBigDecimal("costo_unitario"),
                    leerCantidad(rs.getObject("cantidad_disponible"))));
        }
        return lista;
    }

    @Override
    public int guardarServicio(Servicio servicio) throws SQLException {
        if (servicio.getId() == 0) {
            String sql = "INSERT INTO servicios (id_tercero, nombre_servicio, costo_unitario, cantidad_disponible) "
                    + "VALUES (NULL, ?, ?, ?)";
            try (Connection c = Conexion.getConnection();
                 PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, servicio.getNombre());
                ps.setBigDecimal(2, servicio.getCostoUnitario());
                setCantidadDisponible(ps, 3, servicio.getCantidadDisponible());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) throw new SQLException("No se genero el ID del servicio.");
                    servicio.setId(keys.getInt(1));
                    return servicio.getId();
                }
            }
        }
        String sql = "UPDATE servicios SET nombre_servicio = ?, costo_unitario = ?, cantidad_disponible = ? "
                + "WHERE id_servicio = ?";
        try (Connection c = Conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, servicio.getNombre());
            ps.setBigDecimal(2, servicio.getCostoUnitario());
            setCantidadDisponible(ps, 3, servicio.getCantidadDisponible());
            ps.setInt(4, servicio.getId());
            ps.executeUpdate();
        }
        return servicio.getId();
    }

    @Override
    public boolean eliminarServicio(int idServicio) throws SQLException {
        String sql = "DELETE FROM servicios WHERE id_servicio = ?";
        try (Connection c = Conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idServicio);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<String> validarCapacidadServicios(LocalDate fecha, LocalTime inicio, LocalTime fin,
            List<ServicioContratado> servicios) throws SQLException {
        List<String> problemas = new ArrayList<>();
        String consultarCapacidad = "SELECT nombre_servicio, cantidad_disponible FROM servicios WHERE id_servicio = ?";
        String consultarReservado = "SELECT COALESCE(SUM(es.cantidad), 0) "
                + "FROM evento_servicios es JOIN eventos e ON e.id_evento = es.id_evento "
                + "LEFT JOIN estado_del_evento ee ON ee.id_estado = e.id_estado "
                + "WHERE es.id_servicio = ? AND e.fecha_evento = ? "
                + "AND e.hora_inicio < ? AND e.hora_fin > ? "
                + "AND COALESCE(LOWER(ee.nombre_estado), '') <> 'cancelado'";
        try (Connection c = Conexion.getConnection();
             PreparedStatement psCapacidad = c.prepareStatement(consultarCapacidad);
             PreparedStatement psReservado = c.prepareStatement(consultarReservado)) {
            for (ServicioContratado contratado : servicios) {
                psCapacidad.setInt(1, contratado.getIdServicio());
                String nombreServicio;
                Long disponible;
                try (ResultSet rs = psCapacidad.executeQuery()) {
                    if (!rs.next()) continue;
                    nombreServicio = rs.getString("nombre_servicio");
                    disponible = leerCantidad(rs.getObject("cantidad_disponible"));
                }
                if (disponible == null) continue; // La interfaz pedirá confirmación manual.
                psReservado.setInt(1, contratado.getIdServicio());
                psReservado.setDate(2, java.sql.Date.valueOf(fecha));
                psReservado.setTime(3, java.sql.Time.valueOf(fin));
                psReservado.setTime(4, java.sql.Time.valueOf(inicio));
                long reservado = 0;
                try (ResultSet ocupacion = psReservado.executeQuery()) {
                    if (ocupacion.next()) reservado = ocupacion.getLong(1);
                }
                if (reservado + contratado.getCantidad() > disponible) {
                    problemas.add(nombreServicio + ": se necesitan " + contratado.getCantidad()
                            + ", hay " + Math.max(0, disponible - reservado) + " disponibles para ese horario.");
                }
            }
        }
        return problemas;
    }

    @Override
    public Map<Integer, Integer> listarComponentesPaquete(int idPaquete) throws SQLException {
        Map<Integer, Integer> componentes = new LinkedHashMap<>();
        String sql = "SELECT id_servicio, cantidad FROM paquete_servicios WHERE id_paquete = ?";
        try (Connection c = Conexion.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idPaquete);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) componentes.put(rs.getInt("id_servicio"), rs.getInt("cantidad"));
            }
        }
        return componentes;
    }

    /** Guarda el paquete y reemplaza su lista de componentes dentro de una transaccion. */
    @Override
    public int guardarPaquete(Paquete paquete, Map<Integer, Integer> componentes) throws SQLException {
        String insertar = "INSERT INTO paquetes (nombre_paquete, descripcion, precio_fijo) VALUES (?, ?, ?)";
        String actualizar = "UPDATE paquetes SET nombre_paquete = ?, descripcion = ?, precio_fijo = ? "
                + "WHERE id_paquete = ?";
        try (Connection c = Conexion.getConnection()) {
            boolean autoCommitAnterior = c.getAutoCommit();
            c.setAutoCommit(false);
            try {
                int id = paquete.getId();
                if (id == 0) {
                    try (PreparedStatement ps = c.prepareStatement(insertar, Statement.RETURN_GENERATED_KEYS)) {
                        completarPaquete(ps, paquete);
                        ps.executeUpdate();
                        try (ResultSet keys = ps.getGeneratedKeys()) {
                            if (!keys.next()) throw new SQLException("No se genero el ID del paquete.");
                            id = keys.getInt(1);
                        }
                    }
                } else {
                    try (PreparedStatement ps = c.prepareStatement(actualizar)) {
                        completarPaquete(ps, paquete);
                        ps.setInt(4, id);
                        ps.executeUpdate();
                    }
                }
                try (PreparedStatement borrar = c.prepareStatement(
                        "DELETE FROM paquete_servicios WHERE id_paquete = ?")) {
                    borrar.setInt(1, id);
                    borrar.executeUpdate();
                }
                String agregar = "INSERT INTO paquete_servicios (id_paquete, id_servicio, cantidad) "
                        + "VALUES (?, ?, ?)";
                try (PreparedStatement ps = c.prepareStatement(agregar)) {
                    for (Map.Entry<Integer, Integer> componente : componentes.entrySet()) {
                        ps.setInt(1, id);
                        ps.setInt(2, componente.getKey());
                        ps.setInt(3, componente.getValue());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
                c.commit();
                paquete.setId(id);
                return id;
            } catch (SQLException ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(autoCommitAnterior);
            }
        }
    }

    private void completarPaquete(PreparedStatement ps, Paquete paquete) throws SQLException {
        ps.setString(1, paquete.getNombre());
        ps.setString(2, paquete.getDescripcion());
        BigDecimal precio = paquete.getPrecioFijo() == null ? BigDecimal.ZERO : paquete.getPrecioFijo();
        ps.setBigDecimal(3, precio);
    }

    private Long leerCantidad(Object valor) throws SQLException {
        if (valor == null) return null;
        if (valor instanceof Number) return ((Number) valor).longValue();
        try {
            return Long.valueOf(valor.toString());
        } catch (NumberFormatException ex) {
            throw new SQLException("La disponibilidad del servicio no es un número válido.", ex);
        }
    }

    private void setCantidadDisponible(PreparedStatement ps, int indice, Long cantidad) throws SQLException {
        if (cantidad == null) ps.setNull(indice, java.sql.Types.INTEGER);
        else ps.setLong(indice, cantidad);
    }
}

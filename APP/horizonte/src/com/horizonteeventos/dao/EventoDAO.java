package com.horizonteeventos.dao;

import com.horizonteeventos.modelo.Evento;
import com.horizonteeventos.modelo.ServicioContratado;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/** Operaciones para guardar y consultar eventos. */
public interface EventoDAO {
    int insertar(Evento evento) throws SQLException;
    int insertarConServicios(Evento evento, List<ServicioContratado> servicios) throws SQLException;
    Optional<Evento> buscarPorId(int idEvento) throws SQLException;
    List<Evento> listarTodos() throws SQLException;
    boolean actualizar(Evento evento) throws SQLException;
    boolean eliminar(int idEvento) throws SQLException;
    List<Evento> obtenerEventosPorFecha(LocalDate fecha) throws SQLException;
    List<Evento> obtenerEventosEntreFechas(LocalDate desde, LocalDate hasta) throws SQLException;
    int maximoSimultaneosAlAgregar(LocalDate fecha, LocalTime inicio, LocalTime fin) throws SQLException;
    boolean estaDisponible(int idEspacio, LocalDate fecha, LocalTime inicio,
                           LocalTime fin) throws SQLException;
}

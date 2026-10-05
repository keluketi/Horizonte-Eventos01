package com.horizonteeventos.dao;

import com.horizonteeventos.modelo.Evento;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** Operaciones para guardar y consultar eventos. */
public interface EventoDAO {
    int insertar(Evento evento) throws SQLException;
    Optional<Evento> buscarPorId(int idEvento) throws SQLException;
    List<Evento> listarTodos() throws SQLException;
    boolean actualizar(Evento evento) throws SQLException;
    boolean eliminar(int idEvento) throws SQLException;
    List<Evento> obtenerEventosPorFecha(LocalDate fecha) throws SQLException;
}

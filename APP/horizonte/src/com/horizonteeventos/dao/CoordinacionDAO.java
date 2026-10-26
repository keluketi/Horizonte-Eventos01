package com.horizonteeventos.dao;

import com.horizonteeventos.modelo.EventoCoordinacion;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/** Consultas de eventos para la agenda de Coordinacion. */
public interface CoordinacionDAO {
    List<EventoCoordinacion> listarEventos() throws SQLException;
    List<EventoCoordinacion> listarEventosPorFecha(LocalDate fecha) throws SQLException;
    List<EventoCoordinacion> listarEventosConfirmados() throws SQLException;
    List<Object[]> listarPersonalAsignado() throws SQLException;
    List<Object[]> listarServiciosContratados() throws SQLException;
    List<Object[]> listarModificaciones() throws SQLException;
    List<Object[]> listarIncidencias() throws SQLException;
    boolean registrarIncidencia(int idEvento, String descripcion, String reportadoPor) throws SQLException;
}

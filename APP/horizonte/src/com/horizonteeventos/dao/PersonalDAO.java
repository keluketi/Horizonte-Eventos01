package com.horizonteeventos.dao;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.List;

import com.horizonteeventos.modelo.AsignacionPersonal;

/** Consultas y registro de horas del empleado autenticado. */
public interface PersonalDAO {
    List<AsignacionPersonal> listarAsignaciones(int idEmpleado) throws SQLException;
    List<Object[]> listarIncidenciasAsignadas(int idEmpleado) throws SQLException;
    boolean registrarIncidencia(int idEvento, int idEmpleado, String descripcion,
            String reportadoPor) throws SQLException;
    boolean registrarTrabajo(int idAsignacion, int idEmpleado, LocalTime inicioReal,
            LocalTime finReal, String observaciones) throws SQLException;
}

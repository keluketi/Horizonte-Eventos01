package com.horizonteeventos.dao;

import java.sql.SQLException;

/** Registra las acciones de interfaz realizadas por los empleados. */
public interface AuditoriaAccionDAO {
    void registrar(Integer idEmpleado, String nombreEmpleado, String rol, String pantalla, String accion)
            throws SQLException;
}

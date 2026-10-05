package com.horizonteeventos.dao;

import com.horizonteeventos.modelo.Empleado;
import java.sql.SQLException;
import java.util.List;

public interface EmpleadoDAO {
    int insertar(Empleado empleado, char[] contrasena) throws SQLException;
    List<Empleado> listarTodos() throws SQLException;
}

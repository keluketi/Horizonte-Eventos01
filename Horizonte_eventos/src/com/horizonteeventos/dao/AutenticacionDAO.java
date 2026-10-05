package com.horizonteeventos.dao;

import com.horizonteeventos.modelo.Empleado;
import java.sql.SQLException;
import java.util.Optional;

public interface AutenticacionDAO {
    Optional<Empleado> iniciarSesion(String email, char[] contrasena) throws SQLException;
}

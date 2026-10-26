package com.horizonteeventos.dao;

import com.horizonteeventos.modelo.Cliente;
import java.sql.SQLException;
import java.util.Optional;

/** Autenticación y alta inicial de contraseña de clientes existentes. */
public interface AutenticacionClienteDAO {
    Optional<Cliente> iniciarSesion(String email, char[] contrasena) throws SQLException;
    boolean configurarContrasenaInicial(String email, String dniCuit, char[] contrasena)
            throws SQLException;
}
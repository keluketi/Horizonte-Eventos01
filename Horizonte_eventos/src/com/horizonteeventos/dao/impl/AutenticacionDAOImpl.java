package com.horizonteeventos.dao.impl;

import com.horizonteeventos.config.Conexion;
import com.horizonteeventos.dao.AutenticacionDAO;
import com.horizonteeventos.modelo.Empleado;
import com.horizonteeventos.seguridad.PasswordUtil;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AutenticacionDAOImpl implements AutenticacionDAO {
    @Override
    public Optional<Empleado> iniciarSesion(String email, char[] contrasena)
            throws SQLException {
        String sql = "SELECT id_empleado, id_rol, nombre, apellido, email, password "
                + "FROM empleados WHERE email = ?";
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, email.trim());
            try (ResultSet resultado = consulta.executeQuery()) {
                if (!resultado.next()) return Optional.empty();
                String hash = resultado.getString("password");
                try {
                    if (!PasswordUtil.verificar(contrasena, hash)) return Optional.empty();
                } catch (GeneralSecurityException | IllegalArgumentException ex) {
                    throw new SQLException("No se pudo verificar la contrasena almacenada.", ex);
                }
                int idRol = resultado.getInt("id_rol");
                // Solo los roles con un modulo de escritorio implementado inician sesion.
                if (idRol != 1 && idRol != 4) return Optional.empty();
                return Optional.of(new Empleado(resultado.getInt("id_empleado"),
                        idRol, resultado.getString("nombre"),
                        resultado.getString("apellido"), resultado.getString("email")));
            }
        }
    }
}

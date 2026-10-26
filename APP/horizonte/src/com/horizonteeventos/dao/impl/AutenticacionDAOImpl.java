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
        String sql = "SELECT e.id_empleado, e.id_rol, e.nombre, e.apellido, e.cuil, e.email, "
                + "e.telefono, e.password, r.nombre_rol FROM empleados e "
                + "JOIN roles r ON r.id_rol = e.id_rol WHERE e.email = ?";
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
                // El escritorio habilita los cuatro roles definidos para la empresa.
                if (idRol < 1 || idRol > 4) return Optional.empty();
                return Optional.of(new Empleado(resultado.getInt("id_empleado"),
                        idRol, resultado.getString("nombre"), resultado.getString("apellido"),
                        resultado.getString("cuil"), resultado.getString("email"),
                        resultado.getString("telefono"), resultado.getString("nombre_rol")));
            }
        }
    }
}

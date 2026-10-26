package com.horizonteeventos.dao.impl;

import com.horizonteeventos.config.Conexion;
import com.horizonteeventos.dao.AutenticacionClienteDAO;
import com.horizonteeventos.modelo.Cliente;
import com.horizonteeventos.seguridad.PasswordUtil;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/** Usa clientes.password, que guarda hashes PBKDF2 y puede ser NULL al inicio. */
public class AutenticacionClienteDAOImpl implements AutenticacionClienteDAO {
    @Override
    public Optional<Cliente> iniciarSesion(String email, char[] contrasena) throws SQLException {
        String sql = "SELECT id_cliente, nombre, apellido, dni_cuit, email, telefono, password "
                   + "FROM clientes WHERE email = ?";
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, email.trim());
            try (ResultSet resultado = consulta.executeQuery()) {
                if (!resultado.next()) return Optional.empty();
                String hash = resultado.getString("password");
                try {
                    if (!PasswordUtil.verificar(contrasena, hash)) return Optional.empty();
                } catch (GeneralSecurityException | IllegalArgumentException ex) {
                    throw new SQLException("No se pudo verificar la contraseña del cliente.", ex);
                }
                Cliente cliente = new Cliente(resultado.getInt("id_cliente"),
                        resultado.getString("nombre"), resultado.getString("apellido"),
                        resultado.getString("dni_cuit"), resultado.getString("email"),
                        resultado.getString("telefono"));
                return Optional.of(cliente);
            }
        }
    }

    @Override
    public boolean configurarContrasenaInicial(String email, String dniCuit, char[] contrasena)
            throws SQLException {
        final String hash;
        try {
            hash = PasswordUtil.crearHash(contrasena);
        } catch (GeneralSecurityException ex) {
            throw new SQLException("No se pudo proteger la contraseña.", ex);
        }
        String sql = "UPDATE clientes SET password = ? WHERE email = ? AND dni_cuit = ? "
                   + "AND (password IS NULL OR password = '')";
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, hash);
            consulta.setString(2, email.trim());
            consulta.setString(3, dniCuit.trim());
            return consulta.executeUpdate() == 1;
        }
    }
}
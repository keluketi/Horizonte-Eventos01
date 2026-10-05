package com.horizonteeventos.dao.impl;

import com.horizonteeventos.config.Conexion;
import com.horizonteeventos.dao.EmpleadoDAO;
import com.horizonteeventos.modelo.Empleado;
import com.horizonteeventos.seguridad.PasswordUtil;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Persistencia de empleados. Las contrasenas se guardan como hash PBKDF2. */
public class EmpleadoDAOImpl implements EmpleadoDAO {
    @Override
    public int insertar(Empleado empleado, char[] contrasena) throws SQLException {
        String sql = "INSERT INTO empleados (id_rol, nombre, apellido, cuil, email, password, telefono) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            consulta.setInt(1, empleado.getIdRol());
            consulta.setString(2, empleado.getNombre());
            consulta.setString(3, empleado.getApellido());
            consulta.setString(4, empleado.getCuil());
            consulta.setString(5, empleado.getEmail());
            consulta.setString(6, crearHash(contrasena));
            consulta.setString(7, empleado.getTelefono());
            consulta.executeUpdate();
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) throw new SQLException("No se obtuvo el id del empleado.");
                return claves.getInt(1);
            }
        }
    }

    @Override
    public List<Empleado> listarTodos() throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT e.id_empleado, e.id_rol, e.nombre, e.apellido, e.cuil, "
                + "e.email, e.telefono, r.nombre_rol FROM empleados e "
                + "JOIN roles r ON r.id_rol = e.id_rol ORDER BY e.apellido, e.nombre";
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                empleados.add(new Empleado(resultado.getInt("id_empleado"),
                        resultado.getInt("id_rol"), resultado.getString("nombre"),
                        resultado.getString("apellido"), resultado.getString("cuil"),
                        resultado.getString("email"), resultado.getString("telefono"),
                        resultado.getString("nombre_rol")));
            }
        }
        return empleados;
    }

    private String crearHash(char[] contrasena) throws SQLException {
        try {
            return PasswordUtil.crearHash(contrasena);
        } catch (GeneralSecurityException ex) {
            throw new SQLException("No se pudo proteger la contrasena del empleado.", ex);
        }
    }
}

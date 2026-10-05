package com.horizonteeventos.dao.impl;

import com.horizonteeventos.config.Conexion;
import com.horizonteeventos.dao.RolDAO;
import com.horizonteeventos.modelo.Rol;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RolDAOImpl implements RolDAO {
    @Override
    public List<Rol> listarTodos() throws SQLException {
        List<Rol> roles = new ArrayList<>();
        String sql = "SELECT id_rol, nombre_rol FROM roles ORDER BY id_rol";
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                roles.add(new Rol(resultado.getInt("id_rol"), resultado.getString("nombre_rol")));
            }
        }
        return roles;
    }
}

package com.horizonteeventos.dao.impl;

import com.horizonteeventos.config.Conexion;
import com.horizonteeventos.dao.AuditoriaAccionDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/** Guarda cada clic en la tabla registro_acciones, con sentencias preparadas. */
public class AuditoriaAccionDAOImpl implements AuditoriaAccionDAO {
    @Override
    public void registrar(Integer idEmpleado, String nombreEmpleado, String rol,
                          String pantalla, String accion) throws SQLException {
        String sql = "INSERT INTO registro_acciones "
                + "(id_empleado, empleado_nombre, rol, pantalla, accion) VALUES (?, ?, ?, ?, ?)";
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {
            if (idEmpleado == null) ps.setNull(1, Types.INTEGER); else ps.setInt(1, idEmpleado);
            ps.setString(2, nombreEmpleado);
            ps.setString(3, rol);
            ps.setString(4, pantalla);
            ps.setString(5, accion);
            ps.executeUpdate();
        }
    }
}

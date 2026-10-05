package com.horizonteeventos.dao.impl;

import com.horizonteeventos.config.Conexion;
import com.horizonteeventos.dao.ClienteDAO;
import com.horizonteeventos.modelo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAOImpl implements ClienteDAO {
    @Override
    public int insertar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (nombre, apellido, dni_cuit, email, telefono) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            consulta.setString(1, cliente.getNombre());
            consulta.setString(2, cliente.getApellido());
            consulta.setString(3, cliente.getDniCuit());
            consulta.setString(4, cliente.getEmail());
            consulta.setString(5, cliente.getTelefono());
            consulta.executeUpdate();
            try (ResultSet claves = consulta.getGeneratedKeys()) {
                if (!claves.next()) throw new SQLException("No se obtuvo el id del cliente.");
                cliente.setIdCliente(claves.getInt(1));
                return cliente.getIdCliente();
            }
        }
    }

    @Override
    public List<Cliente> listarTodos() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id_cliente, nombre, apellido, dni_cuit, email, telefono "
                + "FROM clientes ORDER BY apellido, nombre";
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {
            while (resultado.next()) {
                clientes.add(new Cliente(resultado.getInt("id_cliente"),
                        resultado.getString("nombre"), resultado.getString("apellido"),
                        resultado.getString("dni_cuit"), resultado.getString("email"),
                        resultado.getString("telefono")));
            }
        }
        return clientes;
    }

    @Override
    public boolean actualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nombre = ?, apellido = ?, dni_cuit = ?, "
                + "email = ?, telefono = ? WHERE id_cliente = ?";
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setString(1, cliente.getNombre());
            consulta.setString(2, cliente.getApellido());
            consulta.setString(3, cliente.getDniCuit());
            consulta.setString(4, cliente.getEmail());
            consulta.setString(5, cliente.getTelefono());
            consulta.setInt(6, cliente.getIdCliente());
            return consulta.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int idCliente) throws SQLException {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql)) {
            consulta.setInt(1, idCliente);
            return consulta.executeUpdate() > 0;
        }
    }
}

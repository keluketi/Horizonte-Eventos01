package com.horizonteeventos.dao;

import com.horizonteeventos.modelo.Cliente;
import java.sql.SQLException;
import java.util.List;

public interface ClienteDAO {
    int insertar(Cliente cliente) throws SQLException;
    List<Cliente> listarTodos() throws SQLException;
    boolean actualizar(Cliente cliente) throws SQLException;
    boolean eliminar(int idCliente) throws SQLException;
}

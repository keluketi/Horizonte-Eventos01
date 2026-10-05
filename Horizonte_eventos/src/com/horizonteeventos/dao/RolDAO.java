package com.horizonteeventos.dao;

import com.horizonteeventos.modelo.Rol;
import java.sql.SQLException;
import java.util.List;

public interface RolDAO {
    List<Rol> listarTodos() throws SQLException;
}

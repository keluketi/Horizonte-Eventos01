package com.horizonteeventos.dao;

import com.horizonteeventos.modelo.DatosTabla;
import java.sql.SQLException;

/** Consultas de solo lectura que alimentan las tablas de los modulos. */
public interface ConsultaModuloDAO {
    DatosTabla consultar(String modulo) throws SQLException;
}

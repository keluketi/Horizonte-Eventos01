package com.horizonteeventos.dao;

import com.horizonteeventos.modelo.DatosFacturacionEvento;
import java.sql.SQLException;

/** Lectura de presupuestos, pagos y comprobantes asociados a un evento. */
public interface FacturacionSimulacionDAO {
    DatosFacturacionEvento cargarPorEvento(int idEvento) throws SQLException;
}

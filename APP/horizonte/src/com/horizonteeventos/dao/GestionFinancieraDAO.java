package com.horizonteeventos.dao;

import com.horizonteeventos.modelo.EventoConSaldo;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/** Aprobación de presupuestos y registro de pagos del módulo de Administración. */
public interface GestionFinancieraDAO {
    boolean aprobarPresupuesto(int idPresupuesto, BigDecimal montoSena) throws SQLException;
    boolean rechazarPresupuesto(int idPresupuesto) throws SQLException;
    List<EventoConSaldo> listarEventosConSaldo() throws SQLException;
    boolean registrarPago(int idEvento, BigDecimal monto, String medio,
            String referencia, String observaciones) throws SQLException;
}

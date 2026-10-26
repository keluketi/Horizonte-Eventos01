package com.horizonteeventos.dao;

import com.horizonteeventos.modelo.Pago;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface PagoDAO {
    List<Pago> listarPorEvento(int idEvento) throws SQLException;
    BigDecimal calcularTotalPagado(int idEvento) throws SQLException;

    /** Inserta el pago y cambia el estado en una transacción, validando que el evento sea del cliente. */
    void registrarYActualizarEstado(Pago pago, int idCliente) throws SQLException;
}
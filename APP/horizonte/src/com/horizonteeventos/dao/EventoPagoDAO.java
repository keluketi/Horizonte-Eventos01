package com.horizonteeventos.dao;

import com.horizonteeventos.modelo.EventoPagoResumen;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** Consultas de eventos visibles para un cliente autenticado. */
public interface EventoPagoDAO {
    List<EventoPagoResumen> listarHistorial(int idCliente) throws SQLException;
    Optional<EventoPagoResumen> buscarEvento(int idCliente, int idEvento) throws SQLException;
}
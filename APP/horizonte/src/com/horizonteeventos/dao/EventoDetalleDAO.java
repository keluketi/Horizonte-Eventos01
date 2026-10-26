package com.horizonteeventos.dao;

import com.horizonteeventos.modelo.DetalleEvento;
import java.sql.SQLException;
import java.util.Optional;

/** Consulta toda la información necesaria para el detalle de un evento. */
public interface EventoDetalleDAO {
    Optional<DetalleEvento> buscarPorId(int idEvento) throws SQLException;
}

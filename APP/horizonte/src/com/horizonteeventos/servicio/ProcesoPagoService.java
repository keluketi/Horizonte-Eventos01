package com.horizonteeventos.servicio;

import com.horizonteeventos.dao.AutenticacionClienteDAO;
import com.horizonteeventos.dao.EventoPagoDAO;
import com.horizonteeventos.dao.PagoDAO;
import com.horizonteeventos.dao.impl.AutenticacionClienteDAOImpl;
import com.horizonteeventos.dao.impl.EventoPagoDAOImpl;
import com.horizonteeventos.dao.impl.PagoDAOImpl;
import com.horizonteeventos.modelo.EventoPagoResumen;
import com.horizonteeventos.modelo.Pago;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Reglas del flujo de pago simulado, separadas de las ventanas Swing. */
public class ProcesoPagoService {
    private final AutenticacionClienteDAO autenticacionDAO;
    private final EventoPagoDAO eventoDAO;
    private final PagoDAO pagoDAO;

    public ProcesoPagoService() {
        this(new AutenticacionClienteDAOImpl(), new EventoPagoDAOImpl(), new PagoDAOImpl());
    }

    public ProcesoPagoService(AutenticacionClienteDAO autenticacionDAO,
            EventoPagoDAO eventoDAO, PagoDAO pagoDAO) {
        this.autenticacionDAO = autenticacionDAO;
        this.eventoDAO = eventoDAO;
        this.pagoDAO = pagoDAO;
    }

    public AutenticacionClienteDAO getAutenticacionDAO() { return autenticacionDAO; }

    public List<EventoPagoResumen> listarHistorial(int idCliente) throws SQLException {
        return eventoDAO.listarHistorial(idCliente);
    }

    public BigDecimal calcularImporte(int idCliente, int idEvento, String tipoPago)
            throws SQLException {
        EventoPagoResumen evento = buscarEventoCliente(idCliente, idEvento);
        BigDecimal importe;
        if ("Seña".equals(tipoPago)) {
            if (!estadoAceptado(evento.getEstado())) {
                throw new SQLException("La seña solo está disponible cuando el evento fue aceptado.");
            }
            importe = evento.getSena().subtract(evento.getPagado());
        } else if ("Pago final".equals(tipoPago)) {
            if (!"Señado".equalsIgnoreCase(evento.getEstado())) {
                throw new SQLException("El pago final se habilita después de registrar la seña.");
            }
            importe = evento.getSaldo();
        } else {
            throw new SQLException("Selecciona Seña o Pago final.");
        }
        if (importe.signum() <= 0) throw new SQLException("No hay importe pendiente para este tipo de pago.");
        return importe;
    }

    public void procesarPago(int idCliente, int idEvento, String tipoPago, String medioPago)
            throws SQLException {
        BigDecimal importe = calcularImporte(idCliente, idEvento, tipoPago);
        Pago pago = new Pago();
        pago.setIdEvento(idEvento);
        pago.setMonto(importe);
        pago.setMedioPago(medioPago);
        pago.setTipoPago(tipoPago);
        pago.setReferencia("SIM-" + UUID.randomUUID().toString());
        pago.setObservaciones("Pago simulado · " + tipoPago);
        pagoDAO.registrarYActualizarEstado(pago, idCliente);
    }

    private EventoPagoResumen buscarEventoCliente(int idCliente, int idEvento) throws SQLException {
        Optional<EventoPagoResumen> evento = eventoDAO.buscarEvento(idCliente, idEvento);
        if (!evento.isPresent()) throw new SQLException("El evento no pertenece a este cliente o no está disponible para pago.");
        return evento.get();
    }

    private boolean estadoAceptado(String estado) {
        return "Aceptado".equalsIgnoreCase(estado) || "Confirmado".equalsIgnoreCase(estado);
    }
}
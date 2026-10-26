package com.horizonteeventos.modelo;

import java.math.BigDecimal;

/** Servicio elegido para una propuesta concreta, con cantidad y valor acordado. */
public class ServicioContratado {
    private final int idServicio;
    private final int cantidad;
    private final BigDecimal precioAcordado;

    public ServicioContratado(int idServicio, int cantidad, BigDecimal precioAcordado) {
        this.idServicio = idServicio;
        this.cantidad = cantidad;
        this.precioAcordado = precioAcordado;
    }

    public int getIdServicio() { return idServicio; }
    public int getCantidad() { return cantidad; }
    public BigDecimal getPrecioAcordado() { return precioAcordado; }
}

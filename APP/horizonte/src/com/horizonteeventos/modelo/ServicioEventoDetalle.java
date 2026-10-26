package com.horizonteeventos.modelo;

import java.math.BigDecimal;

/** Servicio que se acordó incluir en un evento. */
public class ServicioEventoDetalle {
    private final String nombre;
    private final String proveedor;
    private final int cantidad;
    private final BigDecimal precioAcordado;

    public ServicioEventoDetalle(String nombre, String proveedor, int cantidad, BigDecimal precioAcordado) {
        this.nombre = nombre;
        this.proveedor = proveedor;
        this.cantidad = cantidad;
        this.precioAcordado = precioAcordado;
    }

    public String getNombre() { return nombre; }
    public String getProveedor() { return proveedor; }
    public int getCantidad() { return cantidad; }
    public BigDecimal getPrecioAcordado() { return precioAcordado; }
}

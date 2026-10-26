package com.horizonteeventos.modelo;

import java.math.BigDecimal;

/** Paquete comercial opcional asociado al evento. */
public class Paquete {
    private int id;
    private String nombre;
    private String descripcion;
    private BigDecimal precioFijo;

    public Paquete(int id, String nombre) {
        this(id, nombre, "", BigDecimal.ZERO);
    }

    public Paquete(int id, String nombre, String descripcion, BigDecimal precioFijo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioFijo = precioFijo == null ? BigDecimal.ZERO : precioFijo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getPrecioFijo() { return precioFijo; }
    public void setPrecioFijo(BigDecimal precioFijo) { this.precioFijo = precioFijo; }
    @Override public String toString() { return nombre; }
}

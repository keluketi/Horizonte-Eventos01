package com.horizonteeventos.modelo;

import java.math.BigDecimal;

/** Servicio o recurso que la empresa ofrece y puede incluir en una propuesta. */
public class Servicio {
    private int id;
    private String nombre;
    private BigDecimal costoUnitario;
    private Long cantidadDisponible;

    public Servicio(int id, String nombre, BigDecimal costoUnitario) {
        this(id, nombre, costoUnitario, (Long) null);
    }

    public Servicio(int id, String nombre, BigDecimal costoUnitario, Long cantidadDisponible) {
        this.id = id;
        this.nombre = nombre;
        this.costoUnitario = costoUnitario == null ? BigDecimal.ZERO : costoUnitario;
        this.cantidadDisponible = cantidadDisponible;
    }

    /** Compatibilidad con llamadas que todavía manejan la disponibilidad como Integer. */
    public Servicio(int id, String nombre, BigDecimal costoUnitario, Integer cantidadDisponible) {
        this(id, nombre, costoUnitario,
                cantidadDisponible == null ? null : cantidadDisponible.longValue());
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getCostoUnitario() { return costoUnitario; }
    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario == null ? BigDecimal.ZERO : costoUnitario;
    }
    public Long getCantidadDisponible() { return cantidadDisponible; }
    public void setCantidadDisponible(Long cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }
    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible == null ? null : cantidadDisponible.longValue();
    }
    @Override public String toString() { return nombre; }
}

package com.horizonteeventos.modelo;

/** Estado inicial o actual del evento. */
public class EstadoEvento {
    private final int id;
    private final String nombre;

    public EstadoEvento(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    @Override public String toString() { return nombre; }
}

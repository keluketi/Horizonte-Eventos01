package com.horizonteeventos.modelo;

/** Salon o espacio fisico donde puede realizarse un evento. */
public class Espacio {
    private final int id;
    private final String nombre;
    private final int capacidad;

    public Espacio(int id, String nombre, int capacidad) {
        this.id = id;
        this.nombre = nombre;
        this.capacidad = capacidad;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getCapacidad() { return capacidad; }
    @Override public String toString() { return nombre + " (capacidad " + capacidad + ")"; }
}

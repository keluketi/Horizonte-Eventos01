package com.horizonteeventos.modelo;

/** Tipo configurable de evento (casamiento, cumpleaños, corporativo, etc.). */
public class TipoEvento {
    private final int id;
    private final String nombre;

    public TipoEvento(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    @Override public String toString() { return nombre; }
}

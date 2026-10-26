package com.horizonteeventos.modelo;

import java.util.List;

/** Resultado sencillo para mostrar filas consultadas en una tabla Swing. */
public class DatosTabla {
    private final String[] columnas;
    private final List<Object[]> filas;

    public DatosTabla(String[] columnas, List<Object[]> filas) {
        this.columnas = columnas;
        this.filas = filas;
    }

    public String[] getColumnas() { return columnas; }
    public List<Object[]> getFilas() { return filas; }
}

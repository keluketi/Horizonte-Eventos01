package com.horizonteeventos.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

/** Turno y tarea de un empleado asignado a un evento. */
public class AsignacionPersonal {
    private final int idAsignacion;
    private final int idEvento;
    private final LocalDate fechaEvento;
    private final LocalTime horaInicioEvento;
    private final LocalTime horaFinEvento;
    private final String funcion;
    private final String tarea;
    private final LocalTime horaInicioReal;
    private final LocalTime horaFinReal;
    private final String estado;
    private final String observaciones;

    public AsignacionPersonal(int idAsignacion, int idEvento, LocalDate fechaEvento,
            LocalTime horaInicioEvento, LocalTime horaFinEvento, String funcion,
            String tarea, LocalTime horaInicioReal, LocalTime horaFinReal,
            String estado, String observaciones) {
        this.idAsignacion = idAsignacion;
        this.idEvento = idEvento;
        this.fechaEvento = fechaEvento;
        this.horaInicioEvento = horaInicioEvento;
        this.horaFinEvento = horaFinEvento;
        this.funcion = funcion;
        this.tarea = tarea;
        this.horaInicioReal = horaInicioReal;
        this.horaFinReal = horaFinReal;
        this.estado = estado;
        this.observaciones = observaciones;
    }

    public int getIdAsignacion() { return idAsignacion; }
    public int getIdEvento() { return idEvento; }
    public LocalDate getFechaEvento() { return fechaEvento; }
    public LocalTime getHoraInicioEvento() { return horaInicioEvento; }
    public LocalTime getHoraFinEvento() { return horaFinEvento; }
    public String getFuncion() { return funcion; }
    public String getTarea() { return tarea; }
    public LocalTime getHoraInicioReal() { return horaInicioReal; }
    public LocalTime getHoraFinReal() { return horaFinReal; }
    public String getEstado() { return estado; }
    public String getObservaciones() { return observaciones; }
}

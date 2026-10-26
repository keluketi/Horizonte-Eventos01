package com.horizonteeventos.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

/** Datos de evento que necesita consultar el equipo de Coordinacion. */
public class EventoCoordinacion {
    private final int idEvento;
    private final LocalDate fecha;
    private final LocalTime horaInicio;
    private final LocalTime horaFin;
    private final String cliente;
    private final String espacio;
    private final String estado;
    private final int cantidadInvitados;
    private final Integer asistenciaReal;

    public EventoCoordinacion(int idEvento, LocalDate fecha, LocalTime horaInicio,
            LocalTime horaFin, String cliente, String espacio, String estado,
            int cantidadInvitados, Integer asistenciaReal) {
        this.idEvento = idEvento;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.cliente = cliente;
        this.espacio = espacio;
        this.estado = estado;
        this.cantidadInvitados = cantidadInvitados;
        this.asistenciaReal = asistenciaReal;
    }

    public int getIdEvento() { return idEvento; }
    public LocalDate getFecha() { return fecha; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public String getCliente() { return cliente; }
    public String getEspacio() { return espacio; }
    public String getEstado() { return estado; }
    public int getCantidadInvitados() { return cantidadInvitados; }
    public Integer getAsistenciaReal() { return asistenciaReal; }
}

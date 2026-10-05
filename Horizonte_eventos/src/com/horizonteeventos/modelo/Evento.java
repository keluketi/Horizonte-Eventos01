package com.horizonteeventos.modelo;

import java.time.LocalDate;
import java.time.LocalTime;

/** Representa una fila de la tabla eventos de la base de datos. */
public class Evento {
    private int idEvento;
    private int idCliente;
    private int idEspacio;
    private Integer idPaquete; // Es opcional en la base de datos.
    private int idEstado;
    private LocalDate fechaEvento;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private int cantidadInvitados;

    public Evento() {
    }

    /** Constructor para registrar un evento nuevo; el id lo genera la base. */
    public Evento(int idCliente, int idEspacio, Integer idPaquete, int idEstado,
                  LocalDate fechaEvento, LocalTime horaInicio, LocalTime horaFin,
                  int cantidadInvitados) {
        this(0, idCliente, idEspacio, idPaquete, idEstado, fechaEvento,
                horaInicio, horaFin, cantidadInvitados);
    }

    public Evento(int idEvento, int idCliente, int idEspacio, Integer idPaquete,
                  int idEstado, LocalDate fechaEvento, LocalTime horaInicio,
                  LocalTime horaFin, int cantidadInvitados) {
        this.idEvento = idEvento;
        this.idCliente = idCliente;
        this.idEspacio = idEspacio;
        this.idPaquete = idPaquete;
        this.idEstado = idEstado;
        this.fechaEvento = fechaEvento;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.cantidadInvitados = cantidadInvitados;
    }

    public int getIdEvento() { return idEvento; }
    public void setIdEvento(int idEvento) { this.idEvento = idEvento; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public int getIdEspacio() { return idEspacio; }
    public void setIdEspacio(int idEspacio) { this.idEspacio = idEspacio; }
    public Integer getIdPaquete() { return idPaquete; }
    public void setIdPaquete(Integer idPaquete) { this.idPaquete = idPaquete; }
    public int getIdEstado() { return idEstado; }
    public void setIdEstado(int idEstado) { this.idEstado = idEstado; }
    public LocalDate getFechaEvento() { return fechaEvento; }
    public void setFechaEvento(LocalDate fechaEvento) { this.fechaEvento = fechaEvento; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    public int getCantidadInvitados() { return cantidadInvitados; }
    public void setCantidadInvitados(int cantidadInvitados) {
        this.cantidadInvitados = cantidadInvitados;
    }
}

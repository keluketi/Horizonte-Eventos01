package com.horizonteeventos.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/** Datos ampliados de un evento para el panel de consulta. */
public class DetalleEvento {
    private int id;
    private String tipo;
    private String cliente;
    private String documentoCliente;
    private String emailCliente;
    private String telefonoCliente;
    private String espacio;
    private String estado;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private int invitados;
    private Integer asistenciaReal;
    private String paquete;
    private String descripcionPaquete;
    private BigDecimal precioPaquete;
    private String observaciones;
    private final List<ServicioEventoDetalle> servicios = new ArrayList<>();

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public String getDocumentoCliente() { return documentoCliente; }
    public void setDocumentoCliente(String documentoCliente) { this.documentoCliente = documentoCliente; }
    public String getEmailCliente() { return emailCliente; }
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }
    public String getTelefonoCliente() { return telefonoCliente; }
    public void setTelefonoCliente(String telefonoCliente) { this.telefonoCliente = telefonoCliente; }
    public String getEspacio() { return espacio; }
    public void setEspacio(String espacio) { this.espacio = espacio; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    public int getInvitados() { return invitados; }
    public void setInvitados(int invitados) { this.invitados = invitados; }
    public Integer getAsistenciaReal() { return asistenciaReal; }
    public void setAsistenciaReal(Integer asistenciaReal) { this.asistenciaReal = asistenciaReal; }
    public String getPaquete() { return paquete; }
    public void setPaquete(String paquete) { this.paquete = paquete; }
    public String getDescripcionPaquete() { return descripcionPaquete; }
    public void setDescripcionPaquete(String descripcionPaquete) { this.descripcionPaquete = descripcionPaquete; }
    public BigDecimal getPrecioPaquete() { return precioPaquete; }
    public void setPrecioPaquete(BigDecimal precioPaquete) { this.precioPaquete = precioPaquete; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public List<ServicioEventoDetalle> getServicios() { return servicios; }
}

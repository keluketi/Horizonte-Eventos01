package com.horizonteeventos.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Datos financieros de solo lectura para la vista previa de un evento. */
public class DatosFacturacionEvento {
    private BigDecimal presupuestoAprobado;
    private BigDecimal montoSena;
    private String estadoPresupuesto = "Sin presupuesto";
    private LocalDate fechaPresupuesto;
    private final List<Pago> pagos = new ArrayList<>();
    private final List<Comprobante> comprobantes = new ArrayList<>();

    public BigDecimal getPresupuestoAprobado() { return presupuestoAprobado; }
    public void setPresupuestoAprobado(BigDecimal presupuestoAprobado) { this.presupuestoAprobado = presupuestoAprobado; }
    public BigDecimal getMontoSena() { return montoSena; }
    public void setMontoSena(BigDecimal montoSena) { this.montoSena = montoSena; }
    public String getEstadoPresupuesto() { return estadoPresupuesto; }
    public void setEstadoPresupuesto(String estadoPresupuesto) { this.estadoPresupuesto = estadoPresupuesto; }
    public LocalDate getFechaPresupuesto() { return fechaPresupuesto; }
    public void setFechaPresupuesto(LocalDate fechaPresupuesto) { this.fechaPresupuesto = fechaPresupuesto; }
    public List<Pago> getPagos() { return pagos; }
    public List<Comprobante> getComprobantes() { return comprobantes; }

    public static class Pago {
        private final int id;
        private final LocalDateTime fecha;
        private final BigDecimal monto;
        private final String medio;
        private final String estadoEvento;
        private final String observaciones;

        public Pago(int id, LocalDateTime fecha, BigDecimal monto, String medio,
                String estadoEvento, String observaciones) {
            this.id = id;
            this.fecha = fecha;
            this.monto = monto;
            this.medio = medio;
            this.estadoEvento = estadoEvento;
            this.observaciones = observaciones;
        }
        public int getId() { return id; }
        public LocalDateTime getFecha() { return fecha; }
        public BigDecimal getMonto() { return monto; }
        public String getMedio() { return medio; }
        public String getEstadoEvento() { return estadoEvento; }
        public String getObservaciones() { return observaciones; }
    }

    public static class Comprobante {
        private final String numero;
        private final String tipo;
        private final BigDecimal monto;
        private final LocalDateTime fechaEmision;

        public Comprobante(String numero, String tipo, BigDecimal monto, LocalDateTime fechaEmision) {
            this.numero = numero;
            this.tipo = tipo;
            this.monto = monto;
            this.fechaEmision = fechaEmision;
        }
        public String getNumero() { return numero; }
        public String getTipo() { return tipo; }
        public BigDecimal getMonto() { return monto; }
        public LocalDateTime getFechaEmision() { return fechaEmision; }
    }
}

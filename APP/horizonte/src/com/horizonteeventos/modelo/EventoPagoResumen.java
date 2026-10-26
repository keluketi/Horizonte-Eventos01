package com.horizonteeventos.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Información resumida de un evento del historial financiero del cliente. */
public class EventoPagoResumen {
    private final int idEvento;
    private final String tipo;
    private final LocalDate fecha;
    private final String espacio;
    private final String estado;
    private final BigDecimal total;
    private final BigDecimal sena;
    private final BigDecimal pagado;

    public EventoPagoResumen(int idEvento, String tipo, LocalDate fecha, String espacio,
            String estado, BigDecimal total, BigDecimal sena, BigDecimal pagado) {
        this.idEvento = idEvento;
        this.tipo = tipo;
        this.fecha = fecha;
        this.espacio = espacio;
        this.estado = estado;
        this.total = total == null ? BigDecimal.ZERO : total;
        this.sena = sena == null ? BigDecimal.ZERO : sena;
        this.pagado = pagado == null ? BigDecimal.ZERO : pagado;
    }

    public int getIdEvento() { return idEvento; }
    public String getTipo() { return tipo; }
    public LocalDate getFecha() { return fecha; }
    public String getEspacio() { return espacio; }
    public String getEstado() { return estado; }
    public BigDecimal getTotal() { return total; }
    public BigDecimal getSena() { return sena; }
    public BigDecimal getPagado() { return pagado; }
    public BigDecimal getSaldo() { return total.subtract(pagado); }
}
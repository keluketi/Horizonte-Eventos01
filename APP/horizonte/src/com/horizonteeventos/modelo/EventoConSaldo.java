package com.horizonteeventos.modelo;

import java.math.BigDecimal;

/** Evento con presupuesto aprobado, seña acordada y saldo disponible para cobrar. */
public class EventoConSaldo {
    private final int idEvento;
    private final String cliente;
    private final String estado;
    private final BigDecimal total;
    private final BigDecimal sena;
    private final BigDecimal pagado;
    private final BigDecimal saldo;

    public EventoConSaldo(int idEvento, String cliente, String estado, BigDecimal total,
            BigDecimal sena, BigDecimal pagado) {
        this.idEvento = idEvento;
        this.cliente = cliente;
        this.estado = estado;
        this.total = total;
        this.sena = sena;
        this.pagado = pagado;
        this.saldo = total.subtract(pagado);
    }

    public int getIdEvento() { return idEvento; }
    public String getCliente() { return cliente; }
    public String getEstado() { return estado; }
    public BigDecimal getTotal() { return total; }
    public BigDecimal getSena() { return sena; }
    public BigDecimal getPagado() { return pagado; }
    public BigDecimal getSaldo() { return saldo; }

    @Override public String toString() {
        return "Evento #" + idEvento + " · " + cliente + " · " + estado
                + " · saldo $ " + saldo.toPlainString();
    }
}

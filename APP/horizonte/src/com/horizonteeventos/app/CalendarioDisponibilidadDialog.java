package com.horizonteeventos.app;

import com.horizonteeventos.dao.EventoDAO;
import com.horizonteeventos.modelo.Evento;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/** Calendario mensual que distingue días libres de días con eventos agendados. */
public class CalendarioDisponibilidadDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private static final Color AZUL = new Color(18, 51, 81);
    private static final Color VERDE = new Color(218, 242, 224);
    private static final Color NARANJA = new Color(255, 235, 205);
    private static final Color ROJO = new Color(255, 215, 215);

    private final EventoDAO eventoDAO;
    private final JPanel dias = new JPanel(new GridLayout(0, 7, 5, 5));
    private final JLabel mesLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel estadoLabel = new JLabel("Verde: libre · Naranja: hay eventos · Rojo: en algún horario se alcanzan 3 simultáneos");
    private final JButton mesAnterior = new JButton("‹");
    private YearMonth mesVisible;
    private LocalDate fechaSeleccionada;
    private boolean agendaCargada;
    private List<Evento> eventosDelMes = new ArrayList<>();

    public CalendarioDisponibilidadDialog(JFrame propietario, EventoDAO eventoDAO, LocalDate inicial) {
        super(propietario, "Elegir fecha del evento", true);
        this.eventoDAO = eventoDAO;
        this.fechaSeleccionada = inicial == null ? LocalDate.now() : inicial;
        this.mesVisible = YearMonth.from(fechaSeleccionada);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(610, 470);
        setLocationRelativeTo(propietario);
        construirInterfaz();
        mostrarMes();
    }

    public LocalDate getFechaSeleccionada() { return fechaSeleccionada; }

    private void construirInterfaz() {
        setLayout(new BorderLayout(10, 10));
        JPanel cabecera = new JPanel(new BorderLayout(8, 0));
        JButton siguiente = new JButton("›");
        mesAnterior.addActionListener(e -> {
            YearMonth actual = YearMonth.from(LocalDate.now());
            if (mesVisible.isAfter(actual)) { mesVisible = mesVisible.minusMonths(1); mostrarMes(); }
        });
        siguiente.addActionListener(e -> { mesVisible = mesVisible.plusMonths(1); mostrarMes(); });
        mesLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        cabecera.add(mesAnterior, BorderLayout.WEST);
        cabecera.add(mesLabel, BorderLayout.CENTER);
        cabecera.add(siguiente, BorderLayout.EAST);
        cabecera.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));
        add(cabecera, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(4, 4));
        JPanel encabezados = new JPanel(new GridLayout(1, 7, 5, 5));
        for (DayOfWeek dia : DayOfWeek.values()) {
            JLabel etiqueta = new JLabel(dia.getDisplayName(TextStyle.SHORT, new Locale("es", "AR")), SwingConstants.CENTER);
            etiqueta.setForeground(AZUL);
            etiqueta.setFont(etiqueta.getFont().deriveFont(Font.BOLD));
            encabezados.add(etiqueta);
        }
        centro.add(encabezados, BorderLayout.NORTH);
        dias.setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 5));
        centro.add(dias, BorderLayout.CENTER);
        centro.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        add(centro, BorderLayout.CENTER);

        estadoLabel.setBorder(BorderFactory.createEmptyBorder(0, 14, 12, 14));
        add(estadoLabel, BorderLayout.SOUTH);
    }

    private void mostrarMes() {
        mesLabel.setText(mesVisible.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "AR"))
                + " " + mesVisible.getYear());
        dias.removeAll();
        agendaCargada = true;
        estadoLabel.setText("Verde: libre · Naranja: hay eventos · Rojo: en algún horario se alcanzan 3 simultáneos");
        try {
            eventosDelMes = eventoDAO.obtenerEventosEntreFechas(mesVisible.atDay(1), mesVisible.atEndOfMonth());
        } catch (SQLException ex) {
            agendaCargada = false;
            eventosDelMes = new ArrayList<>();
            estadoLabel.setText("No se pudo cargar la agenda; no selecciones fechas hasta restablecer la conexión.");
            JOptionPane.showMessageDialog(this, "No se pudo consultar la disponibilidad: " + ex.getMessage(),
                    "Error de conexión", JOptionPane.ERROR_MESSAGE);
        }

        int espaciosVacios = mesVisible.atDay(1).getDayOfWeek().getValue() - 1;
        for (int i = 0; i < espaciosVacios; i++) dias.add(new JLabel(""));
        for (int dia = 1; dia <= mesVisible.lengthOfMonth(); dia++) {
            LocalDate fecha = mesVisible.atDay(dia);
            List<Evento> delDia = eventosEn(fecha);
            int pico = maximoSimultaneo(delDia);
            String detalle = detalleDia(delDia);
            JButton botonDia = new JButton("<html><center>" + dia + (delDia.isEmpty() ? "<br>Libre" :
                    "<br>" + delDia.size() + (delDia.size() == 1 ? " evento" : " eventos")) + "</center></html>");
            botonDia.setOpaque(true);
            botonDia.setBackground(delDia.isEmpty() ? VERDE : (pico >= 3 ? ROJO : NARANJA));
            botonDia.setToolTipText("<html>" + fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    + "<br>" + (detalle.isEmpty() ? "Sin eventos agendados" : detalle.replace("\n", "<br>"))
                    + "<br>Pico de simultáneos: " + pico + "/3</html>");
            botonDia.setEnabled(!fecha.isBefore(LocalDate.now()) && agendaCargada);
            botonDia.addActionListener(e -> { fechaSeleccionada = fecha; dispose(); });
            dias.add(botonDia);
        }
        dias.revalidate();
        dias.repaint();
        boolean mesActual = mesVisible.equals(YearMonth.from(LocalDate.now()));
        mesAnterior.setEnabled(!mesActual);
    }

    private List<Evento> eventosEn(LocalDate fecha) {
        List<Evento> resultado = new ArrayList<>();
        for (Evento evento : eventosDelMes) if (evento.getFechaEvento().equals(fecha)) resultado.add(evento);
        return resultado;
    }

    private int maximoSimultaneo(List<Evento> eventos) {
        List<Punto> puntos = new ArrayList<>();
        for (Evento evento : eventos) {
            puntos.add(new Punto(evento.getHoraInicio(), 1));
            puntos.add(new Punto(evento.getHoraFin(), -1));
        }
        puntos.sort(Comparator.comparing((Punto p) -> p.hora).thenComparingInt(p -> p.cambio));
        int activos = 0;
        int maximo = 0;
        for (int i = 0; i < puntos.size();) {
            LocalTime hora = puntos.get(i).hora;
            int cambio = 0;
            while (i < puntos.size() && puntos.get(i).hora.equals(hora)) cambio += puntos.get(i++).cambio;
            activos += cambio;
            maximo = Math.max(maximo, activos);
        }
        return maximo;
    }

    private String detalleDia(List<Evento> eventos) {
        StringBuilder texto = new StringBuilder();
        for (Evento evento : eventos) {
            if (texto.length() > 0) texto.append("\n");
            texto.append(evento.getHoraInicio()).append("- ").append(evento.getHoraFin())
                    .append(" · Salón #").append(evento.getIdEspacio());
        }
        return texto.toString();
    }

    private static final class Punto {
        private final LocalTime hora;
        private final int cambio;
        private Punto(LocalTime hora, int cambio) { this.hora = hora; this.cambio = cambio; }
    }
}

package com.horizonteeventos.app;

import com.horizonteeventos.dao.CoordinacionDAO;
import com.horizonteeventos.dao.impl.CoordinacionDAOImpl;
import com.horizonteeventos.modelo.Empleado;
import com.horizonteeventos.modelo.EventoCoordinacion;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTable;
import javax.swing.SpinnerDateModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

/** Panel de Coordinación con calendario, recursos, cambios e incidencias operativas. */
public class CoordinacionFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");
    private static final String DASHBOARD = "Dashboard";
    private final CoordinacionDAO dao = new CoordinacionDAOImpl();
    private final Empleado usuario;
    private final java.awt.CardLayout tarjetas = new java.awt.CardLayout();
    private final JPanel contenido = new JPanel(tarjetas);
    private final JSpinner selectorFecha = new JSpinner(new SpinnerDateModel(new Date(), null, null,
            java.util.Calendar.DAY_OF_MONTH));

    private final DefaultTableModel modeloAgenda = modelo("ID evento", "Fecha", "Inicio", "Fin",
            "Salón", "Estado", "Invitados", "Asistencia real");
    private final DefaultTableModel modeloConfirmados = modelo("ID evento", "Fecha", "Inicio", "Fin",
            "Cliente", "Salón", "Invitados", "Asistencia real");
    private final DefaultTableModel modeloPersonal = modelo("Evento", "Fecha", "Empleado", "Función",
            "Tarea", "Horario asignado", "Estado");
    private final DefaultTableModel modeloServicios = modelo("Evento", "Fecha", "Servicio", "Proveedor", "Cantidad");
    private final DefaultTableModel modeloCambios = modelo("ID", "Evento", "Fecha", "Modificación");
    private final DefaultTableModel modeloIncidencias = modelo("ID", "Evento", "Fecha", "Descripción", "Reportada por");
    private final JTable tablaAgenda = tabla(modeloAgenda);
    private final JTable tablaConfirmados = tabla(modeloConfirmados);
    private final JTable tablaPersonal = tabla(modeloPersonal);
    private final JTable tablaServicios = tabla(modeloServicios);
    private final JTable tablaCambios = tabla(modeloCambios);
    private final JTable tablaIncidencias = tabla(modeloIncidencias);
    private final JLabel eventosHoy = new JLabel("—", JLabel.CENTER);
    private final JLabel eventosMes = new JLabel("—", JLabel.CENTER);
    private final JLabel incidentesAbiertos = new JLabel("—", JLabel.CENTER);
    private final JLabel resumenAgenda = new JLabel(" ");
    private final JLabel resumenEventos = new JLabel(" ");

    public CoordinacionFrame(Empleado usuario) {
        super("Horizonte Eventos - Panel de Coordinación");
        AccesibilidadUI.aplicar();
        this.usuario = usuario;
        RegistroAccionesUI.iniciar();
        RegistroAccionesUI.establecerUsuario(usuario);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1100, 680));
        setSize(1200, 720);
        setLocationRelativeTo(null);

        JPanel raiz = new JPanel(new BorderLayout(12, 12));
        raiz.setBackground(PanelRolUI.FONDO);
        raiz.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        raiz.add(PanelRolUI.crearEncabezado("Coordinación", usuario, this::cerrarSesion), BorderLayout.NORTH);
        String[] menu = {DASHBOARD, "Calendario", "Eventos confirmados", "Recursos",
                "Modificaciones", "Incidencias"};
        raiz.add(PanelRolUI.crearNavegacion("Menú de Coordinación", menu, this::mostrarModulo), BorderLayout.WEST);
        contenido.setOpaque(false);
        raiz.add(contenido, BorderLayout.CENTER);
        setContentPane(raiz);

        contenido.add(crearDashboard(), DASHBOARD);
        contenido.add(crearPanelCalendario(), "Calendario");
        contenido.add(crearPanelEventosConfirmados(), "Eventos confirmados");
        contenido.add(crearPanelRecursos(), "Recursos");
        contenido.add(crearPanelCambios(), "Modificaciones");
        contenido.add(crearPanelIncidencias(), "Incidencias");
        tarjetas.show(contenido, DASHBOARD);
        cargarDashboard();
        cargarAgenda();
        cargarConfirmados();
    }

    private JPanel crearDashboard() {
        JPanel panel = PanelRolUI.crearModulo("Panel de Coordinación",
                "Agenda operativa, disponibilidad, personal, proveedores e incidencias.");
        JPanel cuerpo = new JPanel(new BorderLayout(12, 16));
        cuerpo.setOpaque(false);
        JPanel kpis = new JPanel(new GridLayout(1, 3, 12, 12));
        kpis.setOpaque(false);
        kpis.add(tarjetaConValor("Eventos de hoy", eventosHoy, PanelRolUI.AZUL));
        kpis.add(tarjetaConValor("Eventos del mes", eventosMes, PanelRolUI.AZUL_CLARO));
        kpis.add(tarjetaConValor("Incidencias registradas", incidentesAbiertos, PanelRolUI.NARANJA));
        cuerpo.add(kpis, BorderLayout.NORTH);
        JPanel ayuda = PanelRolUI.crearPanelBlanco();
        JTextArea resumen = new JTextArea("Desde este panel podés consultar el calendario y los eventos confirmados, "
                + "ver el personal y los servicios asignados, revisar modificaciones y reportar incidencias. "
                + "La información financiera no forma parte de este módulo.");
        resumen.setEditable(false);
        resumen.setLineWrap(true);
        resumen.setWrapStyleWord(true);
        resumen.setFont(new java.awt.Font("Verdana", java.awt.Font.PLAIN, 15));
        resumen.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        ayuda.add(resumen, BorderLayout.CENTER);
        cuerpo.add(ayuda, BorderLayout.CENTER);
        panel.add(cuerpo, BorderLayout.CENTER);
        return panel;
    }

    private JPanel tarjetaConValor(String titulo, JLabel valor, java.awt.Color acento) {
        JPanel tarjeta = PanelRolUI.crearTarjeta(titulo, "", acento);
        tarjeta.remove(tarjeta.getComponent(1));
        valor.setFont(new java.awt.Font("Verdana", java.awt.Font.BOLD, 25));
        valor.setForeground(PanelRolUI.AZUL);
        tarjeta.add(valor, BorderLayout.CENTER);
        return tarjeta;
    }

    private JPanel crearPanelCalendario() {
        JPanel panel = PanelRolUI.crearModulo("Calendario operativo",
                "Seleccioná un día para consultar ocupación, horarios y cantidad de invitados.");
        JPanel cuerpo = PanelRolUI.crearPanelBlanco();
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        controles.setOpaque(false);
        selectorFecha.setEditor(new JSpinner.DateEditor(selectorFecha, "dd/MM/yyyy"));
        JButton anterior = PanelRolUI.boton("Día anterior", PanelRolUI.AZUL_CLARO);
        JButton hoy = PanelRolUI.boton("Hoy", PanelRolUI.AZUL);
        JButton siguiente = PanelRolUI.boton("Día siguiente", PanelRolUI.AZUL_CLARO);
        JButton actualizar = PanelRolUI.boton("Actualizar", PanelRolUI.NARANJA);
        controles.add(anterior); controles.add(hoy); controles.add(siguiente);
        controles.add(new JLabel("Fecha:")); controles.add(selectorFecha); controles.add(actualizar);
        cuerpo.add(controles, BorderLayout.NORTH);
        JPanel listaAgenda = new JPanel(new BorderLayout(0, 8));
        listaAgenda.setOpaque(false);
        listaAgenda.add(PanelRolUI.crearBarraFiltroOrden(tablaAgenda), BorderLayout.NORTH);
        listaAgenda.add(new JScrollPane(tablaAgenda), BorderLayout.CENTER);
        cuerpo.add(listaAgenda, BorderLayout.CENTER);
        resumenAgenda.setFont(new java.awt.Font("Verdana", java.awt.Font.BOLD, 14));
        resumenAgenda.setBorder(BorderFactory.createEmptyBorder(8, 4, 2, 4));
        cuerpo.add(resumenAgenda, BorderLayout.SOUTH);
        panel.add(cuerpo, BorderLayout.CENTER);
        selectorFecha.addChangeListener(e -> cargarAgenda());
        anterior.addActionListener(e -> moverFecha(-1));
        siguiente.addActionListener(e -> moverFecha(1));
        hoy.addActionListener(e -> establecerFecha(LocalDate.now()));
        actualizar.addActionListener(e -> cargarAgenda());
        return panel;
    }

    private JPanel crearPanelEventosConfirmados() {
        JPanel panel = PanelRolUI.crearModulo("Eventos confirmados",
                "Eventos listos para coordinar: horarios, salón e invitados previstos y reales.");
        JPanel cuerpo = PanelRolUI.crearPanelBlanco();
        JButton actualizar = PanelRolUI.boton("Actualizar eventos", PanelRolUI.AZUL_CLARO);
        JPanel pie = new JPanel(new BorderLayout(8, 8));
        pie.setOpaque(false);
        pie.add(resumenEventos, BorderLayout.CENTER);
        pie.add(actualizar, BorderLayout.EAST);
        cuerpo.add(PanelRolUI.crearListaFiltrable(tablaConfirmados), BorderLayout.CENTER);
        cuerpo.add(pie, BorderLayout.SOUTH);
        panel.add(cuerpo, BorderLayout.CENTER);
        tablaConfirmados.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int filaVista = tablaConfirmados.rowAtPoint(e.getPoint());
                if (filaVista < 0) return;
                int fila = tablaConfirmados.convertRowIndexToModel(filaVista);
                JOptionPane.showMessageDialog(CoordinacionFrame.this,
                        "Evento #" + modeloConfirmados.getValueAt(fila, 0)
                        + "\nFecha: " + modeloConfirmados.getValueAt(fila, 1)
                        + "\nHorario: " + modeloConfirmados.getValueAt(fila, 2) + " - "
                        + modeloConfirmados.getValueAt(fila, 3)
                        + "\nCliente de contacto: " + modeloConfirmados.getValueAt(fila, 4)
                        + "\nSalón: " + modeloConfirmados.getValueAt(fila, 5)
                        + "\nInvitados previstos: " + modeloConfirmados.getValueAt(fila, 6)
                        + "\nAsistencia real: " + modeloConfirmados.getValueAt(fila, 7),
                        "Detalle operativo del evento", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        actualizar.addActionListener(e -> cargarConfirmados());
        return panel;
    }

    private JPanel crearPanelRecursos() {
        JPanel panel = PanelRolUI.crearModulo("Personal y servicios asignados",
                "Consulta quién trabaja en cada evento y qué servicios externos o propios se contrataron.");
        JPanel cuerpo = PanelRolUI.crearPanelBlanco();
        javax.swing.JTabbedPane pestanas = new javax.swing.JTabbedPane();
        pestanas.setFont(new java.awt.Font("Verdana", java.awt.Font.BOLD, 14));
        pestanas.addTab("Personal asignado", PanelRolUI.crearListaFiltrable(tablaPersonal));
        pestanas.addTab("Servicios contratados", PanelRolUI.crearListaFiltrable(tablaServicios));
        cuerpo.add(pestanas, BorderLayout.CENTER);
        JButton actualizar = PanelRolUI.boton("Actualizar recursos", PanelRolUI.AZUL_CLARO);
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pie.setOpaque(false);
        pie.add(actualizar);
        cuerpo.add(pie, BorderLayout.SOUTH);
        panel.add(cuerpo, BorderLayout.CENTER);
        actualizar.addActionListener(e -> cargarRecursos());
        return panel;
    }

    private JPanel crearPanelCambios() {
        JPanel panel = PanelRolUI.crearModulo("Historial de modificaciones",
                "Revisá cambios registrados en los eventos y la fecha en que se realizaron.");
        JPanel cuerpo = PanelRolUI.crearPanelBlanco();
        cuerpo.add(PanelRolUI.crearListaFiltrable(tablaCambios), BorderLayout.CENTER);
        JButton actualizar = PanelRolUI.boton("Actualizar historial", PanelRolUI.AZUL_CLARO);
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pie.setOpaque(false);
        pie.add(actualizar);
        cuerpo.add(pie, BorderLayout.SOUTH);
        panel.add(cuerpo, BorderLayout.CENTER);
        actualizar.addActionListener(e -> cargarCambios());
        return panel;
    }

    private JPanel crearPanelIncidencias() {
        JPanel panel = PanelRolUI.crearModulo("Incidencias operativas",
                "Registrá y consultá problemas, roturas o necesidades técnicas reportadas durante los eventos.");
        JPanel cuerpo = PanelRolUI.crearPanelBlanco();
        cuerpo.add(PanelRolUI.crearListaFiltrable(tablaIncidencias), BorderLayout.CENTER);
        JButton nueva = PanelRolUI.boton("Registrar incidencia", PanelRolUI.NARANJA);
        JButton actualizar = PanelRolUI.boton("Actualizar", PanelRolUI.AZUL_CLARO);
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        pie.setOpaque(false);
        pie.add(actualizar); pie.add(nueva);
        cuerpo.add(pie, BorderLayout.SOUTH);
        panel.add(cuerpo, BorderLayout.CENTER);
        nueva.addActionListener(e -> registrarIncidencia());
        actualizar.addActionListener(e -> cargarIncidencias());
        return panel;
    }

    private void mostrarModulo(String modulo) {
        tarjetas.show(contenido, modulo);
        if ("Calendario".equals(modulo)) cargarAgenda();
        else if ("Eventos confirmados".equals(modulo)) cargarConfirmados();
        else if ("Recursos".equals(modulo)) cargarRecursos();
        else if ("Modificaciones".equals(modulo)) cargarCambios();
        else if ("Incidencias".equals(modulo)) cargarIncidencias();
        else if (DASHBOARD.equals(modulo)) cargarDashboard();
    }

    private void cargarDashboard() {
        try {
            List<EventoCoordinacion> eventos = dao.listarEventos();
            int hoy = 0;
            int mes = 0;
            for (EventoCoordinacion evento : eventos) {
                if (evento.getFecha().equals(LocalDate.now())) hoy++;
                if (evento.getFecha().getYear() == LocalDate.now().getYear()
                        && evento.getFecha().getMonth() == LocalDate.now().getMonth()) mes++;
            }
            eventosHoy.setText(String.valueOf(hoy));
            eventosMes.setText(String.valueOf(mes));
            incidentesAbiertos.setText(String.valueOf(dao.listarIncidencias().size()));
        } catch (SQLException ex) {
            mostrarError("No se pudo cargar el resumen operativo. " + ex.getMessage());
        }
    }

    private void cargarAgenda() {
        try {
            LocalDate fecha = obtenerFecha();
            List<EventoCoordinacion> eventos = dao.listarEventosPorFecha(fecha);
            llenarEventos(modeloAgenda, eventos, false);
            int invitados = 0;
            for (EventoCoordinacion evento : eventos) invitados += evento.getCantidadInvitados();
            int maximoSimultaneo = contarSimultaneos(eventos);
            resumenAgenda.setText("Agenda del " + fecha.format(FECHA) + " · " + eventos.size()
                    + " eventos · " + invitados + " invitados previstos · pico simultáneo: "
                    + maximoSimultaneo + " de 3");
        } catch (SQLException ex) {
            mostrarError("No se pudo cargar el calendario. " + ex.getMessage());
        }
    }

    private void cargarConfirmados() {
        try {
            List<EventoCoordinacion> eventos = dao.listarEventosConfirmados();
            llenarEventos(modeloConfirmados, eventos, true);
            resumenEventos.setText("Total: " + eventos.size() + " eventos confirmados.");
        } catch (SQLException ex) {
            mostrarError("No se pudieron cargar los eventos confirmados. " + ex.getMessage());
        }
    }

    private void llenarEventos(DefaultTableModel modelo, List<EventoCoordinacion> eventos, boolean incluirCliente) {
        modelo.setRowCount(0);
        for (EventoCoordinacion evento : eventos) {
            Object asistencia = evento.getAsistenciaReal() == null ? "Pendiente" : evento.getAsistenciaReal();
            if (incluirCliente) {
                modelo.addRow(new Object[] {evento.getIdEvento(), evento.getFecha().format(FECHA),
                        evento.getHoraInicio().format(HORA), evento.getHoraFin().format(HORA),
                        evento.getCliente(), evento.getEspacio(), evento.getCantidadInvitados(), asistencia});
            } else {
                modelo.addRow(new Object[] {evento.getIdEvento(), evento.getFecha().format(FECHA),
                        evento.getHoraInicio().format(HORA), evento.getHoraFin().format(HORA),
                        evento.getEspacio(), evento.getEstado(), evento.getCantidadInvitados(), asistencia});
            }
        }
    }

    private void cargarRecursos() {
        try {
            List<Object[]> personal = dao.listarPersonalAsignado();
            modeloPersonal.setRowCount(0);
            for (Object[] fila : personal) {
                modeloPersonal.addRow(new Object[] {fila[0], fila[1], fila[2], fila[3], fila[4],
                        String.valueOf(fila[5]) + " - " + String.valueOf(fila[6]), fila[7]});
            }
            llenarTabla(modeloServicios, dao.listarServiciosContratados());
        } catch (SQLException ex) {
            mostrarError("No se pudieron cargar las asignaciones y servicios. " + ex.getMessage());
        }
    }

    private void cargarCambios() {
        try { llenarTabla(modeloCambios, dao.listarModificaciones()); }
        catch (SQLException ex) { mostrarError("No se pudo cargar el historial. " + ex.getMessage()); }
    }

    private void cargarIncidencias() {
        try { llenarTabla(modeloIncidencias, dao.listarIncidencias()); }
        catch (SQLException ex) { mostrarError("No se pudieron cargar las incidencias. " + ex.getMessage()); }
    }

    private void registrarIncidencia() {
        try {
            List<EventoCoordinacion> eventos = dao.listarEventos();
            if (eventos.isEmpty()) { mostrarError("No hay eventos disponibles para asociar la incidencia."); return; }
            JComboBox<EventoOpcion> selector = new JComboBox<>();
            for (EventoCoordinacion evento : eventos) selector.addItem(new EventoOpcion(evento));
            JTextArea descripcion = new JTextArea(5, 32);
            descripcion.setLineWrap(true);
            descripcion.setWrapStyleWord(true);
            JPanel formulario = new JPanel(new GridLayout(0, 1, 6, 6));
            formulario.add(new JLabel("Evento:"));
            formulario.add(selector);
            formulario.add(new JLabel("Descripción de la incidencia:"));
            formulario.add(new JScrollPane(descripcion));
            int opcion = JOptionPane.showConfirmDialog(this, formulario, "Registrar incidencia",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (opcion != JOptionPane.OK_OPTION) return;
            if (descripcion.getText().trim().isEmpty()) {
                mostrarError("Escribí una descripción para la incidencia.");
                return;
            }
            EventoOpcion elegido = (EventoOpcion) selector.getSelectedItem();
            if (elegido != null && dao.registrarIncidencia(elegido.evento.getIdEvento(),
                    descripcion.getText().trim(), usuario.getNombre() + " " + usuario.getApellido())) {
                cargarIncidencias();
                mostrarModulo("Incidencias");
                JOptionPane.showMessageDialog(this, "Incidencia registrada.");
            }
        } catch (SQLException ex) {
            mostrarError("No se pudo registrar la incidencia. " + ex.getMessage());
        }
    }

    private void llenarTabla(DefaultTableModel modelo, List<Object[]> filas) {
        modelo.setRowCount(0);
        for (Object[] fila : filas) modelo.addRow(fila);
    }

    private int contarSimultaneos(List<EventoCoordinacion> eventos) {
        int maximo = 0;
        for (EventoCoordinacion punto : eventos) {
            int simultaneos = 0;
            for (EventoCoordinacion evento : eventos) {
                if (evento.getHoraInicio().isBefore(punto.getHoraFin())
                        && evento.getHoraFin().isAfter(punto.getHoraInicio())) simultaneos++;
            }
            maximo = Math.max(maximo, simultaneos);
        }
        return maximo;
    }

    private LocalDate obtenerFecha() {
        Date fecha = (Date) selectorFecha.getValue();
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void establecerFecha(LocalDate fecha) {
        selectorFecha.setValue(Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    private void moverFecha(int dias) { establecerFecha(obtenerFecha().plusDays(dias)); }

    private void cerrarSesion() { PanelRolUI.confirmarCerrarSesion(this); }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Coordinación", JOptionPane.ERROR_MESSAGE);
    }

    private static DefaultTableModel modelo(String... columnas) {
        return new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
        };
    }

    private static JTable tabla(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        PanelRolUI.estilizarTabla(tabla);
        tabla.setAutoCreateRowSorter(true);
        return tabla;
    }

    private static class EventoOpcion {
        private final EventoCoordinacion evento;
        private EventoOpcion(EventoCoordinacion evento) { this.evento = evento; }
        @Override public String toString() {
            return "#" + evento.getIdEvento() + " · " + evento.getFecha().format(FECHA)
                    + " · " + evento.getEspacio() + " · " + evento.getCliente();
        }
    }
}

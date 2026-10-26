package com.horizonteeventos.app;

import com.horizonteeventos.dao.PersonalDAO;
import com.horizonteeventos.dao.impl.PersonalDAOImpl;
import com.horizonteeventos.modelo.AsignacionPersonal;
import com.horizonteeventos.modelo.Empleado;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
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
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTable;

/** Panel personal para consultar eventos, turnos, tareas y reportar incidencias. */
public class PersonalFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");
    private static final String DASHBOARD = "Dashboard";

    private final Empleado usuario;
    private final PersonalDAO dao = new PersonalDAOImpl();
    private final java.awt.CardLayout tarjetas = new java.awt.CardLayout();
    private final JPanel contenido = new JPanel(tarjetas);
    private final List<AsignacionPersonal> asignacionesActuales = new ArrayList<>();
    private final JSpinner selectorFecha = new JSpinner(new SpinnerDateModel(new Date(), null, null,
            java.util.Calendar.DAY_OF_MONTH));
    private final DefaultTableModel modeloEventos = modelo("Asignación", "Evento", "Fecha", "Horario asignado",
            "Función", "Tarea", "Inicio real", "Fin real", "Estado");
    private final DefaultTableModel modeloCronograma = modelo("Asignación", "Evento", "Horario", "Función", "Tarea", "Estado");
    private final DefaultTableModel modeloTareas = modelo("Asignación", "Evento", "Fecha", "Tarea", "Horario", "Estado", "Observaciones");
    private final DefaultTableModel modeloIncidencias = modelo("ID", "Evento", "Fecha", "Descripción", "Reportada por");
    private final JTable tablaEventos = tabla(modeloEventos);
    private final JTable tablaCronograma = tabla(modeloCronograma);
    private final JTable tablaTareas = tabla(modeloTareas);
    private final JTable tablaIncidencias = tabla(modeloIncidencias);
    private final JLabel lblProximos = new JLabel("—", JLabel.CENTER);
    private final JLabel lblPendientes = new JLabel("—", JLabel.CENTER);
    private final JLabel lblCompletados = new JLabel("—", JLabel.CENTER);
    private final JLabel resumenCronograma = new JLabel(" ");

    public PersonalFrame(Empleado usuario) {
        super("Horizonte Eventos - Panel de Personal");
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
        raiz.add(PanelRolUI.crearEncabezado("Personal", usuario, this::cerrarSesion), BorderLayout.NORTH);
        String[] menu = {DASHBOARD, "Mis eventos", "Cronograma", "Mis tareas", "Incidencias"};
        raiz.add(PanelRolUI.crearNavegacion("Menú de Personal", menu, this::mostrarModulo), BorderLayout.WEST);
        contenido.setOpaque(false);
        raiz.add(contenido, BorderLayout.CENTER);
        setContentPane(raiz);

        contenido.add(crearDashboard(), DASHBOARD);
        contenido.add(crearPanelEventos(), "Mis eventos");
        contenido.add(crearPanelCronograma(), "Cronograma");
        contenido.add(crearPanelTareas(), "Mis tareas");
        contenido.add(crearPanelIncidencias(), "Incidencias");
        tarjetas.show(contenido, DASHBOARD);
        cargarAsignaciones();
        cargarIncidencias();
    }

    private JPanel crearDashboard() {
        JPanel panel = PanelRolUI.crearModulo("Mi panel de trabajo",
                "Información personal de los eventos a los que fuiste asignado/a.");
        JPanel cuerpo = new JPanel(new BorderLayout(12, 16));
        cuerpo.setOpaque(false);
        JPanel tarjetasResumen = new JPanel(new GridLayout(1, 3, 12, 12));
        tarjetasResumen.setOpaque(false);
        tarjetasResumen.add(tarjetaConValor("Próximos eventos", lblProximos, PanelRolUI.AZUL));
        tarjetasResumen.add(tarjetaConValor("Tareas pendientes", lblPendientes, PanelRolUI.AZUL_CLARO));
        tarjetasResumen.add(tarjetaConValor("Tareas completadas", lblCompletados, PanelRolUI.NARANJA));
        cuerpo.add(tarjetasResumen, BorderLayout.NORTH);
        JPanel aviso = PanelRolUI.crearPanelBlanco();
        JTextArea texto = new JTextArea("Usá el menú para revisar tus eventos, horarios de presentación y tareas. "
                + "Desde Incidencias podés informar un problema del evento. Este módulo no muestra datos privados "
                + "del cliente ni información de presupuestos o pagos.");
        texto.setEditable(false);
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);
        texto.setFont(new java.awt.Font("Verdana", java.awt.Font.PLAIN, 15));
        texto.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        aviso.add(texto, BorderLayout.CENTER);
        cuerpo.add(aviso, BorderLayout.CENTER);
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

    private JPanel crearPanelEventos() {
        JPanel panel = PanelRolUI.crearModulo("Mis eventos asignados",
                "Consultá salón, fecha, turno, función y estado de tus asignaciones.");
        JPanel cuerpo = PanelRolUI.crearPanelBlanco();
        cuerpo.add(PanelRolUI.crearListaFiltrable(tablaEventos), BorderLayout.CENTER);
        JButton actualizar = PanelRolUI.boton("Actualizar", PanelRolUI.AZUL_CLARO);
        JButton registrar = PanelRolUI.boton("Registrar horas / observación", PanelRolUI.NARANJA);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        acciones.setOpaque(false);
        acciones.add(actualizar);
        acciones.add(registrar);
        cuerpo.add(acciones, BorderLayout.SOUTH);
        panel.add(cuerpo, BorderLayout.CENTER);
        actualizar.addActionListener(e -> cargarAsignaciones());
        registrar.addActionListener(e -> registrarTrabajo());
        return panel;
    }

    private JPanel crearPanelCronograma() {
        JPanel panel = PanelRolUI.crearModulo("Mi cronograma",
                "Elegí una fecha para ver a qué hora y en qué función tenés que presentarte.");
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
        JPanel listaCronograma = new JPanel(new BorderLayout(0, 8));
        listaCronograma.setOpaque(false);
        listaCronograma.add(PanelRolUI.crearBarraFiltroOrden(tablaCronograma), BorderLayout.NORTH);
        listaCronograma.add(new JScrollPane(tablaCronograma), BorderLayout.CENTER);
        cuerpo.add(listaCronograma, BorderLayout.CENTER);
        resumenCronograma.setFont(new java.awt.Font("Verdana", java.awt.Font.BOLD, 14));
        resumenCronograma.setBorder(BorderFactory.createEmptyBorder(8, 4, 2, 4));
        cuerpo.add(resumenCronograma, BorderLayout.SOUTH);
        panel.add(cuerpo, BorderLayout.CENTER);
        selectorFecha.addChangeListener(e -> cargarCronograma());
        anterior.addActionListener(e -> moverFecha(-1));
        siguiente.addActionListener(e -> moverFecha(1));
        hoy.addActionListener(e -> establecerFecha(LocalDate.now()));
        actualizar.addActionListener(e -> cargarCronograma());
        return panel;
    }

    private JPanel crearPanelTareas() {
        JPanel panel = PanelRolUI.crearModulo("Mis tareas",
                "Tareas y observaciones asociadas únicamente a tus propios eventos.");
        JPanel cuerpo = PanelRolUI.crearPanelBlanco();
        cuerpo.add(PanelRolUI.crearListaFiltrable(tablaTareas), BorderLayout.CENTER);
        JButton actualizar = PanelRolUI.boton("Actualizar tareas", PanelRolUI.AZUL_CLARO);
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pie.setOpaque(false);
        pie.add(actualizar);
        cuerpo.add(pie, BorderLayout.SOUTH);
        panel.add(cuerpo, BorderLayout.CENTER);
        actualizar.addActionListener(e -> cargarAsignaciones());
        return panel;
    }

    private JPanel crearPanelIncidencias() {
        JPanel panel = PanelRolUI.crearModulo("Incidencias",
                "Consultá los reportes de tus eventos y registrá problemas operativos.");
        JPanel cuerpo = PanelRolUI.crearPanelBlanco();
        cuerpo.add(PanelRolUI.crearListaFiltrable(tablaIncidencias), BorderLayout.CENTER);
        JButton actualizar = PanelRolUI.boton("Actualizar", PanelRolUI.AZUL_CLARO);
        JButton nueva = PanelRolUI.boton("Reportar incidencia", PanelRolUI.NARANJA);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 6));
        acciones.setOpaque(false);
        acciones.add(actualizar); acciones.add(nueva);
        cuerpo.add(acciones, BorderLayout.SOUTH);
        panel.add(cuerpo, BorderLayout.CENTER);
        actualizar.addActionListener(e -> cargarIncidencias());
        nueva.addActionListener(e -> reportarIncidencia());
        return panel;
    }

    private void mostrarModulo(String modulo) {
        tarjetas.show(contenido, modulo);
        if ("Cronograma".equals(modulo)) cargarCronograma();
        else if ("Mis tareas".equals(modulo)) cargarTareas();
        else if ("Mis eventos".equals(modulo)) cargarEventos();
        else if ("Incidencias".equals(modulo)) cargarIncidencias();
        else if (DASHBOARD.equals(modulo)) cargarResumen();
    }

    private void cargarAsignaciones() {
        try {
            asignacionesActuales.clear();
            asignacionesActuales.addAll(dao.listarAsignaciones(usuario.getIdEmpleado()));
            cargarEventos();
            cargarTareas();
            cargarCronograma();
            cargarResumen();
        } catch (SQLException ex) {
            mostrarError("No se pudieron cargar tus asignaciones. " + ex.getMessage());
        }
    }

    private void cargarEventos() {
        modeloEventos.setRowCount(0);
        for (AsignacionPersonal a : asignacionesActuales) {
            modeloEventos.addRow(new Object[] {a.getIdAsignacion(), a.getIdEvento(), a.getFechaEvento().format(FECHA),
                    horario(a), a.getFuncion(), a.getTarea(), mostrarHora(a.getHoraInicioReal()),
                    mostrarHora(a.getHoraFinReal()), a.getEstado()});
        }
    }

    private void cargarCronograma() {
        LocalDate fecha = obtenerFecha();
        modeloCronograma.setRowCount(0);
        int encontrados = 0;
        for (AsignacionPersonal a : asignacionesActuales) {
            if (!fecha.equals(a.getFechaEvento())) continue;
            encontrados++;
            modeloCronograma.addRow(new Object[] {a.getIdAsignacion(), a.getIdEvento(), horario(a),
                    a.getFuncion(), a.getTarea(), a.getEstado()});
        }
        resumenCronograma.setText(fecha.format(FECHA) + " · " + encontrados + " turno(s) asignado(s)");
    }

    private void cargarTareas() {
        modeloTareas.setRowCount(0);
        for (AsignacionPersonal a : asignacionesActuales) {
            modeloTareas.addRow(new Object[] {a.getIdAsignacion(), a.getIdEvento(), a.getFechaEvento().format(FECHA), a.getTarea(),
                    horario(a), a.getEstado(), a.getObservaciones() == null ? "" : a.getObservaciones()});
        }
    }

    private void cargarResumen() {
        int proximos = 0;
        int pendientes = 0;
        int completados = 0;
        LocalDate hoy = LocalDate.now();
        for (AsignacionPersonal a : asignacionesActuales) {
            if (!a.getFechaEvento().isBefore(hoy)) proximos++;
            if ("Completada".equalsIgnoreCase(a.getEstado())) completados++;
            else pendientes++;
        }
        lblProximos.setText(String.valueOf(proximos));
        lblPendientes.setText(String.valueOf(pendientes));
        lblCompletados.setText(String.valueOf(completados));
    }

    private void cargarIncidencias() {
        try {
            modeloIncidencias.setRowCount(0);
            for (Object[] incidencia : dao.listarIncidenciasAsignadas(usuario.getIdEmpleado())) {
                modeloIncidencias.addRow(new Object[] {incidencia[0], incidencia[1],
                        incidencia[2], incidencia[3], incidencia[4]});
            }
        } catch (SQLException ex) {
            mostrarError("No se pudieron cargar las incidencias de tus eventos. " + ex.getMessage());
        }
    }

    private void registrarTrabajo() {
        int fila = tablaEventos.getSelectedRow();
        if (fila < 0) { mostrarError("Selecciona una asignación de la lista."); return; }
        int filaModelo = tablaEventos.convertRowIndexToModel(fila);
        int idAsignacion = ((Number) modeloEventos.getValueAt(filaModelo, 0)).intValue();
        AsignacionPersonal asignacion = buscarAsignacion(idAsignacion);
        if (asignacion == null) return;
        if (asignacion.getIdAsignacion() < 0) {
            JOptionPane.showMessageDialog(this,
                    "Este turno proviene de una asignación general del evento. "
                    + "Coordinación debe completar la tarea y crear la asignación detallada "
                    + "antes de que puedas registrar tus horas de trabajo.",
                    "Turno pendiente de detalle", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JTextField inicio = new JTextField(mostrarHora(asignacion.getHoraInicioReal()));
        JTextField fin = new JTextField(mostrarHora(asignacion.getHoraFinReal()));
        JTextArea observaciones = new JTextArea(asignacion.getObservaciones() == null ? "" : asignacion.getObservaciones(), 4, 24);
        observaciones.setLineWrap(true);
        observaciones.setWrapStyleWord(true);
        JPanel formulario = new JPanel(new GridLayout(0, 2, 8, 8));
        formulario.add(new JLabel("Inicio real (HH:mm, opcional):")); formulario.add(inicio);
        formulario.add(new JLabel("Fin real (HH:mm, opcional):")); formulario.add(fin);
        formulario.add(new JLabel("Observación / incidencia:")); formulario.add(new JScrollPane(observaciones));
        int opcion = JOptionPane.showConfirmDialog(this, formulario, "Registrar trabajo",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) return;
        try {
            LocalTime horaInicio = parsearHora(inicio.getText());
            LocalTime horaFin = parsearHora(fin.getText());
            if (horaInicio != null && horaFin != null && !horaFin.isAfter(horaInicio)) {
                mostrarError("La hora de fin debe ser posterior a la hora de inicio.");
                return;
            }
            if (dao.registrarTrabajo(asignacion.getIdAsignacion(), usuario.getIdEmpleado(),
                    horaInicio, horaFin, observaciones.getText().trim())) {
                cargarAsignaciones();
                JOptionPane.showMessageDialog(this, "Registro actualizado correctamente.");
            } else mostrarError("No se encontró una asignación propia para actualizar.");
        } catch (DateTimeParseException ex) {
            mostrarError("Ingresa las horas en formato HH:mm, por ejemplo 18:30.");
        } catch (SQLException ex) {
            mostrarError("No se pudo guardar el registro. " + ex.getMessage());
        }
    }

    private void reportarIncidencia() {
        if (asignacionesActuales.isEmpty()) { mostrarError("Todavía no tenés eventos asignados."); return; }
        JComboBox<AsignacionOpcion> selector = new JComboBox<>();
        for (AsignacionPersonal asignacion : asignacionesActuales) selector.addItem(new AsignacionOpcion(asignacion));
        JTextArea descripcion = new JTextArea(5, 30);
        descripcion.setLineWrap(true);
        descripcion.setWrapStyleWord(true);
        JPanel formulario = new JPanel(new GridLayout(0, 1, 6, 6));
        formulario.add(new JLabel("Evento asignado:"));
        formulario.add(selector);
        formulario.add(new JLabel("Descripción:"));
        formulario.add(new JScrollPane(descripcion));
        int opcion = JOptionPane.showConfirmDialog(this, formulario, "Reportar incidencia",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) return;
        if (descripcion.getText().trim().isEmpty()) { mostrarError("Escribí qué ocurrió."); return; }
        AsignacionOpcion elegido = (AsignacionOpcion) selector.getSelectedItem();
        if (elegido == null) return;
        try {
            boolean guardada = dao.registrarIncidencia(elegido.asignacion.getIdEvento(),
                    usuario.getIdEmpleado(), descripcion.getText().trim(),
                    usuario.getNombre() + " " + usuario.getApellido());
            if (guardada) {
                cargarIncidencias();
                mostrarModulo("Incidencias");
                JOptionPane.showMessageDialog(this, "Incidencia enviada a Coordinación.");
            } else mostrarError("La incidencia solo se puede asociar a un evento asignado a tu usuario.");
        } catch (SQLException ex) {
            mostrarError("No se pudo registrar la incidencia. " + ex.getMessage());
        }
    }

    private AsignacionPersonal buscarAsignacion(int idAsignacion) {
        for (AsignacionPersonal a : asignacionesActuales) {
            if (a.getIdAsignacion() == idAsignacion) return a;
        }
        return null;
    }

    private static String horario(AsignacionPersonal a) {
        return a.getHoraInicioEvento().format(HORA) + " - " + a.getHoraFinEvento().format(HORA);
    }

    private LocalDate obtenerFecha() {
        Date fecha = (Date) selectorFecha.getValue();
        return fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private void establecerFecha(LocalDate fecha) {
        selectorFecha.setValue(Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    private void moverFecha(int dias) { establecerFecha(obtenerFecha().plusDays(dias)); }

    private static LocalTime parsearHora(String texto) {
        String valor = texto == null ? "" : texto.trim();
        return valor.isEmpty() ? null : LocalTime.parse(valor, HORA);
    }

    private static String mostrarHora(LocalTime hora) { return hora == null ? "" : hora.format(HORA); }

    private void cerrarSesion() { PanelRolUI.confirmarCerrarSesion(this); }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Personal", JOptionPane.ERROR_MESSAGE);
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
        tabla.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        return tabla;
    }

    private static class AsignacionOpcion {
        private final AsignacionPersonal asignacion;
        private AsignacionOpcion(AsignacionPersonal asignacion) { this.asignacion = asignacion; }
        @Override public String toString() {
            return "Evento #" + asignacion.getIdEvento() + " · " + asignacion.getFechaEvento().format(FECHA)
                    + " · " + asignacion.getFuncion() + " · " + asignacion.getTarea();
        }
    }
}

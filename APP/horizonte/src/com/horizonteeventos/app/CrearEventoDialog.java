package com.horizonteeventos.app;

import com.horizonteeventos.dao.CatalogoEventoDAO;
import com.horizonteeventos.dao.ClienteDAO;
import com.horizonteeventos.dao.EventoDAO;
import com.horizonteeventos.dao.impl.CatalogoEventoDAOImpl;
import com.horizonteeventos.dao.impl.ClienteDAOImpl;
import com.horizonteeventos.dao.impl.EventoDAOImpl;
import com.horizonteeventos.modelo.Cliente;
import com.horizonteeventos.modelo.Espacio;
import com.horizonteeventos.modelo.EstadoEvento;
import com.horizonteeventos.modelo.Evento;
import com.horizonteeventos.modelo.Paquete;
import com.horizonteeventos.modelo.Servicio;
import com.horizonteeventos.modelo.ServicioContratado;
import com.horizonteeventos.modelo.TipoEvento;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/** Flujo de alta presencial: elegir cliente y luego completar los datos del evento. */
public class CrearEventoDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private static final String PASO_CLIENTE = "cliente";
    private static final String PASO_EVENTO = "evento";

    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final CatalogoEventoDAO catalogoDAO = new CatalogoEventoDAOImpl();
    private final EventoDAO eventoDAO = new EventoDAOImpl();
    private final CardLayout pasos = new CardLayout();
    private final JPanel tarjetas = new JPanel(pasos);
    private final JTextField buscarCliente = new JTextField(25);
    private final DefaultTableModel modeloClientes = new DefaultTableModel(
            new Object[] {"ID", "Nombre", "Apellido", "DNI/CUIT", "Email", "Teléfono"}, 0) {
        private static final long serialVersionUID = 1L;
        @Override public boolean isCellEditable(int fila, int columna) { return false; }
    };
    private final JTable tablaClientes = new JTable(modeloClientes);
    private final JLabel clienteElegido = new JLabel("Ningún cliente seleccionado");
    private final JTextField fecha = new JTextField(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 12);
    private final JLabel disponibilidad = new JLabel("Elige una fecha y horario para consultar los cupos.");
    private final JTextField horaInicio = new JTextField("18:00", 8);
    private final JTextField horaFin = new JTextField("23:00", 8);
    private final JTextField invitados = new JTextField("50", 8);
    private final JTextArea observaciones = new JTextArea(3, 28);
    private final JComboBox<TipoEvento> tiposEvento = new JComboBox<>();
    private final JComboBox<Espacio> espacios = new JComboBox<>();
    private final JComboBox<Paquete> paquetes = new JComboBox<>();
    private final JLabel estadoInicial = new JLabel("Presupuestado");
    private EstadoEvento estadoPresupuestado;
    private final JLabel precioPaquete = new JLabel("Sin paquete predeterminado");
    private final DefaultTableModel modeloServicios = new DefaultTableModel(
            new Object[] {"ID", "Incluir", "Recurso / servicio disponible", "Capacidad", "Cantidad",
                    "Precio de lista", "Precio acordado"}, 0) {
        private static final long serialVersionUID = 1L;
        @Override public boolean isCellEditable(int fila, int columna) {
            if (columna == 1 || columna == 4) return true;
            if (columna == 6) {
                Integer idServicio = (Integer) getValueAt(fila, 0);
                return !serviciosIncluidosEnPaquete.contains(idServicio);
            }
            return false;
        }
        @Override public Class<?> getColumnClass(int columna) {
            if (columna == 0 || columna == 4) return Integer.class;
            if (columna == 1) return Boolean.class;
            if (columna == 5 || columna == 6) return String.class;
            return String.class;
        }
    };
    private final JTable tablaServicios = new JTable(modeloServicios);
    private final Map<Integer, Servicio> serviciosDisponibles = new HashMap<>();
    private final Set<Integer> serviciosIncluidosEnPaquete = new HashSet<>();
    private boolean cargandoPaquetes;
    private Cliente clienteSeleccionado;
    private int idEventoCreado;

    public CrearEventoDialog(JFrame propietario) {
        super(propietario, "Crear evento", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(980, 700);
        setMinimumSize(new java.awt.Dimension(860, 600));
        setLocationRelativeTo(propietario);
        setLayout(new BorderLayout());
        tarjetas.add(crearPasoCliente(), PASO_CLIENTE);
        tarjetas.add(crearPasoEvento(), PASO_EVENTO);
        add(tarjetas, BorderLayout.CENTER);
        cargarClientes("");
    }

    /** Devuelve el ID creado, o cero si se cerró el formulario. */
    public int getIdEventoCreado() { return idEventoCreado; }

    private JPanel crearPasoCliente() {
        JPanel panel = new JPanel(new BorderLayout(10, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 22, 20, 22));
        JPanel cabecera = new JPanel(new BorderLayout(8, 8));
        cabecera.add(new JLabel("Paso 1 de 2: buscar y seleccionar al cliente"), BorderLayout.NORTH);
        JPanel busqueda = new JPanel(new BorderLayout(8, 0));
        JButton buscar = new JButton("Buscar");
        busqueda.add(buscarCliente, BorderLayout.CENTER);
        busqueda.add(buscar, BorderLayout.EAST);
        cabecera.add(busqueda, BorderLayout.SOUTH);
        panel.add(cabecera, BorderLayout.NORTH);
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaClientes.setAutoCreateRowSorter(true);
        panel.add(new JScrollPane(tablaClientes), BorderLayout.CENTER);

        JPanel acciones = new JPanel();
        JButton nuevo = new JButton("Crear nuevo cliente");
        JButton seleccionar = new JButton("Seleccionar y continuar");
        JButton cancelar = new JButton("Cancelar");
        acciones.add(nuevo);
        acciones.add(seleccionar);
        acciones.add(cancelar);
        panel.add(acciones, BorderLayout.SOUTH);
        buscar.addActionListener(e -> cargarClientes(buscarCliente.getText()));
        buscarCliente.addActionListener(e -> cargarClientes(buscarCliente.getText()));
        nuevo.addActionListener(e -> crearClienteNuevo());
        seleccionar.addActionListener(e -> seleccionarClienteDeTabla());
        tablaClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) seleccionarClienteDeTabla();
            }
        });
        cancelar.addActionListener(e -> dispose());
        return panel;
    }

    private JPanel crearPasoEvento() {
        JPanel panel = new JPanel(new BorderLayout(10, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 22, 20, 22));
        JPanel cabecera = new JPanel(new BorderLayout(4, 4));
        cabecera.add(new JLabel("Paso 2 de 2: datos del evento"), BorderLayout.NORTH);
        cabecera.add(clienteElegido, BorderLayout.SOUTH);
        panel.add(cabecera, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridBagLayout());
        agregarCampo(formulario, 0, "Tipo de evento:", tiposEvento);
        fecha.setEditable(false);
        JPanel selectorFecha = new JPanel(new BorderLayout(6, 0));
        JButton elegirFecha = new JButton("Abrir calendario");
        selectorFecha.add(fecha, BorderLayout.CENTER);
        selectorFecha.add(elegirFecha, BorderLayout.EAST);
        elegirFecha.addActionListener(e -> abrirCalendario());
        agregarCampo(formulario, 1, "Fecha del evento:", selectorFecha);
        agregarCampo(formulario, 2, "Hora de inicio (HH:mm):", horaInicio);
        agregarCampo(formulario, 3, "Hora de fin (HH:mm):", horaFin);
        agregarCampo(formulario, 4, "Cantidad de invitados:", invitados);
        agregarCampo(formulario, 5, "Salón:", espacios);
        agregarCampo(formulario, 6, "Paquete base (opcional):", paquetes);
        agregarCampo(formulario, 7, "Estado inicial:", estadoInicial);
        agregarCampo(formulario, 8, "Precio de lista del paquete:", precioPaquete);
        observaciones.setLineWrap(true);
        observaciones.setWrapStyleWord(true);
        agregarCampo(formulario, 9, "Necesidades especiales:", new JScrollPane(observaciones));
        agregarCampo(formulario, 10, "Cupo simultáneo:", disponibilidad);
        java.awt.event.FocusAdapter actualizarCupo = new java.awt.event.FocusAdapter() {
            @Override public void focusLost(java.awt.event.FocusEvent e) { actualizarDisponibilidad(); }
        };
        horaInicio.addFocusListener(actualizarCupo);
        horaFin.addFocusListener(actualizarCupo);

        JPanel centro = new JPanel(new BorderLayout(6, 8));
        centro.add(formulario, BorderLayout.NORTH);
        JPanel personalizacion = new JPanel(new BorderLayout(4, 5));
        personalizacion.setBorder(BorderFactory.createTitledBorder(
                "Personaliza los recursos y servicios para este cliente"));
        personalizacion.add(new JLabel("El paquete carga una propuesta inicial. Puedes agregar o quitar servicios "
                + "y ajustar cantidades para este evento."), BorderLayout.NORTH);
        tablaServicios.setRowHeight(24);
        tablaServicios.setAutoCreateRowSorter(true);
        tablaServicios.getColumnModel().getColumn(0).setMinWidth(0);
        tablaServicios.getColumnModel().getColumn(0).setMaxWidth(0);
        tablaServicios.getColumnModel().getColumn(0).setPreferredWidth(0);
        personalizacion.add(new JScrollPane(tablaServicios), BorderLayout.CENTER);
        centro.add(personalizacion, BorderLayout.CENTER);
        JScrollPane scrollFormulario = new JScrollPane(centro,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollFormulario.setBorder(null);
        panel.add(scrollFormulario, BorderLayout.CENTER);

        JPanel acciones = new JPanel();
        JButton volver = new JButton("Volver a clientes");
        JButton guardar = new JButton("Guardar evento");
        JButton cancelar = new JButton("Cancelar");
        acciones.add(volver);
        acciones.add(guardar);
        acciones.add(cancelar);
        panel.add(acciones, BorderLayout.SOUTH);
        volver.addActionListener(e -> pasos.show(tarjetas, PASO_CLIENTE));
        cancelar.addActionListener(e -> dispose());
        guardar.addActionListener(e -> guardarEvento());
        getRootPane().setDefaultButton(guardar);
        paquetes.addActionListener(e -> {
            if (!cargandoPaquetes) cargarComponentesDelPaquete();
        });
        cargarCatalogos();
        actualizarDisponibilidad();
        return panel;
    }

    private void abrirCalendario() {
        CalendarioDisponibilidadDialog calendario = new CalendarioDisponibilidadDialog(
                (JFrame) getOwner(), eventoDAO, fechaSeleccionada);
        calendario.setVisible(true);
        if (calendario.getFechaSeleccionada() != null) {
            fechaSeleccionada = calendario.getFechaSeleccionada();
            fecha.setText(fechaSeleccionada.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            actualizarDisponibilidad();
        }
    }

    private LocalDate fechaSeleccionada = LocalDate.now();

    private void actualizarDisponibilidad() {
        try {
            LocalTime inicio = LocalTime.parse(normalizarHora(horaInicio.getText()));
            LocalTime fin = LocalTime.parse(normalizarHora(horaFin.getText()));
            if (!fin.isAfter(inicio)) {
                disponibilidad.setText("La hora de fin debe ser posterior a la de inicio.");
                disponibilidad.setForeground(new java.awt.Color(170, 45, 45));
                return;
            }
            int pico = eventoDAO.maximoSimultaneosAlAgregar(fechaSeleccionada, inicio, fin);
            if (pico <= 3) {
                disponibilidad.setText("Disponible: con este evento serían " + pico + " de 3 simultáneos.");
                disponibilidad.setForeground(new java.awt.Color(30, 120, 65));
            } else {
                disponibilidad.setText("Sin cupo: ese horario superaría el máximo de 3 eventos simultáneos.");
                disponibilidad.setForeground(new java.awt.Color(170, 45, 45));
            }
        } catch (java.time.format.DateTimeParseException ex) {
            disponibilidad.setText("Ingresa las horas en formato HH:mm para consultar el cupo.");
            disponibilidad.setForeground(new java.awt.Color(120, 120, 120));
        } catch (SQLException ex) {
            disponibilidad.setText("No se pudo consultar la disponibilidad de la base de datos.");
            disponibilidad.setForeground(new java.awt.Color(170, 45, 45));
        }
    }

    private void agregarCampo(JPanel panel, int fila, String etiqueta, Component campo) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.gridy = fila;
        c.gridx = 0;
        c.anchor = GridBagConstraints.LINE_END;
        panel.add(new JLabel(etiqueta), c);
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(campo, c);
    }

    private void cargarClientes(String filtro) {
        try {
            modeloClientes.setRowCount(0);
            List<Cliente> clientes = clienteDAO.buscar(filtro);
            for (Cliente cliente : clientes) {
                modeloClientes.addRow(new Object[] {cliente.getIdCliente(), cliente.getNombre(),
                        cliente.getApellido(), cliente.getDniCuit(), cliente.getEmail(), cliente.getTelefono()});
            }
        } catch (SQLException ex) {
            mostrarError("No se pudo buscar clientes", ex);
        }
    }

    private void seleccionarClienteDeTabla() {
        int filaVista = tablaClientes.getSelectedRow();
        if (filaVista < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente de la lista.");
            return;
        }
        int fila = tablaClientes.convertRowIndexToModel(filaVista);
        int id = (Integer) modeloClientes.getValueAt(fila, 0);
        clienteSeleccionado = new Cliente(id,
                String.valueOf(modeloClientes.getValueAt(fila, 1)),
                String.valueOf(modeloClientes.getValueAt(fila, 2)),
                String.valueOf(modeloClientes.getValueAt(fila, 3)),
                String.valueOf(modeloClientes.getValueAt(fila, 4)),
                String.valueOf(modeloClientes.getValueAt(fila, 5)));
        continuarConCliente(clienteSeleccionado);
    }

    private void continuarConCliente(Cliente cliente) {
        clienteSeleccionado = cliente;
        clienteElegido.setText("Cliente: " + cliente.getNombre() + " " + cliente.getApellido()
                + " · " + cliente.getDniCuit());
        pasos.show(tarjetas, PASO_EVENTO);
    }

    private void crearClienteNuevo() {
        JTextField nombre = new JTextField(18);
        JTextField apellido = new JTextField(18);
        JTextField dni = new JTextField(18);
        JTextField email = new JTextField(18);
        JTextField telefono = new JTextField(18);
        JPanel campos = new JPanel(new GridBagLayout());
        agregarCampo(campos, 0, "Nombre:", nombre);
        agregarCampo(campos, 1, "Apellido:", apellido);
        agregarCampo(campos, 2, "DNI/CUIT:", dni);
        agregarCampo(campos, 3, "Email:", email);
        agregarCampo(campos, 4, "Teléfono:", telefono);
        int opcion = JOptionPane.showConfirmDialog(this, campos, "Registrar cliente",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) return;
        if (nombre.getText().trim().isEmpty() || apellido.getText().trim().isEmpty()
                || dni.getText().trim().isEmpty() || email.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa nombre, apellido, DNI/CUIT y email.");
            return;
        }
        Cliente nuevo = new Cliente(0, nombre.getText().trim(), apellido.getText().trim(),
                dni.getText().trim(), email.getText().trim(), telefono.getText().trim());
        try {
            clienteDAO.insertar(nuevo);
            cargarClientes(buscarCliente.getText());
            continuarConCliente(nuevo);
        } catch (SQLException ex) {
            mostrarError("No se pudo guardar el cliente", ex);
        }
    }

    private void cargarCatalogos() {
        cargandoPaquetes = true;
        try {
            tiposEvento.removeAllItems();
            for (TipoEvento tipo : catalogoDAO.listarTiposEvento()) tiposEvento.addItem(tipo);
            espacios.removeAllItems();
            for (Espacio espacio : catalogoDAO.listarEspacios()) espacios.addItem(espacio);
            paquetes.removeAllItems();
            paquetes.addItem(new Paquete(0, "Sin paquete", "", BigDecimal.ZERO));
            for (Paquete paquete : catalogoDAO.listarPaquetes()) paquetes.addItem(paquete);
            estadoPresupuestado = null;
            for (EstadoEvento estado : catalogoDAO.listarEstados()) {
                if ("Presupuestado".equalsIgnoreCase(estado.getNombre())) estadoPresupuestado = estado;
            }
            estadoInicial.setText(estadoPresupuestado == null
                    ? "No está configurado en la base de datos" : estadoPresupuestado.getNombre());
            serviciosDisponibles.clear();
            modeloServicios.setRowCount(0);
            for (Servicio servicio : catalogoDAO.listarServicios()) {
                serviciosDisponibles.put(servicio.getId(), servicio);
                modeloServicios.addRow(new Object[] {servicio.getId(), Boolean.FALSE,
                        servicio.getNombre(), servicio.getCantidadDisponible() == null
                                ? "Sin informar" : servicio.getCantidadDisponible(), 1,
                        dineroCatalogo(servicio.getCostoUnitario()), dineroCatalogo(servicio.getCostoUnitario())});
            }
            cargarComponentesDelPaquete();
        } catch (SQLException ex) {
            mostrarError("No se pudieron cargar tipos, salones, paquetes y servicios", ex);
        } finally {
            cargandoPaquetes = false;
        }
    }

    private void cargarComponentesDelPaquete() {
        Paquete paquete = (Paquete) paquetes.getSelectedItem();
        if (paquete == null || paquete.getId() == 0) {
            precioPaquete.setText("Sin paquete predeterminado");
            serviciosIncluidosEnPaquete.clear();
            for (int fila = 0; fila < modeloServicios.getRowCount(); fila++) {
                modeloServicios.setValueAt(Boolean.FALSE, fila, 1);
                modeloServicios.setValueAt(1, fila, 4);
                modeloServicios.setValueAt(dineroCatalogo(serviciosDisponibles.get(
                        (Integer) modeloServicios.getValueAt(fila, 0)).getCostoUnitario()), fila, 6);
            }
            return;
        }
        precioPaquete.setText("$ " + paquete.getPrecioFijo().toPlainString());
        try {
            Map<Integer, Integer> componentes = catalogoDAO.listarComponentesPaquete(paquete.getId());
            serviciosIncluidosEnPaquete.clear();
            serviciosIncluidosEnPaquete.addAll(componentes.keySet());
            for (int fila = 0; fila < modeloServicios.getRowCount(); fila++) {
                Integer idServicio = (Integer) modeloServicios.getValueAt(fila, 0);
                Integer cantidad = componentes.get(idServicio);
                modeloServicios.setValueAt(cantidad != null, fila, 1);
                modeloServicios.setValueAt(cantidad == null ? 1 : cantidad, fila, 4);
                BigDecimal precioLista = serviciosDisponibles.get(idServicio).getCostoUnitario();
                modeloServicios.setValueAt(dineroCatalogo(cantidad == null ? precioLista : BigDecimal.ZERO), fila, 6);
            }
        } catch (SQLException ex) {
            mostrarError("No se pudieron cargar los componentes de este paquete", ex);
        }
    }

    private void guardarEvento() {
        if (clienteSeleccionado == null || tiposEvento.getSelectedItem() == null
                || espacios.getSelectedItem() == null || estadoPresupuestado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona cliente, tipo de evento y salón; verifica que el estado Presupuestado exista.");
            return;
        }
        try {
            LocalDate fechaEvento = fechaSeleccionada;
            LocalTime inicio = LocalTime.parse(normalizarHora(horaInicio.getText()));
            LocalTime fin = LocalTime.parse(normalizarHora(horaFin.getText()));
            int cantidad = Integer.parseInt(invitados.getText().trim());
            Espacio espacio = (Espacio) espacios.getSelectedItem();
            TipoEvento tipoEvento = (TipoEvento) tiposEvento.getSelectedItem();
            Paquete paqueteElegido = (Paquete) paquetes.getSelectedItem();
            if (!fin.isAfter(inicio)) {
                JOptionPane.showMessageDialog(this, "La hora de fin debe ser posterior a la hora de inicio.");
                return;
            }
            if (fechaEvento.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "No se pueden crear eventos en fechas anteriores a hoy.");
                return;
            }
            if (cantidad < 1) {
                JOptionPane.showMessageDialog(this, "La cantidad de invitados debe ser mayor que cero.");
                return;
            }
            if (cantidad > espacio.getCapacidad()) {
                JOptionPane.showMessageDialog(this, "El salón admite hasta " + espacio.getCapacidad() + " personas.");
                return;
            }
            int picoSimultaneo = eventoDAO.maximoSimultaneosAlAgregar(fechaEvento, inicio, fin);
            if (picoSimultaneo > 3) {
                JOptionPane.showMessageDialog(this,
                        "Ese horario superaría el máximo de 3 eventos simultáneos de la empresa.",
                        "Sin disponibilidad", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!eventoDAO.estaDisponible(espacio.getId(), fechaEvento, inicio, fin)) {
                JOptionPane.showMessageDialog(this, "Ese salón ya tiene un evento en ese horario.",
                        "Horario ocupado", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer idPaquete = paqueteElegido == null || paqueteElegido.getId() == 0
                    ? null : paqueteElegido.getId();
            List<ServicioContratado> serviciosElegidos = leerServiciosElegidos();
            if (serviciosElegidos == null) return;
            List<String> recursosSinCapacidad = new ArrayList<>();
            for (ServicioContratado contratado : serviciosElegidos) {
                Servicio servicio = serviciosDisponibles.get(contratado.getIdServicio());
                if (servicio != null && servicio.getCantidadDisponible() == null) {
                    recursosSinCapacidad.add(servicio.getNombre());
                }
            }
            if (!recursosSinCapacidad.isEmpty()) {
                int confirmar = JOptionPane.showConfirmDialog(this,
                        "No se informó la capacidad de estos recursos: "
                                + String.join(", ", recursosSinCapacidad)
                                + ". Confirma manualmente con Coordinación o el proveedor que estén disponibles. ¿Continuar?",
                        "Capacidad sin informar", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirmar != JOptionPane.YES_OPTION) return;
            }
            List<String> problemasCapacidad = catalogoDAO.validarCapacidadServicios(
                    fechaEvento, inicio, fin, serviciosElegidos);
            if (!problemasCapacidad.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se puede guardar todavía:\n- "
                        + String.join("\n- ", problemasCapacidad),
                        "Capacidad insuficiente", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Evento evento = new Evento(clienteSeleccionado.getIdCliente(), tipoEvento.getId(),
                    espacio.getId(), idPaquete, estadoPresupuestado.getId(), fechaEvento, inicio, fin, cantidad,
                    observaciones.getText().trim());
            idEventoCreado = eventoDAO.insertarConServicios(evento, serviciosElegidos);
            JOptionPane.showMessageDialog(this, "Evento creado correctamente. Número: " + idEventoCreado);
            dispose();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Revisa el formato: fecha AAAA-MM-DD y horas HH:mm.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Revisa invitados, cantidades y precios acordados; "
                    + "usa números válidos (decimal con punto o coma).");
        } catch (SQLException ex) {
            mostrarError("No se pudo guardar el evento", ex);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            mostrarError("Ocurrió un problema inesperado al procesar el evento", ex);
        }
    }

    private List<ServicioContratado> leerServiciosElegidos() {
        if (tablaServicios.isEditing()) tablaServicios.getCellEditor().stopCellEditing();
        List<ServicioContratado> elegidos = new ArrayList<>();
        for (int fila = 0; fila < modeloServicios.getRowCount(); fila++) {
            if (!Boolean.TRUE.equals(modeloServicios.getValueAt(fila, 1))) continue;
            Servicio servicio = serviciosDisponibles.get((Integer) modeloServicios.getValueAt(fila, 0));
            int cantidad = Integer.parseInt(String.valueOf(modeloServicios.getValueAt(fila, 4)));
            BigDecimal precio = new BigDecimal(String.valueOf(modeloServicios.getValueAt(fila, 6))
                    .trim().replace(',', '.'));
            if (cantidad < 1 || precio.signum() < 0) {
                throw new NumberFormatException("La cantidad y el precio deben ser cero o mayores.");
            }
            if (!serviciosIncluidosEnPaquete.contains(servicio.getId())) {
                BigDecimal minimoPermitido = servicio.getCostoUnitario()
                        .multiply(new BigDecimal("0.90")).setScale(2, RoundingMode.CEILING);
                if (precio.compareTo(minimoPermitido) < 0) {
                    JOptionPane.showMessageDialog(this,
                            "El descuento máximo para '" + servicio.getNombre() + "' es del 10 %. "
                            + "El precio acordado no puede ser menor a $ " + dineroCatalogo(minimoPermitido) + ".",
                            "Descuento superior al permitido", JOptionPane.WARNING_MESSAGE);
                    return null;
                }
            }
            elegidos.add(new ServicioContratado(servicio.getId(), cantidad, precio));
        }
        return elegidos;
    }

    private String dineroCatalogo(BigDecimal importe) {
        return (importe == null ? BigDecimal.ZERO : importe).setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private String normalizarHora(String hora) {
        String valor = hora.trim();
        return valor.length() == 5 ? valor + ":00" : valor;
    }

    private void mostrarError(String titulo, Exception ex) {
        JOptionPane.showMessageDialog(this, titulo + ":\n" + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}

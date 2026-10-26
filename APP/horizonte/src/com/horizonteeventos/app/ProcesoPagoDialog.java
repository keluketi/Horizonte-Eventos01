package com.horizonteeventos.app;

import com.horizonteeventos.modelo.Cliente;
import com.horizonteeventos.modelo.EventoPagoResumen;
import com.horizonteeventos.servicio.ProcesoPagoService;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import com.horizonteeventos.dao.AutenticacionClienteDAO;
import com.horizonteeventos.dao.impl.AutenticacionClienteDAOImpl;

/** Inicio de sesión del cliente, historial de eventos y pago de prueba. */
public class ProcesoPagoDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private static final Color AZUL = new Color(18, 51, 81);
    private static final Color FONDO = new Color(245, 246, 249);
    private static final Font FUENTE = new Font("Verdana", Font.PLAIN, 13);
    private static final NumberFormat DINERO = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final ProcesoPagoService servicio = new ProcesoPagoService();
    private final AutenticacionClienteDAO autenticacionDAO = new AutenticacionClienteDAOImpl();
    private final java.awt.CardLayout tarjetas = new java.awt.CardLayout();
    private final JPanel contenido = new JPanel(tarjetas);
    private final JTextField txtEmail = new JTextField(24);
    private final JPasswordField txtContrasena = new JPasswordField(24);
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[] {"Evento", "Tipo", "Fecha", "Espacio", "Estado", "Total", "Pagado", "Saldo"}, 0) {
        private static final long serialVersionUID = 1L;
        @Override public boolean isCellEditable(int fila, int columna) { return false; }
    };
    private final JTable tabla = new JTable(modelo);
    private final JButton btnPagar = new JButton("Proceder al pago");
    private final JButton btnDetalle = new JButton("Detalle del evento");
    private final JLabel lblCliente = new JLabel();
    private final List<EventoPagoResumen> eventosVisibles = new ArrayList<>();
    private Cliente cliente;

    public ProcesoPagoDialog(Frame propietario) {
        super(propietario, "Horizonte Eventos | Acceso de clientes", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(900, 580));
        setSize(1050, 650);
        setLocationRelativeTo(propietario);
        contenido.add(crearPanelAcceso(), "acceso");
        contenido.add(crearPanelHistorial(), "historial");
        setContentPane(contenido);
        tarjetas.show(contenido, "acceso");
    }
    /** Abre directamente el historial para el cliente autenticado desde el login principal. */
    public ProcesoPagoDialog(Frame propietario, Cliente clienteAutenticado) {
        this(propietario);
        this.cliente = clienteAutenticado;
        lblCliente.setText("Cliente: " + cliente.getNombre() + " " + cliente.getApellido());
        setTitle("Horizonte Eventos | Eventos de " + cliente.getNombre() + " " + cliente.getApellido());
        cargarHistorial();
        tarjetas.show(contenido, "historial");
    }

    private JPanel crearPanelAcceso() {
        JPanel panel = new JPanel(new GridLayout(1, 1));
        panel.setBackground(FONDO);
        JPanel formulario = new JPanel(new GridLayout(0, 1, 8, 8));
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 230)),
                BorderFactory.createEmptyBorder(28, 38, 28, 38)));
        formulario.add(titulo("Acceso de clientes"));
        formulario.add(new JLabel("Correo electrónico"));
        formulario.add(txtEmail);
        formulario.add(new JLabel("Contraseña"));
        formulario.add(txtContrasena);

        JButton ingresar = boton("Iniciar sesión");
        JButton crearClave = boton("Crear contraseña por primera vez");
        JButton cancelar = boton("Volver");
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 4));
        acciones.setOpaque(false);
        acciones.add(ingresar);
        acciones.add(crearClave);
        acciones.add(cancelar);
        formulario.add(acciones);
        JLabel aviso = new JLabel("Módulo de demostración: no procesa tarjetas ni cobra dinero real.",
                SwingConstants.CENTER);
        aviso.setFont(FUENTE);
        formulario.add(aviso);
        panel.add(formulario);

        ingresar.addActionListener(e -> iniciarSesion());
        txtContrasena.addActionListener(e -> iniciarSesion());
        crearClave.addActionListener(e -> configurarContrasena());
        cancelar.addActionListener(e -> dispose());
        return panel;
    }

    private JPanel crearPanelHistorial() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(16, 18, 14, 18));

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(Color.WHITE);
        encabezado.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        encabezado.add(titulo("Historial de eventos"), BorderLayout.WEST);
        encabezado.add(lblCliente, BorderLayout.EAST);
        panel.add(encabezado, BorderLayout.NORTH);

        tabla.setFont(FUENTE);
        tabla.setRowHeight(26);
        tabla.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tabla.getTableHeader().setFont(new Font("Verdana", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(AZUL);
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setAutoCreateRowSorter(true);
        tabla.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent evento) {
                if (evento.getClickCount() == 1 && tabla.rowAtPoint(evento.getPoint()) >= 0) {
                    SwingUtilities.invokeLater(() -> abrirDetalle());
                }
            }
        });
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JButton actualizar = boton("Actualizar historial");
        JButton cerrarSesion = boton("Cerrar sesión");
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        acciones.setOpaque(false);
        acciones.add(btnDetalle);
        acciones.add(btnPagar);
        acciones.add(actualizar);
        acciones.add(cerrarSesion);
        panel.add(acciones, BorderLayout.SOUTH);

        btnDetalle.addActionListener(e -> abrirDetalle());
        btnPagar.addActionListener(e -> procederAlPago());
        actualizar.addActionListener(e -> cargarHistorial());
        cerrarSesion.addActionListener(e -> cerrarSesion());
        tabla.getSelectionModel().addListSelectionListener(e -> actualizarBotones());
        actualizarBotones();
        return panel;
    }

    private void iniciarSesion() {
        String email = txtEmail.getText().trim();
        char[] contrasena = txtContrasena.getPassword();
        if (email.isEmpty() || contrasena.length == 0) {
            JOptionPane.showMessageDialog(this, "Completa el correo y la contraseña.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            Arrays.fill(contrasena, '\0');
            return;
        }
        try {
            Optional<Cliente> resultado = autenticacionDAO.iniciarSesion(email, contrasena);
            if (!resultado.isPresent()) {
                JOptionPane.showMessageDialog(this, "Correo o contraseña incorrectos. Si es tu primer ingreso, crea una contraseña.",
                        "No se pudo ingresar", JOptionPane.WARNING_MESSAGE);
                txtContrasena.setText("");
                return;
            }
            cliente = resultado.get();
            lblCliente.setText("Cliente: " + cliente.getNombre() + " " + cliente.getApellido());
            setTitle("Horizonte Eventos | Eventos de " + cliente.getNombre() + " " + cliente.getApellido());
            cargarHistorial();
            tarjetas.show(contenido, "historial");
        } catch (Exception ex) {
            mostrarError("No se pudo iniciar sesión", ex);
        } finally {
            Arrays.fill(contrasena, '\0');
        }
    }

    private void configurarContrasena() {
        String email = txtEmail.getText().trim();
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe primero tu correo electrónico.",
                    "Correo requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        JTextField dni = new JTextField(18);
        JPasswordField clave = new JPasswordField(18);
        JPasswordField repetir = new JPasswordField(18);
        Object[] campos = {"DNI/CUIT registrado:", dni, "Nueva contraseña (mínimo 8 caracteres):", clave,
                "Repite la contraseña:", repetir};
        int opcion = JOptionPane.showConfirmDialog(this, campos, "Crear contraseña de cliente",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        char[] password = clave.getPassword();
        char[] confirmacion = repetir.getPassword();
        if (opcion != JOptionPane.OK_OPTION) {
            Arrays.fill(password, '\0');
            Arrays.fill(confirmacion, '\0');
            return;
        }
        try {
            if (password.length < 8 || !Arrays.equals(password, confirmacion)) {
                JOptionPane.showMessageDialog(this, "La contraseña debe tener 8 caracteres y coincidir en ambos campos.",
                        "Revisa la contraseña", JOptionPane.WARNING_MESSAGE);
                return;
            }
            boolean creada = autenticacionDAO.configurarContrasenaInicial(email, dni.getText(), password);
            if (creada) {
                JOptionPane.showMessageDialog(this, "Contraseña creada. Ya puedes iniciar sesión.",
                        "Cuenta habilitada", JOptionPane.INFORMATION_MESSAGE);
                txtContrasena.requestFocusInWindow();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo crear. Verifica que el correo y DNI/CUIT coincidan "
                        + "con un cliente sin contraseña configurada.", "No se pudo crear la contraseña",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            mostrarError("No se pudo crear la contraseña", ex);
        } finally {
            Arrays.fill(password, '\0');
            Arrays.fill(confirmacion, '\0');
        }
    }

    private void cargarHistorial() {
        if (cliente == null) return;
        try {
            eventosVisibles.clear();
            eventosVisibles.addAll(servicio.listarHistorial(cliente.getIdCliente()));
            modelo.setRowCount(0);
            for (EventoPagoResumen evento : eventosVisibles) {
                modelo.addRow(new Object[] {evento.getIdEvento(), evento.getTipo(),
                        evento.getFecha() == null ? "Sin fecha" : evento.getFecha().format(FECHA),
                        evento.getEspacio(), evento.getEstado(), dinero(evento.getTotal()),
                        dinero(evento.getPagado()), dinero(evento.getSaldo())});
            }
            actualizarBotones();
            if (eventosVisibles.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todavía no hay eventos con presupuesto aceptado para mostrar.",
                        "Historial vacío", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            mostrarError("No se pudo cargar el historial", ex);
        }
    }

    private EventoPagoResumen eventoSeleccionado() {
        int filaVista = tabla.getSelectedRow();
        if (filaVista < 0) return null;
        int filaModelo = tabla.convertRowIndexToModel(filaVista);
        int id = ((Number) modelo.getValueAt(filaModelo, 0)).intValue();
        for (EventoPagoResumen evento : eventosVisibles) {
            if (evento.getIdEvento() == id) return evento;
        }
        return null;
    }

    private void actualizarBotones() {
        EventoPagoResumen evento = eventoSeleccionado();
        btnDetalle.setEnabled(evento != null);
        btnPagar.setEnabled(evento != null && !"Pagado".equalsIgnoreCase(evento.getEstado())
                && ("Aceptado".equalsIgnoreCase(evento.getEstado())
                || "Señado".equalsIgnoreCase(evento.getEstado())
                || "Confirmado".equalsIgnoreCase(evento.getEstado())));
    }

    private void abrirDetalle() {
        EventoPagoResumen evento = eventoSeleccionado();
        if (evento == null) return;
        Frame propietario = (Frame) getOwner();
        new SimulacionFacturacionDialog(propietario, evento.getIdEvento()).setVisible(true);
    }

    private void procederAlPago() {
        EventoPagoResumen evento = eventoSeleccionado();
        if (evento == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un evento de tu historial.",
                    "Evento requerido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        final String tipoInicial = "Señado".equalsIgnoreCase(evento.getEstado()) ? "Pago final" : "Seña";
        JComboBox<String> tipoPago = new JComboBox<>(new String[] {tipoInicial});
        JComboBox<String> medioPago = new JComboBox<>(new String[] {
                "Transferencia", "Tarjeta", "Efectivo", "Otro"});
        JLabel importe = new JLabel();
        importe.setFont(new Font("Verdana", Font.BOLD, 16));
        importe.setForeground(AZUL);
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 9));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.add(new JLabel("Evento:"));
        panel.add(new JLabel("#" + evento.getIdEvento() + " · " + evento.getTipo()));
        panel.add(new JLabel("Tipo de pago:"));
        panel.add(tipoPago);
        panel.add(new JLabel("Medio simulado:"));
        panel.add(medioPago);
        panel.add(new JLabel("Importe a simular:"));
        panel.add(importe);
        panel.add(new JLabel(""));
        panel.add(new JLabel("No se realizará un cobro real."));
        try {
            BigDecimal monto = servicio.calcularImporte(cliente.getIdCliente(),
                    evento.getIdEvento(), tipoInicial);
            importe.setText(dinero(monto));
        } catch (Exception ex) {
            mostrarError("No se puede calcular el pago", ex);
            return;
        }
        Object[] opciones = {"Simular pago", "Cancelar"};
        int opcion = JOptionPane.showOptionDialog(this, panel, "Proceder al pago simulado",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
        if (opcion != 0) return;
        try {
            servicio.procesarPago(cliente.getIdCliente(), evento.getIdEvento(), tipoInicial,
                    (String) medioPago.getSelectedItem());
            JOptionPane.showMessageDialog(this, "Pago simulado registrado. Se actualizó el estado del evento.",
                    "Pago procesado", JOptionPane.INFORMATION_MESSAGE);
            cargarHistorial();
        } catch (Exception ex) {
            mostrarError("No se pudo registrar el pago", ex);
        }
    }

    private void cerrarSesion() {
        cliente = null;
        eventosVisibles.clear();
        modelo.setRowCount(0);
        txtContrasena.setText("");
        txtEmail.setText("");
        setTitle("Horizonte Eventos | Acceso de clientes");
        tarjetas.show(contenido, "acceso");
    }

    private JLabel titulo(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(new Font("Verdana", Font.BOLD, 21));
        etiqueta.setForeground(AZUL);
        return etiqueta;
    }

    private JButton boton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Verdana", Font.BOLD, 12));
        boton.setBackground(AZUL);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        return boton;
    }

    private String dinero(BigDecimal monto) { return DINERO.format(monto == null ? BigDecimal.ZERO : monto); }

    private void mostrarError(String titulo, Exception ex) {
        JOptionPane.showMessageDialog(this, titulo + ":\n" + ex.getMessage(),
                "Proceso de pago", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProcesoPagoDialog(null).setVisible(true));
    }
}
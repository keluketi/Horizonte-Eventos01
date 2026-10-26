package com.horizonteeventos.app;

import com.horizonteeventos.dao.EventoDetalleDAO;
import com.horizonteeventos.dao.FacturacionSimulacionDAO;
import com.horizonteeventos.dao.impl.EventoDetalleDAOImpl;
import com.horizonteeventos.dao.impl.FacturacionSimulacionDAOImpl;
import com.horizonteeventos.modelo.DatosFacturacionEvento;
import com.horizonteeventos.modelo.DetalleEvento;
import com.horizonteeventos.modelo.ServicioEventoDetalle;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/** Ventana de consulta que simula una factura sin emitirla ni guardar datos. */
public class SimulacionFacturacionDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private static final Color AZUL = new Color(18, 51, 81);
    private static final Color FONDO = new Color(245, 246, 249);
    private static final Font FUENTE = new Font("Verdana", Font.PLAIN, 13);
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final NumberFormat DINERO = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

    private final EventoDetalleDAO eventoDAO = new EventoDetalleDAOImpl();
    private final FacturacionSimulacionDAO facturacionDAO = new FacturacionSimulacionDAOImpl();

    public SimulacionFacturacionDialog(Frame owner, int idEvento) {
        super(owner, "Detalle del evento y simulación de facturación", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(850, 650));
        setSize(1000, 760);
        setLocationRelativeTo(owner);
        try {
            java.util.Optional<DetalleEvento> evento = eventoDAO.buscarPorId(idEvento);
            if (!evento.isPresent()) {
                JOptionPane.showMessageDialog(owner, "No se encontró el evento seleccionado.",
                        "Detalle del evento", JOptionPane.WARNING_MESSAGE);
                dispose();
                return;
            }
            DatosFacturacionEvento datos = facturacionDAO.cargarPorEvento(idEvento);
            construirVentana(evento.get(), datos);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(owner, "No se pudo cargar la ficha del evento:\n" + ex.getMessage(),
                    "Detalle del evento", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void construirVentana(DetalleEvento evento, DatosFacturacionEvento datos) {
        JPanel raiz = new JPanel(new BorderLayout(10, 10));
        raiz.setBackground(FONDO);
        raiz.setBorder(BorderFactory.createEmptyBorder(14, 16, 12, 16));

        JPanel cabecera = new JPanel(new BorderLayout(4, 4));
        cabecera.setBackground(Color.WHITE);
        cabecera.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 220, 230)),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)));
        JLabel titulo = new JLabel("HORIZONTE EVENTOS · EVENTO #" + evento.getId());
        titulo.setFont(new Font("Verdana", Font.BOLD, 18));
        titulo.setForeground(AZUL);
        JLabel aviso = new JLabel("VISTA PREVIA · No es fiscal y no modifica la base de datos. "
                + "Presupuesto = propuesta; pago = dinero recibido; comprobante = documento emitido.");
        aviso.setFont(FUENTE);
        aviso.setForeground(new Color(60, 70, 80));
        cabecera.add(titulo, BorderLayout.NORTH);
        cabecera.add(aviso, BorderLayout.SOUTH);
        raiz.add(cabecera, BorderLayout.NORTH);

        JPanel contenido = new JPanel();
        contenido.setOpaque(false);
        contenido.setLayout(new javax.swing.BoxLayout(contenido, javax.swing.BoxLayout.Y_AXIS));
        contenido.add(crearDatosEmpresa(evento, datos));
        contenido.add(javax.swing.Box.createVerticalStrut(8));
        contenido.add(crearDatosClienteEvento(evento));
        contenido.add(javax.swing.Box.createVerticalStrut(8));
        contenido.add(crearTablaServicios(evento));
        contenido.add(javax.swing.Box.createVerticalStrut(8));
        contenido.add(crearResumenEconomico(evento, datos));
        contenido.add(javax.swing.Box.createVerticalStrut(8));
        contenido.add(crearPestanasPagosComprobantes(datos));

        JScrollPane desplazamiento = new JScrollPane(contenido);
        desplazamiento.setBorder(null);
        desplazamiento.getViewport().setBackground(FONDO);
        raiz.add(desplazamiento, BorderLayout.CENTER);

        JButton cerrar = new JButton("Cerrar");
        cerrar.setFont(new Font("Verdana", Font.BOLD, 13));
        cerrar.setBackground(AZUL);
        cerrar.setForeground(Color.WHITE);
        cerrar.addActionListener(e -> dispose());
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pie.setOpaque(false);
        pie.add(cerrar);
        raiz.add(pie, BorderLayout.SOUTH);
        setContentPane(raiz);
    }

    private JPanel crearDatosEmpresa(DetalleEvento evento, DatosFacturacionEvento datos) {
        String numeroSimulado = String.format("SIM-EVT-%06d", evento.getId());
        JPanel panel = crearSeccion("Comprobante de simulación");
        GridLayout rejilla = new GridLayout(0, 2, 8, 5);
        panel.setLayout(rejilla);
        agregarDato(panel, "Empresa", "Horizonte Eventos");
        agregarDato(panel, "CUIT de la empresa", "Pendiente de configurar");
        agregarDato(panel, "Contacto de la empresa", "Pendiente de configurar");
        agregarDato(panel, "Número de vista previa", numeroSimulado + " · no emitido");
        agregarDato(panel, "Fecha de emisión simulada", LocalDate.now().format(FECHA));
        agregarDato(panel, "Presupuesto de referencia", datos.getFechaPresupuesto() == null
                ? datos.getEstadoPresupuesto() : datos.getEstadoPresupuesto() + " · " + datos.getFechaPresupuesto().format(FECHA));
        return panel;
    }

    private JPanel crearDatosClienteEvento(DetalleEvento evento) {
        JPanel panel = crearSeccion("Cliente y evento");
        panel.setLayout(new GridLayout(0, 2, 8, 5));
        agregarDato(panel, "Cliente / razón social", texto(evento.getCliente()));
        agregarDato(panel, "DNI/CUIT", texto(evento.getDocumentoCliente()));
        agregarDato(panel, "Email", texto(evento.getEmailCliente()));
        agregarDato(panel, "Teléfono", texto(evento.getTelefonoCliente()));
        agregarDato(panel, "Tipo de evento", texto(evento.getTipo()));
        agregarDato(panel, "Fecha", evento.getFecha() == null ? "Sin informar" : evento.getFecha().format(FECHA));
        agregarDato(panel, "Salón o espacio", texto(evento.getEspacio()));
        agregarDato(panel, "Invitados presupuestados", String.valueOf(evento.getInvitados()));
        return panel;
    }

    private JPanel crearTablaServicios(DetalleEvento evento) {
        JPanel panel = crearSeccion("Detalle económico · servicios contratados");
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[] {"Concepto", "Cantidad", "Precio unitario", "Importe"}, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
        };
        if (evento.getPaquete() != null) {
            BigDecimal precio = cero(evento.getPrecioPaquete());
            modelo.addRow(new Object[] {"Paquete: " + evento.getPaquete(), 1, dinero(precio), dinero(precio)});
        }
        for (ServicioEventoDetalle servicio : evento.getServicios()) {
            BigDecimal importe = cero(servicio.getPrecioAcordado()).multiply(BigDecimal.valueOf(servicio.getCantidad()));
            modelo.addRow(new Object[] {servicio.getNombre(), servicio.getCantidad(),
                    dinero(servicio.getPrecioAcordado()), dinero(importe)});
        }
        if (modelo.getRowCount() == 0) {
            modelo.addRow(new Object[] {"No hay servicios ni paquete cargados", "—", "—", dinero(BigDecimal.ZERO)});
        }
        JTable tabla = crearTabla(modelo);
        tabla.setPreferredScrollableViewportSize(new Dimension(900, Math.min(150, 26 * (modelo.getRowCount() + 1))));
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearResumenEconomico(DetalleEvento evento, DatosFacturacionEvento datos) {
        BigDecimal subtotal = cero(evento.getPrecioPaquete());
        for (ServicioEventoDetalle servicio : evento.getServicios()) {
            subtotal = subtotal.add(cero(servicio.getPrecioAcordado()).multiply(BigDecimal.valueOf(servicio.getCantidad())));
        }
        BigDecimal total = datos.getPresupuestoAprobado() == null ? subtotal : datos.getPresupuestoAprobado();
        BigDecimal pagos = BigDecimal.ZERO;
        for (DatosFacturacionEvento.Pago pago : datos.getPagos()) {
            BigDecimal monto = cero(pago.getMonto());
            pagos = pagos.add(monto);
        }
        BigDecimal saldo = total.subtract(pagos);
        JPanel panel = crearSeccion("Resumen de presupuesto y pagos");
        panel.setLayout(new GridLayout(0, 2, 8, 5));
        agregarDato(panel, "Subtotal de paquete y servicios", dinero(subtotal));
        agregarDato(panel, "Descuento", "No registrado en la base de datos");
        agregarDato(panel, "Total", dinero(total) + (datos.getPresupuestoAprobado() == null
                ? " · estimación" : " · presupuesto aceptado"));
        agregarDato(panel, "Seña acordada", datos.getMontoSena() == null
                ? "Pendiente de aprobación" : dinero(datos.getMontoSena()));
        agregarDato(panel, "Pagos registrados", dinero(pagos));
        agregarDato(panel, saldo.signum() >= 0 ? "Saldo pendiente" : "Saldo a favor", dinero(saldo.abs()));
        if (datos.getPresupuestoAprobado() == null) {
            JLabel nota = new JLabel("No hay presupuesto aceptado: el total se estima sumando el paquete y los servicios.");
            nota.setFont(FUENTE);
            nota.setForeground(new Color(70, 80, 90));
            panel.add(nota);
            panel.add(new JLabel(" "));
        }
        return panel;
    }

    private JTabbedPane crearPestanasPagosComprobantes(DatosFacturacionEvento datos) {
        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setFont(FUENTE);
        DefaultTableModel pagos = modeloNoEditable("ID Pago", "Fecha", "Monto", "Medio", "Estado del evento", "Observaciones");
        for (DatosFacturacionEvento.Pago pago : datos.getPagos()) {
            pagos.addRow(new Object[] {pago.getId(), fechaHora(pago.getFecha()), dinero(pago.getMonto()),
                    texto(pago.getMedio()), texto(pago.getEstadoEvento()), texto(pago.getObservaciones())});
        }
        if (pagos.getRowCount() == 0) pagos.addRow(new Object[] {"—", "—", "Sin pagos registrados", "—", "—", "—"});
        pestanas.addTab("Pagos registrados", new JScrollPane(crearTabla(pagos)));

        DefaultTableModel comprobantes = modeloNoEditable("Número", "Tipo", "Fecha de emisión", "Monto abonado");
        for (DatosFacturacionEvento.Comprobante comprobante : datos.getComprobantes()) {
            comprobantes.addRow(new Object[] {texto(comprobante.getNumero()), texto(comprobante.getTipo()),
                    fechaHora(comprobante.getFechaEmision()), dinero(comprobante.getMonto())});
        }
        if (comprobantes.getRowCount() == 0) {
            comprobantes.addRow(new Object[] {"Sin comprobantes emitidos", "—", "—", "—"});
        }
        pestanas.addTab("Comprobantes emitidos", new JScrollPane(crearTabla(comprobantes)));
        pestanas.setPreferredSize(new Dimension(940, 165));
        return pestanas;
    }

    private JPanel crearSeccion(String titulo) {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(190, 205, 218)), titulo),
                BorderFactory.createEmptyBorder(5, 10, 10, 10)));
        return panel;
    }

    private void agregarDato(JPanel panel, String etiqueta, String valor) {
        JLabel lblEtiqueta = new JLabel(etiqueta + ":");
        lblEtiqueta.setFont(new Font("Verdana", Font.BOLD, 13));
        lblEtiqueta.setForeground(AZUL);
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(FUENTE);
        lblValor.setForeground(Color.BLACK);
        panel.add(lblEtiqueta);
        panel.add(lblValor);
    }

    private JTable crearTabla(DefaultTableModel modelo) {
        JTable tabla = new JTable(modelo);
        tabla.setFont(FUENTE);
        tabla.setRowHeight(24);
        tabla.getTableHeader().setFont(new Font("Verdana", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(AZUL);
        tabla.getTableHeader().setForeground(Color.WHITE);
        return tabla;
    }

    private DefaultTableModel modeloNoEditable(String... columnas) {
        return new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
        };
    }

    private static BigDecimal cero(BigDecimal valor) { return valor == null ? BigDecimal.ZERO : valor; }
    private static String dinero(BigDecimal valor) { return DINERO.format(cero(valor)); }
    private static String texto(String valor) { return valor == null || valor.trim().isEmpty() ? "Sin informar" : valor; }
    private static String fechaHora(LocalDateTime valor) { return valor == null ? "Sin informar" : valor.format(FECHA_HORA); }
}

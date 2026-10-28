package com.horizonteeventos.app;

import com.horizonteeventos.dao.ClienteDAO;
import com.horizonteeventos.dao.CatalogoEventoDAO;
import com.horizonteeventos.dao.ConsultaModuloDAO;
import com.horizonteeventos.dao.GestionFinancieraDAO;
import com.horizonteeventos.dao.WebInboxDAO;
import com.horizonteeventos.dao.impl.ClienteDAOImpl;
import com.horizonteeventos.dao.impl.CatalogoEventoDAOImpl;
import com.horizonteeventos.dao.impl.ConsultaModuloDAOImpl;
import com.horizonteeventos.dao.impl.GestionFinancieraDAOImpl;
import com.horizonteeventos.modelo.Cliente;
import com.horizonteeventos.modelo.DatosTabla;
import com.horizonteeventos.modelo.Empleado;
import com.horizonteeventos.modelo.Paquete;
import com.horizonteeventos.modelo.Servicio;
import com.horizonteeventos.modelo.EventoConSaldo;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JDialog;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/** Dashboard de Administración con la paleta de colores institucional unificada. */
public class AdministracionFrame extends JFrame {

    // =========================================================
    // COLORES INSTITUCIONALES UNIFICADOS
    // =========================================================
    private final Color AZUL_PRINCIPAL = new Color(18, 51, 81);
    private final Color AZUL_SECUNDARIO = new Color(28, 61, 88);
    private final Color NARANJA = new Color(150, 59, 12);
    private final Color NARANJA_HOVER = new Color(119, 43, 6);
    private final Color GRIS_FONDO = new Color(245, 246, 249);
    private static final String INICIO = "Dashboard";

    private final Empleado usuario;
    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final CatalogoEventoDAO catalogoEventoDAO = new CatalogoEventoDAOImpl();
    private final ConsultaModuloDAO consultaDAO = new ConsultaModuloDAOImpl();
    private final GestionFinancieraDAO gestionFinancieraDAO = new GestionFinancieraDAOImpl();
    private final WebInboxDAO webInboxDAO = new WebInboxDAO();
    private static final BigDecimal PORCENTAJE_SENA = new BigDecimal("20.00");
    private final CardLayout tarjetas = new CardLayout();
    private final JPanel contenido = new JPanel(tarjetas);
    
    private final DefaultTableModel modeloClientes = new DefaultTableModel(
            new Object[] {"ID", "Nombre", "Apellido", "DNI/CUIT", "Email", "Teléfono"}, 0) {
        private static final long serialVersionUID = 1L;
        @Override public boolean isCellEditable(int fila, int columna) { return false; }
    };
    private final JTable tablaClientes = new JTable(modeloClientes);
    private final DefaultTableModel modeloPaquetes = new DefaultTableModel(
            new Object[] {"ID", "Paquete", "Descripción", "Precio base"}, 0) {
        private static final long serialVersionUID = 1L;
        @Override public boolean isCellEditable(int fila, int columna) { return false; }
    };
    private final JTable tablaPaquetes = new JTable(modeloPaquetes);

    public AdministracionFrame(Empleado empleado) {
        super("Horizonte Eventos - Panel de Administración");
        AccesibilidadUI.aplicar();
        this.usuario = empleado;
        RegistroAccionesUI.iniciar();
        RegistroAccionesUI.establecerUsuario(empleado);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1050, 650));
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        JPanel panelPrincipal = new JPanel(new BorderLayout(12, 12));
        panelPrincipal.setBackground(GRIS_FONDO);
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setContentPane(panelPrincipal);

        panelPrincipal.add(crearEncabezado(), BorderLayout.NORTH);
        panelPrincipal.add(crearNavegacion(), BorderLayout.WEST);
        
        contenido.setOpaque(false);
        panelPrincipal.add(contenido, BorderLayout.CENTER);

        contenido.add(crearInicio(), INICIO);
        contenido.add(crearPanelClientes(), "Clientes");
        contenido.add(crearPanelChatWeb(), "Chat web");
        contenido.add(crearPanelSolicitudesWeb(), "Solicitudes web");
        contenido.add(crearPanelPaquetes(), "Paquetes");
        agregarPanelConsulta("Presupuestos");
        agregarPanelConsulta("Eventos");
        agregarPanelConsulta("Pagos");
        agregarPanelConsulta("Comprobantes");
        agregarPanelConsulta("Proveedores");
        agregarPanelConsulta("Servicios");
        agregarPanelConsulta("Espacios");
        agregarPanelConsulta("Reportes");

        configurarTabla(tablaClientes);
        configurarTabla(tablaPaquetes);
        tarjetas.show(contenido, INICIO);
        cargarClientes();
        cargarPaquetes();
    }

    private JPanel crearEncabezado() {
        JPanel encabezado = new JPanel(new BorderLayout(16, 0));
        encabezado.setBackground(Color.WHITE);
        encabezado.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JPanel marca = new JPanel(new BorderLayout(10, 0));
        marca.setOpaque(false);
        marca.add(LogoUI.crearLogo(48, 48), BorderLayout.WEST);
        JPanel textosMarca = new JPanel();
        textosMarca.setOpaque(false);
        textosMarca.setLayout(new BoxLayout(textosMarca, BoxLayout.Y_AXIS));
        JLabel nombreEmpresa = new JLabel("HORIZONTE EVENTOS");
        nombreEmpresa.setFont(new Font("Verdana", Font.BOLD, 18));
        nombreEmpresa.setForeground(AZUL_PRINCIPAL);
        JLabel subtitulo = new JLabel("Sistema de Gestión · Atención web y administración");
        subtitulo.setFont(new Font("Verdana", Font.PLAIN, 12));
        subtitulo.setForeground(new Color(105, 117, 130));
        textosMarca.add(nombreEmpresa);
        textosMarca.add(Box.createVerticalStrut(3));
        textosMarca.add(subtitulo);
        marca.add(textosMarca, BorderLayout.CENTER);
        encabezado.add(marca, BorderLayout.WEST);

        JPanel sesion = new JPanel(new BorderLayout(12, 0));
        sesion.setOpaque(false);
        JLabel usuarioLabel = new JLabel("Administrador/a: " + usuario.getNombre() + " " + usuario.getApellido());
        usuarioLabel.setFont(new Font("Verdana", Font.BOLD, 13));
        usuarioLabel.setForeground(AZUL_SECUNDARIO);
        sesion.add(usuarioLabel, BorderLayout.CENTER);

        JButton cerrarSesion = crearBotonEstilizado("Cerrar Sesión", new Color(180, 50, 50));
        cerrarSesion.addActionListener(e -> cerrarSesion());
        sesion.add(cerrarSesion, BorderLayout.EAST);
        
        encabezado.add(sesion, BorderLayout.EAST);
        return encabezado;
    }

    private JPanel crearNavegacion() {
        JPanel navegacion = new JPanel();
        navegacion.setLayout(new BoxLayout(navegacion, BoxLayout.Y_AXIS));
        navegacion.setBackground(AZUL_PRINCIPAL);
        navegacion.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        navegacion.setPreferredSize(new Dimension(210, 0));

        JLabel titulo = new JLabel("  MENÚ ADMINISTRATIVO");
        titulo.setForeground(new Color(180, 198, 215));
        titulo.setFont(new Font("Verdana", Font.BOLD, 11));
        titulo.setAlignmentX(LEFT_ALIGNMENT);
        navegacion.add(titulo);
        navegacion.add(Box.createVerticalStrut(12));

        agregarBotonNav(navegacion, "Dashboard", INICIO);
        agregarBotonNav(navegacion, "Clientes", "Clientes");
        agregarBotonNav(navegacion, "Chat de usuarios", "Chat web");
        agregarBotonNav(navegacion, "Solicitudes web", "Solicitudes web");
        agregarBotonNav(navegacion, "Presupuestos", "Presupuestos");
        agregarBotonNav(navegacion, "Paquetes", "Paquetes");
        agregarBotonNav(navegacion, "Eventos", "Eventos");
        agregarBotonNav(navegacion, "Pagos", "Pagos");
        agregarBotonNav(navegacion, "Comprobantes", "Comprobantes");
        agregarBotonNav(navegacion, "Proveedores", "Proveedores");
        agregarBotonNav(navegacion, "Servicios", "Servicios");
        agregarBotonNav(navegacion, "Espacios", "Espacios");
        agregarBotonNav(navegacion, "Reportes", "Reportes");
        
        navegacion.add(Box.createVerticalGlue());
        return navegacion;
    }

    private void agregarBotonNav(JPanel navegacion, String texto, String tarjeta) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Verdana", Font.BOLD, 12));
        boton.setForeground(Color.WHITE);
        boton.setBackground(AZUL_PRINCIPAL);
        boton.setFocusPainted(false);
        boton.setHorizontalAlignment(JButton.LEFT);
        boton.setAlignmentX(LEFT_ALIGNMENT);
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        boton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 10));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(AZUL_SECUNDARIO);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(AZUL_PRINCIPAL);
            }
        });

        boton.addActionListener(e -> tarjetas.show(contenido, tarjeta));
        navegacion.add(boton);
        navegacion.add(Box.createVerticalStrut(2));
    }

    private JPanel crearInicio() {
        JPanel inicio = new JPanel(new BorderLayout(0, 20));
        inicio.setOpaque(false);
        inicio.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel titulos = new JPanel();
        titulos.setOpaque(false);
        titulos.setLayout(new BoxLayout(titulos, BoxLayout.Y_AXIS));
        JLabel titulo = new JLabel("Dashboard General");
        titulo.setFont(new Font("Verdana", Font.BOLD, 24));
        titulo.setForeground(AZUL_PRINCIPAL);
        JLabel descripcion = new JLabel("Resumen operativo del sistema Horizonte Eventos");
        descripcion.setFont(new Font("Verdana", Font.PLAIN, 13));
        descripcion.setForeground(new Color(105, 117, 130));
        titulos.add(titulo);
        titulos.add(Box.createVerticalStrut(4));
        titulos.add(descripcion);
        inicio.add(titulos, BorderLayout.NORTH);

        JPanel tarjetasResumen = new JPanel(new GridLayout(1, 2, 15, 15));
        tarjetasResumen.setOpaque(false);
        
        DatosTabla reporte = null;
        try { reporte = consultaDAO.consultar("Reportes"); } catch (SQLException ignored) { }
        
        tarjetasResumen.add(crearTarjetaKPI("Eventos registrados", valorReporte(reporte, "Eventos registrados"), AZUL_SECUNDARIO));
        tarjetasResumen.add(crearTarjetaKPI("Presupuestos aceptados", valorReporte(reporte, "Presupuestos aceptados"), AZUL_PRINCIPAL));

        JPanel cuerpo = new JPanel(new BorderLayout(0, 15));
        cuerpo.setOpaque(false);
        cuerpo.add(tarjetasResumen, BorderLayout.NORTH);
        
        JPanel bienvenida = new JPanel(new BorderLayout());
        bienvenida.setBackground(Color.WHITE);
        bienvenida.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(228, 233, 239)),
                BorderFactory.createEmptyBorder(20, 22, 20, 22)));
        JLabel mensaje = new JLabel("<html><b>Bienvenido/a, " + usuario.getNombre() + ".</b><br><br>Utiliza el menú lateral izquierdo para navegar y administrar los diferentes módulos operativos y comerciales de la empresa.</html>");
        mensaje.setFont(new Font("Verdana", Font.PLAIN, 14));
        bienvenida.add(mensaje, BorderLayout.CENTER);
        
        cuerpo.add(bienvenida, BorderLayout.CENTER);
        inicio.add(cuerpo, BorderLayout.CENTER);
        return inicio;
    }

    private JPanel crearTarjetaKPI(String titulo, String valor, Color colorBorde) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBorde, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitulo = new JLabel(titulo, JLabel.CENTER);
        lblTitulo.setFont(new Font("Verdana", Font.BOLD, 13));
        lblTitulo.setForeground(colorBorde);

        JLabel lblValor = new JLabel(valor, JLabel.CENTER);
        lblValor.setFont(new Font("Verdana", Font.BOLD, 22));
        lblValor.setForeground(AZUL_PRINCIPAL);

        tarjeta.add(lblTitulo, BorderLayout.NORTH);
        tarjeta.add(Box.createVerticalStrut(10), BorderLayout.CENTER);
        tarjeta.add(lblValor, BorderLayout.SOUTH);

        return tarjeta;
    }

    private String valorReporte(DatosTabla datos, String indicador) {
        if (datos == null) return "—";
        for (Object[] fila : datos.getFilas()) {
            if (indicador.equals(String.valueOf(fila[0]))) return String.valueOf(fila[1]);
        }
        return "0";
    }

    private JPanel crearPanelPaquetes() {
        JPanel panel = crearPanelBase("Paquetes", "Modifica las plantillas y sus recursos incluidos");
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        acciones.setOpaque(false);
        JButton nuevo = crearBotonEstilizado("Crear paquete", NARANJA);
        JButton editar = crearBotonEstilizado("Editar seleccionado", AZUL_SECUNDARIO);
        JButton actualizar = crearBotonEstilizado("Actualizar lista", AZUL_PRINCIPAL);
        acciones.add(nuevo);
        acciones.add(editar);
        acciones.add(actualizar);
        panel.add(PanelRolUI.crearListaFiltrable(tablaPaquetes), BorderLayout.CENTER);
        panel.add(acciones, BorderLayout.SOUTH);
        nuevo.addActionListener(e -> abrirEditorPaquete(new Paquete(0, "", "", BigDecimal.ZERO)));
        editar.addActionListener(e -> editarPaqueteSeleccionado());
        actualizar.addActionListener(e -> cargarPaquetes());
        return panel;
    }

    private void editarPaqueteSeleccionado() {
        int filaVista = tablaPaquetes.getSelectedRow();
        if (filaVista < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un paquete para modificarlo.");
            return;
        }
        int fila = tablaPaquetes.convertRowIndexToModel(filaVista);
        Object descripcion = modeloPaquetes.getValueAt(fila, 2);
        Paquete paquete = new Paquete((Integer) modeloPaquetes.getValueAt(fila, 0),
                String.valueOf(modeloPaquetes.getValueAt(fila, 1)),
                descripcion == null ? "" : String.valueOf(descripcion),
                (BigDecimal) modeloPaquetes.getValueAt(fila, 3));
        abrirEditorPaquete(paquete);
    }

    private void abrirEditorPaquete(Paquete paquete) {
        EditarPaqueteDialog dialogo = new EditarPaqueteDialog(this, catalogoEventoDAO, paquete);
        dialogo.setVisible(true);
        if (dialogo.isGuardado()) cargarPaquetes();
    }

    private void cargarPaquetes() {
        try {
            modeloPaquetes.setRowCount(0);
            for (Paquete paquete : catalogoEventoDAO.listarPaquetes()) {
                modeloPaquetes.addRow(new Object[] {paquete.getId(), paquete.getNombre(),
                        paquete.getDescripcion(), paquete.getPrecioFijo()});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar los paquetes: " + ex.getMessage(),
                    "Error de paquetes", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel crearPanelClientes() {
        JPanel panel = crearPanelBase("Gestión de Clientes", "Registro y mantenimiento de clientes en el sistema");
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        acciones.setOpaque(false);
        
        JButton nuevo = crearBotonEstilizado("Nuevo cliente", NARANJA);
        JButton editar = crearBotonEstilizado("Editar seleccionado", AZUL_SECUNDARIO);
        JButton eliminar = crearBotonEstilizado("Eliminar seleccionado", new Color(180, 50, 50));
        JButton actualizar = crearBotonEstilizado("Actualizar lista", AZUL_PRINCIPAL);
        
        acciones.add(nuevo); 
        acciones.add(editar); 
        acciones.add(eliminar); 
        acciones.add(actualizar);
        
        panel.add(PanelRolUI.crearListaFiltrable(tablaClientes), BorderLayout.CENTER);
        panel.add(acciones, BorderLayout.SOUTH);
        
        nuevo.addActionListener(e -> nuevoCliente());
        editar.addActionListener(e -> editarCliente());
        eliminar.addActionListener(e -> eliminarCliente());
        actualizar.addActionListener(e -> cargarClientes());
        return panel;
    }

    private JPanel crearPanelChatWeb() {
        JPanel panel = crearPanelBase("Chat de usuarios", "Consultas recibidas desde el sitio web y respuestas del equipo");
        DefaultTableModel modelo = new DefaultTableModel(new Object[] {"ID", "Cliente", "Email", "Mensaje", "Fecha"}, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
        };
        JTable tabla = new JTable(modelo);
        configurarTabla(tabla);
        JButton responder = crearBotonEstilizado("Ver conversación y responder", NARANJA);
        JButton actualizar = crearBotonEstilizado("Actualizar", AZUL_SECUNDARIO);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        acciones.setOpaque(false); acciones.add(responder); acciones.add(actualizar);
        panel.add(PanelRolUI.crearListaFiltrable(tabla), BorderLayout.CENTER);
        panel.add(acciones, BorderLayout.SOUTH);
        actualizar.addActionListener(e -> cargarMensajesWeb(modelo));
        responder.addActionListener(e -> {
            int row = tabla.getSelectedRow();
            if (row < 0) { JOptionPane.showMessageDialog(this, "Selecciona una conversación."); return; }
            int modelRow = tabla.convertRowIndexToModel(row);
            abrirConversacionWeb((Integer) modelo.getValueAt(modelRow, 0), modelo);
        });
        cargarMensajesWeb(modelo);
        return panel;
    }

    private void cargarMensajesWeb(DefaultTableModel modelo) {
        try {
            modelo.setRowCount(0);
            for (WebInboxDAO.Message m : webInboxDAO.messages()) {
                if (!m.admin && m.replyTo == null) modelo.addRow(new Object[] {m.id, m.name, m.email, m.message, m.date});
            }
        } catch (SQLException ex) { mostrarError("No se pudieron cargar los mensajes web: " + ex.getMessage()); }
    }

    private void abrirConversacionWeb(int rootId, DefaultTableModel modelo) {
        try {
            StringBuilder transcript = new StringBuilder();
            for (WebInboxDAO.Message m : webInboxDAO.messages()) {
                if (m.id == rootId || (m.replyTo != null && m.replyTo == rootId)) {
                    transcript.append(m.admin ? "\nEquipo Horizonte · " : "\n" + m.name + " · ").append(m.date)
                            .append("\n").append(m.message).append("\n");
                }
            }
            JTextArea history = new JTextArea(transcript.toString(), 14, 48);
            history.setEditable(false); history.setLineWrap(true); history.setWrapStyleWord(true);
            JTextArea reply = new JTextArea(4, 48); reply.setLineWrap(true); reply.setWrapStyleWord(true);
            JPanel body = new JPanel(new BorderLayout(8, 8));
            body.add(new JScrollPane(history), BorderLayout.CENTER);
            body.add(new JScrollPane(reply), BorderLayout.SOUTH);
            int choice = JOptionPane.showConfirmDialog(this, body, "Conversación web · Responder", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (choice == JOptionPane.OK_OPTION) {
                String text = reply.getText().trim();
                if (text.isEmpty()) { JOptionPane.showMessageDialog(this, "Escribe una respuesta antes de enviarla."); return; }
                webInboxDAO.reply(text, rootId);
                cargarMensajesWeb(modelo);
                JOptionPane.showMessageDialog(this, "Respuesta enviada. El cliente la verá en el chat del sitio.");
            }
        } catch (SQLException ex) { mostrarError("No se pudo abrir la conversación: " + ex.getMessage()); }
    }

    private JPanel crearPanelSolicitudesWeb() {
        JPanel panel = crearPanelBase("Solicitudes de eventos", "Revisa cada propuesta, responde al cliente y actualiza su seguimiento");
        DefaultTableModel modelo = new DefaultTableModel(new Object[] {"ID", "Cliente", "Evento", "Fecha tentativa", "Invitados", "Estado", "Recibida"}, 0) {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
        };
        JTable tabla = new JTable(modelo); configurarTabla(tabla);
        JButton atender = crearBotonEstilizado("Ver detalle y responder", NARANJA);
        JButton actualizar = crearBotonEstilizado("Actualizar", AZUL_SECUNDARIO);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        acciones.setOpaque(false); acciones.add(atender); acciones.add(actualizar);
        panel.add(PanelRolUI.crearListaFiltrable(tabla), BorderLayout.CENTER); panel.add(acciones, BorderLayout.SOUTH);
        actualizar.addActionListener(e -> cargarSolicitudesWeb(modelo));
        atender.addActionListener(e -> {
            int row=tabla.getSelectedRow(); if(row<0){JOptionPane.showMessageDialog(this,"Selecciona una solicitud.");return;}
            int modelRow=tabla.convertRowIndexToModel(row); long id=((Number)modelo.getValueAt(modelRow,0)).longValue();
            abrirSolicitudWeb(id, modelo);
        });
        cargarSolicitudesWeb(modelo);
        return panel;
    }

    private void cargarSolicitudesWeb(DefaultTableModel modelo) {
        try {
            modelo.setRowCount(0);
            for (WebInboxDAO.Request r : webInboxDAO.requests()) modelo.addRow(new Object[] {r.id,r.name,r.type,r.date,r.guests,r.status,r.created});
        } catch (SQLException ex) { mostrarError("No se pudieron cargar las solicitudes: " + ex.getMessage()); }
    }

    private void abrirSolicitudWeb(long id, DefaultTableModel modelo) {
        try {
            WebInboxDAO.Request request = null;
            for (WebInboxDAO.Request item : webInboxDAO.requests()) if (item.id == id) { request=item; break; }
            if (request == null) { cargarSolicitudesWeb(modelo); return; }
            JTextArea detail = new JTextArea("Cliente: " + request.name + "\nEmail: " + request.email + "\nTeléfono: " + request.phone
                    + "\nEvento: " + request.type + "\nFecha: " + request.date + " · " + request.time
                    + "\nInvitados: " + request.guests + "\nEspacio: " + request.space + "\nServicios: " + request.services
                    + "\nComentarios: " + request.comments, 9, 44);
            detail.setEditable(false); detail.setLineWrap(true); detail.setWrapStyleWord(true);
            JComboBox<String> status = new JComboBox<>(new String[] {"Pendiente", "Contactado", "Aprobado", "Rechazado"}); status.setSelectedItem(request.status);
            JTextArea response = new JTextArea(request.response == null ? "" : request.response, 5, 44); response.setLineWrap(true); response.setWrapStyleWord(true);
            JPanel form = new JPanel(new BorderLayout(6,6)); form.add(new JScrollPane(detail),BorderLayout.NORTH);
            JPanel answer = new JPanel(new BorderLayout(4,4)); answer.add(new JLabel("Estado de seguimiento"),BorderLayout.NORTH); answer.add(status,BorderLayout.CENTER);
            answer.add(new JLabel("Respuesta para el cliente (visible en Mi cuenta)"),BorderLayout.SOUTH);
            JPanel lower = new JPanel(new BorderLayout(5,5)); lower.add(answer,BorderLayout.NORTH); lower.add(new JScrollPane(response),BorderLayout.CENTER); form.add(lower,BorderLayout.CENTER);
            if (JOptionPane.showConfirmDialog(this,form,"Solicitud web #"+id,JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE)==JOptionPane.OK_OPTION) {
                webInboxDAO.answerRequest(id,(String)status.getSelectedItem(),response.getText().trim());
                cargarSolicitudesWeb(modelo); JOptionPane.showMessageDialog(this,"Solicitud actualizada; el cliente verá la respuesta en su cuenta.");
            }
        } catch (SQLException ex) { mostrarError("No se pudo actualizar la solicitud: " + ex.getMessage()); }
    }

    private JPanel crearPanelBase(String titulo, String subtitulo) {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel encabezado = new JPanel();
        encabezado.setOpaque(false);
        encabezado.setLayout(new BoxLayout(encabezado, BoxLayout.Y_AXIS));
        
        JLabel nombre = new JLabel(titulo);
        nombre.setFont(new Font("Verdana", Font.BOLD, 22));
        nombre.setForeground(AZUL_PRINCIPAL);
        
        JLabel descripcion = new JLabel(subtitulo);
        descripcion.setFont(new Font("Verdana", Font.PLAIN, 13));
        descripcion.setForeground(new Color(105, 117, 130));
        
        encabezado.add(nombre);
        encabezado.add(Box.createVerticalStrut(3));
        encabezado.add(descripcion);
        panel.add(encabezado, BorderLayout.NORTH);
        return panel;
    }

    private void agregarPanelConsulta(String nombreModulo) {
        JPanel panel = crearPanelBase(nombreModulo, "Consulta general de información registrada en base de datos");
        DefaultTableModel modelo = new DefaultTableModel() {
            private static final long serialVersionUID = 1L;
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
        };
        JTable tabla = new JTable(modelo);
        configurarTabla(tabla);
        
        JButton actualizar = crearBotonEstilizado("Actualizar información", AZUL_SECUNDARIO);
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        pie.setOpaque(false);
        pie.add(actualizar);
        if ("Presupuestos".equals(nombreModulo)) {
            JButton aprobar = crearBotonEstilizado("Aprobar presupuesto / definir seña (20%)", NARANJA);
            JButton rechazar = crearBotonEstilizado("Rechazar presupuesto", new Color(180, 50, 50));
            pie.add(aprobar);
            pie.add(rechazar);
            aprobar.addActionListener(e -> aprobarPresupuestoSeleccionado(tabla, modelo));
            rechazar.addActionListener(e -> rechazarPresupuestoSeleccionado(tabla, modelo));
        }
        if ("Pagos".equals(nombreModulo)) {
            JButton registrarPago = crearBotonEstilizado("Registrar pago", NARANJA);
            pie.add(registrarPago);
            registrarPago.addActionListener(e -> registrarPago(modelo));
        }
        if ("Eventos".equals(nombreModulo)) {
            JButton crearEvento = crearBotonEstilizado("Crear evento", NARANJA);
            pie.add(crearEvento);
            crearEvento.addActionListener(e -> {
                try {
                    CrearEventoDialog dialogo = new CrearEventoDialog(this);
                    dialogo.setVisible(true);
                    int nuevoId = dialogo.getIdEventoCreado();
                    cargarModulo(nombreModulo, modelo);
                    if (nuevoId > 0) seleccionarEvento(tabla, modelo, nuevoId);
                } catch (RuntimeException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "No se pudo abrir el formulario de creación de eventos:\n" + ex.getMessage(),
                            "Error al abrir el formulario", JOptionPane.ERROR_MESSAGE);
                }
            });
        }
        if ("Servicios".equals(nombreModulo)) {
            JButton nuevoServicio = crearBotonEstilizado("Agregar recurso / servicio", NARANJA);
            JButton editarServicio = crearBotonEstilizado("Editar seleccionado", AZUL_SECUNDARIO);
            JButton eliminarServicio = crearBotonEstilizado("Quitar del catálogo", new Color(180, 50, 50));
            pie.add(nuevoServicio);
            pie.add(editarServicio);
            pie.add(eliminarServicio);
            nuevoServicio.addActionListener(e -> editarServicio(modelo, null));
            editarServicio.addActionListener(e -> {
                int filaVista = tabla.getSelectedRow();
                if (filaVista < 0) {
                    JOptionPane.showMessageDialog(this, "Selecciona un servicio para modificar.");
                    return;
                }
                int fila = tabla.convertRowIndexToModel(filaVista);
                Servicio servicio = new Servicio((Integer) modelo.getValueAt(fila, 0),
                        String.valueOf(modelo.getValueAt(fila, 1)),
                        new BigDecimal(String.valueOf(modelo.getValueAt(fila, 3))),
                        leerCapacidad(modelo.getValueAt(fila, 4)));
                editarServicio(modelo, servicio);
            });
            eliminarServicio.addActionListener(e -> eliminarServicioSeleccionado(tabla, modelo));
        }
        
        panel.add(PanelRolUI.crearListaFiltrable(tabla), BorderLayout.CENTER);
        if ("Eventos".equals(nombreModulo)) {
            tabla.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                    int filaVista = tabla.rowAtPoint(e.getPoint());
                    if (filaVista < 0) return;
                    tabla.setRowSelectionInterval(filaVista, filaVista);
                    int filaModelo = tabla.convertRowIndexToModel(filaVista);
                    Object id = modelo.getValueAt(filaModelo, 0);
                    if (id instanceof Number) {
                        new SimulacionFacturacionDialog(AdministracionFrame.this,
                                ((Number) id).intValue()).setVisible(true);
                    }
                }
            });
        }
        panel.add(pie, BorderLayout.SOUTH);
        
        actualizar.addActionListener(e -> cargarModulo(nombreModulo, modelo));
        contenido.add(panel, nombreModulo);
        cargarModulo(nombreModulo, modelo);
    }

    private void seleccionarEvento(JTable tabla, DefaultTableModel modelo, int idEvento) {
        for (int fila = 0; fila < modelo.getRowCount(); fila++) {
            Object valor = modelo.getValueAt(fila, 0);
            if (valor instanceof Number && ((Number) valor).intValue() == idEvento) {
                int filaVista = tabla.convertRowIndexToView(fila);
                if (filaVista >= 0) tabla.setRowSelectionInterval(filaVista, filaVista);
                return;
            }
        }
    }

    private void editarServicio(DefaultTableModel modelo, Servicio original) {
        JTextField nombre = new JTextField(original == null ? "" : original.getNombre(), 22);
        JTextField costo = new JTextField(original == null ? "0.00" : original.getCostoUnitario().toPlainString(), 12);
        JTextField capacidad = new JTextField(original == null || original.getCantidadDisponible() == null
                ? "" : String.valueOf(original.getCantidadDisponible()), 12);
        JPanel formulario = new JPanel(new GridLayout(0, 2, 8, 8));
        formulario.add(new JLabel("Nombre del recurso o servicio:"));
        formulario.add(nombre);
        formulario.add(new JLabel("Precio unitario de lista:"));
        formulario.add(costo);
        formulario.add(new JLabel("Cantidad disponible (vacío = confirmar manualmente):"));
        formulario.add(capacidad);
        int opcion = JOptionPane.showConfirmDialog(this, formulario,
                original == null ? "Agregar al catálogo" : "Editar recurso o servicio",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) return;
        if (nombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe el nombre del recurso o servicio.");
            return;
        }
        try {
            BigDecimal valor = new BigDecimal(costo.getText().trim().replace(',', '.'));
            if (valor.signum() < 0) throw new NumberFormatException();
            Long unidades = capacidad.getText().trim().isEmpty()
                    ? null : Long.valueOf(capacidad.getText().trim());
            if (unidades != null && unidades < 0) throw new NumberFormatException();
            Servicio servicio = original == null ? new Servicio(0, nombre.getText().trim(), valor) : original;
            servicio.setNombre(nombre.getText().trim());
            servicio.setCostoUnitario(valor);
            servicio.setCantidadDisponible(unidades);
            catalogoEventoDAO.guardarServicio(servicio);
            cargarModulo("Servicios", modelo);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Revisa costo y disponibilidad. La disponibilidad debe ser "
                    + "un entero mayor o igual a cero; déjala vacía si aún no se conoce.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el recurso: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Long leerCapacidad(Object valor) {
        if (valor == null || "Sin informar".equalsIgnoreCase(String.valueOf(valor))) return null;
        if (valor instanceof Number) return ((Number) valor).longValue();
        try { return Long.valueOf(String.valueOf(valor)); }
        catch (NumberFormatException ex) { return null; }
    }

    private void eliminarServicioSeleccionado(JTable tabla, DefaultTableModel modelo) {
        int filaVista = tabla.getSelectedRow();
        if (filaVista < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un recurso del catálogo.");
            return;
        }
        int fila = tabla.convertRowIndexToModel(filaVista);
        int id = (Integer) modelo.getValueAt(fila, 0);
        String nombre = String.valueOf(modelo.getValueAt(fila, 1));
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Quitar '" + nombre + "' del catálogo? Si ya se usa en un paquete o evento, la base lo protegerá.",
                "Confirmar", JOptionPane.YES_NO_OPTION);
        if (opcion != JOptionPane.YES_OPTION) return;
        try {
            catalogoEventoDAO.eliminarServicio(id);
            cargarModulo("Servicios", modelo);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se puede quitar: el recurso ya está asociado a un paquete o evento.\n" + ex.getMessage(),
                    "Recurso en uso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void configurarTabla(JTable tabla) {
        PanelRolUI.estilizarTabla(tabla);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private JButton crearBotonEstilizado(String texto, Color colorBase) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Verdana", Font.BOLD, 12));
        boton.setForeground(Color.WHITE);
        boton.setBackground(colorBase);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (colorBase == NARANJA) boton.setBackground(NARANJA_HOVER);
                else boton.setBackground(colorBase.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(colorBase);
            }
        });
        return boton;
    }

    private void cargarModulo(String nombre, DefaultTableModel modelo) {
        try {
            DatosTabla datos = consultaDAO.consultar(nombre);
            Object[][] matrizFilas = datos.getFilas().toArray(new Object[0][]);
            modelo.setDataVector(matrizFilas, datos.getColumnas());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar " + nombre + ": " + ex.getMessage(),
                    "Error de consulta", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void aprobarPresupuestoSeleccionado(JTable tabla, DefaultTableModel modelo) {
        int filaVista = tabla.getSelectedRow();
        if (filaVista < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un presupuesto.");
            return;
        }
        int fila = tabla.convertRowIndexToModel(filaVista);
        int idPresupuesto = ((Number) modelo.getValueAt(fila, 0)).intValue();
        BigDecimal total = new BigDecimal(String.valueOf(modelo.getValueAt(fila, 7)));
        BigDecimal sena = total.multiply(PORCENTAJE_SENA).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        String estado = String.valueOf(modelo.getValueAt(fila, 9));
        if (!"Pendiente".equalsIgnoreCase(estado) && !"Aceptado".equalsIgnoreCase(estado)) {
            JOptionPane.showMessageDialog(this, "Solo se puede aprobar un presupuesto pendiente o definir la seña de uno aceptado.");
            return;
        }
        int confirmar = JOptionPane.showConfirmDialog(this,
                "Total del presupuesto: " + dinero(total) + "\nSeña fija (20 %): " + dinero(sena)
                        + "\n\n¿Confirmas la aprobación y habilitas el registro de pagos?",
                "Aprobar presupuesto", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirmar != JOptionPane.YES_OPTION) return;
        try {
            gestionFinancieraDAO.aprobarPresupuesto(idPresupuesto, sena);
            cargarModulo("Presupuestos", modelo);
            JOptionPane.showMessageDialog(this, "Presupuesto aprobado. La seña acordada es " + dinero(sena) + ".");
        } catch (SQLException ex) { mostrarError(ex); }
    }

    private void rechazarPresupuestoSeleccionado(JTable tabla, DefaultTableModel modelo) {
        int filaVista = tabla.getSelectedRow();
        if (filaVista < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un presupuesto pendiente.");
            return;
        }
        int fila = tabla.convertRowIndexToModel(filaVista);
        int id = ((Number) modelo.getValueAt(fila, 0)).intValue();
        if (!"Pendiente".equalsIgnoreCase(String.valueOf(modelo.getValueAt(fila, 9)))) {
            JOptionPane.showMessageDialog(this, "Solo se pueden rechazar presupuestos pendientes.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "¿Rechazar el presupuesto seleccionado?", "Rechazar presupuesto",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return;
        try {
            if (gestionFinancieraDAO.rechazarPresupuesto(id)) {
                cargarModulo("Presupuestos", modelo);
                JOptionPane.showMessageDialog(this, "Presupuesto rechazado.");
            }
        } catch (SQLException ex) { mostrarError(ex); }
    }

    private void registrarPago(DefaultTableModel modeloPagos) {
        try {
            List<EventoConSaldo> eventos = gestionFinancieraDAO.listarEventosConSaldo();
            if (eventos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay eventos aprobados con seña definida y saldo pendiente.");
                return;
            }
            JComboBox<EventoConSaldo> selectorEvento = new JComboBox<>(eventos.toArray(new EventoConSaldo[0]));
            JTextField importe = new JTextField();
            JComboBox<String> medio = new JComboBox<>(new String[] {"Efectivo", "Transferencia", "Tarjeta", "Otro"});
            JTextField observaciones = new JTextField();
            JPanel formulario = new JPanel(new GridLayout(0, 2, 8, 8));
            formulario.add(new JLabel("Evento con saldo:")); formulario.add(selectorEvento);
            formulario.add(new JLabel("Importe del pago:")); formulario.add(importe);
            formulario.add(new JLabel("Medio de pago:")); formulario.add(medio);
            formulario.add(new JLabel("Observaciones (opcional):")); formulario.add(observaciones);
            int opcion = JOptionPane.showConfirmDialog(this, formulario, "Registrar pago",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (opcion != JOptionPane.OK_OPTION) return;
            EventoConSaldo seleccionado = (EventoConSaldo) selectorEvento.getSelectedItem();
            BigDecimal monto = new BigDecimal(importe.getText().trim().replace(',', '.'));
            if (monto.signum() <= 0 || monto.compareTo(seleccionado.getSaldo()) > 0) {
                JOptionPane.showMessageDialog(this, "El importe debe ser mayor que cero y no superar el saldo de "
                        + dinero(seleccionado.getSaldo()) + ".", "Importe inválido", JOptionPane.WARNING_MESSAGE);
                return;
            }
            gestionFinancieraDAO.registrarPago(seleccionado.getIdEvento(), monto,
                    String.valueOf(medio.getSelectedItem()), null, observaciones.getText());
            cargarModulo("Pagos", modeloPagos);
            JOptionPane.showMessageDialog(this, "Pago registrado. El estado del evento se actualizó automáticamente.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingresa un importe numérico válido.", "Importe inválido", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) { mostrarError(ex); }
    }

    private String dinero(BigDecimal valor) {
        NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));
        return formato.format(valor);
    }
    private void cargarClientes() {
        try {
            modeloClientes.setRowCount(0);
            List<Cliente> clientes = clienteDAO.listarTodos();
            for (Cliente c : clientes) {
                modeloClientes.addRow(new Object[] {c.getIdCliente(), c.getNombre(), c.getApellido(),
                        c.getDniCuit(), c.getEmail(), c.getTelefono()});
            }
        } catch (SQLException ex) { mostrarError(ex); }
    }

    private void nuevoCliente() {
        Cliente cliente = pedirDatos(null);
        if (cliente == null) return;
        try {
            clienteDAO.insertar(cliente);
            cargarClientes();
            JOptionPane.showMessageDialog(this, "Cliente guardado correctamente.");
        } catch (SQLException ex) { mostrarError(ex); }
    }

    private void editarCliente() {
        Cliente seleccionado = clienteSeleccionado();
        if (seleccionado == null) return;
        Cliente cambios = pedirDatos(seleccionado);
        if (cambios == null) return;
        cambios.setIdCliente(seleccionado.getIdCliente());
        try {
            if (clienteDAO.actualizar(cambios)) {
                cargarClientes();
                JOptionPane.showMessageDialog(this, "Cliente actualizado correctamente.");
            }
        } catch (SQLException ex) { mostrarError(ex); }
    }

    private void eliminarCliente() {
        Cliente cliente = clienteSeleccionado();
        if (cliente == null) return;
        int confirmar = JOptionPane.showConfirmDialog(this,
                "Se eliminará a " + cliente.getNombre() + " " + cliente.getApellido() + ". ¿Deseas continuar?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmar != JOptionPane.YES_OPTION) return;
        try {
            clienteDAO.eliminar(cliente.getIdCliente());
            cargarClientes();
            JOptionPane.showMessageDialog(this, "Cliente eliminado.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar. Es posible que el cliente ya tenga eventos asociados.\n"
                            + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Cliente clienteSeleccionado() {
        int fila = tablaClientes.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona primero un cliente de la tabla.");
            return null;
        }
        fila = tablaClientes.convertRowIndexToModel(fila);
        return new Cliente((Integer) modeloClientes.getValueAt(fila, 0),
                (String) modeloClientes.getValueAt(fila, 1), (String) modeloClientes.getValueAt(fila, 2),
                (String) modeloClientes.getValueAt(fila, 3), (String) modeloClientes.getValueAt(fila, 4),
                (String) modeloClientes.getValueAt(fila, 5));
    }

    private Cliente pedirDatos(Cliente actual) {
        JTextField nombre = new JTextField(actual == null ? "" : actual.getNombre());
        JTextField apellido = new JTextField(actual == null ? "" : actual.getApellido());
        JTextField dni = new JTextField(actual == null ? "" : actual.getDniCuit());
        JTextField email = new JTextField(actual == null ? "" : actual.getEmail());
        JTextField telefono = new JTextField(actual == null ? "" : actual.getTelefono());
        
        JPanel formulario = new JPanel(new GridLayout(0, 2, 8, 8));
        formulario.add(new JLabel("Nombre:")); formulario.add(nombre);
        formulario.add(new JLabel("Apellido:")); formulario.add(apellido);
        formulario.add(new JLabel("DNI/CUIT:")); formulario.add(dni);
        formulario.add(new JLabel("Email:")); formulario.add(email);
        formulario.add(new JLabel("Teléfono:")); formulario.add(telefono);
        
        int opcion = JOptionPane.showConfirmDialog(this, formulario,
                actual == null ? "Nuevo cliente" : "Editar cliente", JOptionPane.OK_CANCEL_OPTION);
        if (opcion != JOptionPane.OK_OPTION) return null;
        if (nombre.getText().trim().isEmpty() || apellido.getText().trim().isEmpty()
                || dni.getText().trim().isEmpty() || email.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre, apellido, DNI/CUIT y email son obligatorios.");
            return null;
        }
        return new Cliente(0, nombre.getText().trim(), apellido.getText().trim(),
                dni.getText().trim(), email.getText().trim(), telefono.getText().trim());
    }

    private void cerrarSesion() {
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Deseas cerrar la sesión actual?", "Confirmar cierre de sesión",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (respuesta != JOptionPane.YES_OPTION) return;
        RegistroAccionesUI.establecerUsuario(null);
        dispose();
        new LoginFrame().setVisible(true);
    }

    private void mostrarError(SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error de base de datos: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}

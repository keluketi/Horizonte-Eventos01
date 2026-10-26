package com.horizonteeventos.app;

import com.horizonteeventos.config.Conexion;
import com.horizonteeventos.dao.EmpleadoDAO;
import com.horizonteeventos.dao.EventoDAO;
import com.horizonteeventos.dao.RolDAO;
import com.horizonteeventos.dao.impl.EmpleadoDAOImpl;
import com.horizonteeventos.dao.impl.EventoDAOImpl;
import com.horizonteeventos.dao.impl.RolDAOImpl;
import com.horizonteeventos.modelo.Empleado;
import com.horizonteeventos.modelo.Evento;
import com.horizonteeventos.modelo.Rol;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/** Panel de Gerencia con menú lateral y diseño unificado al Administrador. */
public class GerenciaFrame extends JFrame {

    // =========================================================
    // COLORES INSTITUCIONALES UNIFICADOS
    // =========================================================
    private final Color AZUL_PRINCIPAL = new Color(18, 51, 81);
    private final Color AZUL_SECUNDARIO = new Color(28, 61, 88);
    private final Color NARANJA = new Color(150, 59, 12);
    private final Color NARANJA_HOVER = new Color(119, 43, 6);
    private final Color GRIS_FONDO = new Color(245, 246, 249);
    private static final String INICIO = "Dashboard";

    // DAOs
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAOImpl();
    private final RolDAO rolDAO = new RolDAOImpl();
    private final EventoDAO eventoDAO = new EventoDAOImpl();

    private final Empleado usuario;
    private final CardLayout tarjetas = new CardLayout();
    private final JPanel contenido = new JPanel(tarjetas);

    // Tablas y Modelos
    private final DefaultTableModel modeloEmpleados = new DefaultTableModel(
            new Object[] {"ID", "Rol", "Nombre", "Apellido", "CUIL", "Email", "Teléfono"}, 0) {
        private static final long serialVersionUID = 1L;
        @Override public boolean isCellEditable(int fila, int columna) { return false; }
    };
    private final JTable tablaEmpleados = new JTable(modeloEmpleados);

    private final DefaultTableModel modeloEventos = new DefaultTableModel(
            new Object[] {"ID Evento", "ID Cliente", "ID Espacio", "Fecha", "Hora Inicio", "Hora Fin", "Invitados"}, 0) {
        private static final long serialVersionUID = 1L;
        @Override public boolean isCellEditable(int fila, int columna) { return false; }
    };
    private final JTable tablaEventos = new JTable(modeloEventos);

    private final DefaultTableModel modeloIngresos = new DefaultTableModel(
            new Object[] {"ID Pago", "ID Evento", "Fecha Pago", "Monto ($)", "Medio de Pago", "Estado del evento", "Observaciones"}, 0) {
        private static final long serialVersionUID = 1L;
        @Override public boolean isCellEditable(int fila, int columna) { return false; }
    };
    private final JTable tablaIngresos = new JTable(modeloIngresos);

    private final DefaultTableModel modeloEspacios = new DefaultTableModel(
            new Object[] {"ID Espacio", "Nombre del Espacio", "Capacidad Máxima", "Precio Base Alquiler ($)"}, 0) {
        private static final long serialVersionUID = 1L;
        @Override public boolean isCellEditable(int fila, int columna) { return false; }
    };
    private final JTable tablaEspacios = new JTable(modeloEspacios);

    private final DefaultTableModel modeloDesempeno = new DefaultTableModel(
            new Object[] {"ID Evento", "Empleado", "Rol en Evento"}, 0) {
        private static final long serialVersionUID = 1L;
        @Override public boolean isCellEditable(int fila, int columna) { return false; }
    };
    private final JTable tablaDesempeno = new JTable(modeloDesempeno);

    private final DefaultTableModel modeloAuditoria = new DefaultTableModel(
            new Object[] {"ID", "Tipo", "Empleado", "Rol", "Pantalla", "Acción", "Fecha y Hora", "ID Evento", "ID Empleado"}, 0) {
        private static final long serialVersionUID = 1L;
        @Override public boolean isCellEditable(int fila, int columna) { return false; }
    };
    private final JTable tablaAuditoria = new JTable(modeloAuditoria);

    // Etiquetas para KPIs en Dashboard
    private final JLabel lblTotalClientes = new JLabel("Cargando...", JLabel.CENTER);
    private final JLabel lblTotalEventos = new JLabel("Cargando...", JLabel.CENTER);
    private final JLabel lblTotalRecaudado = new JLabel("Cargando...", JLabel.CENTER);

    public GerenciaFrame(Empleado empleado) {
        super("Horizonte Eventos - Panel de Gerencia");
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
        contenido.add(crearPanelEmpleados(), "Empleados");
        contenido.add(crearPanelEventos(), "Eventos");
        contenido.add(crearPanelIngresos(), "Ingresos");
        contenido.add(crearPanelEspacios(), "Espacios");
        contenido.add(crearPanelDesempeno(), "Desempeño");
        contenido.add(crearPanelAuditoria(), "Auditoría");

        tarjetas.show(contenido, INICIO);
        cargarDatosGenerales();
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
        JLabel subtitulo = new JLabel("Sistema de Gestión · Panel de Gerencia");
        subtitulo.setFont(new Font("Verdana", Font.PLAIN, 12));
        subtitulo.setForeground(new Color(105, 117, 130));
        textosMarca.add(nombreEmpresa);
        textosMarca.add(Box.createVerticalStrut(3));
        textosMarca.add(subtitulo);
        marca.add(textosMarca, BorderLayout.CENTER);
        encabezado.add(marca, BorderLayout.WEST);

        JPanel sesion = new JPanel(new BorderLayout(12, 0));
        sesion.setOpaque(false);
        JLabel usuarioLabel = new JLabel("Gerente/a: " + usuario.getNombre() + " " + usuario.getApellido());
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

        JLabel titulo = new JLabel("  MENÚ GERENCIAL");
        titulo.setForeground(new Color(180, 198, 215));
        titulo.setFont(new Font("Verdana", Font.BOLD, 11));
        titulo.setAlignmentX(LEFT_ALIGNMENT);
        navegacion.add(titulo);
        navegacion.add(Box.createVerticalStrut(12));

        agregarBotonNav(navegacion, "Dashboard", INICIO);
        agregarBotonNav(navegacion, "Empleados", "Empleados");
        agregarBotonNav(navegacion, "Eventos", "Eventos");
        agregarBotonNav(navegacion, "Ingresos", "Ingresos");
        agregarBotonNav(navegacion, "Espacios", "Espacios");
        agregarBotonNav(navegacion, "Desempeño", "Desempeño");
        agregarBotonNav(navegacion, "Auditoría", "Auditoría");

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

        boton.addActionListener(e -> {
            tarjetas.show(contenido, tarjeta);
            if (tarjeta.equals("Empleados")) cargarEmpleados();
            else if (tarjeta.equals("Eventos")) cargarEventos();
            else if (tarjeta.equals("Ingresos")) cargarIngresos();
            else if (tarjeta.equals("Espacios")) cargarEspacios();
            else if (tarjeta.equals("Desempeño")) cargarDesempeno();
            else if (tarjeta.equals("Auditoría")) cargarAuditoria();
            else if (tarjeta.equals(INICIO)) cargarDatosGenerales();
        });
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
        JLabel titulo = new JLabel("Dashboard Gerencial");
        titulo.setFont(new Font("Verdana", Font.BOLD, 24));
        titulo.setForeground(AZUL_PRINCIPAL);
        JLabel descripcion = new JLabel("Indicadores clave y resumen operativo de Horizonte Eventos");
        descripcion.setFont(new Font("Verdana", Font.PLAIN, 13));
        descripcion.setForeground(new Color(105, 117, 130));
        titulos.add(titulo);
        titulos.add(Box.createVerticalStrut(4));
        titulos.add(descripcion);
        inicio.add(titulos, BorderLayout.NORTH);

        JPanel tarjetasResumen = new JPanel(new GridLayout(1, 3, 15, 15));
        tarjetasResumen.setOpaque(false);

        tarjetasResumen.add(crearTarjetaKPI("Clientes Totales", lblTotalClientes, AZUL_SECUNDARIO));
        tarjetasResumen.add(crearTarjetaKPI("Eventos Registrados", lblTotalEventos, AZUL_PRINCIPAL));
        tarjetasResumen.add(crearTarjetaKPI("Recaudación Total", lblTotalRecaudado, NARANJA));

        JPanel cuerpo = new JPanel(new BorderLayout(0, 15));
        cuerpo.setOpaque(false);
        cuerpo.add(tarjetasResumen, BorderLayout.NORTH);

        JPanel bienvenida = new JPanel(new BorderLayout());
        bienvenida.setBackground(Color.WHITE);
        bienvenida.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(228, 233, 239)),
                BorderFactory.createEmptyBorder(20, 22, 20, 22)));
        JLabel mensaje = new JLabel("<html><b>Bienvenido/a, " + usuario.getNombre() + ".</b><br><br>Utiliza el menú lateral izquierdo para consultar los reportes de personal, eventos, ingresos, ocupación de espacios y auditoría general.</html>");
        mensaje.setFont(new Font("Verdana", Font.PLAIN, 14));
        bienvenida.add(mensaje, BorderLayout.CENTER);

        cuerpo.add(bienvenida, BorderLayout.CENTER);
        inicio.add(cuerpo, BorderLayout.CENTER);
        return inicio;
    }

    private JPanel crearTarjetaKPI(String titulo, JLabel lblValor, Color colorBorde) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBorde, 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitulo = new JLabel(titulo, JLabel.CENTER);
        lblTitulo.setFont(new Font("Verdana", Font.BOLD, 13));
        lblTitulo.setForeground(colorBorde);

        lblValor.setFont(new Font("Verdana", Font.BOLD, 22));
        lblValor.setForeground(AZUL_PRINCIPAL);

        tarjeta.add(lblTitulo, BorderLayout.NORTH);
        tarjeta.add(Box.createVerticalStrut(10), BorderLayout.CENTER);
        tarjeta.add(lblValor, BorderLayout.SOUTH);

        return tarjeta;
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

    private JPanel crearPanelEmpleados() {
        JPanel panel = crearPanelBase("Gestión de Empleados", "Control y alta de personal en la institución");
        configurarTabla(tablaEmpleados);
        JScrollPane scrollTabla = new JScrollPane(tablaEmpleados);
        scrollTabla.getViewport().setBackground(Color.WHITE);
        JPanel lista = new JPanel(new BorderLayout(0, 8));
        lista.setOpaque(false);
        lista.add(PanelRolUI.crearBarraFiltroOrden(tablaEmpleados), BorderLayout.NORTH);
        lista.add(scrollTabla, BorderLayout.CENTER);
        panel.add(lista, BorderLayout.CENTER);

        JButton nuevo = crearBotonEstilizado("Crear empleado", NARANJA);
        JButton actualizar = crearBotonEstilizado("Actualizar lista", AZUL_SECUNDARIO);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        acciones.setOpaque(false);
        acciones.add(nuevo);
        acciones.add(actualizar);
        panel.add(acciones, BorderLayout.SOUTH);

        nuevo.addActionListener(e -> crearEmpleado());
        actualizar.addActionListener(e -> cargarEmpleados());
        return panel;
    }

    private JPanel crearPanelEventos() {
        JPanel panel = crearPanelBase("Listado de Eventos", "Monitoreo general de eventos agendados");
        configurarTabla(tablaEventos);
        tablaEventos.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int filaVista = tablaEventos.rowAtPoint(e.getPoint());
                if (filaVista < 0) return;
                tablaEventos.setRowSelectionInterval(filaVista, filaVista);
                int filaModelo = tablaEventos.convertRowIndexToModel(filaVista);
                Object id = modeloEventos.getValueAt(filaModelo, 0);
                if (id instanceof Number) {
                    new SimulacionFacturacionDialog(GerenciaFrame.this, ((Number) id).intValue()).setVisible(true);
                }
            }
        });
        panel.add(PanelRolUI.crearListaFiltrable(tablaEventos), BorderLayout.CENTER);

        JButton actualizarEventos = crearBotonEstilizado("Actualizar eventos", AZUL_SECUNDARIO);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        acciones.setOpaque(false);
        acciones.add(actualizarEventos);
        panel.add(acciones, BorderLayout.SOUTH);

        actualizarEventos.addActionListener(e -> cargarEventos());
        return panel;
    }

    private JPanel crearPanelIngresos() {
        JPanel panel = crearPanelBase("Reporte de Ingresos", "Control de pagos y recaudaciones registradas");
        configurarTabla(tablaIngresos);
        panel.add(PanelRolUI.crearListaFiltrable(tablaIngresos), BorderLayout.CENTER);

        JButton actualizarIngresos = crearBotonEstilizado("Actualizar ingresos", AZUL_SECUNDARIO);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        acciones.setOpaque(false);
        acciones.add(actualizarIngresos);
        panel.add(acciones, BorderLayout.SOUTH);

        actualizarIngresos.addActionListener(e -> cargarIngresos());
        return panel;
    }

    private JPanel crearPanelEspacios() {
        JPanel panel = crearPanelBase("Ocupación de Espacios", "Disponibilidad y características de salones institucionales");
        configurarTabla(tablaEspacios);
        panel.add(PanelRolUI.crearListaFiltrable(tablaEspacios), BorderLayout.CENTER);

        JButton actualizarEspacios = crearBotonEstilizado("Actualizar espacios", AZUL_SECUNDARIO);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        acciones.setOpaque(false);
        acciones.add(actualizarEspacios);
        panel.add(acciones, BorderLayout.SOUTH);

        actualizarEspacios.addActionListener(e -> cargarEspacios());
        return panel;
    }

    private JPanel crearPanelDesempeno() {
        JPanel panel = crearPanelBase("Desempeño de Personal", "Relación de empleados asignados por evento");
        configurarTabla(tablaDesempeno);
        panel.add(PanelRolUI.crearListaFiltrable(tablaDesempeno), BorderLayout.CENTER);

        JButton actualizarDesempeno = crearBotonEstilizado("Actualizar desempeño", AZUL_SECUNDARIO);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        acciones.setOpaque(false);
        acciones.add(actualizarDesempeno);
        panel.add(acciones, BorderLayout.SOUTH);

        actualizarDesempeno.addActionListener(e -> cargarDesempeno());
        return panel;
    }

    private JPanel crearPanelAuditoria() {
        JPanel panel = crearPanelBase("Auditoría del Sistema", "Historial de modificaciones y registros de actividad");
        configurarTabla(tablaAuditoria);
        tablaAuditoria.setAutoCreateRowSorter(true);
        tablaAuditoria.getColumnModel().getColumn(7).setMinWidth(0);
        tablaAuditoria.getColumnModel().getColumn(7).setMaxWidth(0);
        tablaAuditoria.getColumnModel().getColumn(7).setPreferredWidth(0);
        tablaAuditoria.getColumnModel().getColumn(8).setMinWidth(0);
        tablaAuditoria.getColumnModel().getColumn(8).setMaxWidth(0);
        tablaAuditoria.getColumnModel().getColumn(8).setPreferredWidth(0);
        panel.add(PanelRolUI.crearListaFiltrable(tablaAuditoria), BorderLayout.CENTER);
        tablaAuditoria.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                int filaVista = tablaAuditoria.rowAtPoint(e.getPoint());
                if (filaVista >= 0) mostrarVentanaDetalleAuditoria(filaVista);
            }
        });

        JButton verDetalle = crearBotonEstilizado("Ver detalle seleccionado", NARANJA);
        JButton actualizarAuditoria = crearBotonEstilizado("Actualizar auditoría", AZUL_SECUNDARIO);
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        acciones.setOpaque(false);
        acciones.add(verDetalle);
        acciones.add(actualizarAuditoria);
        panel.add(acciones, BorderLayout.SOUTH);

        verDetalle.addActionListener(e -> {
            int filaVista = tablaAuditoria.getSelectedRow();
            if (filaVista < 0) {
                JOptionPane.showMessageDialog(this, "Selecciona un registro de auditoría primero.");
                return;
            }
            mostrarVentanaDetalleAuditoria(filaVista);
        });
        actualizarAuditoria.addActionListener(e -> cargarAuditoria());
        return panel;
    }

    private void mostrarVentanaDetalleAuditoria(int filaVista) {
        int fila = tablaAuditoria.convertRowIndexToModel(filaVista);
        String tipo = String.valueOf(modeloAuditoria.getValueAt(fila, 1));
        String empleado = String.valueOf(modeloAuditoria.getValueAt(fila, 2));
        String rol = String.valueOf(modeloAuditoria.getValueAt(fila, 3));
        String pantalla = String.valueOf(modeloAuditoria.getValueAt(fila, 4));
        String accion = String.valueOf(modeloAuditoria.getValueAt(fila, 5));
        Object idEvento = modeloAuditoria.getValueAt(fila, 7);
        Object idEmpleado = modeloAuditoria.getValueAt(fila, 8);
        String fechaHora = String.valueOf(modeloAuditoria.getValueAt(fila, 6));

        JDialog ventana = new JDialog(this, "Detalle de auditoría", true);
        ventana.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        ventana.setSize(620, 470);
        ventana.setLocationRelativeTo(this);
        JPanel contenidoDetalle = new JPanel(new BorderLayout(12, 12));
        contenidoDetalle.setBorder(BorderFactory.createEmptyBorder(18, 20, 16, 20));
        JLabel encabezado = new JLabel("Registro de auditoría #" + modeloAuditoria.getValueAt(fila, 0));
        encabezado.setFont(new Font("Verdana", Font.BOLD, 19));
        encabezado.setForeground(AZUL_PRINCIPAL);
        contenidoDetalle.add(encabezado, BorderLayout.NORTH);

        JPanel campos = new JPanel(new java.awt.GridBagLayout());
        int filaCampo = 0;
        agregarCampoAuditoria(campos, filaCampo++, "Tipo de registro:", tipo);
        agregarCampoAuditoria(campos, filaCampo++, "Empleado:", empleado);
        agregarCampoAuditoria(campos, filaCampo++, "Rol:", rol);
        if (idEmpleado != null) agregarCampoAuditoria(campos, filaCampo++, "ID de empleado:", String.valueOf(idEmpleado));
        agregarCampoAuditoria(campos, filaCampo++, "Pantalla o ventana:", pantalla);
        agregarCampoAuditoria(campos, filaCampo++, "Fecha y hora:", fechaHora);
        if (idEvento != null) agregarCampoAuditoria(campos, filaCampo++, "Evento relacionado:", "#" + idEvento);

        JLabel etiquetaAccion = new JLabel("Acción o cambio registrado:");
        etiquetaAccion.setFont(new Font("Verdana", Font.BOLD, 13));
        JTextArea detalleAccion = new JTextArea(accion, 4, 35);
        detalleAccion.setEditable(false);
        detalleAccion.setLineWrap(true);
        detalleAccion.setWrapStyleWord(true);
        detalleAccion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 200, 210)),
                BorderFactory.createEmptyBorder(8, 9, 8, 9)));
        JPanel seccionAccion = new JPanel(new BorderLayout(4, 6));
        seccionAccion.add(etiquetaAccion, BorderLayout.NORTH);
        seccionAccion.add(new JScrollPane(detalleAccion), BorderLayout.CENTER);
        JPanel centroDetalle = new JPanel(new BorderLayout(0, 12));
        centroDetalle.add(campos, BorderLayout.NORTH);
        centroDetalle.add(seccionAccion, BorderLayout.CENTER);
        contenidoDetalle.add(centroDetalle, BorderLayout.CENTER);

        String explicacion = tipo.startsWith("Clic")
                ? "Este registro confirma que se pulsó el botón indicado en esa pantalla."
                : "Este registro describe un cambio de evento guardado en el historial.";
        JLabel nota = new JLabel("<html><body style='width:540px'>" + explicacion + "</body></html>");
        nota.setForeground(new Color(95, 105, 115));
        JButton cerrar = crearBotonEstilizado("Cerrar", AZUL_SECUNDARIO);
        JPanel pie = new JPanel(new BorderLayout(8, 8));
        pie.add(nota, BorderLayout.CENTER);
        JPanel botonCerrar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        botonCerrar.add(cerrar);
        pie.add(botonCerrar, BorderLayout.SOUTH);
        contenidoDetalle.add(pie, BorderLayout.SOUTH);
        cerrar.addActionListener(e -> ventana.dispose());
        ventana.setContentPane(contenidoDetalle);
        ventana.setVisible(true);
    }

    private void agregarCampoAuditoria(JPanel panel, int fila, String etiqueta, String valor) {
        java.awt.GridBagConstraints c = new java.awt.GridBagConstraints();
        c.gridx = 0;
        c.gridy = fila;
        c.anchor = java.awt.GridBagConstraints.LINE_START;
        c.insets = new java.awt.Insets(4, 4, 4, 14);
        JLabel titulo = new JLabel(etiqueta);
        titulo.setFont(new Font("Verdana", Font.BOLD, 13));
        panel.add(titulo, c);
        c.gridx = 1;
        c.weightx = 1;
        c.fill = java.awt.GridBagConstraints.HORIZONTAL;
        JLabel contenido = new JLabel(valor == null || "null".equals(valor) ? "—" : valor);
        contenido.setFont(new Font("Verdana", Font.PLAIN, 13));
        panel.add(contenido, c);
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

    private void cargarEmpleados() {
        try {
            modeloEmpleados.setRowCount(0);
            List<Empleado> empleados = empleadoDAO.listarTodos();
            for (Empleado e : empleados) {
                modeloEmpleados.addRow(new Object[] {
                    e.getIdEmpleado(), e.getNombreRol(), e.getNombre(),
                    e.getApellido(), e.getCuil(), e.getEmail(), e.getTelefono()
                });
            }
        } catch (SQLException ex) {
            mostrarError("No se pudieron cargar los empleados. " + ex.getMessage());
        }
    }

    private void cargarEventos() {
        try {
            modeloEventos.setRowCount(0);
            List<Evento> eventos = eventoDAO.listarTodos();
            for (Evento ev : eventos) {
                modeloEventos.addRow(new Object[] {
                    ev.getIdEvento(), ev.getIdCliente(), ev.getIdEspacio(), 
                    ev.getFechaEvento(), ev.getHoraInicio(), ev.getHoraFin(), ev.getCantidadInvitados()
                });
            }
        } catch (SQLException ex) {
            mostrarError("No se pudieron cargar los eventos. " + ex.getMessage());
        }
    }

    private void cargarIngresos() {
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT p.id_pago, p.id_evento, p.fecha_pago, p.monto, "
                     + "p.medio_pago, e.nombre_estado, p.observaciones FROM pagos p "
                     + "JOIN eventos ev ON ev.id_evento = p.id_evento "
                     + "JOIN estado_del_evento e ON e.id_estado = ev.id_estado ORDER BY p.fecha_pago DESC");
             ResultSet rs = ps.executeQuery()) {
            modeloIngresos.setRowCount(0);
            while (rs.next()) {
                modeloIngresos.addRow(new Object[] {
                    rs.getInt("id_pago"),
                    rs.getInt("id_evento"),
                    rs.getTimestamp("fecha_pago"),
                    rs.getBigDecimal("monto"),
                    rs.getString("medio_pago"),
                    rs.getString("nombre_estado"),
                    rs.getString("observaciones")
                });
            }
        } catch (SQLException ex) {
            mostrarError("No se pudieron cargar los ingresos. " + ex.getMessage());
        }
    }

    private void cargarEspacios() {
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT id_espacio, nombre_espacio, capacidad_maxima, precio_base_alquiler FROM espacios");
             ResultSet rs = ps.executeQuery()) {
            modeloEspacios.setRowCount(0);
            while (rs.next()) {
                modeloEspacios.addRow(new Object[] {
                    rs.getInt("id_espacio"),
                    rs.getString("nombre_espacio"),
                    rs.getInt("capacidad_maxima"),
                    rs.getBigDecimal("precio_base_alquiler")
                });
            }
        } catch (SQLException ex) {
            mostrarError("No se pudieron cargar los espacios. " + ex.getMessage());
        }
    }

    private void cargarDesempeno() {
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT ee.id_evento, CONCAT(e.nombre, ' ', e.apellido) AS empleado, ee.rol_en_evento FROM evento_empleados ee JOIN empleados e ON ee.id_empleado = e.id_empleado");
             ResultSet rs = ps.executeQuery()) {
            modeloDesempeno.setRowCount(0);
            while (rs.next()) {
                modeloDesempeno.addRow(new Object[] {
                    rs.getInt("id_evento"),
                    rs.getString("empleado"),
                    rs.getString("rol_en_evento")
                });
            }
        } catch (SQLException ex) {
            mostrarError("No se pudo cargar el desempeño. " + ex.getMessage());
        }
    }

    private void cargarAuditoria() {
        String sql = "SELECT id, tipo, empleado, rol, pantalla, accion, fecha, id_evento, id_empleado FROM ("
                + "SELECT id_modificacion AS id, 'Cambio de evento' AS tipo, 'Sistema' AS empleado, "
                + "'—' AS rol, 'Eventos' AS pantalla, descripcion_cambio AS accion, fecha_cambio AS fecha, "
                + "id_evento, NULL AS id_empleado "
                + "FROM historial_modificaciones UNION ALL "
                + "SELECT id_accion, 'Clic en botón', empleado_nombre, rol, pantalla, accion, fecha_hora, "
                + "NULL, id_empleado "
                + "FROM registro_acciones) auditoria ORDER BY fecha DESC LIMIT 500";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            modeloAuditoria.setRowCount(0);
            while (rs.next()) {
                modeloAuditoria.addRow(new Object[] {
                    rs.getInt("id"), rs.getString("tipo"), rs.getString("empleado"), rs.getString("rol"),
                    rs.getString("pantalla"), rs.getString("accion"), rs.getTimestamp("fecha"),
                    rs.getObject("id_evento"), rs.getObject("id_empleado")
                });
            }
        } catch (SQLException ex) {
            mostrarError("No se pudo cargar la auditoría. " + ex.getMessage());
        }
    }

    private void cargarDatosGenerales() {
        try (Connection con = Conexion.getConnection()) {
            try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM clientes");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) lblTotalClientes.setText(rs.getInt(1) + " Clientes");
            }
            try (PreparedStatement ps = con.prepareStatement("SELECT COUNT(*) FROM eventos");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) lblTotalEventos.setText(rs.getInt(1) + " Eventos");
            }
            try (PreparedStatement ps = con.prepareStatement("SELECT SUM(monto) FROM pagos");
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double total = rs.getDouble(1);
                    lblTotalRecaudado.setText("$ " + String.format("%.2f", total));
                }
            }
        } catch (SQLException ex) {
            lblTotalClientes.setText("Error");
            lblTotalEventos.setText("Error");
            lblTotalRecaudado.setText("Error");
        }
    }

    private void crearEmpleado() {
        JTextField nombre = new JTextField();
        JTextField apellido = new JTextField();
        JTextField cuil = new JTextField();
        JTextField email = new JTextField();
        JTextField telefono = new JTextField();
        JPasswordField contrasena = new JPasswordField();
        JComboBox<Rol> rol = new JComboBox<>();
        
        try {
            for (Rol opcion : rolDAO.listarTodos()) {
                rol.addItem(opcion);
            }
        } catch (SQLException ex) {
            mostrarError("No se pudieron cargar los roles. " + ex.getMessage());
            return;
        }
        
        if (rol.getItemCount() == 0) {
            mostrarError("No hay roles cargados.");
            return;
        }

        JPanel formulario = new JPanel(new GridLayout(0, 2, 8, 8));
        formulario.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        formulario.add(new JLabel("Rol:")); formulario.add(rol);
        formulario.add(new JLabel("Nombre:")); formulario.add(nombre);
        formulario.add(new JLabel("Apellido:")); formulario.add(apellido);
        formulario.add(new JLabel("CUIL:")); formulario.add(cuil);
        formulario.add(new JLabel("Email:")); formulario.add(email);
        formulario.add(new JLabel("Teléfono:")); formulario.add(telefono);
        formulario.add(new JLabel("Contraseña:")); formulario.add(contrasena);

        JLabel error = new JLabel(" ");
        error.setForeground(new Color(150, 30, 30));
        error.setBorder(BorderFactory.createEmptyBorder(0, 12, 6, 12));
        JDialog dialogo = new JDialog(this, "Crear empleado", true);
        dialogo.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        JPanel contenidoDialogo = new JPanel(new BorderLayout(8, 8));
        contenidoDialogo.add(formulario, BorderLayout.CENTER);
        contenidoDialogo.add(error, BorderLayout.NORTH);

        JButton guardar = crearBotonEstilizado("Guardar empleado", NARANJA);
        JButton cancelar = crearBotonEstilizado("Cancelar", AZUL_SECUNDARIO);
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        botones.add(cancelar);
        botones.add(guardar);
        contenidoDialogo.add(botones, BorderLayout.SOUTH);
        dialogo.setContentPane(contenidoDialogo);
        dialogo.setSize(520, 390);
        dialogo.setMinimumSize(new Dimension(480, 360));
        dialogo.setLocationRelativeTo(this);

        cancelar.addActionListener(e -> dialogo.dispose());
        guardar.addActionListener(e -> {
            Rol rolElegido = (Rol) rol.getSelectedItem();
            String nombreValor = nombre.getText().trim();
            String apellidoValor = apellido.getText().trim();
            String cuilValor = cuil.getText().trim();
            String emailValor = email.getText().trim();
            char[] clave = contrasena.getPassword();
            try {
                if (rolElegido == null || nombreValor.isEmpty() || apellidoValor.isEmpty()
                        || cuilValor.isEmpty() || emailValor.isEmpty()) {
                    error.setText("Completa rol, nombre, apellido, CUIL y email. Los datos siguen cargados.");
                    return;
                }
                String cuilNumerico = cuilValor.replaceAll("[^0-9]", "");
                if (cuilNumerico.length() != 11) {
                    error.setText("El CUIL debe contener 11 dígitos. Corregilo sin volver a cargar el formulario.");
                    cuil.requestFocusInWindow();
                    return;
                }
                if (!emailValor.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                    error.setText("Escribe un email válido. Los demás datos se conservaron.");
                    email.requestFocusInWindow();
                    return;
                }
                if (clave.length < 8) {
                    error.setText("La contraseña debe tener al menos 8 caracteres. Los demás datos se conservaron.");
                    contrasena.requestFocusInWindow();
                    return;
                }
                Empleado empleado = new Empleado(0, rolElegido.getIdRol(), nombreValor,
                        apellidoValor, cuilValor, emailValor, telefono.getText().trim(), rolElegido.getNombre());
                empleadoDAO.insertar(empleado, clave);
                cargarEmpleados();
                dialogo.dispose();
                JOptionPane.showMessageDialog(this, "Empleado creado correctamente.");
            } catch (SQLException ex) {
                error.setText("No se pudo guardar: " + ex.getMessage() + " · Corrige el dato y vuelve a intentar.");
            } finally {
                java.util.Arrays.fill(clave, '\0');
            }
        });
        dialogo.getRootPane().setDefaultButton(guardar);
        dialogo.setVisible(true);
    }

    private void cerrarSesion() {
        PanelRolUI.confirmarCerrarSesion(this);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Gerencia", JOptionPane.ERROR_MESSAGE);
    }
}

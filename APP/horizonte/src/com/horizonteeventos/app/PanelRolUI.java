package com.horizonteeventos.app;

import com.horizonteeventos.modelo.Empleado;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.SortOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.event.TableModelEvent;

/** Piezas visuales compartidas por los paneles internos con el estilo institucional. */
public final class PanelRolUI {
    public static final Color AZUL = new Color(18, 51, 81);
    public static final Color AZUL_CLARO = new Color(28, 61, 88);
    public static final Color NARANJA = new Color(150, 59, 12);
    public static final Color FONDO = new Color(245, 246, 249);
    private static final Font NORMAL = new Font("Verdana", Font.PLAIN, 14);
    private static final Font NEGRITA = new Font("Verdana", Font.BOLD, 14);

    private PanelRolUI() { }

    public static JPanel crearEncabezado(String modulo, Empleado usuario, Runnable accionCerrar) {
        JPanel header = new JPanel(new BorderLayout(16, 0));
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(228, 233, 239)),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)));
        JPanel marca = new JPanel(new BorderLayout(10, 0));
        marca.setOpaque(false);
        marca.add(LogoUI.crearLogo(48, 48), BorderLayout.WEST);
        JPanel textosMarca = new JPanel();
        textosMarca.setOpaque(false);
        textosMarca.setLayout(new BoxLayout(textosMarca, BoxLayout.Y_AXIS));
        JLabel titulo = new JLabel("HORIZONTE EVENTOS");
        titulo.setFont(new Font("Verdana", Font.BOLD, 18));
        titulo.setForeground(AZUL);
        JLabel subtitulo = new JLabel("Sistema de Gestión · Panel de " + modulo);
        subtitulo.setFont(new Font("Verdana", Font.PLAIN, 12));
        subtitulo.setForeground(new Color(70, 82, 94));
        textosMarca.add(titulo);
        textosMarca.add(Box.createVerticalStrut(3));
        textosMarca.add(subtitulo);
        marca.add(textosMarca, BorderLayout.CENTER);
        header.add(marca, BorderLayout.WEST);

        JPanel sesion = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 3));
        sesion.setOpaque(false);
        JLabel usuarioLabel = new JLabel(modulo + ": " + usuario.getNombre() + " " + usuario.getApellido());
        usuarioLabel.setFont(NEGRITA);
        usuarioLabel.setForeground(AZUL);
        JButton cerrar = boton("Cerrar sesión", new Color(155, 35, 35));
        cerrar.addActionListener(e -> accionCerrar.run());
        sesion.add(usuarioLabel);
        sesion.add(cerrar);
        header.add(sesion, BorderLayout.EAST);
        return header;
    }

    public static JPanel crearNavegacion(String titulo, String[] opciones, Consumer<String> alElegir) {
        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(AZUL);
        menu.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        menu.setPreferredSize(new Dimension(220, 0));
        JLabel encabezado = new JLabel("  " + titulo.toUpperCase());
        encabezado.setFont(new Font("Verdana", Font.BOLD, 11));
        encabezado.setForeground(new Color(220, 230, 238));
        encabezado.setAlignmentX(Component.LEFT_ALIGNMENT);
        menu.add(encabezado);
        menu.add(Box.createVerticalStrut(12));
        for (String opcion : opciones) {
            JButton boton = new JButton(opcion);
            boton.setFont(new Font("Verdana", Font.BOLD, 12));
            boton.setForeground(Color.WHITE);
            boton.setBackground(AZUL);
            boton.setFocusPainted(false);
            boton.setHorizontalAlignment(JButton.LEFT);
            boton.setAlignmentX(Component.LEFT_ALIGNMENT);
            boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            boton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 10));
            boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            boton.addActionListener(e -> alElegir.accept(opcion));
            boton.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseEntered(java.awt.event.MouseEvent e) { boton.setBackground(AZUL_CLARO); }
                @Override public void mouseExited(java.awt.event.MouseEvent e) { boton.setBackground(AZUL); }
            });
            menu.add(boton);
            menu.add(Box.createVerticalStrut(2));
        }
        menu.add(Box.createVerticalGlue());
        return menu;
    }

    public static JPanel crearModulo(String titulo, String descripcion) {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel cabecera = new JPanel();
        cabecera.setOpaque(false);
        cabecera.setLayout(new BoxLayout(cabecera, BoxLayout.Y_AXIS));
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Verdana", Font.BOLD, 23));
        lblTitulo.setForeground(AZUL);
        JLabel lblDescripcion = new JLabel(descripcion);
        lblDescripcion.setFont(NORMAL);
        lblDescripcion.setForeground(new Color(65, 78, 91));
        cabecera.add(lblTitulo);
        cabecera.add(Box.createVerticalStrut(4));
        cabecera.add(lblDescripcion);
        panel.add(cabecera, BorderLayout.NORTH);
        return panel;
    }

    public static JPanel crearTarjeta(String titulo, String valor, Color acento) {
        JPanel panel = new JPanel(new BorderLayout(4, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(acento, 2),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));
        JLabel nombre = new JLabel(titulo, JLabel.CENTER);
        nombre.setFont(NEGRITA);
        nombre.setForeground(acento);
        JLabel dato = new JLabel(valor, JLabel.CENTER);
        dato.setFont(new Font("Verdana", Font.BOLD, 24));
        dato.setForeground(AZUL);
        panel.add(nombre, BorderLayout.NORTH);
        panel.add(dato, BorderLayout.CENTER);
        return panel;
    }

    public static JPanel crearPanelBlanco() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 227, 234)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        return panel;
    }

    public static JButton boton(String texto, Color fondo) {
        JButton boton = new JButton(texto);
        boton.setFont(NEGRITA);
        boton.setForeground(Color.WHITE);
        boton.setBackground(fondo);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(9, 15, 9, 15));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return boton;
    }

    public static void estilizarTabla(JTable tabla) {
        tabla.setFont(NORMAL);
        tabla.setRowHeight(28);
        tabla.setFillsViewportHeight(true);
        tabla.setSelectionBackground(AZUL_CLARO);
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setGridColor(new Color(210, 220, 230));
        JTableHeader header = tabla.getTableHeader();
        header.setFont(NEGRITA);
        header.setBackground(AZUL);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        DefaultTableCellRenderer filasAlternadas = new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override public Component getTableCellRendererComponent(JTable origen, Object valor,
                    boolean seleccionado, boolean foco, int fila, int columna) {
                Component componente = super.getTableCellRendererComponent(origen, valor,
                        seleccionado, foco, fila, columna);
                if (!seleccionado) {
                    componente.setBackground(fila % 2 == 0
                            ? new Color(232, 234, 236) : new Color(245, 239, 220));
                    componente.setForeground(Color.BLACK);
                }
                return componente;
            }
        };
        tabla.setDefaultRenderer(Object.class, filasAlternadas);
        if (tabla.getRowSorter() == null) {
            TableRowSorter<TableModel> ordenador = new TableRowSorter<>(tabla.getModel());
            tabla.setRowSorter(ordenador);
        }
    }

    /** Barra de búsqueda y ordenamiento para las tablas de todos los módulos. */
    public static JPanel crearBarraFiltroOrden(JTable tabla) {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        barra.setOpaque(false);
        JLabel buscarLabel = new JLabel("Buscar en la lista:");
        buscarLabel.setFont(NORMAL);
        JTextField buscar = new JTextField(18);
        buscar.setFont(NORMAL);
        JLabel ordenarLabel = new JLabel("Ordenar por:");
        ordenarLabel.setFont(NORMAL);
        JComboBox<String> columnas = new JComboBox<>();
        columnas.addItem("Sin ordenar");
        cargarColumnasOrden(tabla, columnas);
        JLabel sentidoLabel = new JLabel("Sentido:");
        sentidoLabel.setFont(NORMAL);
        JComboBox<String> sentido = new JComboBox<>(new String[] {"Ascendente", "Descendente"});
        sentidoLabel.setVisible(false);
        sentido.setVisible(false);
        if (tabla.getRowSorter() == null) {
            tabla.setRowSorter(new TableRowSorter<TableModel>(tabla.getModel()));
        }
        TableRowSorter<?> ordenador = (TableRowSorter<?>) tabla.getRowSorter();
        Runnable actualizarVisibilidadSentido = () -> {
            boolean hayColumna = columnas.getSelectedIndex() > 0;
            sentidoLabel.setVisible(hayColumna);
            sentido.setVisible(hayColumna);
            barra.revalidate();
            barra.repaint();
        };
        Runnable aplicarOrden = () -> {
            int columna = columnas.getSelectedIndex() - 1;
            if (columna < 0) {
                ordenador.setSortKeys(new ArrayList<javax.swing.RowSorter.SortKey>());
                actualizarVisibilidadSentido.run();
                return;
            }
            actualizarVisibilidadSentido.run();
            List<javax.swing.RowSorter.SortKey> claves = new ArrayList<>();
            claves.add(new javax.swing.RowSorter.SortKey(columna,
                    sentido.getSelectedIndex() == 0 ? SortOrder.ASCENDING : SortOrder.DESCENDING));
            ordenador.setSortKeys(claves);
        };
        columnas.addActionListener(e -> aplicarOrden.run());
        sentido.addActionListener(e -> aplicarOrden.run());
        tabla.getModel().addTableModelListener(e -> {
            if (e.getFirstRow() != TableModelEvent.HEADER_ROW) return;
            String columnaElegida = (String) columnas.getSelectedItem();
            columnas.removeAllItems();
            columnas.addItem("Sin ordenar");
            cargarColumnasOrden(tabla, columnas);
            if (columnaElegida != null) columnas.setSelectedItem(columnaElegida);
            if (columnas.getSelectedIndex() < 0) columnas.setSelectedIndex(0);
            aplicarOrden.run();
        });
        buscar.getDocument().addDocumentListener(new DocumentListener() {
            private void aplicarFiltro() {
                String texto = buscar.getText().trim();
                ordenador.setRowFilter(texto.isEmpty() ? null
                        : RowFilter.regexFilter("(?i)" + Pattern.quote(texto)));
            }
            @Override public void insertUpdate(DocumentEvent e) { aplicarFiltro(); }
            @Override public void removeUpdate(DocumentEvent e) { aplicarFiltro(); }
            @Override public void changedUpdate(DocumentEvent e) { aplicarFiltro(); }
        });
        barra.add(buscarLabel);
        barra.add(buscar);
        barra.add(ordenarLabel);
        barra.add(columnas);
        barra.add(sentidoLabel);
        barra.add(sentido);
        return barra;
    }

    private static void cargarColumnasOrden(JTable tabla, JComboBox<String> columnas) {
        for (int i = 0; i < tabla.getModel().getColumnCount(); i++) {
            columnas.addItem(tabla.getModel().getColumnName(i));
        }
    }

    /** Envuelve una tabla con búsqueda, ordenamiento y desplazamiento compartidos. */
    public static JPanel crearListaFiltrable(JTable tabla) {
        JPanel lista = new JPanel(new BorderLayout(0, 8));
        lista.setOpaque(false);
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.getViewport().setBackground(Color.WHITE);
        lista.add(crearBarraFiltroOrden(tabla), BorderLayout.NORTH);
        lista.add(scroll, BorderLayout.CENTER);
        return lista;
    }

    public static void confirmarCerrarSesion(javax.swing.JFrame ventana) {
        int respuesta = javax.swing.JOptionPane.showConfirmDialog(ventana,
                "¿Deseas cerrar la sesión actual?", "Confirmar cierre de sesión",
                javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);
        if (respuesta != javax.swing.JOptionPane.YES_OPTION) return;
        RegistroAccionesUI.establecerUsuario(null);
        ventana.dispose();
        new LoginFrame();
    }
}

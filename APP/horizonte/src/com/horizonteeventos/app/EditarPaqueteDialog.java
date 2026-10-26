package com.horizonteeventos.app;

import com.horizonteeventos.dao.CatalogoEventoDAO;
import com.horizonteeventos.modelo.Paquete;
import com.horizonteeventos.modelo.Servicio;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/** Editor administrativo para ajustar plantillas de paquete y sus recursos incluidos. */
public class EditarPaqueteDialog extends JDialog {
    private static final long serialVersionUID = 1L;
    private final CatalogoEventoDAO catalogoDAO;
    private final Paquete paquete;
    private final JTextField nombre = new JTextField(24);
    private final JTextField precio = new JTextField(12);
    private final JTextField descripcion = new JTextField(34);
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[] {"ID", "Incluir", "Servicio / recurso", "Cantidad"}, 0) {
        private static final long serialVersionUID = 1L;
        @Override public boolean isCellEditable(int fila, int columna) {
            return columna == 1 || columna == 3;
        }
        @Override public Class<?> getColumnClass(int columna) {
            if (columna == 0 || columna == 3) return Integer.class;
            if (columna == 1) return Boolean.class;
            return String.class;
        }
    };
    private final JTable tabla = new JTable(modelo);
    private boolean guardado;

    public EditarPaqueteDialog(JFrame owner, CatalogoEventoDAO catalogoDAO, Paquete paquete) {
        super(owner, paquete.getId() == 0 ? "Crear paquete" : "Modificar paquete", true);
        this.catalogoDAO = catalogoDAO;
        this.paquete = paquete;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(850, 600);
        setMinimumSize(new java.awt.Dimension(760, 520));
        setLocationRelativeTo(owner);
        construirInterfaz();
        cargarServicios();
    }

    public boolean isGuardado() { return guardado; }

    private void construirInterfaz() {
        setLayout(new BorderLayout(10, 10));
        JPanel datos = new JPanel(new GridLayout(0, 2, 8, 8));
        datos.setBorder(BorderFactory.createEmptyBorder(16, 18, 8, 18));
        datos.add(new JLabel("Nombre del paquete:"));
        datos.add(nombre);
        datos.add(new JLabel("Descripción:"));
        datos.add(descripcion);
        datos.add(new JLabel("Precio base de lista:"));
        datos.add(precio);
        if (paquete.getId() != 0) {
            nombre.setText(paquete.getNombre());
            descripcion.setText(paquete.getDescripcion());
            precio.setText(paquete.getPrecioFijo().toPlainString());
        }
        add(datos, BorderLayout.NORTH);

        tabla.setRowHeight(25);
        tabla.getColumnModel().getColumn(0).setMinWidth(0);
        tabla.getColumnModel().getColumn(0).setMaxWidth(0);
        tabla.getColumnModel().getColumn(0).setPreferredWidth(0);
        JPanel componentes = new JPanel(new BorderLayout(6, 6));
        componentes.setBorder(BorderFactory.createTitledBorder(
                "Recursos y servicios que incluye esta plantilla"));
        componentes.add(new JLabel("Marca los componentes incluidos y define la cantidad predeterminada."),
                BorderLayout.NORTH);
        componentes.add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(componentes, BorderLayout.CENTER);

        JPanel botones = new JPanel();
        JButton guardar = new JButton("Guardar paquete");
        JButton cancelar = new JButton("Cancelar");
        botones.add(guardar);
        botones.add(cancelar);
        add(botones, BorderLayout.SOUTH);
        guardar.addActionListener(e -> guardarPaquete());
        cancelar.addActionListener(e -> dispose());
    }

    private void cargarServicios() {
        try {
            Map<Integer, Integer> seleccionados = paquete.getId() == 0
                    ? new LinkedHashMap<Integer, Integer>()
                    : catalogoDAO.listarComponentesPaquete(paquete.getId());
            List<Servicio> servicios = catalogoDAO.listarServicios();
            for (Servicio servicio : servicios) {
                Integer cantidad = seleccionados.get(servicio.getId());
                modelo.addRow(new Object[] {servicio.getId(), cantidad != null,
                        servicio.getNombre(), cantidad == null ? 1 : cantidad});
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudieron cargar los recursos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarPaquete() {
        if (tabla.isEditing()) tabla.getCellEditor().stopCellEditing();
        if (nombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe el nombre del paquete.");
            return;
        }
        try {
            BigDecimal monto = new BigDecimal(precio.getText().trim().replace(',', '.'));
            if (monto.signum() < 0) throw new NumberFormatException();
            Map<Integer, Integer> componentes = new LinkedHashMap<>();
            for (int fila = 0; fila < modelo.getRowCount(); fila++) {
                if (!Boolean.TRUE.equals(modelo.getValueAt(fila, 1))) continue;
                int idServicio = (Integer) modelo.getValueAt(fila, 0);
                int cantidad = Integer.parseInt(String.valueOf(modelo.getValueAt(fila, 3)));
                if (cantidad < 1) {
                    JOptionPane.showMessageDialog(this, "La cantidad de cada componente debe ser mayor que cero.");
                    return;
                }
                componentes.put(idServicio, cantidad);
            }
            paquete.setNombre(nombre.getText().trim());
            paquete.setDescripcion(descripcion.getText().trim());
            paquete.setPrecioFijo(monto);
            catalogoDAO.guardarPaquete(paquete, componentes);
            guardado = true;
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Escribe un precio válido, por ejemplo 900000 o 900000,50.");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el paquete: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

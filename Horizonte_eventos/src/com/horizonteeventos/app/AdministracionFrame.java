package com.horizonteeventos.app;

import com.horizonteeventos.dao.ClienteDAO;
import com.horizonteeventos.dao.impl.ClienteDAOImpl;
import com.horizonteeventos.modelo.Cliente;
import com.horizonteeventos.modelo.Empleado;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/** Espacio de trabajo de Administracion. */
public class AdministracionFrame extends JFrame {
    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[] {"ID", "Nombre", "Apellido", "DNI/CUIT", "Email", "Telefono"}, 0) {
        private static final long serialVersionUID = 1L;
        @Override public boolean isCellEditable(int fila, int columna) { return false; }
    };
    private final JTable tabla = new JTable(modelo);

    public AdministracionFrame(Empleado empleado) {
        super("Horizonte Eventos - Administracion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        add(new JLabel("  Bienvenido/a, " + empleado.getNombre() + " " + empleado.getApellido()),
                BorderLayout.NORTH);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton nuevo = new JButton("Nuevo cliente");
        JButton editar = new JButton("Editar seleccionado");
        JButton eliminar = new JButton("Eliminar seleccionado");
        JButton actualizar = new JButton("Actualizar lista");
        JPanel acciones = new JPanel();
        acciones.add(nuevo); acciones.add(editar); acciones.add(eliminar); acciones.add(actualizar);
        JPanel clientesPanel = new JPanel(new BorderLayout(8, 8));
        clientesPanel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        clientesPanel.add(acciones, BorderLayout.SOUTH);

        JTabbedPane modulos = new JTabbedPane();
        modulos.addTab("Clientes", clientesPanel);
        agregarModuloPendiente(modulos, "Presupuestos");
        agregarModuloPendiente(modulos, "Eventos");
        agregarModuloPendiente(modulos, "Pagos");
        agregarModuloPendiente(modulos, "Comprobantes");
        agregarModuloPendiente(modulos, "Proveedores");
        agregarModuloPendiente(modulos, "Servicios");
        agregarModuloPendiente(modulos, "Espacios");
        agregarModuloPendiente(modulos, "Reportes");
        add(modulos, BorderLayout.CENTER);
        nuevo.addActionListener(e -> nuevoCliente());
        editar.addActionListener(e -> editarCliente());
        eliminar.addActionListener(e -> eliminarCliente());
        actualizar.addActionListener(e -> cargarClientes());
        setSize(1000, 520);
        setLocationRelativeTo(null);
        cargarClientes();
    }

    private void agregarModuloPendiente(JTabbedPane pestañas, String nombre) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Acceso asignado a Administracion. El modulo " + nombre
                + " todavia debe desarrollarse.", JLabel.CENTER), BorderLayout.CENTER);
        pestañas.addTab(nombre, panel);
    }

    private void cargarClientes() {
        try {
            modelo.setRowCount(0);
            List<Cliente> clientes = clienteDAO.listarTodos();
            for (Cliente c : clientes) {
                modelo.addRow(new Object[] {c.getIdCliente(), c.getNombre(), c.getApellido(),
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
            JOptionPane.showMessageDialog(this, "Cliente guardado.");
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
                JOptionPane.showMessageDialog(this, "Cliente actualizado.");
            }
        } catch (SQLException ex) { mostrarError(ex); }
    }

    private void eliminarCliente() {
        Cliente cliente = clienteSeleccionado();
        if (cliente == null) return;
        int confirmar = JOptionPane.showConfirmDialog(this,
                "Se eliminara a " + cliente.getNombre() + " " + cliente.getApellido() + ". ¿Continuar?",
                "Confirmar eliminacion", JOptionPane.YES_NO_OPTION);
        if (confirmar != JOptionPane.YES_OPTION) return;
        try {
            clienteDAO.eliminar(cliente.getIdCliente());
            cargarClientes();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar. Es posible que el cliente ya tenga eventos asociados.\n"
                            + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Cliente clienteSeleccionado() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona primero un cliente de la tabla.");
            return null;
        }
        return new Cliente((Integer) modelo.getValueAt(fila, 0),
                (String) modelo.getValueAt(fila, 1), (String) modelo.getValueAt(fila, 2),
                (String) modelo.getValueAt(fila, 3), (String) modelo.getValueAt(fila, 4),
                (String) modelo.getValueAt(fila, 5));
    }

    private Cliente pedirDatos(Cliente actual) {
        JTextField nombre = new JTextField(actual == null ? "" : actual.getNombre());
        JTextField apellido = new JTextField(actual == null ? "" : actual.getApellido());
        JTextField dni = new JTextField(actual == null ? "" : actual.getDniCuit());
        JTextField email = new JTextField(actual == null ? "" : actual.getEmail());
        JTextField telefono = new JTextField(actual == null ? "" : actual.getTelefono());
        JPanel panel = new JPanel(new GridLayout(0, 2, 6, 6));
        panel.add(new JLabel("Nombre:")); panel.add(nombre);
        panel.add(new JLabel("Apellido:")); panel.add(apellido);
        panel.add(new JLabel("DNI/CUIT:")); panel.add(dni);
        panel.add(new JLabel("Email:")); panel.add(email);
        panel.add(new JLabel("Telefono:")); panel.add(telefono);
        int opcion = JOptionPane.showConfirmDialog(this, panel,
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

    private void mostrarError(SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error de base de datos: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }
}

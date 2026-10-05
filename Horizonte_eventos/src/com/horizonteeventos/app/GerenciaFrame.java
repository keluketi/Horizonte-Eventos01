package com.horizonteeventos.app;

import com.horizonteeventos.dao.EmpleadoDAO;
import com.horizonteeventos.dao.RolDAO;
import com.horizonteeventos.dao.impl.EmpleadoDAOImpl;
import com.horizonteeventos.dao.impl.RolDAOImpl;
import com.horizonteeventos.modelo.Empleado;
import com.horizonteeventos.modelo.Rol;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/** Modulo de Gerencia para consultar empleados y dar de alta nuevos usuarios. */
public class GerenciaFrame extends JFrame {
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAOImpl();
    private final RolDAO rolDAO = new RolDAOImpl();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new Object[] {"ID", "Rol", "Nombre", "Apellido", "CUIL", "Email", "Telefono"}, 0) {
        private static final long serialVersionUID = 1L;
        @Override public boolean isCellEditable(int fila, int columna) { return false; }
    };
    private final JTable tabla = new JTable(modelo);

    public GerenciaFrame(Empleado usuario) {
        super("Horizonte Eventos - Gerencia");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));
        add(new JLabel("  Bienvenido/a, " + usuario.getNombre() + " " + usuario.getApellido()
                + " — Administración de empleados"), BorderLayout.NORTH);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JButton nuevo = new JButton("Crear empleado");
        JButton actualizar = new JButton("Actualizar lista");
        JPanel acciones = new JPanel();
        acciones.add(nuevo);
        acciones.add(actualizar);
        add(acciones, BorderLayout.SOUTH);
        nuevo.addActionListener(e -> crearEmpleado());
        actualizar.addActionListener(e -> cargarEmpleados());
        setSize(900, 450);
        setLocationRelativeTo(null);
        cargarEmpleados();
    }

    private void cargarEmpleados() {
        try {
            modelo.setRowCount(0);
            List<Empleado> empleados = empleadoDAO.listarTodos();
            for (Empleado e : empleados) {
                modelo.addRow(new Object[] {e.getIdEmpleado(), e.getNombreRol(), e.getNombre(),
                        e.getApellido(), e.getCuil(), e.getEmail(), e.getTelefono()});
            }
        } catch (SQLException ex) {
            mostrarError("No se pudieron cargar los empleados. " + ex.getMessage());
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
            for (Rol opcion : rolDAO.listarTodos()) rol.addItem(opcion);
        } catch (SQLException ex) {
            mostrarError("No se pudieron cargar los roles. " + ex.getMessage());
            return;
        }
        if (rol.getItemCount() == 0) {
            mostrarError("No hay roles cargados. Ejecuta primero database/01_roles_iniciales.sql.");
            return;
        }

        JPanel formulario = new JPanel(new GridLayout(0, 2, 6, 6));
        formulario.add(new JLabel("Rol:")); formulario.add(rol);
        formulario.add(new JLabel("Nombre:")); formulario.add(nombre);
        formulario.add(new JLabel("Apellido:")); formulario.add(apellido);
        formulario.add(new JLabel("CUIL:")); formulario.add(cuil);
        formulario.add(new JLabel("Email:")); formulario.add(email);
        formulario.add(new JLabel("Telefono:")); formulario.add(telefono);
        formulario.add(new JLabel("Contrasena:")); formulario.add(contrasena);

        int opcion = JOptionPane.showConfirmDialog(this, formulario, "Crear empleado",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) return;
        char[] clave = contrasena.getPassword();
        try {
            Rol rolElegido = (Rol) rol.getSelectedItem();
            if (rolElegido == null || nombre.getText().trim().isEmpty()
                    || apellido.getText().trim().isEmpty() || cuil.getText().trim().isEmpty()
                    || email.getText().trim().isEmpty()) {
                mostrarError("Completa rol, nombre, apellido, CUIL y email.");
                return;
            }
            if (clave.length < 8) {
                mostrarError("La contrasena debe tener al menos 8 caracteres.");
                return;
            }
            Empleado empleado = new Empleado(0, rolElegido.getIdRol(), nombre.getText().trim(),
                    apellido.getText().trim(), cuil.getText().trim(), email.getText().trim(),
                    telefono.getText().trim(), rolElegido.getNombre());
            empleadoDAO.insertar(empleado, clave);
            cargarEmpleados();
            JOptionPane.showMessageDialog(this, "Empleado creado correctamente.");
        } catch (SQLException ex) {
            mostrarError("No se pudo crear el empleado. Revisa que el email y CUIL no esten repetidos. "
                    + ex.getMessage());
        } finally {
            java.util.Arrays.fill(clave, '\0');
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Gerencia", JOptionPane.ERROR_MESSAGE);
    }
}

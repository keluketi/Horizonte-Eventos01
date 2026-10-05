package com.horizonteeventos.app;

import com.horizonteeventos.dao.AutenticacionDAO;
import com.horizonteeventos.dao.impl.AutenticacionDAOImpl;
import com.horizonteeventos.modelo.Empleado;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.Optional;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/** Ventana de inicio de sesion para empleados administrativos. */
public class LoginFrame extends JFrame {
    private final JTextField email = new JTextField(22);
    private final JPasswordField contrasena = new JPasswordField(22);
    private final AutenticacionDAO autenticacionDAO = new AutenticacionDAOImpl();

    public LoginFrame() {
        super("Horizonte Eventos - Iniciar sesion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel campos = new JPanel(new GridLayout(2, 2, 8, 8));
        campos.add(new JLabel("Correo del empleado:"));
        campos.add(email);
        campos.add(new JLabel("Contrasena:"));
        campos.add(contrasena);
        JButton ingresar = new JButton("Ingresar");
        ingresar.addActionListener(e -> iniciarSesion());
        contrasena.addActionListener(e -> iniciarSesion());
        JPanel contenido = new JPanel(new BorderLayout(8, 8));
        contenido.add(campos, BorderLayout.CENTER);
        contenido.add(ingresar, BorderLayout.SOUTH);
        contenido.setBorder(javax.swing.BorderFactory.createEmptyBorder(16, 16, 16, 16));
        setContentPane(contenido);
        pack();
        setLocationRelativeTo(null);
    }

    private void iniciarSesion() {
        char[] clave = contrasena.getPassword();
        try {
            Optional<Empleado> empleado = autenticacionDAO.iniciarSesion(email.getText(), clave);
            if (empleado.isPresent()) {
                Empleado usuario = empleado.get();
                if (usuario.getIdRol() == 1) {
                    new AdministracionFrame(usuario).setVisible(true);
                } else {
                    new GerenciaFrame(usuario).setVisible(true);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Correo o contrasena incorrectos, o el usuario no es de Administracion.",
                        "No se pudo ingresar", JOptionPane.WARNING_MESSAGE);
                contrasena.setText("");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error de conexion: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            java.util.Arrays.fill(clave, '\0');
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}

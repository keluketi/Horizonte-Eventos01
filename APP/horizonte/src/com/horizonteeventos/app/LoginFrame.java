package com.horizonteeventos.app;

import com.horizonteeventos.dao.AutenticacionDAO;
import com.horizonteeventos.dao.impl.AutenticacionDAOImpl;
import com.horizonteeventos.modelo.Empleado;
import com.horizonteeventos.modelo.Cliente;
import com.horizonteeventos.dao.AutenticacionClienteDAO;
import com.horizonteeventos.dao.impl.AutenticacionClienteDAOImpl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class LoginFrame extends JFrame {

    // =========================================================
    // COLORES
    // =========================================================
    private final Color FONDO = new Color(18, 51, 81);
    private final Color AZUL_OSCURO = new Color(18, 51, 81);
    private final Color NARANJA = new Color(150, 59, 12);
    private final Color NARANJA_HOVER = new Color(119, 43, 6);

    // =========================================================
    // COMPONENTES Y DAO
    // =========================================================
    private JTextField txtUsuario;
    private JPasswordField txtPassword;
    private JButton btnIniciar;
    
    private final AutenticacionDAO autenticacionDAO = new AutenticacionDAOImpl();
    private final AutenticacionClienteDAO autenticacionClienteDAO = new AutenticacionClienteDAOImpl();

    // =========================================================
    // CONSTRUCTOR
    // =========================================================
    public LoginFrame() {
        AccesibilidadUI.aplicar();
        RegistroAccionesUI.iniciar();
        setTitle("Horizonte Eventos - Iniciar sesión");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Panel Principal
        FondoPrincipalPanel fondoPrincipal = new FondoPrincipalPanel();
        fondoPrincipal.setLayout(null);
        fondoPrincipal.setBackground(FONDO);
        setContentPane(fondoPrincipal);

        // Contenedor Redondeado Central
        PanelRedondeado contenedor = new PanelRedondeado(32, new Color(245, 246, 249));
        contenedor.setLayout(null);
        contenedor.setBounds(60, 55, 880, 540);
        fondoPrincipal.add(contenedor);

        // Panel Izquierdo (Formulario)
        JPanel panelLogin = new JPanel();
        panelLogin.setLayout(null);
        panelLogin.setOpaque(false);
        panelLogin.setBounds(0, 0, 440, 532);
        contenedor.add(panelLogin);

        // Empresa
        JLabel logo = LogoUI.crearLogo(56, 56);
        logo.setBounds(52, 30, 56, 56);
        panelLogin.add(logo);

        JLabel lblEmpresa = new JLabel("HORIZONTE");
        lblEmpresa.setFont(new Font("Verdana", Font.BOLD, 20));
        lblEmpresa.setForeground(AZUL_OSCURO);
        lblEmpresa.setBounds(120, 35, 270, 30);
        panelLogin.add(lblEmpresa);

        JLabel lblEventos = new JLabel("EVENTOS");
        lblEventos.setFont(new Font("Verdana", Font.PLAIN, 10));
        lblEventos.setForeground(NARANJA);
        lblEventos.setBounds(122, 63, 200, 20);
        panelLogin.add(lblEventos);

        // Título
        JLabel lblTitulo = new JLabel("Iniciar sesión");
        lblTitulo.setFont(new Font("Verdana", Font.BOLD, 32));
        lblTitulo.setForeground(Color.BLACK);
        lblTitulo.setBounds(55, 125, 330, 45);
        panelLogin.add(lblTitulo);

        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Ingresá a tu cuenta para continuar");
        lblSubtitulo.setFont(new Font("Verdana", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(125, 125, 130));
        lblSubtitulo.setBounds(57, 170, 330, 25);
        panelLogin.add(lblSubtitulo);

        // Campo Usuario / Correo
        txtUsuario = new JTextField();
        txtUsuario.setFont(new Font("Verdana", Font.PLAIN, 14));
        txtUsuario.setForeground(new Color(90, 90, 95));
        txtUsuario.setBackground(Color.WHITE);
        txtUsuario.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(new Color(225, 226, 230), 12),
                new EmptyBorder(0, 16, 0, 16)
        ));
        txtUsuario.setBounds(55, 215, 330, 50);
        panelLogin.add(txtUsuario);
        agregarPlaceholder(txtUsuario, "Correo del cliente o empleado");

        // Campo Contraseña
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Verdana", Font.PLAIN, 14));
        txtPassword.setForeground(new Color(90, 90, 95));
        txtPassword.setBackground(Color.WHITE);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(new Color(225, 226, 230), 12),
                new EmptyBorder(0, 16, 0, 16)
        ));
        txtPassword.setBounds(55, 280, 330, 50);
        panelLogin.add(txtPassword);
        agregarPlaceholderPassword(txtPassword, "Contraseña");

        // Botón Iniciar Sesión
        btnIniciar = new RoundedButton("INICIAR SESIÓN", NARANJA);
        btnIniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnIniciar.setBounds(55, 350, 330, 52);
        panelLogin.add(btnIniciar);
// Hover del botón
        btnIniciar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ((RoundedButton) btnIniciar).setButtonColor(NARANJA_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                ((RoundedButton) btnIniciar).setButtonColor(NARANJA);
            }
        });

        // Panel Derecho (Imagen)
        PanelImagen panelImagen = new PanelImagen();
        panelImagen.setBounds(440, 0, 432, 532);
        contenedor.add(panelImagen);

        // =====================================================
        // EVENTO LOGIN CONECTADO AL DAO 
        // =====================================================
        btnIniciar.addActionListener(e -> realizarLogin());
        txtPassword.addActionListener(e -> realizarLogin());

        setVisible(true);
    }

    private void realizarLogin() {
        String email = txtUsuario.getText().trim();
        char[] clave = txtPassword.getPassword();

        if (email.isEmpty() || email.equals("Correo del cliente o empleado")
                || clave.length == 0 || String.valueOf(clave).equals("Contraseña")) {
            JOptionPane.showMessageDialog(this, "Completá el correo y la contraseña.",
                    "Datos incompletos", JOptionPane.WARNING_MESSAGE);
            java.util.Arrays.fill(clave, '\0');
            return;
        }

        btnIniciar.setEnabled(false);
        try {
            // Primero buscamos una cuenta de empleado.
            Optional<Empleado> empleadoOpt = autenticacionDAO.iniciarSesion(email, clave);
            if (empleadoOpt.isPresent()) {
                Empleado usuario = empleadoOpt.get();
                RegistroAccionesUI.establecerUsuario(usuario);

                // Cada rol de empleado abre su propio módulo.
                if (usuario.getIdRol() == 1) {
                    new AdministracionFrame(usuario).setVisible(true);
                } else if (usuario.getIdRol() == 2) {
                    new CoordinacionFrame(usuario).setVisible(true);
                } else if (usuario.getIdRol() == 3) {
                    new PersonalFrame(usuario).setVisible(true);
                } else if (usuario.getIdRol() == 4) {
                    new GerenciaFrame(usuario).setVisible(true);
                } else {
                    RegistroAccionesUI.establecerUsuario(null);
                    JOptionPane.showMessageDialog(this,
                            "El empleado no tiene un rol habilitado para el escritorio.",
                            "Acceso no disponible", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                dispose();
                return;
            }

            // Si no es empleado, probamos las mismas credenciales contra clientes.
            Optional<Cliente> clienteOpt = autenticacionClienteDAO.iniciarSesion(email, clave);
            if (clienteOpt.isPresent()) {
                new ProcesoPagoDialog(this, clienteOpt.get()).setVisible(true);
                dispose();
                return;
            }

            JOptionPane.showMessageDialog(this, "Correo o contraseña incorrectos.",
                    "No se pudo ingresar", JOptionPane.WARNING_MESSAGE);
            txtPassword.setText("");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error de conexión: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            java.util.Arrays.fill(clave, '\0');
            btnIniciar.setEnabled(true);
        }
    }
    // =========================================================
    // MÉTODOS DE PLACEHOLDERS Y COMPONENTES VISUALES
    // =========================================================
    private void agregarPlaceholder(JTextField campo, String texto) {
        campo.setText(texto);
        campo.setForeground(new Color(155, 155, 160));
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campo.getText().equals(texto)) {
                    campo.setText("");
                    campo.setForeground(new Color(60, 60, 65));
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getText().isEmpty()) {
                    campo.setText(texto);
                    campo.setForeground(new Color(155, 155, 160));
                }
            }
        });
    }

    private void agregarPlaceholderPassword(JPasswordField campo, String texto) {
        campo.setEchoChar((char) 0);
        campo.setText(texto);
        campo.setForeground(new Color(155, 155, 160));
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                String actual = String.valueOf(campo.getPassword());
                if (actual.equals(texto)) {
                    campo.setText("");
                    campo.setEchoChar('•');
                    campo.setForeground(new Color(60, 60, 65));
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (campo.getPassword().length == 0) {
                    campo.setEchoChar((char) 0);
                    campo.setText(texto);
                    campo.setForeground(new Color(155, 155, 160));
                }
            }
        });
    }

    static class RoundedBorder extends javax.swing.border.AbstractBorder {
        private final Color color;
        private final int radius;
        public RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(5, 10, 5, 10);
        }
    }

    static class PanelRedondeado extends JPanel {
        private final int radio;
        private final Color color;
        public PanelRedondeado(int radio, Color color) {
            this.radio = radio;
            this.color = color;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 65));
            g2.fillRoundRect(7, 9, getWidth() - 14, getHeight() - 14, radio, radio);
            g2.setColor(color);
            g2.fillRoundRect(0, 0, getWidth() - 8, getHeight() - 8, radio, radio);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class PanelImagen extends JPanel {
        private BufferedImage imagen;
        public PanelImagen() {
            setOpaque(false);
            try {
                java.net.URL recurso = LoginFrame.class.getResource("/img/fondo.jpg");
                if (recurso != null) {
                    imagen = ImageIO.read(recurso);
                } else {
                    File archivo = new File("APP/horizonte/img/fondo.jpg");
                    if (!archivo.isFile()) archivo = new File("img/fondo.jpg");
                    if (archivo.isFile()) imagen = ImageIO.read(archivo);
                }
            } catch (IOException e) {
                imagen = null;
            }
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (imagen != null) {
                g2.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
            } else {
                g2.setColor(new Color(90, 105, 125));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
            g2.setColor(new Color(10, 25, 40, 55));
            g2.fillRect(0, 0, getWidth(), getHeight());
            
            GradientPaint degradado = new GradientPaint(
                    0, 0, new Color(210, 112, 55, 100),
                    getWidth(), getHeight(), new Color(18, 51, 81, 100)
            );
            g2.setPaint(degradado);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(new Color(255, 255, 255, 235));
            g2.setFont(new Font("Verdana", Font.BOLD, 25));
            g2.drawString("Viví el momento.", 45, 450);

            g2.setFont(new Font("Verdana", Font.PLAIN, 14));
            g2.setColor(new Color(255, 255, 255, 210));
            g2.drawString("Nosotros hacemos que suceda.", 45, 478);
            g2.dispose();
        }
    }

    static class FondoPrincipalPanel extends JPanel {
        public FondoPrincipalPanel() { setOpaque(false); }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint fondo = new GradientPaint(0, 0, new Color(11, 29, 48), getWidth(), getHeight(), new Color(28, 61, 88));
            g2.setPaint(fondo);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    static class RoundedButton extends JButton {
        private Color color;
        public RoundedButton(String texto, Color color) {
            super(texto);
            this.color = color;
            setForeground(Color.WHITE);
            setFont(new Font("Verdana", Font.BOLD, 13));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        public void setButtonColor(Color color) {
            this.color = color;
            repaint();
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}

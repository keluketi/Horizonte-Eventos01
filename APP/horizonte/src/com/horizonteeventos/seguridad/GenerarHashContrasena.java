package com.horizonteeventos.seguridad;

import javax.swing.JPasswordField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/** Herramienta pequena para preparar el hash de la contrasena de un empleado. */
public final class GenerarHashContrasena {
    private GenerarHashContrasena() { }

    public static void main(String[] args) throws Exception {
        JPasswordField campo = new JPasswordField();
        int opcion = JOptionPane.showConfirmDialog(null, campo, "Escribe la contrasena",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opcion != JOptionPane.OK_OPTION) return;
        char[] clave = campo.getPassword();
        if (clave.length < 8) {
            java.util.Arrays.fill(clave, '\0');
            JOptionPane.showMessageDialog(null, "Usa al menos 8 caracteres.");
            return;
        }
        try {
            String hash = PasswordUtil.crearHash(clave);
            JTextField campoHash = new JTextField(hash, 48);
            campoHash.setEditable(false);
            campoHash.selectAll();
            JOptionPane.showMessageDialog(null,
                    new Object[] {"Copia este valor completo para empleados.password:", campoHash},
                    "Hash generado", JOptionPane.INFORMATION_MESSAGE);
        } finally {
            java.util.Arrays.fill(clave, '\0');
        }
    }
}

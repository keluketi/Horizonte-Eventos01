package com.horizonteeventos.seguridad;

import com.horizonteeventos.config.Conexion;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import javax.swing.JOptionPane;

/**
 * Herramienta de ejecución única para preparar el acceso de los clientes.
 * Actualiza clientes.password con hashes PBKDF2; no guarda la clave en texto plano.
 */
public final class AsignarContrasenaPredeterminadaClientes {
    private AsignarContrasenaPredeterminadaClientes() { }

    public static void main(String[] args) {
        boolean ejecucionConfirmada = args.length > 0 && "--confirmar".equals(args[0]);
        if (!ejecucionConfirmada) {
            int opcion = JOptionPane.showConfirmDialog(null,
                    "Se asignará la contraseña 1234 a todos los clientes. ¿Continuar?",
                    "Confirmar cambio de contraseñas", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (opcion != JOptionPane.YES_OPTION) {
                return;
            }
        }

        char[] contrasena = "1234".toCharArray();
        try (Connection conexion = Conexion.getConnection()) {
            conexion.setAutoCommit(false);
            int actualizados = 0;
            try {
                try (PreparedStatement buscar = conexion.prepareStatement(
                        "SELECT id_cliente FROM clientes");
                     ResultSet clientes = buscar.executeQuery();
                     PreparedStatement actualizar = conexion.prepareStatement(
                        "UPDATE clientes SET password = ? WHERE id_cliente = ?")) {
                    while (clientes.next()) {
                        actualizar.setString(1, PasswordUtil.crearHash(contrasena));
                        actualizar.setInt(2, clientes.getInt("id_cliente"));
                        actualizados += actualizar.executeUpdate();
                    }
                }
                conexion.commit();
                String mensaje = "Listo: se actualizaron " + actualizados
                        + " clientes. Ahora ingresan con su correo y la contraseña 1234.";
                if (ejecucionConfirmada) {
                    System.out.println(mensaje);
                } else {
                    JOptionPane.showMessageDialog(null, mensaje,
                            "Contraseñas actualizadas", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException | GeneralSecurityException ex) {
                conexion.rollback();
                throw ex;
            }
        } catch (SQLException | GeneralSecurityException ex) {
            String mensajeError = "No se pudieron actualizar las contraseñas: " + ex.getMessage();
            if (ejecucionConfirmada) {
                System.err.println(mensajeError);
            } else {
                JOptionPane.showMessageDialog(null, mensajeError, "Error", JOptionPane.ERROR_MESSAGE);
            }
            ex.printStackTrace();
        } finally {
            Arrays.fill(contrasena, '\0');
        }
    }
}
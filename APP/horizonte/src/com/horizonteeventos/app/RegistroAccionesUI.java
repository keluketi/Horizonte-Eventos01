package com.horizonteeventos.app;

import com.horizonteeventos.dao.AuditoriaAccionDAO;
import com.horizonteeventos.dao.impl.AuditoriaAccionDAOImpl;
import com.horizonteeventos.modelo.Empleado;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.AbstractButton;
import javax.swing.SwingUtilities;
import java.awt.Window;

/** Captura clics de ratón sobre botones Swing y los persiste en segundo plano. */
public final class RegistroAccionesUI {
    private static final AuditoriaAccionDAO DAO = new AuditoriaAccionDAOImpl();
    private static final ExecutorService COLA = Executors.newSingleThreadExecutor(r -> {
        Thread hilo = new Thread(r, "registro-auditoria-ui");
        hilo.setDaemon(true);
        return hilo;
    });
    private static volatile Empleado usuarioActual;
    private static boolean activo;

    private RegistroAccionesUI() { }

    public static synchronized void iniciar() {
        if (activo) return;
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override public void eventDispatched(AWTEvent evento) {
                if (!(evento instanceof MouseEvent) || evento.getID() != MouseEvent.MOUSE_PRESSED) return;
                Object origen = evento.getSource();
                if (!(origen instanceof AbstractButton)) return;
                AbstractButton boton = (AbstractButton) origen;
                if (!boton.isEnabled()) return;
                String accion = boton.getText();
                if (accion == null || accion.trim().isEmpty()) accion = boton.getAccessibleContext().getAccessibleName();
                if (accion == null || accion.trim().isEmpty()) accion = boton.getClass().getSimpleName();
                String pantalla = "Ventana";
                Window ventana = SwingUtilities.getWindowAncestor(boton);
                if (ventana != null) {
                    if (ventana instanceof java.awt.Frame) pantalla = ((java.awt.Frame) ventana).getTitle();
                    else if (ventana instanceof java.awt.Dialog) pantalla = ((java.awt.Dialog) ventana).getTitle();
                }
                Empleado actor = usuarioActual;
                Integer id = actor == null ? null : actor.getIdEmpleado();
                String nombre = actor == null ? "Sin iniciar sesión" : actor.getNombre() + " " + actor.getApellido();
                String rol = actor == null ? "No autenticado" : actor.getNombreRol();
                if (rol == null || rol.trim().isEmpty()) rol = actor == null
                        ? "No autenticado" : "Rol " + actor.getIdRol();
                final String rolFinal = rol;
                final String accionFinal = accion.length() > 250 ? accion.substring(0, 250) : accion;
                final String pantallaFinal = pantalla.length() > 150 ? pantalla.substring(0, 150) : pantalla;
                COLA.execute(() -> {
                    try { DAO.registrar(id, nombre, rolFinal, pantallaFinal, accionFinal); }
                    catch (Exception ex) { System.err.println("No se pudo registrar la acción de interfaz: " + ex.getMessage()); }
                });
            }
        }, AWTEvent.MOUSE_EVENT_MASK);
        activo = true;
    }

    public static void establecerUsuario(Empleado usuario) {
        usuarioActual = usuario;
    }
}

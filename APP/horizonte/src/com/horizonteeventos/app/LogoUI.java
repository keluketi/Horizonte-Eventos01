package com.horizonteeventos.app;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/** Carga y escala el logo para reutilizarlo en los distintos módulos. */
public final class LogoUI {
    private static final String RECURSO = "/img/logo-horizonte.jpeg";

    private LogoUI() { }

    public static JLabel crearLogo(int ancho, int alto) {
        BufferedImage original = cargarImagen();
        JLabel etiqueta = new JLabel();
        if (original != null) {
            BufferedImage escalada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
            java.awt.Graphics2D grafico = escalada.createGraphics();
            grafico.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            grafico.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            grafico.drawImage(original, 0, 0, ancho, alto, null);
            grafico.dispose();
            etiqueta.setIcon(new ImageIcon(escalada));
        }
        etiqueta.setPreferredSize(new java.awt.Dimension(ancho, alto));
        return etiqueta;
    }

    private static BufferedImage cargarImagen() {
        try {
            java.net.URL recurso = LogoUI.class.getResource(RECURSO);
            if (recurso != null) return ImageIO.read(recurso);
            File archivo = new File("img/logo-horizonte.jpeg");
            if (!archivo.isFile()) archivo = new File("src/img/logo-horizonte.jpeg");
            return archivo.isFile() ? ImageIO.read(archivo) : null;
        } catch (IOException ex) {
            System.err.println("No se pudo cargar el logo de Horizonte Eventos: " + ex.getMessage());
            return null;
        }
    }
}

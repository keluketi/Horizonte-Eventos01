package com.horizonteeventos.app;

import java.awt.Color;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

/** Ajustes compartidos de fuente y contraste para la aplicación Swing. */
public final class AccesibilidadUI {
    private static final Color AZUL = new Color(18, 51, 81);

    private AccesibilidadUI() { }

    public static void aplicar() {
        FontUIResource normal = new FontUIResource("Verdana", Font.PLAIN, 14);
        FontUIResource negrita = new FontUIResource("Verdana", Font.BOLD, 14);
        UIManager.put("defaultFont", normal);
        UIManager.put("Label.font", normal);
        UIManager.put("Button.font", negrita);
        UIManager.put("TextField.font", normal);
        UIManager.put("PasswordField.font", normal);
        UIManager.put("TextArea.font", normal);
        UIManager.put("ComboBox.font", normal);
        UIManager.put("Table.font", normal);
        UIManager.put("TableHeader.font", negrita);
        UIManager.put("OptionPane.messageFont", normal);
        UIManager.put("OptionPane.buttonFont", negrita);
        UIManager.put("Button.background", AZUL);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("Table.foreground", Color.BLACK);
        UIManager.put("Table.selectionBackground", AZUL);
        UIManager.put("Table.selectionForeground", Color.WHITE);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", Color.BLACK);
        UIManager.put("PasswordField.background", Color.WHITE);
        UIManager.put("PasswordField.foreground", Color.BLACK);
    }
}

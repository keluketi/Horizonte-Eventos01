package com.horizonteeventos.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/** Abre conexiones usando la configuración local o variables de entorno. */
public final class Conexion {
    private static final String ARCHIVO_CONFIGURACION = "database.properties";

    private Conexion() {
    }

    public static Connection getConnection() throws SQLException {
        Properties config = cargarConfiguracion();
        String host = obtener(config, "db.host", "HORIZONTE_DB_HOST", "pma.torga.com.ar");
        String puerto = obtener(config, "db.port", "HORIZONTE_DB_PORT", "3306");
        String base = obtener(config, "db.name", "HORIZONTE_DB_NAME", "s29_horizonte_eventos");
        String usuario = obtener(config, "db.user", "HORIZONTE_DB_USER", "");
        String contrasena = obtener(config, "db.password", "HORIZONTE_DB_PASSWORD", "");
        String url = obtener(config, "db.url", "HORIZONTE_DB_URL",
                "jdbc:mariadb://" + host + ":" + puerto + "/" + base);
        if (usuario.trim().isEmpty()) {
            throw new SQLException("Falta configurar db.user en database.properties o HORIZONTE_DB_USER.");
        }
        if (contrasena.isEmpty()) {
            throw new SQLException("Falta configurar la contraseña en database.properties o HORIZONTE_DB_PASSWORD.");
        }
        return DriverManager.getConnection(url, usuario, contrasena);
    }

    private static Properties cargarConfiguracion() throws SQLException {
        Properties config = new Properties();
        Path archivo = Paths.get(ARCHIVO_CONFIGURACION);
        if (!Files.isRegularFile(archivo)) {
            Path proyectoApp = Paths.get("APP", "horizonte", ARCHIVO_CONFIGURACION);
            if (Files.isRegularFile(proyectoApp)) archivo = proyectoApp;
        }
        if (Files.isRegularFile(archivo)) {
            try (InputStream entrada = Files.newInputStream(archivo)) {
                config.load(entrada);
            } catch (IOException ex) {
                throw new SQLException("No se pudo leer " + archivo + ".", ex);
            }
        }
        return config;
    }

    private static String obtener(Properties config, String clave, String variableEntorno, String valorPredeterminado) {
        String valor = config.getProperty(clave);
        if (valor == null || valor.trim().isEmpty()) valor = System.getenv(variableEntorno);
        return valor == null || valor.trim().isEmpty() ? valorPredeterminado : valor.trim();
    }
}

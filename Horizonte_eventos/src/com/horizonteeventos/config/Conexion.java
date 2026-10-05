package com.horizonteeventos.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Abre una conexion nueva a la base MySQL/MariaDB. */
public final class Conexion {
    // Cambia estos tres valores para que coincidan con tu instalacion local.
    // XAMPP incluye MariaDB; por eso usamos el prefijo jdbc:mariadb.
    private static final String URL = "jdbc:mariadb://localhost:3306/horizonte_eventos";
    private static final String USUARIO = "root";
    private static final String CONTRASENA = "";

    private Conexion() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}

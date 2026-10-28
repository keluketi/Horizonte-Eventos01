package com.horizonteeventos.dao;

import com.horizonteeventos.config.Conexion;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Acceso de escritorio a los mensajes y solicitudes recibidos desde la web. */
public final class WebInboxDAO {
    /** Verifica las tablas y columnas requeridas por este módulo. */
    public void ensureSchema() throws SQLException {
        try (Connection c = Conexion.getConnection()) {
            DatabaseMetaData metadata = c.getMetaData();
            String catalog = c.getCatalog();
            if (!hasTable(metadata, catalog, "chat_mensajes")) {
                throw new SQLException("No existe la tabla chat_mensajes. Inicializa el chat desde el sitio web o aplica la migración 08.");
            }
            if (!hasTable(metadata, catalog, "solicitudes_web")) {
                throw new SQLException("No existe solicitudes_web. Ejecuta la migración 05_modulos_personal_y_solicitudes_web.sql.");
            }
            if (!hasColumn(metadata, catalog, "solicitudes_web", "respuesta_admin")) {
                try (java.sql.Statement statement = c.createStatement()) {
                    statement.executeUpdate("ALTER TABLE solicitudes_web ADD COLUMN respuesta_admin TEXT NULL AFTER estado");
                } catch (SQLException ex) {
                    throw new SQLException("El host no permitió agregar la columna respuesta_admin a solicitudes_web. "
                            + "Verifica que el usuario de database.properties tenga permiso ALTER o ejecuta "
                            + "APP/horizonte/database/08_respuestas_solicitudes_web.sql desde el panel del hosting. "
                            + "Detalle del host: " + ex.getMessage(), ex);
                }
                if (!hasColumn(c.getMetaData(), c.getCatalog(), "solicitudes_web", "respuesta_admin")) {
                    throw new SQLException("No se encontró respuesta_admin en solicitudes_web después de actualizar el esquema.");
                }
            }
        }
    }

    private boolean hasTable(DatabaseMetaData metadata, String catalog, String table) throws SQLException {
        try (ResultSet tables = metadata.getTables(catalog, null, table, new String[] {"TABLE"})) {
            return tables.next();
        }
    }

    private boolean hasColumn(DatabaseMetaData metadata, String catalog, String table, String column) throws SQLException {
        try (ResultSet columns = metadata.getColumns(catalog, null, table, column)) {
            return columns.next();
        }
    }

    public static final class Message {
        public final int id;
        public final String name, email, message, date;
        public final boolean admin;
        public final Integer replyTo;
        public Message(int id, String name, String email, String message, boolean admin, Integer replyTo, String date) {
            this.id=id; this.name=name; this.email=email; this.message=message; this.admin=admin; this.replyTo=replyTo; this.date=date;
        }
    }
    public static final class Request {
        public final long id;
        public final String name, email, phone, type, date, time, space, services, comments, status, response, created;
        public final int guests;
        public Request(long id, String name, String email, String phone, String type, String date, String time,
                int guests, String space, String services, String comments, String status, String response, String created) {
            this.id=id; this.name=name; this.email=email; this.phone=phone; this.type=type; this.date=date; this.time=time;
            this.guests=guests; this.space=space; this.services=services; this.comments=comments; this.status=status; this.response=response; this.created=created;
        }
    }

    public List<Message> messages() throws SQLException {
        ensureSchema();
        List<Message> rows = new ArrayList<>();
        String sql = "SELECT id_mensaje,nombre,email,mensaje,es_admin,respuesta_a,creado_en FROM chat_mensajes ORDER BY creado_en,id_mensaje";
        try (Connection c=Conexion.getConnection(); PreparedStatement p=c.prepareStatement(sql); ResultSet r=p.executeQuery()) {
            while(r.next()) { Object parent=r.getObject("respuesta_a"); rows.add(new Message(r.getInt("id_mensaje"),r.getString("nombre"),r.getString("email"),r.getString("mensaje"),r.getBoolean("es_admin"),parent==null?null:((Number)parent).intValue(),r.getString("creado_en"))); }
        }
        return rows;
    }
    public void reply(String message, int rootId) throws SQLException {
        ensureSchema();
        String sql="INSERT INTO chat_mensajes (nombre,email,mensaje,es_admin,respuesta_a,creado_en) VALUES ('Equipo Horizonte','admin@horizonte.eventos',?,1,?,NOW())";
        try(Connection c=Conexion.getConnection(); PreparedStatement p=c.prepareStatement(sql)){p.setString(1,message);p.setInt(2,rootId);p.executeUpdate();}
    }
    public List<Request> requests() throws SQLException {
        ensureSchema();
        List<Request> rows=new ArrayList<>();
        String sql="SELECT id_solicitud,nombre,apellido,email,telefono,tipo_evento,fecha_tentativa,horario,invitados,espacio_preferido,servicios,comentarios,estado,respuesta_admin,fecha_creacion FROM solicitudes_web ORDER BY fecha_creacion DESC";
        try(Connection c=Conexion.getConnection();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){
            while(r.next()) rows.add(new Request(r.getLong("id_solicitud"),r.getString("nombre")+" "+r.getString("apellido"),r.getString("email"),r.getString("telefono"),r.getString("tipo_evento"),r.getString("fecha_tentativa"),r.getString("horario"),r.getInt("invitados"),r.getString("espacio_preferido"),r.getString("servicios"),r.getString("comentarios"),r.getString("estado"),r.getString("respuesta_admin"),r.getString("fecha_creacion")));
        }
        return rows;
    }
    public void answerRequest(long id, String status, String response) throws SQLException {
        ensureSchema();
        String sql="UPDATE solicitudes_web SET estado=?, respuesta_admin=? WHERE id_solicitud=?";
        try(Connection c=Conexion.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setString(1,status);p.setString(2,response);p.setLong(3,id);p.executeUpdate();}
    }
}

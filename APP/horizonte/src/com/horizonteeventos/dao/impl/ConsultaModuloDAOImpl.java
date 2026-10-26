package com.horizonteeventos.dao.impl;

import com.horizonteeventos.config.Conexion;
import com.horizonteeventos.dao.ConsultaModuloDAO;
import com.horizonteeventos.modelo.DatosTabla;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Consultas seguras predefinidas para las vistas administrativas. */
public class ConsultaModuloDAOImpl implements ConsultaModuloDAO {
    @Override
    public DatosTabla consultar(String modulo) throws SQLException {
        String sql = sqlPara(modulo);
        try (Connection conexion = Conexion.getConnection();
             PreparedStatement consulta = conexion.prepareStatement(sql);
             ResultSet resultado = consulta.executeQuery()) {
            ResultSetMetaData metadatos = resultado.getMetaData();
            int cantidadColumnas = metadatos.getColumnCount();
            String[] columnas = new String[cantidadColumnas];
            for (int i = 0; i < cantidadColumnas; i++) {
                columnas[i] = metadatos.getColumnLabel(i + 1);
            }
            List<Object[]> filas = new ArrayList<>();
            while (resultado.next()) {
                Object[] fila = new Object[cantidadColumnas];
                for (int i = 0; i < cantidadColumnas; i++) {
                    fila[i] = resultado.getObject(i + 1);
                }
                filas.add(fila);
            }
            return new DatosTabla(columnas, filas);
        }
    }

    private String sqlPara(String modulo) throws SQLException {
        switch (modulo) {
            case "Presupuestos":
                return "SELECT p.id_presupuesto AS ID, e.id_evento AS Evento, c.nombre AS Cliente, c.apellido AS Apellido, "
                        + "e.fecha_evento AS Fecha, p.fecha_emision AS Emitido, "
                        + "p.fecha_vencimiento AS Vence, p.monto_total AS Total, p.monto_sena AS Seña, "
                        + "p.estado_aprobacion AS Estado FROM presupuestos p "
                        + "JOIN eventos e ON e.id_evento=p.id_evento "
                        + "JOIN clientes c ON c.id_cliente=e.id_cliente ORDER BY p.fecha_emision DESC";
            case "Eventos":
                return "SELECT e.id_evento AS ID, COALESCE(t.nombre,'Sin tipo') AS Tipo, "
                        + "c.nombre AS Cliente, c.apellido AS Apellido, "
                        + "s.nombre_espacio AS Espacio, es.nombre_estado AS Estado, e.fecha_evento AS Fecha, "
                        + "e.hora_inicio AS Inicio, e.hora_fin AS Fin, e.cantidad_invitados AS Invitados, "
                        + "e.asistencia_real AS Asistencia, e.observaciones AS Requerimientos FROM eventos e "
                        + "JOIN clientes c ON c.id_cliente=e.id_cliente "
                        + "JOIN espacios s ON s.id_espacio=e.id_espacio "
                        + "JOIN estado_del_evento es ON es.id_estado=e.id_estado "
                        + "LEFT JOIN tipos_evento t ON t.id_tipo_evento=e.id_tipo_evento "
                        + "ORDER BY e.fecha_evento, e.hora_inicio";
            case "Pagos":
                return "SELECT p.id_pago AS ID, p.fecha_pago AS Fecha, c.nombre AS Cliente, "
                        + "c.apellido AS Apellido, p.monto AS Importe, p.medio_pago AS Medio, "
                        + "ee.nombre_estado AS Estado, p.observaciones AS Observaciones "
                        + "FROM pagos p JOIN eventos e ON e.id_evento=p.id_evento "
                        + "JOIN clientes c ON c.id_cliente=e.id_cliente "
                        + "JOIN estado_del_evento ee ON ee.id_estado=e.id_estado "
                        + "ORDER BY p.fecha_pago DESC";
            case "Comprobantes":
                return "SELECT f.id_factura AS ID, f.nro_factura AS Numero, "
                        + "f.tipo_comprobante AS Tipo, f.fecha_emision AS Emision, "
                        + "f.monto_abonado AS Importe, c.nombre AS Cliente, c.apellido AS Apellido "
                        + "FROM facturas f JOIN eventos e ON e.id_evento=f.id_evento "
                        + "JOIN clientes c ON c.id_cliente=e.id_cliente ORDER BY f.fecha_emision DESC";
            case "Proveedores":
                return "SELECT id_tercero AS ID, razon_social AS Proveedor, contacto_nombre AS Contacto, "
                        + "telefono AS Telefono, rubro AS Rubro FROM terceros ORDER BY razon_social";
            case "Servicios":
                return "SELECT s.id_servicio AS ID, s.nombre_servicio AS Servicio, "
                        + "COALESCE(t.razon_social,'Servicio propio') AS Proveedor, "
                        + "s.costo_unitario AS 'Precio de lista', "
                        + "COALESCE(CAST(s.cantidad_disponible AS CHAR), 'Sin informar') AS Disponibilidad "
                        + "FROM servicios s "
                        + "LEFT JOIN terceros t ON t.id_tercero=s.id_tercero ORDER BY s.nombre_servicio";
            case "Espacios":
                return "SELECT id_espacio AS ID, nombre_espacio AS Espacio, "
                        + "capacidad_maxima AS Capacidad, precio_base_alquiler AS Precio "
                        + "FROM espacios ORDER BY nombre_espacio";
            case "Reportes":
                return "SELECT 'Eventos registrados' AS Indicador, COUNT(*) AS Valor FROM eventos "
                        + "UNION ALL SELECT 'Eventos confirmados', COUNT(*) FROM eventos e "
                        + "JOIN estado_del_evento s ON s.id_estado=e.id_estado "
                        + "WHERE s.nombre_estado IN ('Aceptado','Señado','Pagado','Confirmado') "
                        + "UNION ALL SELECT 'Presupuestos aceptados', COUNT(*) FROM presupuestos "
                        + "WHERE estado_aprobacion='Aceptado' "
                        + "UNION ALL SELECT 'Incidencias registradas', COUNT(*) FROM incidencias "
                        + "UNION ALL SELECT CONCAT('Eventos en ',s.nombre_espacio), COUNT(e.id_evento) "
                        + "FROM espacios s LEFT JOIN eventos e ON e.id_espacio=s.id_espacio "
                        + "GROUP BY s.id_espacio,s.nombre_espacio ORDER BY Indicador";
            default:
                throw new SQLException("No existe una consulta para el modulo: " + modulo);
        }
    }
}

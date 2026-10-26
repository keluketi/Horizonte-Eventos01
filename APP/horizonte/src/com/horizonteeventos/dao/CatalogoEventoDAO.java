package com.horizonteeventos.dao;

import com.horizonteeventos.modelo.Espacio;
import com.horizonteeventos.modelo.EstadoEvento;
import com.horizonteeventos.modelo.Paquete;
import com.horizonteeventos.modelo.Servicio;
import com.horizonteeventos.modelo.TipoEvento;
import com.horizonteeventos.modelo.ServicioContratado;
import java.time.LocalDate;
import java.time.LocalTime;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/** Carga las opciones existentes para el formulario de evento. */
public interface CatalogoEventoDAO {
    List<Espacio> listarEspacios() throws SQLException;
    List<Paquete> listarPaquetes() throws SQLException;
    List<EstadoEvento> listarEstados() throws SQLException;
    List<TipoEvento> listarTiposEvento() throws SQLException;
    List<Servicio> listarServicios() throws SQLException;
    int guardarServicio(Servicio servicio) throws SQLException;
    boolean eliminarServicio(int idServicio) throws SQLException;
    List<String> validarCapacidadServicios(LocalDate fecha, LocalTime inicio, LocalTime fin,
            List<ServicioContratado> servicios) throws SQLException;
    Map<Integer, Integer> listarComponentesPaquete(int idPaquete) throws SQLException;
    int guardarPaquete(Paquete paquete, Map<Integer, Integer> componentes) throws SQLException;
}

package com.horizonteeventos.modelo;

/** Datos de un empleado autenticado. Nunca contiene su contrasena. */
public class Empleado {
    private final int idEmpleado;
    private final int idRol;
    private final String nombre;
    private final String apellido;
    private final String cuil;
    private final String email;
    private final String telefono;
    private final String nombreRol;

    public Empleado(int idEmpleado, int idRol, String nombre, String apellido, String email) {
        this(idEmpleado, idRol, nombre, apellido, null, email, null, null);
    }

    public Empleado(int idEmpleado, int idRol, String nombre, String apellido,
                    String cuil, String email, String telefono, String nombreRol) {
        this.idEmpleado = idEmpleado;
        this.idRol = idRol;
        this.nombre = nombre;
        this.apellido = apellido;
        this.cuil = cuil;
        this.email = email;
        this.telefono = telefono;
        this.nombreRol = nombreRol;
    }

    public int getIdEmpleado() { return idEmpleado; }
    public int getIdRol() { return idRol; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getCuil() { return cuil; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public String getNombreRol() { return nombreRol; }
}

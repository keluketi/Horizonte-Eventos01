package com.horizonteeventos.modelo;

/** Informacion basica de un cliente. */
public class Cliente {
    private int idCliente;
    private String nombre;
    private String apellido;
    private String dniCuit;
    private String email;
    private String telefono;

    public Cliente() { }

    public Cliente(int idCliente, String nombre, String apellido, String dniCuit,
                   String email, String telefono) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dniCuit = dniCuit;
        this.email = email;
        this.telefono = telefono;
    }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getDniCuit() { return dniCuit; }
    public void setDniCuit(String dniCuit) { this.dniCuit = dniCuit; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}

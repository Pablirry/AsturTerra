package model;

import java.util.Arrays;

public class Usuario {

    private int id;
    private String nombre;
    private String correo;
    private String contrasena;
    private String tipo;
    private byte[] imagenPerfil;

    /**
     * Constructor de la clase Usuario
     * 
     * @param id           : entero
     * @param nombre       : String
     * @param correo       : String
     * @param contrasena   : String
     * @param tipo         : String
     * @param imagenPerfil : byte[]
     */

    public Usuario(int id, String nombre, String correo, String contrasena, String tipo, byte[] imagenPerfil) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.tipo = tipo;
        this.imagenPerfil = imagenPerfil;
    }

    /**
     * Método para obtener el id del usuario
     * 
     * @return : entero
     */

    public int getId() {
        return id;
    }

    /**
     * Método para establecer el id del usuario
     * 
     * @param id : entero
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Métodos para obtener el nombre del usuario
     * 
     * @return : String
     */

    public String getNombre() {
        return nombre;
    }

    /**
     * Método para establecer el nombre del usuario
     * 
     * @param nombre : String
     */

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Métodos para obtener el correo del usuario
     * 
     * @return : String
     */

    public String getCorreo() {
        return correo;
    }

    /**
     * Método para establecer el correo del usuario
     * 
     * @param correo : String
     */

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Métodos para obtener la contraseña del usuario
     * 
     * @return : String
     */

    public String getContrasena() {
        return contrasena;
    }

    /**
     * Método para establecer la contraseña del usuario
     * 
     * @param contrasena : String
     */

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    /**
     * Métodos para obtener el tipo del usuario
     * 
     * @return : String
     */

    public String getTipo() {
        return tipo;
    }

    /**
     * Método para establecer el tipo del usuario
     * 
     * @param tipo : String
     */

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Métodos para obtener la imagen del perfil del usuario
     * 
     * @return : byte[]
     */

    public byte[] getImagenPerfil() {
        return imagenPerfil;
    }

    /**
     * Método para establecer la imagen del perfil del usuario
     * 
     * @param imagenPerfil : byte[]
     */

    public void setImagenPerfil(byte[] imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    /**
     * Método para verificar si el usuario es administrador
     * 
     * @return : boolean
     */

    public boolean isAdmin() {
        return tipo != null && tipo.equalsIgnoreCase("admin");
    }

    /**
     * Método para verificar si el usuario es cliente
     * 
     * @return : boolean
     */

    @Override
    public String toString() {
        return "Usuario [id=" + id + ", nombre=" + nombre + ", correo=" + correo + ", contrasena=" + contrasena
                + ", tipo=" + tipo + ", imagenPerfil=" + Arrays.toString(imagenPerfil) + "]";
    }

}

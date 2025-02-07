package modelo;

import java.util.Arrays;

public class Usuario {

    private int id;
    private String nombre;
    private String correo;
    private String contrasena;
    private String tipo;
    private byte[] imagenPerfil;

    public Usuario(int id, String nombre, String correo, String contrasena, String tipo, byte[] imagenPerfil) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
        this.tipo = tipo;
        this.imagenPerfil = imagenPerfil;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public byte[] getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(byte[] imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    @Override
    public String toString() {
        return "Usuario [id=" + id + ", nombre=" + nombre + ", correo=" + correo + ", contrasena=" + contrasena
                + ", tipo=" + tipo + ", imagenPerfil=" + Arrays.toString(imagenPerfil) + "]";
    }
    
}

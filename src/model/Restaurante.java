package model;

import java.util.Arrays;

public class Restaurante {

    private int id;
    private String nombre;
    private String ubicacion;
    private byte[] imagen;
    
    public Restaurante(int id, String nombre, String ubicacion, byte[] imagen) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.imagen = imagen;
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

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "Restaurante [id=" + id + ", nombre=" + nombre + ", ubicacion=" + ubicacion + 
        ", imagen=" + Arrays.toString(imagen) + "]";
    }
    
}

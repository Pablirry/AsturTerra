package model;

import java.util.Arrays;

public class Ruta {

    private int id;
    private String nombre;
    private String descripcion;
    private byte[] imagen;
    private double precio;
    private String dificultad;
    
    public Ruta(int id, String nombre, String descripcion, byte[] imagen, double precio, String dificultad) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.precio = precio;
        this.dificultad = dificultad;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    @Override
    public String toString() {
        return "Ruta [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", imagen="
                + Arrays.toString(imagen) + ", precio=" + precio + ", dificultad=" + dificultad + "]";
    }
    
}

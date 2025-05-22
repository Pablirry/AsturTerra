package model;

import java.util.Arrays;

public class Restaurante {

    private int id;
    private String nombre;
    private String ubicacion;
    private byte[] imagen;
    private String especialidad;
    private String descripcion;

    public Restaurante(int id, String nombre, String ubicacion, byte[] image, String especialidad, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.imagen = image;
        this.especialidad = especialidad;
        this.descripcion = descripcion;
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

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Restaurante [id=" + id + ", nombre=" + nombre + ", ubicacion=" + ubicacion +
        ", imagen=" + Arrays.toString(imagen) + ", especialidad=" + especialidad + ", descripcion=" + descripcion + "]";
    }

}

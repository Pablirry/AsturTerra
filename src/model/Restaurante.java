package model;

import java.util.Arrays;

public class Restaurante {

    private int id;
    private String nombre;
    private String ubicacion;
    private byte[] imagen;
    private String especialidad;
    private String descripcion;

    /**
     * Constructor de la clase Restaurante
     * 
     * @param id          : entero
     * @param nombre      : String
     * @param ubicacion   : String
     * @param image       : byte[]
     * @param especialidad: String
     * @param descripcion : String
     */

    public Restaurante(int id, String nombre, String ubicacion, byte[] image, String especialidad, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.imagen = image;
        this.especialidad = especialidad;
        this.descripcion = descripcion;
    }

    /**
     * Método para obtener el id del restaurante
     * 
     * @return : entero
     */

    public int getId() {
        return id;
    }

    /**
     * Método para establecer el id del restaurante
     * 
     * @param id : entero
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Métodos para obtener el nombre del restaurante
     * 
     * @return : String
     */

    public String getNombre() {
        return nombre;
    }

    /**
     * Método para establecer el nombre del restaurante
     * 
     * @param nombre : String
     */

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Método para obtener la ubicacion del restaurante
     * 
     * @return : String
     */

    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * Método para establecer la ubicacion del restaurante
     * 
     * @param ubicacion : String
     */

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    /**
     * Método para obtener la imagen del restaurante
     * 
     * @return : byte[]
     */

    public byte[] getImagen() {
        return imagen;
    }

    /**
     * Método para establecer la imagen del restaurante
     * 
     * @param imagen : byte[]
     */

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    /**
     * Método para obtener la especialidad del restaurante
     * 
     * @return : String
     */

    public String getEspecialidad() {
        return especialidad;
    }

    /**
     * Método para establecer la especialidad del restaurante
     * 
     * @param especialidad : String
     */

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    /**
     * Método para obtener la descripcion del restaurante
     * 
     * @return : String
     */

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Método para establecer la descripcion del restaurante
     * 
     * @param descripcion : String
     */

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Método para obtener la cadena de caracteres del restaurante
     * 
     * @return : String
     */

    @Override
    public String toString() {
        return "Restaurante [id=" + id + ", nombre=" + nombre + ", ubicacion=" + ubicacion +
        ", imagen=" + Arrays.toString(imagen) + ", especialidad=" + especialidad + ", descripcion=" + descripcion + "]";
    }

}

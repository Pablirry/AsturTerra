package model;

public class Ruta {

    private int id;
    private String nombre;
    private String descripcion;
    private byte[] imagen;
    private double precio;
    private int dificultad;

    /**
     * Constructor de la clase Ruta
     * 
     * @param id          : entero
     * @param nombre      : String
     * @param descripcion : String
     * @param imagen      : byte[]
     * @param precio      : double
     * @param dificultad  : entero
     */

    public Ruta(int id, String nombre, String descripcion, byte[] imagen, double precio, int dificultad) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.precio = precio;
        this.dificultad = dificultad;
    }

    /**
     * Método para obtener el id de la ruta
     * 
     * @return : entero
     */

    public int getId() {
        return id;
    }

    /**
     * Método para establecer el id de la ruta
     * 
     * @param id : entero
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Métodos para obtener el nombre de la ruta
     * 
     * @return : String
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Método para establecer el nombre de la ruta
     * 
     * @param nombre : String
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Método para obtener la descripción de la ruta
     * 
     * @return : String
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Método para establecer la descripción de la ruta
     * 
     * @param descripcion : String
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Método para obtener la imagen de la ruta
     * 
     * @return : byte[]
     */
    public byte[] getImagen() {
        return imagen;
    }

    /**
     * Método para establecer la imagen de la ruta
     * 
     * @param imagen : byte[]
     */
    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    /**
     * Método para obtener el precio de la ruta
     * 
     * @return : double
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Método para establecer el precio de la ruta
     * 
     * @param precio : double
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Método para obtener la dificultad de la ruta
     * 
     * @return : entero
     */
    public int getDificultad() {
        return dificultad;
    }

    /**
     * Método para establecer la dificultad de la ruta
     * 
     * @param dificultad : entero
     */
    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }
}
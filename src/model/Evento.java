package model;

/**
 * @author Pablo
 * 
 */

public class Evento {
    private int id;
    private String nombre;
    private String descripcion;
    private String ubicacion;
    private String tipo;
    private double precio;
    private byte[] imagen;

    /**
     * Constructor de la clase Evento
     * @param id : entero
     * @param nombre : String
     * @param descripcion : String
     * @param ubicacion : String
     * @param tipo : String
     * @param precio : double
     * @param imagen : byte[]
     */

    public Evento(int id, String nombre, String descripcion, String ubicacion, String tipo, double precio, byte[] imagen) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.tipo = tipo;
        this.precio = precio;
        this.imagen = imagen;
    }

    /**
     * Método para obtener el id del evento
     * @return : entero
     */

    public int getId() {
        return id;
    }

    /**
     * Método para establecer el id del evento
     * @param id : entero
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Métodos para obtener el nombre del evento
     * @return : String
     */

    public String getNombre() {
        return nombre;
    }

    /**
     * Método para establecer el nombre del evento
     * @param nombre : String
     */

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Métodos para obtener la descripción del evento
     * @return : String
     */

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Método para establecer la descripción del evento
     * @param descripcion : String
     */

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Métodos para obtener la ubicación del evento
     * @return : String
     */

    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * Método para establecer la ubicación del evento
     * @param ubicacion : String
     */

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    /**
     * Métodos para obtener el tipo del evento
     * @return : String
     */

    public String getTipo() {
        return tipo;
    }

    /**
     * Método para establecer el tipo del evento
     * @param tipo : String
     */

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Métodos para obtener el precio del evento
     * @return : double
     */

    public double getPrecio() {
        return precio;
    }

    /**
     * Método para establecer el precio del evento
     * @param precio : double
     */

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Métodos para obtener la imagen del evento
     * @return : byte[]
     */

    public byte[] getImagen() {
        return imagen;
    }

    /**
     * Método para establecer la imagen del evento
     * @param imagen : byte[]
     */

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }
    
}

package model;

import java.util.Date;

/**
 * @author Pablo
 */

public class Historial {
    
    private int id;
    private int idUsuario;
    private String accion;
    private Date fecha;

    /**
     * Constructor de la clase Historial
     * @param id : entero
     * @param idUsuario : entero
     * @param accion : String
     * @param fecha : Date
     */
    
    public Historial(int id, int idUsuario, String accion, Date fecha) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.accion = accion;
        this.fecha = fecha;
    }

    /**
     * Método para obtener el id del historial
     * @return : entero
     */

    public int getId() {
        return id;
    }

    /**
     * Método para establecer el id del historial
     * @param id : entero
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Métodos para obtener el id del usuario
     * @return : entero
     */

    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Método para establecer el id del usuario
     * @param idUsuario : entero
     */

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Métodos para obtener la acción del historial
     * @return : String
     */

    public String getAccion() {
        return accion;
    }

    /**
     * Método para establecer la acción del historial
     * @param accion : String
     */

    public void setAccion(String accion) {
        this.accion = accion;
    }

    /**
     * Métodos para obtener la fecha del historial
     * @return : Date
     */

    public Date getFecha() {
        return fecha;
    }

    /**
     * Método para establecer la fecha del historial
     * @param fecha : Date
     */

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Método toString para mostrar el historial
     * @return : String
     */

    @Override
    public String toString() {
        return "Historial [id=" + id + ", idUsuario=" + idUsuario + ", accion=" + accion + ", fecha=" + fecha + "]";
    }
    
}

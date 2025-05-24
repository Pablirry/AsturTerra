package model;

import java.util.Date;

/**
 * @author Pablo
 */

public class Reserva {
    private int id;
    private int idUsuario;
    private int idRuta;
    private Date fecha;
    private boolean confirmada;

    /**
     * Constructor de la clase Reserva
     * 
     * @param id        : entero
     * @param idUsuario : entero
     * @param idRuta    : entero
     * @param fecha     : Date
     * @param confirmada: boolean
     */

    public Reserva(int id, int idUsuario, int idRuta, Date fecha, boolean confirmada) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idRuta = idRuta;
        this.fecha = fecha;
        this.confirmada = confirmada;
    }

    /**
     * Método para obtener el id de la reserva
     * 
     * @return : entero
     */

    public int getId() { return id; }

    /**
     * Método para establecer el id de la reserva
     * 
     * @param id : entero
     */

    public int getIdUsuario() { return idUsuario; }

    /**
     * Método para obtener el id de la ruta
     * 
     * @return : entero
     */
    public int getIdRuta() { return idRuta; }

    /**
     * Método para obtener la fecha de la reserva
     * 
     * @return : Date
     */
    public Date getFecha() { return fecha; }

    /**
     * Método para verificar si la reserva está confirmada
     * 
     * @return : boolean
     */
    public boolean isConfirmada() { return confirmada; }

    /**
     * Método para establecer el estado de confirmación de la reserva
     * 
     * @param confirmada : boolean
     */
    public void setConfirmada(boolean confirmada) { this.confirmada = confirmada; }
}
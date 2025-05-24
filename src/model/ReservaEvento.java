package model;

import java.sql.Date;

/**
 * @author Pablo
 */

public class ReservaEvento {

    private int id;
    private int usuarioId;
    private int eventoId;
    private Date fechaReserva;
    private boolean confirmada;

    /**
     * Constructor de la clase ReservaEvento
     * @param id : entero
     * @param usuarioId : entero
     * @param eventoId : entero
     * @param fechaReserva : Date
     * @param confirmada : boolean
     */

    public ReservaEvento(int id, int usuarioId, int eventoId, Date fechaReserva, boolean confirmada) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.eventoId = eventoId;
        this.fechaReserva = fechaReserva;
        this.confirmada = confirmada;
    }

    /**
     * Método para obtener el id de la reserva
     * @return : entero
     */

    public int getId() { return id; }


    /**
     * Método para establecer el id de la reserva
     * @param id : entero
     */
    public int getUsuarioId() { return usuarioId; }

    /**
     * Método para establecer el id del usuario
     * @param usuarioId : entero
     */
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    /**
     * Método para obtener el id del evento
     * @return : entero
     */
    public int getEventoId() { return eventoId; }

    /**
     * Método para establecer el id del evento
     * @param eventoId : entero
     */
    public Date getFechaReserva() { return fechaReserva; }

    /**
     * Método para establecer la fecha de la reserva
     * @param fechaReserva : Date
     */
    public boolean isConfirmada() { return confirmada; }

    /**
     * Método para verificar si la reserva está confirmada
     * @return : boolean
     */
    public void setConfirmada(boolean confirmada) { this.confirmada = confirmada; }
}
    


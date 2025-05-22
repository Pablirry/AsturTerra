package model;

import java.sql.Date;

public class ReservaEvento {

    private int id;
    private int usuarioId;
    private int eventoId;
    private Date fechaReserva;
    private boolean confirmada;

    public ReservaEvento(int id, int usuarioId, int eventoId, Date fechaReserva, boolean confirmada) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.eventoId = eventoId;
        this.fechaReserva = fechaReserva;
        this.confirmada = confirmada;
    }

    public int getId() { return id; }
    public int getUsuarioId() { return usuarioId; }
    public int getEventoId() { return eventoId; }
    public Date getFechaReserva() { return fechaReserva; }
    public boolean isConfirmada() { return confirmada; }
    public void setConfirmada(boolean confirmada) { this.confirmada = confirmada; }
}
    


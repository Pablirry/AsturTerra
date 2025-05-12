package model;

import java.util.Date;

public class Reserva {
    private int id;
    private int idUsuario;
    private int idRuta;
    private Date fecha;
    private boolean confirmada;

    public Reserva(int id, int idUsuario, int idRuta, Date fecha, boolean confirmada) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idRuta = idRuta;
        this.fecha = fecha;
        this.confirmada = confirmada;
    }

    public int getId() { return id; }
    public int getIdUsuario() { return idUsuario; }
    public int getIdRuta() { return idRuta; }
    public Date getFecha() { return fecha; }
    public boolean isConfirmada() { return confirmada; }
    public void setConfirmada(boolean confirmada) { this.confirmada = confirmada; }
}
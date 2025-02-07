package modelo;

import java.util.Date;

public class Reserva {

    private int id;
    private int idUsuario;
    private int idRuta;
    private Date fecha;
    
    public Reserva(int id, int idUsuario, int idRuta, Date fecha) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idRuta = idRuta;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Reserva [id=" + id + ", idUsuario=" + idUsuario + ", idRuta=" + idRuta + ", fecha=" + fecha + "]";
    }
    
}

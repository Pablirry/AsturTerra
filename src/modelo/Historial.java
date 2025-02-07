package modelo;

import java.util.Date;

public class Historial {
    
    private int id;
    private int idUsuario;
    private String accion;
    private Date fecha;
    
    public Historial(int id, int idUsuario, String accion, Date fecha) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.accion = accion;
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

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Historial [id=" + id + ", idUsuario=" + idUsuario + ", accion=" + accion + ", fecha=" + fecha + "]";
    }
    
}

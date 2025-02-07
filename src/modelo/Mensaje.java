package modelo;

import java.util.Date;

public class Mensaje {

    private int id;
    private int idUsuario;
    private String mensaje;
    private String respuesta;
    private Date fecha;
    
    public Mensaje(int id, int idUsuario, String mensaje, String respuesta, Date fecha) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.mensaje = mensaje;
        this.respuesta = respuesta;
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

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Mensaje [id=" + id + ", idUsuario=" + idUsuario + ", mensaje=" + mensaje + ", respuesta=" + respuesta
                + ", fecha=" + fecha + "]";
    }
    
}

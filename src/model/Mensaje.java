package model;

import java.util.Date;

public class Mensaje {

    private int id;
    private int idUsuario;
    private String UsuarioNombre;
    private String mensaje;
    private String respuesta;
    private Date fecha;

    public Mensaje() {
    }

    public Mensaje(int id,int idUsuario, String UsuarioNombre, String mensaje, String respuesta, Date fecha) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.UsuarioNombre = UsuarioNombre;
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

 
    public String getMensaje() {
        return mensaje;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getUsuarioNombre() {
        return UsuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        UsuarioNombre = usuarioNombre;
    }

    
    @Override
    public String toString() {
        return "Mensaje [id=" + id + ", NombreUsuario=" + UsuarioNombre + ", mensaje=" + mensaje + ", respuesta=" + respuesta
                + ", fecha=" + fecha + "]";
    }


}

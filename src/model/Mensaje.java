package model;

import java.util.Date;

/**
 * @author Pablo
 */

public class Mensaje {

    private int id;
    private int idUsuario;
    private String UsuarioNombre;
    private String mensaje;
    private String respuesta;
    private Date fecha;

    /**
     * Constructor de la clase Mensaje vacio
     */

    public Mensaje() {
    }

    /**
     * Constructor de la clase Mensaje
     * 
     * @param id          : entero
     * @param idUsuario   : entero
     * @param UsuarioNombre: String
     * @param mensaje     : String
     * @param respuesta   : String
     * @param fecha       : Date
     */

    public Mensaje(int id,int idUsuario, String UsuarioNombre, String mensaje, String respuesta, Date fecha) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.UsuarioNombre = UsuarioNombre;
        this.mensaje = mensaje;
        this.respuesta = respuesta;
        this.fecha = fecha;
    }

    /**
     * Método para obtener el id del mensaje
     * 
     * @return : entero
     */

    public int getId() {
        return id;
    }

    /**
     * Método para establecer el id del mensaje
     * 
     * @param id : entero
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Método para obtener el mensaje
     * 
     * @return : String
     */

    public String getMensaje() {
        return mensaje;
    }

    /**
     * Método para obtener el id del usuario
     * 
     * @return : entero
     */

    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Método para establecer el id del usuario
     * 
     * @param idUsuario : entero
     */

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Método para establecer el mensaje
     * 
     * @param mensaje : String
     */

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * Método para obtener la respuesta
     * 
     * @return : String
     */

    public String getRespuesta() {
        return respuesta;
    }

    /**
     * Método para establecer la respuesta
     * 
     * @param respuesta : String
     */

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    /**
     * Método para obtener la fecha
     * 
     * @return : Date
     */

    public Date getFecha() {
        return fecha;
    }

    /**
     * Método para establecer la fecha
     * 
     * @param fecha : Date
     */

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * Método para obtener el nombre del usuario
     * 
     * @return : String
     */

    public String getUsuarioNombre() {
        return UsuarioNombre;
    }

    /**
     * Método para establecer el nombre del usuario
     * 
     * @param usuarioNombre : String
     */

    public void setUsuarioNombre(String usuarioNombre) {
        UsuarioNombre = usuarioNombre;
    }

    /**
     * Método para mostrar el mensaje
     * 
     * @return : String
     */

    
    @Override
    public String toString() {
        return "Mensaje [id=" + id + ", NombreUsuario=" + UsuarioNombre + ", mensaje=" + mensaje + ", respuesta=" + respuesta
                + ", fecha=" + fecha + "]";
    }


}

package model;

public class ValoracionRestaurante {

    private int id;
    private int idUsuario;
    private int idRestaurante;
    private int puntuacion;
    private String comentario;

    /**
     * Constructor de la clase ValoracionRestaurante vacio
     */

    public ValoracionRestaurante() {
        
    }

    /**
     * Constructor de la clase ValoracionRestaurante
     * 
     * @param id          : entero
     * @param idUsuario   : entero
     * @param idRestaurante: entero
     * @param puntuacion  : entero
     * @param comentario   : String
     */

    public ValoracionRestaurante(int id, int idUsuario, int idRestaurante, int puntuacion, String comentario) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idRestaurante = idRestaurante;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
    }

    /**
     * Método para obtener el id de la valoración
     * 
     * @return : entero
     */

    public int getId() {
        return id;
    }

    /**
     * Método para establecer el id de la valoración
     * 
     * @param id : entero
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Métodos para obtener el id del usuario
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
     * Métodos para obtener el id del restaurante
     * 
     * @return : entero
     */

    public int getIdRestaurante() {
        return idRestaurante;
    }

    /**
     * Método para establecer el id del restaurante
     * 
     * @param idRestaurante : entero
     */

    public void setIdRestaurante(int idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    /**
     * Métodos para obtener la puntuacion de la valoración
     * 
     * @return : entero
     */

    public int getPuntuacion() {
        return puntuacion;
    }

    /**
     * Método para establecer la puntuación de la valoración
     * 
     * @param puntuacion : entero
     */

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    /**
     * Métodos para obtener el comentario de la valoración
     * 
     * @return : String
     */

    public String getComentario() {
        return comentario;
    }

    /**
     * Método para establecer el comentario de la valoración
     * 
     * @param comentario : String
     */

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    /**
     * Método para obtener la cadena de texto de la valoración
     * 
     * @return : String
     */

    @Override
    public String toString() {
        return "ValoracionRestaurante [id=" + id + ", idUsuario=" + idUsuario + ", idRestaurante=" + idRestaurante + ", puntuacion=" + puntuacion + ", comentario=" + comentario + "]";
    }
}

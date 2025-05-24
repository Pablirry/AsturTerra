package model;

public class ValoracionEvento {

    private int id;
    private int idUsuario;
    private int idEvento;
    private int puntuacion;
    private String comentario;

    /**
     * Constructor de la clase ValoracionEvento
     * 
     * @param id          : entero
     * @param idUsuario   : entero
     * @param idEvento    : entero
     * @param puntuacion: entero
     * @param comentario: String
     */

    public ValoracionEvento(int id, int idUsuario, int idEvento, int puntuacion, String comentario) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idEvento = idEvento;
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
     * Métodos para obtener el id del evento
     * 
     * @return : entero
     */

    public int getIdEvento() {
        return idEvento;
    }

    /**
     * Método para establecer el id del evento
     * 
     * @param idEvento : entero
     */

    public void setIdEvento(int idEvento) {
        this.idEvento = idEvento;
    }

    /**
     * Métodos para obtener la puntuación de la valoración
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

}

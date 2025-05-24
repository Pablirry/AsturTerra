package model;

public class ValoracionRuta {

    private int id;
    private int idUsuario;
    private int idRuta;
    private int puntuacion;
    private String comentario;

    /**
     * Constructor de la clase ValoracionRuta
     * @param id
     * @param idUsuario
     * @param idRuta
     * @param puntuacion
     * @param comentario
     */
    public ValoracionRuta(int id, int idUsuario, int idRuta, int puntuacion, String comentario) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idRuta = idRuta;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
    }

    /**
     * Método para obtener el id de la valoración
     * @return : entero
     */

    public int getId() {
        return id;
    }

    /**
     * Método para establecer el id de la valoración
     * @param id : entero
     */

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Métodos para obtener el id del usuario
     * @return : entero
     */

    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Método para establecer el id del usuario
     * @param idUsuario : entero
     */

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Métodos para obtener el id de la ruta
     * @return : entero
     */

    public int getIdRuta() {
        return idRuta;
    }

    /**
     * Método para establecer el id de la ruta
     * @param idRuta : entero
     */

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
    }

    /**
     * Métodos para obtener la puntuación
     * @return : entero
     */

    public int getPuntuacion() {
        return puntuacion;
    }

    /**
     * Método para establecer la puntuación
     * @param puntuacion : entero
     */

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    /**
     * Métodos para obtener el comentario
     * @return : String
     */

    public String getComentario() {
        return comentario;
    }

    /**
     * Método para establecer el comentario
     * @param comentario : String
     */

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    /**
     * Método para obtener la representación en cadena de la valoración
     * @return : String
     */

    @Override
    public String toString() {
        return "ValoracionRuta [id=" + id + ", idUsuario=" + idUsuario + ", idRuta=" + idRuta + ", puntuacion=" + puntuacion + ", comentario=" + comentario + "]";
    }
}

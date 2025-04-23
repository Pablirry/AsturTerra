package model;

public class ValoracionRestaurante {

    private int id;
    private int idUsuario;
    private int idRestaurante;
    private int puntuacion;
    private String comentario;

    public ValoracionRestaurante() {
        
    }

    public ValoracionRestaurante(int id, int idUsuario, int idRestaurante, int puntuacion, String comentario) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idRestaurante = idRestaurante;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
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

    public int getIdRestaurante() {
        return idRestaurante;
    }

    public void setIdRestaurante(int idRestaurante) {
        this.idRestaurante = idRestaurante;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    @Override
    public String toString() {
        return "ValoracionRestaurante [id=" + id + ", idUsuario=" + idUsuario + ", idRestaurante=" + idRestaurante + ", puntuacion=" + puntuacion + ", comentario=" + comentario + "]";
    }
}

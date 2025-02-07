package modelo;

public class Valoracion {

    private int id;
    private int idUsuario;
    private int idRuta;
    private int puntuacion;
    private String comentario;
    
    public Valoracion(int id, int idUsuario, int idRuta, int puntuacion, String comentario) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idRuta = idRuta;
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

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
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
        return "Valoracion [id=" + id + ", idUsuario=" + idUsuario + ", idRuta=" + idRuta + ", puntuacion=" + puntuacion
                + ", comentario=" + comentario + "]";
    }
    
}

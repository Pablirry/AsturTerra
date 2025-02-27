package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import config.ConexionDB;
import model.Valoracion;

public class ValoracionDAO {

    public boolean registrarValoracion(Valoracion valoracion) {
        try (Connection con = ConexionDB.getConection()) {
            String sql = "INSERT INTO valoraciones (id_usuario, id_restaurante, puntuacion, comentario) VALUES (?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, valoracion.getIdUsuario());
            ps.setInt(2, valoracion.getIdRestaurante());
            ps.setInt(3, valoracion.getPuntuacion());
            ps.setString(4, valoracion.getComentario());

            return ps.executeUpdate() > 0;

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al registrar valoración: " + e.getMessage());
            return false;
        }
    }

    public boolean valorarRuta(int idUsuario, int idRuta, int puntuacion, String comentario) {
        try (Connection con = ConexionDB.getConection()) {
            String sql = "INSERT INTO valoraciones (id_usuario, id_ruta, puntuacion, comentario) VALUES (?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idRuta);
            ps.setInt(3, puntuacion);
            ps.setString(4, comentario);

            return ps.executeUpdate() > 0;

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al valorar ruta: " + e.getMessage());
            return false;
        }
    }
}

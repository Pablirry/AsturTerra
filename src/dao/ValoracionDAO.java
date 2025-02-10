package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import config.ConexionDB;

public class ValoracionDAO {

    public void valorarRuta(int idUsuario, int idRuta, int puntuacion, String comentario) throws ClassNotFoundException {
        Connection con = ConexionDB.getConection();
        String sql = "INSERT INTO valoraciones (id_usuario, id_ruta, puntuacion, comentario) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, idRuta);
            ps.setInt(3, puntuacion);
            ps.setString(4, comentario);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}

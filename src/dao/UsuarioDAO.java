package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import config.ConexionDB;
import model.Usuario;

public class UsuarioDAO {

    public Usuario iniciarSesion(String correo, String contrasena) throws ClassNotFoundException {
        try (Connection con = ConexionDB.getConection()) {
            String sql = "SELECT * FROM usuarios WHERE correo = ? AND contrasena = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, correo);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();
    
            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("contrasena"),
                        rs.getString("tipo"),
                        rs.getBytes("imagen_perfil")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error iniciarSesion: " + e.getMessage());
        }
        return null;
    }

    public boolean registrarUsuario(Usuario usuario) throws ClassNotFoundException {
        try (Connection con = ConexionDB.getConection()) {
            String sql = "INSERT INTO usuarios (nombre, correo, contrasena, tipo, imagen_perfil) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getContrasena());
            ps.setString(4, usuario.getTipo());
            ps.setBytes(5, usuario.getImagenPerfil());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error registrarUsuario: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarUsuario(Usuario usuario) throws ClassNotFoundException {
        try (Connection con = ConexionDB.getConection()) {
            String sql = "UPDATE usuarios SET nombre = ?, correo = ?, contrasena = ?, tipo = ?, imagen_perfil = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getCorreo());
            ps.setString(3, usuario.getContrasena());
            ps.setString(4, usuario.getTipo());
            ps.setBytes(5, usuario.getImagenPerfil());
            ps.setInt(6, usuario.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error actualizarUsuario: " + e.getMessage());
            return false;
        }
    }

    public Usuario obtenerUsuarioPorId(int id) throws ClassNotFoundException {
        try (Connection con = ConexionDB.getConection()) {
            String sql = "SELECT * FROM usuarios WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("contrasena"),
                        rs.getString("tipo"),
                        rs.getBytes("imagen_perfil")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error obtenerUsuarioPorId: " + e.getMessage());
        }
        return null;
    }
}
package services;

import dao.*;
import model.*;
import javax.swing.*;
import java.util.*;
import java.sql.*;
import java.util.Date;

public class TurismoService {
    private static TurismoService instance;
    private final UsuarioDAO usuarioDAO;
    private final RutaDAO rutaDAO;
    private final ValoracionDAO valoracionDAO;
    private final RestauranteDAO restauranteDAO;
    private final ReservarDAO reservaDAO;
    private final HistorialDAO historialDAO;
    private final MensajeDAO mensajeDAO;

    private TurismoService() {
        usuarioDAO = new UsuarioDAO();
        rutaDAO = new RutaDAO();
        valoracionDAO = new ValoracionDAO();
        restauranteDAO = new RestauranteDAO();
        reservaDAO = new ReservarDAO();
        historialDAO = new HistorialDAO();
        mensajeDAO = new MensajeDAO();
    }

    public static TurismoService getInstance() {
        if (instance == null) {
            instance = new TurismoService();
        }
        return instance;
    }

    // Usuario
    public Usuario iniciarSesion(String correo, String contrasena) throws ClassNotFoundException {
        return usuarioDAO.iniciarSesion(correo, contrasena);
    }

    public boolean registrarUsuario(Usuario usuario) throws ClassNotFoundException {
        return usuarioDAO.registrarUsuario(usuario);
    }

    public boolean actualizarUsuario(Usuario usuario) throws ClassNotFoundException {
        return usuarioDAO.actualizarUsuario(usuario);
    }

    public Usuario obtenerUsuarioPorId(int id) throws ClassNotFoundException {
        return usuarioDAO.obtenerUsuarioPorId(id);
    }

    // Rutas
    public List<Ruta> obtenerRutas() throws ClassNotFoundException {
        return rutaDAO.listarRutas();
    }

    public boolean agregarRuta(Ruta ruta) throws ClassNotFoundException {
        return rutaDAO.agregarRuta(ruta);
    }

    public boolean eliminarRuta(int idRuta) throws ClassNotFoundException {
        return rutaDAO.eliminarRuta(idRuta);
    }

    public Ruta obtenerRutaPorId(int id) throws ClassNotFoundException {
        return rutaDAO.obtenerRutaPorId(id);
    }

    // Valoraciones
    public boolean valorarRuta(ValoracionRuta valoracion) throws ClassNotFoundException {
        return valoracionDAO.registrarValoracionRuta(valoracion);
    }

    public boolean valorarRestaurante(ValoracionRestaurante valoracion) throws ClassNotFoundException {
        return valoracionDAO.registrarValoracionRestaurante(valoracion);
    }

    public List<ValoracionRuta> obtenerValoracionesRuta(int idRuta) throws ClassNotFoundException {
        return valoracionDAO.obtenerValoracionesRuta(idRuta);
    }

    public List<ValoracionRestaurante> obtenerValoracionesRestaurante(int idRestaurante) throws ClassNotFoundException {
        return valoracionDAO.obtenerValoracionesRestaurante(idRestaurante);
    }

    public double obtenerValoracionMediaRuta(int idRuta) throws ClassNotFoundException {
        return valoracionDAO.obtenerValoracionMediaRuta(idRuta);
    }

    public double obtenerValoracionMediaRestaurante(int idRestaurante) throws ClassNotFoundException {
        return valoracionDAO.obtenerValoracionMediaRestaurante(idRestaurante);
    }

    // Restaurantes
    public List<Restaurante> obtenerRestaurantes() throws ClassNotFoundException {
        return restauranteDAO.listarRestaurantes();
    }

    public boolean agregarRestaurante(Restaurante restaurante) throws ClassNotFoundException {
        return restauranteDAO.agregarRestaurante(restaurante);
    }

    public boolean eliminarRestaurante(int idRestaurante) throws ClassNotFoundException {
        return restauranteDAO.eliminarRestaurante(idRestaurante);
    }

    public Restaurante obtenerRestaurantePorId(int id) throws ClassNotFoundException {
        return restauranteDAO.obtenerRestaurantePorId(id);
    }

    public Restaurante obtenerRestaurantePorNombre(String nombreRestaurante) throws ClassNotFoundException {
        return restauranteDAO.obtenerRestaurantePorNombre(nombreRestaurante);
    }

    // Reservas
    public boolean reservarRuta(int idUsuario, int idRuta, Date fecha) throws ClassNotFoundException {
        return reservaDAO.reservarRuta(idUsuario, idRuta, fecha);
    }

    public List<Reserva> obtenerReservasUsuario(int idUsuario) throws ClassNotFoundException {
        return reservaDAO.obtenerReservasUsuario(idUsuario);
    }

    public boolean cancelarReserva(int idReserva) throws ClassNotFoundException {
        return reservaDAO.cancelarReserva(idReserva);
    }

    // Historial
    public boolean registrarActividad(Historial historial) throws ClassNotFoundException {
        return historialDAO.registrarActividad(historial);
    }

    public List<Historial> obtenerHistorialUsuario(int idUsuario) throws ClassNotFoundException {
        return historialDAO.obtenerHistorialUsuario(idUsuario);
    }

    // Mensajes
    public boolean enviarMensaje(int idUsuario, Mensaje mensaje) throws ClassNotFoundException {
        return mensajeDAO.enviarMensaje(idUsuario, mensaje);
    }

    public List<Mensaje> obtenerMensajesUsuario(int idUsuario) throws ClassNotFoundException {
        return mensajeDAO.obtenerMensajesUsuario(idUsuario);
    }

    public List<Mensaje> obtenerConversacion(int idEmisor, int idReceptor) throws ClassNotFoundException {
        return mensajeDAO.obtenerConversacion(idEmisor, idReceptor);
    }

    public boolean marcarMensajeComoLeido(int idMensaje) throws ClassNotFoundException {
        return mensajeDAO.marcarMensajeComoLeido(idMensaje);
    }
    
}
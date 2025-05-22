package services;

import dao.RutaDAO;
import dao.UsuarioDAO;
import dao.ValoracionDAO;
import dao.EventoDAO;
import dao.MensajeDAO;
import dao.ReservarDAO;
import dao.RestauranteDAO;
import model.Ruta;
import model.Usuario;
import model.ValoracionEvento;
import model.ValoracionRestaurante;
import model.ValoracionRuta;
import model.Evento;
import model.Mensaje;
import model.Reserva;
import model.ReservaEvento;
import model.Restaurante;
import java.util.List;

public class TurismoService {
    private static TurismoService instance;
    private final RutaDAO rutaDAO;
    private final RestauranteDAO restauranteDAO;
    private final ReservarDAO reservarDAO;
    private final UsuarioDAO usuarioDAO;
    private final ValoracionDAO valoracionDAO;
    private final MensajeDAO mensajeDAO;
    private final EventoDAO eventoDAO;

    private TurismoService() {
        rutaDAO = new RutaDAO();
        restauranteDAO = new RestauranteDAO();
        this.reservarDAO = new ReservarDAO();
        this.usuarioDAO = new UsuarioDAO();
        this.mensajeDAO = new MensajeDAO();
        this.valoracionDAO = new ValoracionDAO();
        this.eventoDAO = new EventoDAO();
    }

    public static TurismoService getInstance() {
        if (instance == null) {
            instance = new TurismoService();
        }
        return instance;
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

    public Ruta obtenerRutaPorId(int idRuta) throws ClassNotFoundException {
        return rutaDAO.obtenerRutaPorId(idRuta);
    }

    public boolean actualizarRuta(Ruta ruta) throws ClassNotFoundException {
        return rutaDAO.actualizarRuta(ruta);
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

    public boolean actualizarRestaurante(Restaurante restaurante) throws ClassNotFoundException {
        return restauranteDAO.actualizarRestaurante(restaurante);
    }

    // Reservas
    public List<Reserva> obtenerReservas() throws ClassNotFoundException {
        return reservarDAO.listarReservas();
    }

    public boolean eliminarReserva(int idReserva) throws ClassNotFoundException {
        return reservarDAO.eliminarReserva(idReserva);
    }

    public boolean confirmarReserva(int idReserva) throws ClassNotFoundException {
        return reservarDAO.confirmarReserva(idReserva);
    }

    public List<ReservaEvento> obtenerReservasEvento() throws ClassNotFoundException {
        return reservarDAO.listarReservasEvento();
    }

    public boolean agregarReservaEvento(ReservaEvento reserva) throws Exception {
        return reservarDAO.agregarReservaEvento(reserva);
    }

    public boolean eliminarReservaEvento(int idReserva) throws ClassNotFoundException {
        return reservarDAO.eliminarReservaEvento(idReserva);
    }

    public boolean confirmarReservaEvento(int idReserva) throws ClassNotFoundException {
        return reservarDAO.confirmarReservaEvento(idReserva);
    }

    // Usuario
    public Usuario obtenerUsuarioPorId(int idUsuario) throws ClassNotFoundException {
        return usuarioDAO.obtenerUsuarioPorId(idUsuario);
    }

    public List<Mensaje> obtenerMensajesSoporte() {
        try {
            return mensajeDAO.obtenerTodosMensajes();
        } catch (Exception e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }

    public boolean responderMensajeSoporte(int idMensaje, String respuesta) {
        try {
            return mensajeDAO.responderMensaje(idMensaje, respuesta);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Valoraciones
    public List<ValoracionRestaurante> obtenerValoracionesRestaurante() throws ClassNotFoundException {
        return valoracionDAO.obtenerTodasValoracionesRestaurante();
    }

    public List<ValoracionRuta> obtenerValoracionesRuta() throws ClassNotFoundException {
        return valoracionDAO.obtenerTodasValoracionesRuta();
    }

    public boolean eliminarValoracionRestaurante(int id) throws ClassNotFoundException {
        return valoracionDAO.eliminarValoracionRestaurante(id);
    }

    public boolean eliminarValoracionRuta(int id) throws ClassNotFoundException {
        return valoracionDAO.eliminarValoracionRuta(id);
    }

     public List<ValoracionEvento> obtenerValoracionesEvento() {
        try {
            return valoracionDAO.obtenerTodasValoracionesEvento();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return java.util.Collections.emptyList();
        }
    }

    public boolean eliminarValoracionEvento(int id) throws ClassNotFoundException {
        return valoracionDAO.eliminarValoracionEvento(id);
    }

    // Eventos
    public List<Evento> obtenerEventos() throws ClassNotFoundException {
        return eventoDAO.listarEventos();
    }

    public boolean agregarEvento(Evento evento) throws ClassNotFoundException {
        return eventoDAO.agregarEvento(evento);
    }

    public boolean eliminarEvento(int idEvento) throws ClassNotFoundException {
        return eventoDAO.eliminarEvento(idEvento);
    }

    public boolean actualizarEvento(Evento evento) throws ClassNotFoundException {
        return eventoDAO.actualizarEvento(evento);
    }

}
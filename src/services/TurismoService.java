package services;

import dao.RutaDAO;
import dao.UsuarioDAO;
import dao.ReservarDAO;
import dao.RestauranteDAO;
import model.Ruta;
import model.Usuario;
import model.Reserva;
import model.Restaurante;
import java.util.List;

public class TurismoService {
    private static TurismoService instance;
    private final RutaDAO rutaDAO;
    private final RestauranteDAO restauranteDAO;
    private final ReservarDAO reservarDAO;
    private final UsuarioDAO usuarioDAO;

    private TurismoService() {
        rutaDAO = new RutaDAO();
        restauranteDAO = new RestauranteDAO();
        this.reservarDAO = new ReservarDAO();
        this.usuarioDAO = new UsuarioDAO();
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

    //Reservas
    public List<Reserva> obtenerReservas() throws ClassNotFoundException {
        return reservarDAO.listarReservas();
    }
    public boolean eliminarReserva(int idReserva) throws ClassNotFoundException {
        return reservarDAO.eliminarReserva(idReserva);
    }
    public boolean confirmarReserva(int idReserva) throws ClassNotFoundException {
        return reservarDAO.confirmarReserva(idReserva);
    }

    //Usuario
    public Usuario obtenerUsuarioPorId(int idUsuario) throws ClassNotFoundException {
        return usuarioDAO.obtenerUsuarioPorId(idUsuario);
    }
}
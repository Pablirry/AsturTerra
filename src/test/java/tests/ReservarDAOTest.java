package test.java.tests;

import dao.ReservarDAO;
import model.Reserva;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;


import java.util.List;

class ReservarDAOTest {
    private ReservarDAO reservarDAO;

    @BeforeEach
    void setUp() {
        reservarDAO = new ReservarDAO();
    }

    @Test
    void testListarConfirmarEliminarReserva() throws Exception {
        // Este test asume que ya existe al menos una reserva en la BD
        List<Reserva> reservas = reservarDAO.listarReservas();
        if (reservas.isEmpty()) {
            // Si no hay reservas, no se puede probar confirmar/eliminar
            return;
        }
        Reserva reserva = reservas.get(0);

        boolean confirmada = reservarDAO.confirmarReserva(reserva.getId());
        assertTrue(confirmada);

        boolean eliminada = reservarDAO.eliminarReserva(reserva.getId());
        assertTrue(eliminada);
    }
}
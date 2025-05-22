package test.java.tests;


import dao.EventoDAO;
import model.Evento;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class EventoDAOTest {
    private EventoDAO eventoDAO;

    @BeforeEach
    void setUp() {
        eventoDAO = new EventoDAO();
    }

    @Test
    void testAgregarListarActualizarEliminarEvento() throws Exception {
        Evento evento = new Evento(0, "Evento Test", "Desc", "Ubicacion", "Tipo", 20.0, null);
        boolean added = eventoDAO.agregarEvento(evento);
        assertTrue(added);

        List<Evento> eventos = eventoDAO.listarEventos();
        Evento eventoBD = eventos.stream()
            .filter(e -> "Evento Test".equals(e.getNombre()))
            .findFirst()
            .orElse(null);

        assertNotNull(eventoBD);

        // Actualizar
        eventoBD.setNombre("Evento Modificado");
        boolean updated = eventoDAO.actualizarEvento(eventoBD);
        assertTrue(updated);

        // Eliminar
        boolean deleted = eventoDAO.eliminarEvento(eventoBD.getId());
        assertTrue(deleted);
    }
}
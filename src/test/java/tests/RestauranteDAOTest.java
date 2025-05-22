package test.java.tests;

import dao.RestauranteDAO;
import model.Restaurante;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class RestauranteDAOTest {
    private RestauranteDAO restauranteDAO;

    @BeforeEach
    void setUp() {
        restauranteDAO = new RestauranteDAO();
    }

    @Test
    void testAgregarListarActualizarEliminarRestaurante() throws Exception {
        Restaurante restaurante = new Restaurante(0, "Restaurante Test", "Ubicacion Test", null, "Especialidad Test", "Descripcion Test");
        boolean added = restauranteDAO.agregarRestaurante(restaurante);
        assertTrue(added);

        List<Restaurante> restaurantes = restauranteDAO.listarRestaurantes();
        Restaurante restBD = restaurantes.stream()
            .filter(r -> "Restaurante Test".equals(r.getNombre()))
            .findFirst()
            .orElse(null);

        assertNotNull(restBD);

        // Actualizar
        restBD.setNombre("Restaurante Modificado");
        boolean updated = restauranteDAO.actualizarRestaurante(restBD);
        assertTrue(updated);
        Restaurante restActualizado = restauranteDAO.obtenerRestaurantePorId(restBD.getId());
        assertEquals("Restaurante Modificado", restActualizado.getNombre());

        // Eliminar
        boolean deleted = restauranteDAO.eliminarRestaurante(restBD.getId());
        assertTrue(deleted);
        Restaurante restEliminado = restauranteDAO.obtenerRestaurantePorId(restBD.getId());
        assertNull(restEliminado);
    }
}
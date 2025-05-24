package test.java.tests;

import dao.RutaDAO;

import model.Ruta;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class RutaDAOTest {
    private RutaDAO rutaDAO;

    @BeforeEach
    void setUp() {
        rutaDAO = new RutaDAO();
    }

    @Test
    void testAgregarYListarYEliminarRuta() throws Exception {
        Ruta ruta = new Ruta(0, "Ruta Test", "Descripción test", null, 10.0, 2);
        boolean added = rutaDAO.agregarRuta(ruta);
        assertTrue(added);

        List<Ruta> rutas = rutaDAO.listarRutas();
        Ruta rutaBD = rutas.stream()
            .filter(r -> "Ruta Test".equals(r.getNombre()))
            .findFirst()
            .orElse(null);

        assertNotNull(rutaBD);
        assertEquals("Ruta Test", rutaBD.getNombre());

        // Actualizar
        rutaBD.setNombre("Ruta Modificada");
        boolean updated = rutaDAO.actualizarRuta(rutaBD);
        assertTrue(updated);
        Ruta rutaActualizada = rutaDAO.obtenerRutaPorId(rutaBD.getId());
        assertEquals("Ruta Modificada", rutaActualizada.getNombre());

        // Eliminar
        boolean deleted = rutaDAO.eliminarRuta(rutaBD.getId());
        assertTrue(deleted);
        Ruta rutaEliminada = rutaDAO.obtenerRutaPorId(rutaBD.getId());
        assertNull(rutaEliminada);
    }
}
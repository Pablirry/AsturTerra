package test.java.tests;

import dao.ValoracionDAO;
import model.ValoracionRuta;
import model.ValoracionRestaurante;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class ValoracionDAOTest {
    private ValoracionDAO valoracionDAO;

    @BeforeEach
    void setUp() {
        valoracionDAO = new ValoracionDAO();
    }

    @Test
    void testRegistrarObtenerEliminarValoracionRuta() throws Exception {
        ValoracionRuta valoracion = new ValoracionRuta(0, 1, 1, 5, "Muy buena ruta");
        boolean registrado = valoracionDAO.registrarValoracionRuta(valoracion);
        assertTrue(registrado);

        List<ValoracionRuta> valoraciones = valoracionDAO.obtenerTodasValoracionesRuta();
        ValoracionRuta v = valoraciones.stream()
            .filter(val -> "Muy buena ruta".equals(val.getComentario()))
            .findFirst()
            .orElse(null);

        assertNotNull(v);

        boolean eliminado = valoracionDAO.eliminarValoracionRuta(v.getId());
        assertTrue(eliminado);
    }

    @Test
    void testRegistrarObtenerEliminarValoracionRestaurante() throws Exception {
        ValoracionRestaurante valoracion = new ValoracionRestaurante(0, 1, 1, 4, "Buen restaurante");
        boolean registrado = valoracionDAO.registrarValoracionRestaurante(valoracion);
        assertTrue(registrado);

        List<ValoracionRestaurante> valoraciones = valoracionDAO.obtenerTodasValoracionesRestaurante();
        ValoracionRestaurante v = valoraciones.stream()
            .filter(val -> "Buen restaurante".equals(val.getComentario()))
            .findFirst()
            .orElse(null);

        assertNotNull(v);

        boolean eliminado = valoracionDAO.eliminarValoracionRestaurante(v.getId());
        assertTrue(eliminado);
    }
}
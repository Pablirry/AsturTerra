package test.java.tests;

import services.TurismoService;

import model.Ruta;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class TurismoServiceTest {
    private TurismoService service;

    @BeforeEach
    void setUp() {
        service = TurismoService.getInstance();
    }

    @Test
    void testObtenerRutas() throws Exception {
        var rutas = service.obtenerRutas();
        assertNotNull(rutas);
    }

    @Test
    void testAgregarYEliminarRuta() throws Exception {
        Ruta ruta = new Ruta(0, "Ruta JUnit", "Test", null, 5.0, 1);
        boolean added = service.agregarRuta(ruta);
        assertTrue(added);

        var rutas = service.obtenerRutas();
        Ruta rutaTest = rutas.stream().filter(r -> "Ruta JUnit".equals(r.getNombre())).findFirst().orElse(null);
        assertNotNull(rutaTest);

        boolean deleted = service.eliminarRuta(rutaTest.getId());
        assertTrue(deleted);
    }
}
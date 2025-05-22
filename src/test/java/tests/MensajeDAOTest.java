package test.java.tests;

import dao.MensajeDAO;
import model.Mensaje;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

class MensajeDAOTest {
    private MensajeDAO mensajeDAO;

    @BeforeEach
    void setUp() {
        mensajeDAO = new MensajeDAO();
    }

    @Test
    void testEnviarObtenerResponderMensaje() throws Exception {
        Mensaje mensaje = new Mensaje(0, 1, "TestUser", "Mensaje de prueba", "", new Date());
        boolean enviado = mensajeDAO.enviarMensaje(1, mensaje);
        assertTrue(enviado);

        List<Mensaje> mensajes = mensajeDAO.obtenerMensajesUsuario(1);
        Mensaje m = mensajes.stream()
            .filter(msg -> "Mensaje de prueba".equals(msg.getMensaje()))
            .findFirst()
            .orElse(null);

        assertNotNull(m);

        boolean respondido = mensajeDAO.responderMensaje(m.getId(), "Respuesta de prueba");
        assertTrue(respondido);
    }
}
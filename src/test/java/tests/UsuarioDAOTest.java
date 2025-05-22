package test.java.tests;

import dao.UsuarioDAO;
import model.Usuario;
import org.junit.jupiter.api.*;
import utils.PasswordUtils;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDAOTest {
    private UsuarioDAO usuarioDAO;

    @BeforeEach
    void setUp() throws Exception {
        usuarioDAO = new UsuarioDAO();
        usuarioDAO.eliminarUsuarioPorCorreo("testuser@email.com");
        Usuario usuario = new Usuario(0, "TestUser", "testuser@email.com", PasswordUtils.hash("Test1234!"), "cliente", null);
        usuarioDAO.registrarUsuario(usuario);
    }

    @Test
    void testRegistrarYObtenerUsuario() throws Exception {
        Usuario usuarioBD = usuarioDAO.obtenerUsuarioPorCorreo("testuser@email.com");
        assertNotNull(usuarioBD);
        assertEquals("TestUser", usuarioBD.getNombre());
    }

    @Test
    void testIniciarSesion() throws Exception {
        Usuario usuario = usuarioDAO.iniciarSesion("testuser@email.com", "Test1234!");
        assertNotNull(usuario);
        assertEquals("TestUser", usuario.getNombre());
    }

    @Test
    void testActualizarUsuario() throws Exception {
        Usuario usuario = usuarioDAO.obtenerUsuarioPorCorreo("testuser@email.com");
        usuario.setNombre("NuevoNombre");
        boolean actualizado = usuarioDAO.actualizarUsuario(usuario);
        assertTrue(actualizado);
        Usuario usuarioActualizado = usuarioDAO.obtenerUsuarioPorCorreo("testuser@email.com");
        assertEquals("NuevoNombre", usuarioActualizado.getNombre());
    }

    @Test
    void testObtenerUsuarioPorId() throws Exception {
        Usuario usuario = usuarioDAO.obtenerUsuarioPorCorreo("testuser@email.com");
        Usuario usuarioPorId = usuarioDAO.obtenerUsuarioPorId(usuario.getId());
        assertNotNull(usuarioPorId);
        assertEquals(usuario.getCorreo(), usuarioPorId.getCorreo());
    }

    @Test
    void testEliminarUsuarioPorCorreo() throws Exception {
        boolean eliminado = usuarioDAO.eliminarUsuarioPorCorreo("testuser@email.com");
        assertTrue(eliminado);
        Usuario usuario = usuarioDAO.obtenerUsuarioPorCorreo("testuser@email.com");
        assertNull(usuario);
    }
}
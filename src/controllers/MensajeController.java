package controllers;

import dao.MensajeDAO;
import model.Mensaje;
import views.VistaChat;
import javax.swing.*;
import java.util.List;

public class MensajeController {
    private MensajeDAO mensajeDAO;
    private VistaChat vistaChat;

    public MensajeController(VistaChat vistaChat) {
        this.mensajeDAO = new MensajeDAO();
        this.vistaChat = vistaChat;
        cargarMensajes();
        agregarEventos();
    }

    private void agregarEventos() {
        vistaChat.getBtnEnviar().addActionListener(e -> enviarMensaje());
    }

    private void enviarMensaje() {
        int idUsuario = 1; // Usuario de ejemplo
        String mensaje = vistaChat.getTxtMensaje().getText();

        if (!mensaje.isEmpty()) {
            boolean exito = mensajeDAO.enviarMensaje(idUsuario, mensaje);
            if (exito) {
                vistaChat.getTxtMensajes().append("Tú: " + mensaje + "\n");
                vistaChat.getTxtMensaje().setText("");
            } else {
                JOptionPane.showMessageDialog(vistaChat, "Error al enviar mensaje.");
            }
        } else {
            JOptionPane.showMessageDialog(vistaChat, "El mensaje no puede estar vacío.");
        }
    }

    private void cargarMensajes() {
        int idUsuario = 1;
        List<Mensaje> mensajes = mensajeDAO.obtenerMensajes(idUsuario);
        for (Mensaje mensaje : mensajes) {
            vistaChat.getTxtMensajes().append("Tú: " + mensaje.getMensaje() + "\n");
        }
    }
}


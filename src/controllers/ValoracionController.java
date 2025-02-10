package controllers;

import dao.ValoracionDAO;
import model.Valoracion;
import views.VistaValoraciones;
import javax.swing.*;

public class ValoracionController {
    private ValoracionDAO valoracionDAO;
    private VistaValoraciones vistaValoraciones;

    public ValoracionController(VistaValoraciones vistaValoraciones) {
        this.valoracionDAO = new ValoracionDAO();
        this.vistaValoraciones = vistaValoraciones;
        agregarEventos();
    }

    private void agregarEventos() {
        vistaValoraciones.getBtnEnviarValoracion().addActionListener(e -> valorarRuta());
    }

    private void valorarRuta() {
        try {
            int idUsuario = 1; // Usuario de prueba
            int idRuta = vistaValoraciones.getRutaSeleccionada();
            int puntuacion = vistaValoraciones.getPuntuacionSeleccionada();
            String comentario = vistaValoraciones.getTxtComentario().getText();

            if (puntuacion < 1 || puntuacion > 5) {
                JOptionPane.showMessageDialog(vistaValoraciones, "La puntuación debe estar entre 1 y 5.");
                return;
            }

            boolean exito = valoracionDAO.valorarRuta(idUsuario, idRuta, puntuacion, comentario);
            if (exito) {
                JOptionPane.showMessageDialog(vistaValoraciones, "Valoración enviada con éxito.");
            } else {
                JOptionPane.showMessageDialog(vistaValoraciones, "Error al enviar la valoración.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vistaValoraciones, "Error al valorar ruta: " + e.getMessage());
        }
    }
}




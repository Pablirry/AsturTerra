package controllers;

import dao.ValoracionDAO;
import model.ValoracionRuta;
import views.ValorarRuta;

import javax.swing.*;

public class ValorarRutaController {
    private ValorarRuta valorarRutaVista;
    private String ruta;
    private ValoracionDAO valoracionDAO;

    public ValorarRutaController(ValorarRuta valorarRutaVista, String ruta) {
        this.valorarRutaVista = valorarRutaVista;
        this.ruta = ruta;
        this.valoracionDAO = new ValoracionDAO();
    }

    public void enviarValoracion() {
        try {
            int puntuacion = (int) valorarRutaVista.getCmbPuntuacion().getSelectedItem();
            String comentario = valorarRutaVista.getTxtComentario().getText();

            if (comentario.isEmpty()) {
                JOptionPane.showMessageDialog(valorarRutaVista, "El comentario no puede estar vacío.");
                return;
            }

            ValoracionRuta valoracion = new ValoracionRuta(0, 1, 1, puntuacion, comentario); // Cambiar por el ID del usuario y de la ruta
            boolean exito = valoracionDAO.registrarValoracionRuta(valoracion);

            if (exito) {
                JOptionPane.showMessageDialog(valorarRutaVista, "Valoración enviada con éxito.");
                valorarRutaVista.dispose();
            } else {
                JOptionPane.showMessageDialog(valorarRutaVista, "Error al enviar la valoración.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(valorarRutaVista, "Error al enviar la valoración: " + e.getMessage());
        }
    }
}

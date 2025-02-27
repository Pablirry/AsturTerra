package controllers;

import dao.ValoracionDAO;
import model.Valoracion;
import views.ValorarRestaurantes;

import javax.swing.*;

public class ValorarRestauranteController {
    private ValorarRestaurantes valorarRestauranteVista;
    private String restaurante;
    private ValoracionDAO valoracionDAO;

    public ValorarRestauranteController(ValorarRestaurantes valorarRestauranteVista, String restaurante) {
        this.valorarRestauranteVista = valorarRestauranteVista;
        this.restaurante = restaurante;
        this.valoracionDAO = new ValoracionDAO();
    }

    public void enviarValoracion() {
        try {
            int puntuacion = (int) valorarRestauranteVista.getCmbPuntuacion().getSelectedItem();
            String comentario = valorarRestauranteVista.getTxtComentario().getText();

            if (comentario.isEmpty()) {
                JOptionPane.showMessageDialog(valorarRestauranteVista, "El comentario no puede estar vacío.");
                return;
            }

            Valoracion valoracion = new Valoracion(0, 1, 1, puntuacion, comentario); // Cambiar por el ID del usuario y del restaurante
            boolean exito = valoracionDAO.registrarValoracion(valoracion);

            if (exito) {
                JOptionPane.showMessageDialog(valorarRestauranteVista, "Valoración enviada con éxito.");
                valorarRestauranteVista.dispose();
            } else {
                JOptionPane.showMessageDialog(valorarRestauranteVista, "Error al enviar la valoración.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(valorarRestauranteVista, "Error al enviar la valoración: " + e.getMessage());
        }
    }
}

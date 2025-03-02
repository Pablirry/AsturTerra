package controllers;

import dao.ValoracionDAO;
import model.ValoracionRuta;
import views.VistaValoraciones;

import javax.swing.*;

public class ValorarRutaController {
    private VistaValoraciones vistaValoraciones;
    private String ruta;
    private ValoracionDAO valoracionDAO;

    public ValorarRutaController(VistaValoraciones vistaValoraciones, String ruta) {
        this.vistaValoraciones = vistaValoraciones;
        this.ruta = ruta;
        this.valoracionDAO = new ValoracionDAO();
        agregarEventos();
    }

    private void agregarEventos() {
        vistaValoraciones.getBtnEnviar().addActionListener(e -> enviarValoracion());
    }

    public void enviarValoracion() {
        try {
            int idUsuario = 1; 
            int idRuta = vistaValoraciones.getRutaSeleccionada();
            int puntuacion = vistaValoraciones.getPuntuacionSeleccionada();
            String comentario = vistaValoraciones.getTxtComentario().getText();

            if (comentario.isEmpty()) {
                JOptionPane.showMessageDialog(vistaValoraciones, "El comentario no puede estar vacío.");
                return;
            }

            ValoracionRuta valoracion = new ValoracionRuta(0, idUsuario, idRuta, puntuacion, comentario);
            boolean exito = valoracionDAO.registrarValoracionRuta(valoracion);

            if (exito) {
                JOptionPane.showMessageDialog(vistaValoraciones, "Valoración enviada con éxito.");
                vistaValoraciones.dispose();
            } else {
                JOptionPane.showMessageDialog(vistaValoraciones, "Error al enviar la valoración.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vistaValoraciones, "Error al enviar la valoración: " + e.getMessage());
        }
    }
}
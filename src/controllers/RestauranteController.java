package controllers;

import dao.RestauranteDAO;
import model.Restaurante;
import views.VistaRestaurantes;
import javax.swing.*;
import java.util.List;

public class RestauranteController {
    private RestauranteDAO restauranteDAO;
    private VistaRestaurantes vistaRestaurantes;

    public RestauranteController(VistaRestaurantes vistaRestaurantes) {
        this.restauranteDAO = new RestauranteDAO();
        this.vistaRestaurantes = vistaRestaurantes;
        cargarRestaurantes();
        agregarEventos();
    }

    private void cargarRestaurantes() {
        try {
            List<Restaurante> restaurantes = restauranteDAO.listarRestaurantes();
            DefaultListModel<String> modeloLista = new DefaultListModel<>();

            for (Restaurante r : restaurantes) {
                modeloLista.addElement(r.getNombre() + " - " + r.getValoracion() + "⭐");
            }

            vistaRestaurantes.getListaRestaurantes().setModel(modeloLista);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vistaRestaurantes, "Error al cargar restaurantes: " + e.getMessage());
        }
    }

    private void agregarEventos() {
        vistaRestaurantes.getBtnValorar().addActionListener(e -> valorarRestaurante());
    }

    private void valorarRestaurante() {
        JOptionPane.showMessageDialog(vistaRestaurantes, "Funcionalidad en desarrollo.");
    }
}


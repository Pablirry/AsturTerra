package controllers;

import dao.RestauranteDAO;
import model.Restaurante;
import views.VistaRestaurantes;
import views.AgregarRestaurante;
import views.ValorarRestaurantes;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class RestauranteController {
    private RestauranteDAO restauranteDAO;
    private VistaRestaurantes vistaRestaurantes;
    private AgregarRestaurante agregarRestauranteVista;

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
        vistaRestaurantes.getBtnAgregar().addActionListener(e -> abrirAgregarRestaurante());
    }

    public void valorarRestaurante() {
        int selectedIndex = vistaRestaurantes.getListaRestaurantes().getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(vistaRestaurantes, "Seleccione un restaurante para valorar.");
            return;
        }

        String restauranteSeleccionado = vistaRestaurantes.getListaRestaurantes().getSelectedValue();
        new ValorarRestaurantes(restauranteSeleccionado);
    }

    public void abrirAgregarRestaurante() {
        agregarRestauranteVista = new AgregarRestaurante(this);
    }

    public void agregarRestaurante() {
        try {
            String nombre = agregarRestauranteVista.getTxtNombre().getText();
            String ubicacion = agregarRestauranteVista.getTxtUbicacion().getText();
            float valoracion = (float) agregarRestauranteVista.getSpnValoracion().getValue();
            File imagenPerfil = agregarRestauranteVista.getImagenPerfil();
            byte[] imagenBytes = null;

            if (imagenPerfil != null) {
                imagenBytes = Files.readAllBytes(imagenPerfil.toPath());
            }

            Restaurante restaurante = new Restaurante(0, nombre, ubicacion, valoracion, imagenBytes);
            boolean exito = restauranteDAO.agregarRestaurante(restaurante);

            if (exito) {
                JOptionPane.showMessageDialog(agregarRestauranteVista, "Restaurante agregado con éxito.");
                cargarRestaurantes();
                agregarRestauranteVista.dispose();
            } else {
                JOptionPane.showMessageDialog(agregarRestauranteVista, "Error al agregar el restaurante.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(agregarRestauranteVista, "Error al agregar el restaurante: " + e.getMessage());
        }
    }

    public void mostrarImagenRestaurante() {
        int selectedIndex = vistaRestaurantes.getListaRestaurantes().getSelectedIndex();
        if (selectedIndex == -1) {
            vistaRestaurantes.getLblImagen().setIcon(null);
            return;
        }

        String restauranteSeleccionado = vistaRestaurantes.getListaRestaurantes().getSelectedValue();
        try {
            Restaurante restaurante = restauranteDAO.obtenerRestaurantePorNombre(restauranteSeleccionado.split(" - ")[0]);

            if (restaurante != null && restaurante.getImagen() != null) {
                ImageIcon imagenIcon = new ImageIcon(restaurante.getImagen());
                Image imagen = imagenIcon.getImage();
                if (imagen != null) {
                    Image imagenEscalada = imagen.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    vistaRestaurantes.getLblImagen().setIcon(new ImageIcon(imagenEscalada));
                } else {
                    vistaRestaurantes.getLblImagen().setIcon(null);
                }
            } else {
                vistaRestaurantes.getLblImagen().setIcon(null);
            }
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(vistaRestaurantes, "Error al obtener la imagen del restaurante: " + e.getMessage());
        }
    }
}
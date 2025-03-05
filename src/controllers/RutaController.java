package controllers;

import dao.RutaDAO;
import model.Ruta;
import views.VistaRutas;
import views.AgregarRuta;
import views.VistaValoraciones;
import views.VistaReservas;

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

public class RutaController {
    private RutaDAO rutaDAO;
    private VistaRutas vistaRutas;
    private AgregarRuta agregarRutaVista;

    public RutaController(VistaRutas vistaRutas) {
        this.rutaDAO = new RutaDAO();
        this.vistaRutas = vistaRutas;
        cargarRutas();
        agregarEventos();
    }

    private void cargarRutas() {
        try {
            List<Ruta> rutas = rutaDAO.listarRutas();
            DefaultListModel<String> modeloLista = new DefaultListModel<>();

            for (Ruta ruta : rutas) {
                modeloLista.addElement(ruta.getId() + " - " + ruta.getNombre() + " - " + ruta.getPrecio() + "€");
            }

            vistaRutas.getListaRutas().setModel(modeloLista);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vistaRutas, "Error al cargar rutas: " + e.getMessage());
        }
    }

    private void agregarEventos() {
        vistaRutas.getBtnAgregar().addActionListener(e -> abrirAgregarRuta());
        vistaRutas.getBtnEliminar().addActionListener(e -> eliminarRuta());
        vistaRutas.getBtnVerDetalles().addActionListener(e -> verDetallesRuta());
        vistaRutas.getBtnValorar().addActionListener(e -> valorarRuta());
        vistaRutas.getBtnReservar().addActionListener(e -> reservarRuta());
    }

    public void abrirAgregarRuta() {
        agregarRutaVista = new AgregarRuta(this);
    }

    public void agregarRuta() {
        try {
            String nombre = agregarRutaVista.getTxtNombre().getText();
            String descripcion = agregarRutaVista.getTxtDescripcion().getText();
            double precio = Double.parseDouble(agregarRutaVista.getTxtPrecio().getText());
            String dificultad = (String) agregarRutaVista.getCmbDificultad().getSelectedItem();
            File imagenRuta = agregarRutaVista.getImagenRuta();
            byte[] imagenBytes = null;

            if (imagenRuta != null) {
                imagenBytes = Files.readAllBytes(imagenRuta.toPath());
            }

            Ruta ruta = new Ruta(0, nombre, descripcion, imagenBytes, precio, dificultad);
            boolean exito = rutaDAO.agregarRuta(ruta);

            if (exito) {
                JOptionPane.showMessageDialog(agregarRutaVista, "Ruta agregada con éxito.");
                cargarRutas();
                agregarRutaVista.dispose();
            } else {
                JOptionPane.showMessageDialog(agregarRutaVista, "Error al agregar la ruta.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(agregarRutaVista, "Error al agregar la ruta: " + e.getMessage());
        }
    }

    public void eliminarRuta() {
        int selectedIndex = vistaRutas.getListaRutas().getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(vistaRutas, "Seleccione una ruta para eliminar.");
            return;
        }

        String rutaSeleccionada = vistaRutas.getListaRutas().getSelectedValue();
        int idRuta = Integer.parseInt(rutaSeleccionada.split(" - ")[0]);

        try {
            rutaDAO.eliminarRuta(idRuta);
            JOptionPane.showMessageDialog(vistaRutas, "Ruta eliminada con éxito.");
            cargarRutas();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vistaRutas, "Error al eliminar la ruta: " + e.getMessage());
        }
    }

    public void verDetallesRuta() {
        int selectedIndex = vistaRutas.getListaRutas().getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(vistaRutas, "Seleccione una ruta para ver los detalles.");
            return;
        }

        String rutaSeleccionada = vistaRutas.getListaRutas().getSelectedValue();
        JOptionPane.showMessageDialog(vistaRutas, "Detalles de la ruta: " + rutaSeleccionada);
    }

    public void valorarRuta() {
        int selectedIndex = vistaRutas.getListaRutas().getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(vistaRutas, "Seleccione una ruta para valorar.");
            return;
        }

        String rutaSeleccionada = vistaRutas.getListaRutas().getSelectedValue();
        int idRuta = Integer.parseInt(rutaSeleccionada.split(" - ")[0]);
        String nombreRuta = rutaSeleccionada.split(" - ")[1];
        new VistaValoraciones(idRuta, nombreRuta);
    }

    public void reservarRuta() {
        int selectedIndex = vistaRutas.getListaRutas().getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(vistaRutas, "Seleccione una ruta para reservar.");
            return;
        }

        String rutaSeleccionada = vistaRutas.getListaRutas().getSelectedValue();
        new VistaReservas(vistaRutas.getUsuario(), rutaSeleccionada).setVisible(true);
    }

    public Ruta obtenerRutaPorId(int idRuta) throws ClassNotFoundException {
        return rutaDAO.obtenerRutaPorId(idRuta);
    }
}
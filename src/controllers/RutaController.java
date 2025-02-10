package controllers;

import dao.RutaDAO;
import model.Ruta;
import views.VistaRutas;
import javax.swing.*;
import java.util.List;

public class RutaController {
    private RutaDAO rutaDAO;
    private VistaRutas vistaRutas;

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
                modeloLista.addElement(ruta.getNombre() + " - " + ruta.getPrecio() + "€");
            }
            vistaRutas.getListaRutas().setModel(modeloLista);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vistaRutas, "Error al cargar rutas: " + e.getMessage());
        }
    }
    
    private void agregarEventos() {
        vistaRutas.getBtnAgregar().addActionListener(e -> JOptionPane.showMessageDialog(vistaRutas, "Función aún no implementada."));
        vistaRutas.getBtnEliminar().addActionListener(e -> JOptionPane.showMessageDialog(vistaRutas, "Función aún no implementada."));
    }
}

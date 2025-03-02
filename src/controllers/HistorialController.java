package controllers;

import dao.HistorialDAO;
import model.Historial;
import views.VistaHistorial;
import javax.swing.*;
import java.util.List;

public class HistorialController {

	private HistorialDAO historialDAO;
	private VistaHistorial vistaHistorial;

	public HistorialController(VistaHistorial vistaHistorial) {
		this.historialDAO = new HistorialDAO();
		this.vistaHistorial = vistaHistorial;
		cargarHistorial();
	}

	private void cargarHistorial() {
		try {
			int idUsuario = 1;
			List<Historial> historial = historialDAO.obtenerHistorialUsuario(idUsuario);
			DefaultListModel<String> modeloLista = new DefaultListModel<>();

			for (Historial h : historial) {
				modeloLista.addElement(h.getFecha() + " - " + h.getAccion());
			}

			vistaHistorial.getListaHistorial().setModel(modeloLista);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(vistaHistorial, "Error al cargar historial: " + e.getMessage());
		}
	}

}

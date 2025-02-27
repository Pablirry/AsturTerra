package controllers;

import dao.ReservarDAO;
import dao.HistorialDAO;
import model.Reserva;
import views.VistaReservas;

import javax.swing.*;
import java.util.Date;
import java.util.List;

public class ReservaController {
    private ReservarDAO reservaDAO;
    private HistorialDAO historialDAO;
    private VistaReservas vistaReservas;

    public ReservaController(VistaReservas vistaReservas) {
        this.reservaDAO = new ReservarDAO();
        this.historialDAO = new HistorialDAO();
        this.vistaReservas = vistaReservas;
        cargarReservas();
        agregarEventos();
    }

    private void cargarReservas() {
        try {
            List<Reserva> reservas = reservaDAO.obtenerReservasUsuario(1); // Cambiar por el ID del usuario actual
            DefaultListModel<String> modeloLista = new DefaultListModel<>();
            for (Reserva reserva : reservas) {
                modeloLista.addElement(reserva.toString());
            }
            vistaReservas.getListaReservas().setModel(modeloLista);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vistaReservas, "Error al cargar reservas: " + e.getMessage());
        }
    }

    private void agregarEventos() {
        vistaReservas.getBtnReservar().addActionListener(e -> reservarRuta());
        vistaReservas.getBtnCancelar().addActionListener(e -> cancelarReserva());
    }

    public void reservarRuta() {
        try {
            int idUsuario = 1; // Cambiar por el ID del usuario actual
            int idRuta = 1; // Cambiar por el ID de la ruta seleccionada
            Date fecha = new Date();

            boolean exito = reservaDAO.reservarRuta(idUsuario, idRuta, fecha);
            if (exito) {
                historialDAO.registrarAccion(idUsuario, "Reserva realizada para la ruta " + idRuta);
                JOptionPane.showMessageDialog(vistaReservas, "Reserva realizada con éxito.");
                cargarReservas();
            } else {
                JOptionPane.showMessageDialog(vistaReservas, "Error al realizar la reserva.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vistaReservas, "Error en la reserva: " + e.getMessage());
        }
    }

    public void cancelarReserva() {
        try {
            int idReserva = vistaReservas.getListaReservas().getSelectedIndex(); // Cambiar por el ID de la reserva seleccionada
            if (idReserva == -1) {
                JOptionPane.showMessageDialog(vistaReservas, "Seleccione una reserva para cancelar.");
                return;
            }

            reservaDAO.cancelarReserva(idReserva);
            historialDAO.registrarAccion(1, "Reserva cancelada con ID " + idReserva); // Cambiar por el ID del usuario actual
            JOptionPane.showMessageDialog(vistaReservas, "Reserva cancelada con éxito.");
            cargarReservas();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vistaReservas, "Error al cancelar la reserva: " + e.getMessage());
        }
    }
}

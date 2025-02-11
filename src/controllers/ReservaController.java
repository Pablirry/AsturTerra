package controllers;

import dao.ReservarDAO;
import dao.HistorialDAO;
import model.Reserva;
import views.VistaReservas;

import javax.swing.*;
import java.util.Date;

public class ReservaController {
    private ReservarDAO reservaDAO;
    private HistorialDAO historialDAO;
    private VistaReservas vistaReservas;

    public ReservaController(VistaReservas vistaReservas) {
        this.reservaDAO = new ReservarDAO();
        this.historialDAO = new HistorialDAO();
        this.vistaReservas = vistaReservas;
        agregarEventos();
    }

    private void agregarEventos() {
        vistaReservas.getBtnReservar().addActionListener(e -> reservarRuta());
    }

    private void reservarRuta() {
        try {
            int idUsuario = 1;
            int idRuta = 1;
            Date fecha = new Date();

            boolean exito = reservaDAO.reservarRuta(idUsuario, idRuta, fecha);
            if (exito) {
                historialDAO.registrarAccion(idUsuario, "Reserva realizada para la ruta " + idRuta);
                JOptionPane.showMessageDialog(vistaReservas, "Reserva realizada con éxito.");
            } else {
                JOptionPane.showMessageDialog(vistaReservas, "Error al realizar la reserva.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vistaReservas, "Error en la reserva: " + e.getMessage());
        }
    }
}

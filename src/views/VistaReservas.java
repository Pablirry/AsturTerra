package views;

import javax.swing.*;

import dao.ReservarDAO;
import dao.RutaDAO;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import model.Reserva;
import model.Ruta;
import model.Usuario;

public class VistaReservas extends JFrame {

    private static VistaReservas instance;

    private JList<String> listaReservas;
    private JButton btnReservar, btnCancelar, btnVolver;
    private Usuario usuario;
    private String rutaSeleccionada;

    private List<Reserva> reservas;

    public static VistaReservas getInstance(Usuario usuario) {
        if (instance == null || !instance.isVisible()) {
            instance = new VistaReservas(usuario);
        }
        instance.toFront();
        return instance;
    }

    public static VistaReservas getInstance(Usuario usuario, String rutaSeleccionada) {
        if (instance == null || !instance.isVisible()) {
            instance = new VistaReservas(usuario, rutaSeleccionada);
        }
        instance.toFront();
        return instance;
    }

    public VistaReservas(Usuario usuario) {
        this.usuario = usuario;
        inicializarComponentes();
        cargarReservas();
    }

    public VistaReservas(Usuario usuario, String rutaSeleccionada) {
        this.usuario = usuario;
        this.rutaSeleccionada = rutaSeleccionada;
        inicializarComponentes();
        cargarReservas();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                instance = null;
            }
        });
    }

    private void inicializarComponentes() {
        setTitle("Gestión de Reservas");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(44, 62, 80));
        JLabel lblTitulo = new JLabel("Gestión de Reservas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        listaReservas = new JList<>();
        JScrollPane scrollPane = new JScrollPane(listaReservas);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        Color panelBotonesBg = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK
                ? new Color(44, 62, 80)
                : new Color(220, 230, 241);
        panelBotones.setBackground(panelBotonesBg);

        btnReservar = new JButton("Confirmar reserva");
        btnReservar.setBackground(new Color(46, 204, 113));
        btnReservar.setForeground(Color.WHITE);
        btnReservar.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnReservar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnCancelar);

        btnVolver = new JButton("Volver al Menú");
        btnVolver.setBackground(new Color(52, 152, 219));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnVolver);

        add(panelBotones, BorderLayout.SOUTH);

        btnReservar.addActionListener(e -> confirmarReserva());
        btnCancelar.addActionListener(e -> cancelarReserva());
        btnVolver.addActionListener(e -> {
            new MenuPrincipal(usuario).setVisible(true);
            dispose();
        });
        ThemeManager.setTheme(ThemeManager.getCurrentTheme(), this);
        setVisible(true);
    }

    private void cargarReservas() {
        try {
            ReservarDAO dao = new ReservarDAO();
            reservas = dao.obtenerReservasUsuario(usuario.getId());

            DefaultListModel<String> model = new DefaultListModel<>();
            RutaDAO rutaDAO = new RutaDAO();

            for (Reserva reserva : reservas) {
                Ruta ruta = rutaDAO.obtenerRutaPorId(reserva.getIdRuta());
                String estado = reserva.isConfirmada() ? "CONFIRMADA" : "PENDIENTE";
                model.addElement("ID Reserva: " + reserva.getId() +
                        " - Ruta: " + (ruta != null ? ruta.getNombre() : "Desconocida") +
                        " - Fecha: " + reserva.getFecha() +
                        " - Estado: " + estado);
            }
            listaReservas.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar reservas: " + e.getMessage());
        }
    }

    private void confirmarReserva() {
        int index = listaReservas.getSelectedIndex();
        if (index == -1 || reservas == null || index >= reservas.size()) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva.");
            return;
        }
        Reserva reserva = reservas.get(index);
        if (reserva.isConfirmada()) {
            JOptionPane.showMessageDialog(this, "La reserva ya está confirmada.");
            return;
        }
        try {
            ReservarDAO dao = new ReservarDAO();
            boolean confirmado = dao.confirmarReserva(reserva.getId());
            if (confirmado) {
                JOptionPane.showMessageDialog(this, "Reserva confirmada. Proceda al pago.");
                cargarReservas();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo confirmar la reserva.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al confirmar reserva: " + e.getMessage());
        }
    }

    private void cancelarReserva() {
        int index = listaReservas.getSelectedIndex();
        if (index == -1 || reservas == null || index >= reservas.size()) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva.");
            return;
        }
        Reserva reserva = reservas.get(index);
        if (reserva.isConfirmada()) {
            JOptionPane.showMessageDialog(this, "No se puede cancelar una reserva confirmada.");
            return;
        }
        try {
            ReservarDAO dao = new ReservarDAO();
            boolean cancelado = dao.cancelarReserva(reserva.getId());
            if (cancelado) {
                JOptionPane.showMessageDialog(this, "Reserva cancelada.");
                cargarReservas();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo cancelar la reserva.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cancelar reserva: " + e.getMessage());
        }
    }

    public JList<String> getListaReservas() {
        return listaReservas;
    }

    public JButton getBtnReservar() {
        return btnReservar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public JButton getBtnVolver() {
        return btnVolver;
    }

    public String getRutaSeleccionada() {
        return rutaSeleccionada;
    }
}
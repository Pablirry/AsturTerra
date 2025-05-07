package views;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import model.Reserva;
import model.Ruta;
import model.Usuario;
import dao.ReservarDAO;
import dao.RutaDAO;

public class VistaReservas extends JFrame {

    private static VistaReservas instance;

    private JPanel panelTarjetas;
    private JButton btnVolver;
    private Usuario usuario;
    private List<Reserva> reservas;

    public static VistaReservas getInstance(Usuario usuario) {
        if (instance == null || !instance.isVisible()) {
            instance = new VistaReservas(usuario);
        }
        instance.toFront();
        return instance;
    }

    public VistaReservas(Usuario usuario) {
        this.usuario = usuario;
        inicializarComponentes();
        cargarReservas();
    }

    private void inicializarComponentes() {
        setTitle("Gestión de Reservas");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(44, 62, 80));
        JLabel lblTitulo = new JLabel("Gestión de Reservas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        panelTarjetas = new JPanel();
        panelTarjetas.setLayout(new FlowLayout(FlowLayout.LEFT, 24, 24));
        panelTarjetas.setBackground(new Color(236, 240, 241));
        JScrollPane scroll = new JScrollPane(panelTarjetas);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(236, 240, 241));

        btnVolver = new JButton("Volver al Menú");
        btnVolver.setBackground(new Color(52, 152, 219));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setFont(new Font("Arial", Font.BOLD, 18));
        btnVolver.setPreferredSize(new Dimension(170, 48));
        btnVolver.addActionListener(e -> {
            new MenuPrincipal(usuario).setVisible(true);
            dispose();
        });
        panelBotones.add(btnVolver);

        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void cargarReservas() {
        panelTarjetas.removeAll();
        try {
            ReservarDAO dao = new ReservarDAO();
            reservas = dao.obtenerReservasUsuario(usuario.getId());
            RutaDAO rutaDAO = new RutaDAO();

            for (Reserva reserva : reservas) {
                Ruta ruta = rutaDAO.obtenerRutaPorId(reserva.getIdRuta());
                panelTarjetas.add(crearTarjetaReserva(reserva, ruta));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar reservas: " + e.getMessage());
        }
        panelTarjetas.revalidate();
        panelTarjetas.repaint();
    }

    private JPanel crearTarjetaReserva(Reserva reserva, Ruta ruta) {
        JPanel tarjeta = new JPanel();
        tarjeta.setPreferredSize(new Dimension(340, 140));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new ThemeManager.RoundedBorder(new Color(52, 152, 219), 2, 24),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        tarjeta.setLayout(new BorderLayout(12, 0));

        JLabel lblRuta = new JLabel("Ruta: " + (ruta != null ? ruta.getNombre() : "Desconocida"));
        lblRuta.setFont(new Font("Arial", Font.BOLD, 18));
        lblRuta.setForeground(new Color(44, 62, 80));

        JLabel lblFecha = new JLabel("Fecha: " + reserva.getFecha());
        lblFecha.setFont(new Font("Arial", Font.PLAIN, 15));
        lblFecha.setForeground(new Color(100, 100, 100));

        JLabel lblEstado = new JLabel("Estado: " + (reserva.isConfirmada() ? "CONFIRMADA" : "PENDIENTE"));
        lblEstado.setFont(new Font("Arial", Font.BOLD, 15));
        lblEstado.setForeground(reserva.isConfirmada() ? new Color(46, 204, 113) : new Color(241, 196, 15));

        JPanel panelInfo = new JPanel();
        panelInfo.setOpaque(false);
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.add(lblRuta);
        panelInfo.add(lblFecha);
        panelInfo.add(lblEstado);

        tarjeta.add(panelInfo, BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelAcciones.setOpaque(false);

        if (!reserva.isConfirmada()) {
            JButton btnConfirmar = new JButton("Confirmar");
            btnConfirmar.setBackground(new Color(46, 204, 113));
            btnConfirmar.setForeground(Color.WHITE);
            btnConfirmar.setFont(new Font("Arial", Font.BOLD, 14));
            btnConfirmar.addActionListener(e -> confirmarReserva(reserva));
            panelAcciones.add(btnConfirmar);

            JButton btnCancelar = new JButton("Cancelar");
            btnCancelar.setBackground(new Color(231, 76, 60));
            btnCancelar.setForeground(Color.WHITE);
            btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
            btnCancelar.addActionListener(e -> cancelarReserva(reserva));
            panelAcciones.add(btnCancelar);
        }

        tarjeta.add(panelAcciones, BorderLayout.SOUTH);

        return tarjeta;
    }

    private void confirmarReserva(Reserva reserva) {
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

    private void cancelarReserva(Reserva reserva) {
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
}
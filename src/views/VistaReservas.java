package views;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import model.Reserva;
import java.util.List;
import model.Ruta;
import model.Usuario;
import services.TurismoService;
import utils.I18n;
import utils.WrapLayout;

public class VistaReservas extends JFrame {
    private final Usuario usuario;
    private JPanel panelTarjetas;

    public VistaReservas(Usuario usuario) {
        this.usuario = usuario;
        setTitle(I18n.t("titulo.reservas"));
        setSize(1200, 750); // Más largo
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color bg = dark ? new Color(44, 62, 80) : new Color(236, 240, 241);

        // Panel superior con título y botón menú principal
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(44, 62, 80));

        JLabel lblTitulo = new JLabel(I18n.t("titulo.reservas"));
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        panelSuperior.add(lblTitulo, BorderLayout.WEST);

        JButton btnMenu = new JButton(I18n.t("boton.volver"));
        btnMenu.setFont(new Font("Arial", Font.BOLD, 16));
        btnMenu.setForeground(Color.WHITE);
        btnMenu.setBackground(new Color(41, 128, 185));
        btnMenu.setFocusPainted(false);
        btnMenu.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 2, true));
        btnMenu.setPreferredSize(new Dimension(140, 40));
        btnMenu.addActionListener(e -> {
            new MenuPrincipal(usuario).setVisible(true);
            dispose();
        });
        btnMenu.setOpaque(true);

        JPanel panelMenu = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        panelMenu.setOpaque(false);
        panelMenu.add(btnMenu);
        panelSuperior.add(panelMenu, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        // Panel de tarjetas de reservas
        panelTarjetas = new JPanel(new WrapLayout(FlowLayout.CENTER, 24, 24));
        panelTarjetas.setBackground(bg);

        JScrollPane scroll = new JScrollPane(panelTarjetas,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(bg);
        add(scroll, BorderLayout.CENTER);

        cargarReservas();
        setVisible(true);
    }

    public void cargarReservas() {
        panelTarjetas.removeAll();
        try {
            List<Reserva> reservas = TurismoService.getInstance().obtenerReservas();
            Date ahora = new Date();
            List<Reserva> reservasValidas = new ArrayList<>();
            for (Reserva r : reservas) {
                if (r.getFecha().before(ahora)) {
                    // Eliminar reservas pasadas
                    try {
                        TurismoService.getInstance().eliminarReserva(r.getId());
                    } catch (Exception ex) {
                        // Ignorar error de eliminación individual
                    }
                } else {
                    reservasValidas.add(r);
                }
            }
            if (reservasValidas.isEmpty()) {
                JLabel lblVacio = new JLabel("No hay reservas registradas.");
                lblVacio.setFont(new Font("Arial", Font.ITALIC, 22));
                lblVacio.setForeground(Color.GRAY);
                panelTarjetas.add(lblVacio);
            } else {
                for (Reserva r : reservasValidas) {
                    panelTarjetas.add(crearTarjetaReserva(r));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar reservas", "Error", JOptionPane.ERROR_MESSAGE);
        }
        panelTarjetas.revalidate();
        panelTarjetas.repaint();
    }

    private JPanel crearTarjetaReserva(Reserva reserva) {
        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color borderColor = new Color(52, 152, 219);
        Color bgTarjeta = dark ? new Color(52, 73, 94) : Color.WHITE;
        Color fgPanel = dark ? Color.WHITE : new Color(44, 62, 80);

        JPanel tarjeta = new JPanel();
        tarjeta.setPreferredSize(new Dimension(700, 180)); // Más largo y alto
        tarjeta.setBackground(bgTarjeta);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new ThemeManager.RoundedBorder(borderColor, 2, 24),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        tarjeta.setLayout(new BorderLayout(16, 0));

        // Info principal
        JPanel panelInfo = new JPanel();
        panelInfo.setOpaque(false);
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setAlignmentY(Component.TOP_ALIGNMENT);
        panelInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        try {
            Usuario usuarioReserva = TurismoService.getInstance().obtenerUsuarioPorId(reserva.getIdUsuario());
            Ruta ruta = TurismoService.getInstance().obtenerRutaPorId(reserva.getIdRuta());

            JLabel lblUsuario = new JLabel(I18n.t("columna.usuario") + ": " + (usuarioReserva != null ? usuarioReserva.getNombre() : "Desconocido"));
            lblUsuario.setFont(new Font("Arial", Font.BOLD, 18));
            lblUsuario.setForeground(fgPanel);

            JLabel lblRuta = new JLabel("Ruta: " + (ruta != null ? ruta.getNombre() : "Desconocida"));
            lblRuta.setFont(new Font("Arial", Font.PLAIN, 16));
            lblRuta.setForeground(dark ? Color.LIGHT_GRAY : new Color(100, 100, 100));

            // Formato fecha y hora real
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String fechaHora = sdf.format(reserva.getFecha());
            JLabel lblFecha = new JLabel("Fecha: " + fechaHora);
            lblFecha.setFont(new Font("Arial", Font.PLAIN, 15));
            lblFecha.setForeground(dark ? Color.LIGHT_GRAY : new Color(120, 120, 120));

            JLabel lblConfirmada = new JLabel("Confirmada: " + (reserva.isConfirmada() ? "Sí" : "No"));
            lblConfirmada.setFont(new Font("Arial", Font.BOLD, 15));
            lblConfirmada.setForeground(reserva.isConfirmada() ? new Color(39, 174, 96) : new Color(231, 76, 60));

            panelInfo.add(lblUsuario);
            panelInfo.add(Box.createVerticalStrut(6));
            panelInfo.add(lblRuta);
            panelInfo.add(Box.createVerticalStrut(6));
            panelInfo.add(lblFecha);
            panelInfo.add(Box.createVerticalStrut(6));
            panelInfo.add(lblConfirmada);
        } catch (Exception e) {
            JLabel lblError = new JLabel("Error al obtener datos de la reserva.");
            lblError.setForeground(Color.RED);
            panelInfo.add(lblError);
        }

        tarjeta.add(panelInfo, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        panelBotones.setOpaque(false);

        JButton btnConfirmar = new JButton(I18n.t("boton.confirmar"));
        btnConfirmar.setBackground(new Color(39, 174, 96));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setFont(new Font("Dialog", Font.BOLD, 15));
        btnConfirmar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnConfirmar.setPreferredSize(new Dimension(170, 38));
        btnConfirmar.setEnabled(!reserva.isConfirmada());
        btnConfirmar.addActionListener(e -> {
            confirmarReserva(reserva);
        });

        JButton btnEliminar = new JButton(I18n.t("boton.eliminar"));
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setFont(new Font("Dialog", Font.BOLD, 15));
        btnEliminar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnEliminar.setPreferredSize(new Dimension(130, 38));
        btnEliminar.setEnabled(!reserva.isConfirmada());
        btnEliminar.addActionListener(e -> {
            eliminarReserva(reserva);
        });

        panelBotones.add(btnConfirmar);
        panelBotones.add(btnEliminar);

        tarjeta.add(panelBotones, BorderLayout.SOUTH);

        return tarjeta;
    }

    private void eliminarReserva(Reserva reserva) {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar la reserva?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                TurismoService.getInstance().eliminarReserva(reserva.getId());
                cargarReservas();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar reserva", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void confirmarReserva(Reserva reserva) {
        try {
            TurismoService.getInstance().confirmarReserva(reserva.getId());
            cargarReservas();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al confirmar reserva", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
package views;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import model.Reserva;
import model.ReservaEvento;
import java.util.List;
import model.Ruta;
import model.Usuario;
import services.TurismoService;
import utils.I18n;
import utils.WrapLayout;

public class VistaReservas extends JFrame {
    private final Usuario usuario;
    private JPanel panelTarjetas;
    private boolean mostrandoRutas = true; // true = rutas, false = eventos

    public VistaReservas(Usuario usuario) {
        this.usuario = usuario;
        setTitle(I18n.t("titulo.reservas"));
        setSize(1200, 750);
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

        // Panel inferior con los dos botones grandes ocupando toda la barra
        JPanel panelInferior = new JPanel(new GridLayout(1, 2, 0, 0));
        panelInferior.setBackground(bg);

        JButton btnReservasRutas = new JButton(I18n.t("boton.reservas.rutas"));
        btnReservasRutas.setFont(new Font("Arial", Font.BOLD, 20));
        btnReservasRutas.setForeground(Color.WHITE);
        btnReservasRutas.setBackground(new Color(41, 128, 185));
        btnReservasRutas.setFocusPainted(false);
        btnReservasRutas.setPreferredSize(new Dimension(0, 60));
        btnReservasRutas.addActionListener(e -> {
            mostrandoRutas = true;
            cargarReservas();
        });

        JButton btnReservasEventos = new JButton(I18n.t("boton.reservas.eventos"));
        btnReservasEventos.setFont(new Font("Arial", Font.BOLD, 20));
        btnReservasEventos.setForeground(Color.WHITE);
        btnReservasEventos.setBackground(new Color(39, 174, 96));
        btnReservasEventos.setFocusPainted(false);
        btnReservasEventos.setPreferredSize(new Dimension(0, 60));
        btnReservasEventos.addActionListener(e -> {
            mostrandoRutas = false;
            cargarReservas();
        });

        panelInferior.add(btnReservasRutas);
        panelInferior.add(btnReservasEventos);
        add(panelInferior, BorderLayout.SOUTH);

        cargarReservas();
        setVisible(true);
    }

    public void cargarReservas() {
        panelTarjetas.removeAll();
        if (mostrandoRutas) {
            cargarReservasRutas();
        } else {
            cargarReservasEventos();
        }
        panelTarjetas.revalidate();
        panelTarjetas.repaint();
    }

    private void cargarReservasRutas() {
        try {
            List<Reserva> reservas = TurismoService.getInstance().obtenerReservas();
            Date ahora = new Date();
            List<Reserva> reservasValidas = new ArrayList<>();
            for (Reserva r : reservas) {
                if (r.getFecha().before(ahora)) {
                    try {
                        TurismoService.getInstance().eliminarReserva(r.getId());
                    } catch (Exception ex) {}
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
    }

    private void cargarReservasEventos() {
        try {
            List<ReservaEvento> reservas = TurismoService.getInstance().obtenerReservasEvento();
            Date ahora = new Date();
            List<ReservaEvento> reservasValidas = new ArrayList<>();
            for (ReservaEvento r : reservas) {
                if (r.getFechaReserva().before(ahora)) {
                    try {
                        TurismoService.getInstance().eliminarReservaEvento(r.getId());
                    } catch (Exception ex) {}
                } else {
                    reservasValidas.add(r);
                }
            }
            if (reservasValidas.isEmpty()) {
                JLabel lblVacio = new JLabel("No hay reservas de eventos registradas.");
                lblVacio.setFont(new Font("Arial", Font.ITALIC, 22));
                lblVacio.setForeground(Color.GRAY);
                panelTarjetas.add(lblVacio);
            } else {
                for (ReservaEvento r : reservasValidas) {
                    panelTarjetas.add(crearTarjetaReservaEvento(r));
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar reservas de eventos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel crearTarjetaReserva(Reserva reserva) {
        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color borderColor = new Color(52, 152, 219);
        Color bgTarjeta = dark ? new Color(52, 73, 94) : Color.WHITE;
        Color fgPanel = dark ? Color.WHITE : new Color(44, 62, 80);

        JPanel tarjeta = new JPanel();
        tarjeta.setPreferredSize(new Dimension(700, 180));
        tarjeta.setBackground(bgTarjeta);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new ThemeManager.RoundedBorder(borderColor, 2, 24),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        tarjeta.setLayout(new BorderLayout(16, 0));

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

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 12, 0));
        panelBotones.setOpaque(false);

        JButton btnConfirmar = new JButton(I18n.t("boton.confirmar"));
        btnConfirmar.setBackground(new Color(39, 174, 96));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setFont(new Font("Dialog", Font.BOLD, 15));
        btnConfirmar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnConfirmar.setEnabled(!reserva.isConfirmada());
        btnConfirmar.addActionListener(e -> {
            confirmarReserva(reserva);
        });

        JButton btnCancelar = new JButton(I18n.t("boton.cancelar"));
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Dialog", Font.BOLD, 15));
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.setEnabled(!reserva.isConfirmada());
        btnCancelar.addActionListener(e -> {
            cancelarReserva(reserva);
        });

        panelBotones.add(btnConfirmar);
        panelBotones.add(btnCancelar);

        tarjeta.add(panelBotones, BorderLayout.SOUTH);

        return tarjeta;
    }

    private JPanel crearTarjetaReservaEvento(ReservaEvento reserva) {
        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color borderColor = new Color(39, 174, 96);
        Color bgTarjeta = dark ? new Color(52, 73, 94) : Color.WHITE;
        Color fgPanel = dark ? Color.WHITE : new Color(44, 62, 80);

        JPanel tarjeta = new JPanel();
        tarjeta.setPreferredSize(new Dimension(700, 180));
        tarjeta.setBackground(bgTarjeta);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new ThemeManager.RoundedBorder(borderColor, 2, 24),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        tarjeta.setLayout(new BorderLayout(16, 0));

        JPanel panelInfo = new JPanel();
        panelInfo.setOpaque(false);
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setAlignmentY(Component.TOP_ALIGNMENT);
        panelInfo.setAlignmentX(Component.LEFT_ALIGNMENT);

        try {
            Usuario usuarioReserva = TurismoService.getInstance().obtenerUsuarioPorId(reserva.getUsuarioId());
            // Puedes obtener el evento si tienes el modelo Evento y su DAO

            JLabel lblUsuario = new JLabel(I18n.t("columna.usuario") + ": " + (usuarioReserva != null ? usuarioReserva.getNombre() : "Desconocido"));
            lblUsuario.setFont(new Font("Arial", Font.BOLD, 18));
            lblUsuario.setForeground(fgPanel);

            JLabel lblEvento = new JLabel("Evento ID: " + reserva.getEventoId());
            lblEvento.setFont(new Font("Arial", Font.PLAIN, 16));
            lblEvento.setForeground(dark ? Color.LIGHT_GRAY : new Color(100, 100, 100));

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String fechaHora = sdf.format(reserva.getFechaReserva());
            JLabel lblFecha = new JLabel("Fecha: " + fechaHora);
            lblFecha.setFont(new Font("Arial", Font.PLAIN, 15));
            lblFecha.setForeground(dark ? Color.LIGHT_GRAY : new Color(120, 120, 120));

            JLabel lblConfirmada = new JLabel("Confirmada: " + (reserva.isConfirmada() ? "Sí" : "No"));
            lblConfirmada.setFont(new Font("Arial", Font.BOLD, 15));
            lblConfirmada.setForeground(reserva.isConfirmada() ? new Color(39, 174, 96) : new Color(231, 76, 60));

            panelInfo.add(lblUsuario);
            panelInfo.add(Box.createVerticalStrut(6));
            panelInfo.add(lblEvento);
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

        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 12, 0));
        panelBotones.setOpaque(false);

        JButton btnConfirmar = new JButton(I18n.t("boton.confirmar"));
        btnConfirmar.setBackground(new Color(39, 174, 96));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setFont(new Font("Dialog", Font.BOLD, 15));
        btnConfirmar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnConfirmar.setEnabled(!reserva.isConfirmada());
        btnConfirmar.addActionListener(e -> {
            confirmarReservaEvento(reserva);
        });

        JButton btnCancelar = new JButton(I18n.t("boton.cancelar"));
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Dialog", Font.BOLD, 15));
        btnCancelar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnCancelar.setEnabled(!reserva.isConfirmada());
        btnCancelar.addActionListener(e -> {
            cancelarReservaEvento(reserva);
        });

        panelBotones.add(btnConfirmar);
        panelBotones.add(btnCancelar);

        tarjeta.add(panelBotones, BorderLayout.SOUTH);

        return tarjeta;
    }

    private void cancelarReserva(Reserva reserva) {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de cancelar la reserva? Esta acción la bloqueará.", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Aquí puedes marcar la reserva como bloqueada (por ejemplo, confirmada=false y no editable)
                TurismoService.getInstance().eliminarReserva(reserva.getId());
                cargarReservas();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al cancelar reserva", "Error", JOptionPane.ERROR_MESSAGE);
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

    private void cancelarReservaEvento(ReservaEvento reserva) {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de cancelar la reserva de evento? Esta acción la bloqueará.", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                TurismoService.getInstance().eliminarReservaEvento(reserva.getId());
                cargarReservas();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al cancelar reserva de evento", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void confirmarReservaEvento(ReservaEvento reserva) {
        try {
            TurismoService.getInstance().confirmarReservaEvento(reserva.getId());
            cargarReservas();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al confirmar reserva de evento", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
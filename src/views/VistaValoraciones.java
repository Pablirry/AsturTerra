package views;

import model.Usuario;
import model.ValoracionRestaurante;
import model.ValoracionRuta;
import model.ValoracionEvento;
import services.TurismoService;
import utils.I18n;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VistaValoraciones extends JFrame {
    private final Usuario usuario;
    private JPanel panelLista;
    private JToggleButton btnRestaurantes, btnRutas, btnEventos;

    public VistaValoraciones(Usuario usuario) {
        this.usuario = usuario;
        setTitle(I18n.t("titulo.valoraciones"));
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color bg = dark ? new Color(34, 40, 49) : new Color(236, 240, 241);

        // Panel superior con título a la izquierda y botón volver a la derecha
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(new Color(44, 62, 80));

        JLabel lblTitulo = new JLabel(I18n.t("titulo.valoraciones"));
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        panelSuperior.add(lblTitulo, BorderLayout.WEST);

        JButton btnVolver = new JButton(I18n.t("boton.volver"));
        btnVolver.setFont(new Font("Arial", Font.BOLD, 16));
        btnVolver.setForeground(Color.WHITE);
        btnVolver.setBackground(new Color(41, 128, 185));
        btnVolver.setFocusPainted(false);
        btnVolver.setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 2, true));
        btnVolver.setPreferredSize(new Dimension(180, 40));
        btnVolver.addActionListener(e -> {
            new MenuPrincipal(usuario).setVisible(true);
            dispose();
        });

        JPanel panelMenu = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        panelMenu.setOpaque(false);
        panelMenu.add(btnVolver);
        panelSuperior.add(panelMenu, BorderLayout.EAST);

        add(panelSuperior, BorderLayout.NORTH);

        // Selector superior con 3 botones
        JPanel panelSelector = new JPanel(new GridLayout(1, 3));
        panelSelector.setBackground(bg);

        btnRestaurantes = new JToggleButton(I18n.t("boton.restaurantes"));
        btnRutas = new JToggleButton(I18n.t("boton.rutas"));
        btnEventos = new JToggleButton(I18n.t("boton.eventos"));
        ButtonGroup grupo = new ButtonGroup();
        grupo.add(btnRestaurantes);
        grupo.add(btnRutas);
        grupo.add(btnEventos);

        btnRestaurantes.setSelected(true);
        btnRestaurantes.setFont(new Font("Arial", Font.BOLD, 18));
        btnRutas.setFont(new Font("Arial", Font.BOLD, 18));
        btnEventos.setFont(new Font("Arial", Font.BOLD, 18));
        btnRestaurantes.setBackground(dark ? new Color(52, 152, 219) : new Color(52, 152, 219));
        btnRestaurantes.setForeground(Color.WHITE);
        btnRutas.setBackground(dark ? new Color(39, 174, 96) : new Color(39, 174, 96));
        btnRutas.setForeground(Color.WHITE);
        btnEventos.setBackground(new Color(230, 126, 34));
        btnEventos.setForeground(Color.WHITE);

        btnRestaurantes.addActionListener(e -> cargarValoracionesRestaurantes());
        btnRutas.addActionListener(e -> cargarValoracionesRutas());
        btnEventos.addActionListener(e -> cargarValoracionesEventos());

        panelSelector.add(btnRestaurantes);
        panelSelector.add(btnRutas);
        panelSelector.add(btnEventos);

        add(panelSelector, BorderLayout.AFTER_LAST_LINE);

        // Panel lista
        panelLista = new JPanel();
        panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));
        panelLista.setBackground(bg);

        JScrollPane scroll = new JScrollPane(panelLista);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(bg);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(30);
        add(scroll, BorderLayout.CENTER);

        cargarValoracionesRestaurantes();
        setVisible(true);
    }

    private void cargarValoracionesRestaurantes() {
        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color bg = dark ? new Color(34, 40, 49) : new Color(236, 240, 241);
        panelLista.removeAll();
        try {
            List<ValoracionRestaurante> valoraciones = TurismoService.getInstance().obtenerValoracionesRestaurante();
            if (valoraciones.isEmpty()) {
                JLabel lblVacio = new JLabel("No hay valoraciones de restaurantes.");
                lblVacio.setFont(new Font("Arial", Font.ITALIC, 20));
                lblVacio.setForeground(Color.GRAY);
                panelLista.add(lblVacio);
            } else {
                for (ValoracionRestaurante v : valoraciones) {
                    panelLista.add(crearPanelValoracionRestaurante(v, dark, bg));
                    panelLista.add(Box.createVerticalStrut(10));
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar valoraciones: " + ex.getMessage());
        }
        panelLista.revalidate();
        panelLista.repaint();
    }

    private void cargarValoracionesRutas() {
        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color bg = dark ? new Color(34, 40, 49) : new Color(236, 240, 241);
        panelLista.removeAll();
        try {
            List<ValoracionRuta> valoraciones = TurismoService.getInstance().obtenerValoracionesRuta();
            if (valoraciones.isEmpty()) {
                JLabel lblVacio = new JLabel("No hay valoraciones de rutas.");
                lblVacio.setFont(new Font("Arial", Font.ITALIC, 20));
                lblVacio.setForeground(Color.GRAY);
                panelLista.add(lblVacio);
            } else {
                for (ValoracionRuta v : valoraciones) {
                    panelLista.add(crearPanelValoracionRuta(v, dark, bg));
                    panelLista.add(Box.createVerticalStrut(10));
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar valoraciones: " + ex.getMessage());
        }
        panelLista.revalidate();
        panelLista.repaint();
    }

    private void cargarValoracionesEventos() {
        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color bg = dark ? new Color(34, 40, 49) : new Color(236, 240, 241);
        panelLista.removeAll();
        try {
            List<ValoracionEvento> valoraciones = TurismoService.getInstance().obtenerValoracionesEvento();
            if (valoraciones.isEmpty()) {
                JLabel lblVacio = new JLabel("No hay valoraciones de eventos.");
                lblVacio.setFont(new Font("Arial", Font.ITALIC, 20));
                lblVacio.setForeground(Color.GRAY);
                panelLista.add(lblVacio);
            } else {
                for (ValoracionEvento v : valoraciones) {
                    panelLista.add(crearPanelValoracionEvento(v, dark, bg));
                    panelLista.add(Box.createVerticalStrut(10));
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar valoraciones: " + ex.getMessage());
        }
        panelLista.revalidate();
        panelLista.repaint();
    }

    private JPanel crearPanelValoracionRestaurante(ValoracionRestaurante v, boolean dark, Color bg) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 0));
        panel.setMaximumSize(new Dimension(700, 80));
        panel.setPreferredSize(new Dimension(700, 80));
        panel.setBackground(dark ? new Color(44, 62, 80) : Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2, true),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)));

        String usuario = "Usuario ID: " + v.getIdUsuario();
        String restaurante = "Restaurante ID: " + v.getIdRestaurante();
        String estrellas = getEstrellas(v.getPuntuacion());
        JLabel lblInfo = new JLabel(
                "<html><b>" + usuario + "</b> | " + restaurante
                        + " | <span style='vertical-align:middle; font-size:14px; color:gold;'>" + estrellas
                        + "</span></html>");
        lblInfo.setFont(new Font("Segoe UI Symbol", Font.BOLD, 14));
        lblInfo.setForeground(dark ? new Color(52, 152, 219) : new Color(41, 128, 185));

        JTextArea txtComentario = new JTextArea(v.getComentario());
        txtComentario.setFont(new Font("Arial", Font.PLAIN, 14));
        txtComentario.setLineWrap(true);
        txtComentario.setWrapStyleWord(true);
        txtComentario.setEditable(false);
        txtComentario.setOpaque(false);
        txtComentario.setForeground(dark ? Color.WHITE : Color.DARK_GRAY);
        txtComentario.setBorder(null);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setFont(new Font("Dialog", Font.BOLD, 13));
        btnEliminar.setPreferredSize(new Dimension(100, 32));
        btnEliminar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar esta valoración?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    TurismoService.getInstance().eliminarValoracionRestaurante(v.getId());
                    cargarValoracionesRestaurantes();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar valoración: " + ex.getMessage());
                }
            }
        });

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setOpaque(false);
        panelCentro.add(lblInfo, BorderLayout.NORTH);
        panelCentro.add(txtComentario, BorderLayout.CENTER);

        panel.add(panelCentro, BorderLayout.CENTER);
        panel.add(btnEliminar, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearPanelValoracionRuta(ValoracionRuta v, boolean dark, Color bg) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 0));
        panel.setMaximumSize(new Dimension(700, 80));
        panel.setPreferredSize(new Dimension(700, 80));
        panel.setBackground(dark ? new Color(44, 62, 80) : Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(39, 174, 96), 2, true),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)));

        String usuario = "Usuario ID: " + v.getIdUsuario();
        String ruta = "Ruta ID: " + v.getIdRuta();
        String estrellas = getEstrellas(v.getPuntuacion());
        JLabel lblInfo = new JLabel(
                "<html><b>" + usuario + "</b> | " + ruta
                        + " | <span style='vertical-align:middle; font-size:14px; color:gold;'>" + estrellas
                        + "</span></html>");
        lblInfo.setFont(new Font("Segoe UI Symbol", Font.BOLD, 14));
        lblInfo.setForeground(dark ? new Color(52, 152, 219) : new Color(41, 128, 185));

        JTextArea txtComentario = new JTextArea(v.getComentario());
        txtComentario.setFont(new Font("Arial", Font.PLAIN, 14));
        txtComentario.setLineWrap(true);
        txtComentario.setWrapStyleWord(true);
        txtComentario.setEditable(false);
        txtComentario.setOpaque(false);
        txtComentario.setForeground(dark ? Color.WHITE : Color.DARK_GRAY);
        txtComentario.setBorder(null);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setFont(new Font("Dialog", Font.BOLD, 13));
        btnEliminar.setPreferredSize(new Dimension(100, 32));
        btnEliminar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar esta valoración?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    TurismoService.getInstance().eliminarValoracionRuta(v.getId());
                    cargarValoracionesRutas();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar valoración: " + ex.getMessage());
                }
            }
        });

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setOpaque(false);
        panelCentro.add(lblInfo, BorderLayout.NORTH);
        panelCentro.add(txtComentario, BorderLayout.CENTER);

        panel.add(panelCentro, BorderLayout.CENTER);
        panel.add(btnEliminar, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearPanelValoracionEvento(ValoracionEvento v, boolean dark, Color bg) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 0));
        panel.setMaximumSize(new Dimension(700, 80));
        panel.setPreferredSize(new Dimension(700, 80));
        panel.setBackground(dark ? new Color(44, 62, 80) : Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 126, 34), 2, true),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)));

        String usuario = "Usuario ID: " + v.getIdUsuario();
        String evento = "Evento ID: " + v.getIdEvento();
        String estrellas = getEstrellas(v.getPuntuacion());
        JLabel lblInfo = new JLabel(
                "<html><b>" + usuario + "</b> | " + evento
                        + " | <span style='vertical-align:middle; font-size:14px; color:gold;'>" + estrellas
                        + "</span></html>");
        lblInfo.setFont(new Font("Segoe UI Symbol", Font.BOLD, 14));
        lblInfo.setForeground(new Color(230, 126, 34));

        JTextArea txtComentario = new JTextArea(v.getComentario());
        txtComentario.setFont(new Font("Arial", Font.PLAIN, 14));
        txtComentario.setLineWrap(true);
        txtComentario.setWrapStyleWord(true);
        txtComentario.setEditable(false);
        txtComentario.setOpaque(false);
        txtComentario.setForeground(dark ? Color.WHITE : Color.DARK_GRAY);
        txtComentario.setBorder(null);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(231, 76, 60));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setFont(new Font("Dialog", Font.BOLD, 13));
        btnEliminar.setPreferredSize(new Dimension(100, 32));
        btnEliminar.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar esta valoración?", "Confirmar",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    TurismoService.getInstance().eliminarValoracionEvento(v.getId());
                    cargarValoracionesEventos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar valoración: " + ex.getMessage());
                }
            }
        });

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setOpaque(false);
        panelCentro.add(lblInfo, BorderLayout.NORTH);
        panelCentro.add(txtComentario, BorderLayout.CENTER);

        panel.add(panelCentro, BorderLayout.CENTER);
        panel.add(btnEliminar, BorderLayout.EAST);

        return panel;
    }

    private String getEstrellas(int puntuacion) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(i < puntuacion ? "★" : "☆");
        }
        return sb.toString();
    }
}
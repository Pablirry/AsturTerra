package views;

import javax.swing.*;
import model.Restaurante;
import model.Usuario;
import java.awt.*;


public class ValorarRestaurantes extends JFrame {

    private int idRestaurante;
    private Usuario usuario;
    private JTextArea txtComentario;
    private int puntuacionSeleccionada = 0; // Por defecto ninguna
    private JLabel[] estrellas = new JLabel[5];

    public ValorarRestaurantes(String nombreRestaurante, Usuario usuario) {
        this.usuario = usuario;

        try {
            Restaurante restaurante = services.TurismoService.getInstance().obtenerRestaurantePorNombre(nombreRestaurante);
            if (restaurante == null) {
                JOptionPane.showMessageDialog(this, "No se encontró el restaurante.");
                dispose();
                return;
            }
            this.idRestaurante = restaurante.getId();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error obteniendo restaurante: " + e.getMessage());
            dispose();
            return;
        }

        setTitle("Valorar Restaurante");
        setSize(420, 340);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        actualizarTema();

        setVisible(true);
    }

    private void actualizarTema() {
        getContentPane().removeAll();

        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color fondo = dark ? new Color(44, 62, 80) : Color.WHITE;
        Color texto = dark ? Color.WHITE : new Color(44, 62, 80);

        // Panel título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(dark ? new Color(44, 62, 80) : new Color(236, 240, 241));
        JLabel lblTitulo = new JLabel("Valorar Restaurante");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(texto);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Panel contenido central
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panelContenido.setBackground(fondo);

        // Puntuación con estrellas visuales
        JPanel panelPuntuacion = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelPuntuacion.setBackground(fondo);
        JLabel lblPuntuacion = new JLabel("Puntuación:");
        lblPuntuacion.setFont(new Font("Arial", Font.PLAIN, 16));
        lblPuntuacion.setForeground(texto);
        panelPuntuacion.add(lblPuntuacion);

        // Estrellas visuales (todas vacías al inicio)
        for (int i = 0; i < 5; i++) {
            estrellas[i] = new JLabel("☆");
            estrellas[i].setFont(new Font("Segoe UI Symbol", Font.BOLD, 28)); 
            estrellas[i].setForeground(new Color(241, 196, 15));
            final int estrellaIndex = i;
            estrellas[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            estrellas[i].addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    puntuacionSeleccionada = estrellaIndex + 1;
                    actualizarEstrellas();
                }
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    resaltarEstrellas(estrellaIndex + 1);
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    actualizarEstrellas();
                }
            });
            panelPuntuacion.add(estrellas[i]);
        }
        panelContenido.add(panelPuntuacion);
        panelContenido.add(Box.createVerticalStrut(18));

        // Comentario
        JLabel lblComentario = new JLabel("Comentario:");
        lblComentario.setFont(new Font("Arial", Font.PLAIN, 16));
        lblComentario.setForeground(texto);
        panelContenido.add(lblComentario);

        txtComentario = new JTextArea(4, 20);
        txtComentario.setFont(new Font("Arial", Font.PLAIN, 15));
        txtComentario.setLineWrap(true);
        txtComentario.setWrapStyleWord(true);
        txtComentario.setBackground(dark ? new Color(52, 73, 94) : Color.WHITE);
        txtComentario.setForeground(texto);
        JScrollPane scrollComentario = new JScrollPane(txtComentario);
        scrollComentario.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        panelContenido.add(scrollComentario);

        add(panelContenido, BorderLayout.CENTER);

        // Panel botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        panelBotones.setBackground(dark ? new Color(44, 62, 80) : new Color(236, 240, 241));

        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.setBackground(new Color(46, 204, 113));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFont(new Font("Arial", Font.BOLD, 16));
        btnEnviar.setFocusPainted(false);
        btnEnviar.setPreferredSize(new Dimension(110, 38));
        btnEnviar.setBorder(BorderFactory.createLineBorder(new Color(39, 174, 96), 2, true));
        btnEnviar.addActionListener(e -> enviarValoracion());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 16));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setPreferredSize(new Dimension(110, 38));
        btnCancelar.setBorder(BorderFactory.createLineBorder(new Color(192, 57, 43), 2, true));
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnEnviar);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        actualizarEstrellas();

        revalidate();
        repaint();
    }

    private void actualizarEstrellas() {
        for (int i = 0; i < 5; i++) {
            if (i < puntuacionSeleccionada) {
                estrellas[i].setText("★");
                estrellas[i].setForeground(new Color(241, 196, 15));
            } else {
                estrellas[i].setText("☆");
                estrellas[i].setForeground(new Color(189, 195, 199));
            }
        }
    }
    
    private void resaltarEstrellas(int hasta) {
        for (int i = 0; i < 5; i++) {
            if (i < hasta) {
                estrellas[i].setFont(new Font("Dialog", Font.BOLD, 28));
                estrellas[i].setText("★");
                estrellas[i].setForeground(new Color(241, 196, 15));
            } else {
                estrellas[i].setFont(new Font("Dialog", Font.BOLD, 28));
                estrellas[i].setText("☆");
                estrellas[i].setForeground(new Color(189, 195, 199));
            }
        }
    }

    private void enviarValoracion() {
        if (puntuacionSeleccionada == 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una puntuación.");
            return;
        }
        String comentario = txtComentario.getText().trim();
        try {
            services.TurismoService.getInstance().valorarRestaurante(idRestaurante, usuario.getId(), puntuacionSeleccionada, comentario);
            JOptionPane.showMessageDialog(this, "¡Gracias por tu valoración!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al enviar valoración: " + ex.getMessage());
        }
    }
}

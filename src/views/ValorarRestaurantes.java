package views;

import javax.swing.*;
import model.Restaurante;
import model.Usuario;
import java.awt.*;

public class ValorarRestaurantes extends JFrame {

    private int idRestaurante;
    private Usuario usuario;
    private JTextArea txtComentario;
    private int puntuacionSeleccionada = 0;
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
        setSize(700, 520);
        setMinimumSize(new Dimension(550, 420));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        actualizarTema();

        setVisible(true);
    }

    private void actualizarTema() {
        getContentPane().removeAll();

        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color bgGlobal = dark ? new Color(44, 62, 80) : new Color(236, 240, 241);
        Color fgPanel = dark ? Color.WHITE : new Color(44, 62, 80);

        // Panel título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(44, 62, 80));
        JLabel lblTitulo = new JLabel("Valorar Restaurante");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(18, 0, 10, 0));
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Panel central con fondo y borde redondeado
        JPanel panelCentral = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgGlobal);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
                g2.dispose();
            }
        };
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(24, 36, 24, 36));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nombre restaurante
        JLabel lblRestaurante;
        try {
            lblRestaurante = new JLabel("Restaurante: " + services.TurismoService.getInstance().obtenerRestaurantePorId(idRestaurante).getNombre());
        } catch (ClassNotFoundException e) {
            lblRestaurante = new JLabel("Restaurante: Información no disponible");
            e.printStackTrace();
        }
        lblRestaurante.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblRestaurante.setForeground(fgPanel);
        panelCentral.add(lblRestaurante, gbc);

        // Puntuación con estrellas
        gbc.gridy++;
        JPanel panelPuntuacion = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelPuntuacion.setOpaque(false);
        JLabel lblPuntuacion = new JLabel("Puntuación:");
        lblPuntuacion.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        lblPuntuacion.setForeground(fgPanel);
        panelPuntuacion.add(lblPuntuacion);

        for (int i = 0; i < 5; i++) {
            estrellas[i] = new JLabel("☆");
            estrellas[i].setFont(new Font("Segoe UI Symbol", Font.BOLD, 32));
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
        panelCentral.add(panelPuntuacion, gbc);

        // Comentario
        gbc.gridy++;
        JLabel lblComentario = new JLabel("Comentario:");
        lblComentario.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        lblComentario.setForeground(fgPanel);
        panelCentral.add(lblComentario, gbc);

        gbc.gridy++;
        txtComentario = new JTextArea(3, 20);
        txtComentario.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtComentario.setLineWrap(true);
        txtComentario.setWrapStyleWord(true);
        txtComentario.setBackground(dark ? new Color(52, 73, 94) : Color.WHITE);
        txtComentario.setForeground(fgPanel);
        txtComentario.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        JScrollPane scrollComentario = new JScrollPane(txtComentario);
        scrollComentario.setBorder(BorderFactory.createLineBorder(new Color(189, 195, 199)));
        scrollComentario.setPreferredSize(new Dimension(320, 80));
        panelCentral.add(scrollComentario, gbc);

        add(panelCentral, BorderLayout.CENTER);

        // Panel botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        panelBotones.setBackground(bgGlobal);

        JButton btnEnviar = new JButton("Enviar");
        btnEnviar.setBackground(new Color(46, 204, 113));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFont(new Font("Arial", Font.BOLD, 16));
        btnEnviar.setFocusPainted(false);
        getRootPane().setDefaultButton(btnEnviar);
        btnEnviar.setPreferredSize(new Dimension(120, 40));
        btnEnviar.setBorder(BorderFactory.createLineBorder(new Color(39, 174, 96), 2, true));
        btnEnviar.addActionListener(e -> enviarValoracion());

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 16));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setPreferredSize(new Dimension(120, 40));
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
                estrellas[i].setFont(new Font("Segoe UI Symbol", Font.BOLD, 32));
                estrellas[i].setText("★");
                estrellas[i].setForeground(new Color(241, 196, 15));
            } else {
                estrellas[i].setFont(new Font("Segoe UI Symbol", Font.BOLD, 32));
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
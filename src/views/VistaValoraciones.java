package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import dao.ValoracionDAO;
import model.Usuario;
import model.ValoracionRuta;
import utils.I18n;

import java.util.List;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class VistaValoraciones extends JFrame {

    private static VistaValoraciones instance;
    private JTable tablaValoraciones;
    private DefaultTableModel modeloTabla;
    private JTextArea txtComentario;
    private JButton btnEnviar, btnCancelar;
    private int idRuta;
    private Usuario usuario;
    private int puntuacionSeleccionada = 0;
    private JLabel[] estrellas = new JLabel[5];
    private VistaRutas parentVistaRutas;

    public static VistaValoraciones getInstance(VistaRutas parent, int idRuta, String nombreRuta, Usuario usuario) {
        if (instance == null || !instance.isVisible()) {
            instance = new VistaValoraciones(parent, idRuta, nombreRuta, usuario);
        }
        instance.toFront();
        return instance;
    }

    public VistaValoraciones(VistaRutas parent, int idRuta, String nombreRuta, Usuario usuario) {
        this.parentVistaRutas = parent;
        this.usuario = usuario;
        this.idRuta = idRuta;
        setTitle("Valorar Ruta: " + nombreRuta);
        setSize(950, 650);
        setMinimumSize(new Dimension(800, 500));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
        Color bgGlobal = dark ? new Color(34, 45, 65) : new Color(235, 241, 250);
        Color fgPanel = dark ? Color.WHITE : new Color(44, 62, 80);
        Color borderColor = new Color(52, 152, 219);

        JPanel panelFondo = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(bgGlobal);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelFondo.setOpaque(true);

        // Panel principal con esquinas redondeadas
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgGlobal);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 36, 36);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(32, 48, 32, 48));

        // Título
        JLabel lblTitulo = new JLabel("Valorar " + nombreRuta);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblTitulo.setForeground(borderColor);
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(260, 2));
        separator.setForeground(borderColor);
        separator.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(2));
        panel.add(separator);
        panel.add(Box.createVerticalStrut(18));

        // Tabla de valoraciones en panel con fondo y borde redondeado
        JPanel panelTabla = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(dark ? new Color(44, 62, 80) : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.dispose();
            }
        };
        panelTabla.setOpaque(false);
        panelTabla.setLayout(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        modeloTabla = new DefaultTableModel(new String[] {
                I18n.t("columna.usuario"),
                I18n.t("columna.puntuacion"),
                I18n.t("columna.comentario") }, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tablaValoraciones = new JTable(modeloTabla);
        tablaValoraciones.getTableHeader().setReorderingAllowed(false);
        tablaValoraciones.setBackground(dark ? new Color(44, 62, 80) : Color.WHITE);
        tablaValoraciones.setForeground(fgPanel);
        tablaValoraciones.setRowHeight(28);
        tablaValoraciones.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        tablaValoraciones.getTableHeader().setBackground(borderColor);
        tablaValoraciones.getTableHeader().setForeground(Color.WHITE);
        tablaValoraciones.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 15));

        JScrollPane scrollTabla = new JScrollPane(tablaValoraciones);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder());
        scrollTabla.setPreferredSize(new Dimension(600, 180));
        scrollTabla.getViewport().setBackground(dark ? new Color(44, 62, 80) : Color.WHITE);

        panelTabla.add(scrollTabla, BorderLayout.CENTER);
        panel.add(panelTabla);
        panel.add(Box.createVerticalStrut(18));

        // Panel formulario valoración
        JPanel panelFormulario = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(dark ? new Color(44, 62, 80) : Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.dispose();
            }
        };
        panelFormulario.setOpaque(false);
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
        panelFormulario.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Puntuación estrellas
        JPanel panelPuntuacion = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelPuntuacion.setOpaque(false);
        JLabel lblPuntuacion = new JLabel("Puntuación:");
        lblPuntuacion.setFont(new Font("Arial", Font.PLAIN, 16));
        lblPuntuacion.setForeground(fgPanel);
        panelPuntuacion.add(lblPuntuacion);

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
        panelFormulario.add(panelPuntuacion);
        panelFormulario.add(Box.createVerticalStrut(10));

        JPanel panelComentario = new JPanel();
        panelComentario.setLayout(new BorderLayout());
        panelComentario.setOpaque(false);

        JLabel lblComentario = new JLabel("Comentario:");
        lblComentario.setFont(new Font("Arial", Font.PLAIN, 16));
        lblComentario.setForeground(fgPanel);
        lblComentario.setBorder(BorderFactory.createEmptyBorder(0, 2, 6, 0)); // pequeño margen inferior

        panelComentario.add(lblComentario, BorderLayout.NORTH);

        txtComentario = new JTextArea(3, 20);
        txtComentario.setFont(new Font("Arial", Font.PLAIN, 15));
        txtComentario.setLineWrap(true);
        txtComentario.setWrapStyleWord(true);
        txtComentario.setBackground(dark ? new Color(52, 73, 94) : Color.WHITE);
        txtComentario.setForeground(fgPanel);
        txtComentario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 2, true),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        txtComentario.setMaximumSize(new Dimension(600, 80));
        JScrollPane scrollComentario = new JScrollPane(txtComentario);
        scrollComentario.setBorder(BorderFactory.createEmptyBorder());
        scrollComentario.setBackground(dark ? new Color(52, 73, 94) : Color.WHITE);
        scrollComentario.setPreferredSize(new Dimension(600, 80));
        scrollComentario.setMaximumSize(new Dimension(600, 80));

        panelComentario.add(scrollComentario, BorderLayout.CENTER);

        panelFormulario.add(panelComentario);

        panel.add(panelFormulario);
        panel.add(Box.createVerticalStrut(18));

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(bgGlobal);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panelBotones.setOpaque(false);

        btnEnviar = new JButton(I18n.t("boton.enviar"));
        btnEnviar.setBackground(new Color(46, 204, 113));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnEnviar.setFocusPainted(false);
        btnEnviar.setPreferredSize(new Dimension(180, 38));

        btnCancelar = new JButton(I18n.t("boton.cancelar"));
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setPreferredSize(new Dimension(180, 38));

        panelBotones.add(btnEnviar);
        panelBotones.add(btnCancelar);

        panel.add(panelBotones);

        panelFondo.add(panel, BorderLayout.CENTER);
        setContentPane(panelFondo);

        // Responsividad: no dejar que los cuadros y tabla se hagan demasiado pequeños
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int w = getWidth();
                int minWidth = 800;
                int minHeight = 500;
                if (w < minWidth) setSize(minWidth, getHeight());
                if (getHeight() < minHeight) setSize(getWidth(), minHeight);
            }
        });

        btnEnviar.addActionListener(e -> enviarValoracion());
        btnCancelar.addActionListener(e -> {
            dispose();
            if (parentVistaRutas != null) parentVistaRutas.cargarRutas();
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (parentVistaRutas != null) {
                    parentVistaRutas.cargarRutas();
                }
            }
        });

        cargarValoraciones();
        actualizarEstrellas();

        setVisible(true);
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

    private void cargarValoraciones() {
        try {
            ValoracionDAO dao = new ValoracionDAO();
            List<ValoracionRuta> valoraciones = dao.obtenerValoracionesRuta(idRuta);
            modeloTabla.setRowCount(0);

            for (ValoracionRuta v : valoraciones) {
                modeloTabla.addRow(new Object[] {
                        "Usuario ID: " + v.getIdUsuario(),
                        v.getPuntuacion(),
                        v.getComentario()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar valoraciones: " + e.getMessage());
        }
    }

    private void enviarValoracion() {
        if (puntuacionSeleccionada == 0) {
            JOptionPane.showMessageDialog(this, "Selecciona una puntuación.");
            return;
        }
        String comentario = txtComentario.getText().trim();
        try {
            ValoracionRuta valoracion = new ValoracionRuta(
                    0,
                    usuario.getId(),
                    idRuta,
                    puntuacionSeleccionada,
                    comentario);

            ValoracionDAO dao = new ValoracionDAO();
            boolean registrada = dao.registrarValoracionRuta(valoracion);

            if (registrada) {
                JOptionPane.showMessageDialog(this, "¡Valoración enviada con éxito!");
                cargarValoraciones();
                txtComentario.setText("");
                puntuacionSeleccionada = 0;
                actualizarEstrellas();
                if (parentVistaRutas != null) parentVistaRutas.cargarRutas();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar la valoración.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
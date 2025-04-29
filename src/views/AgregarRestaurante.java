package views;

import model.Restaurante;
import services.TurismoService;
import utils.UIUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;

public class AgregarRestaurante extends JFrame {

    private JTextField txtNombre, txtUbicacion;
    private JLabel lblImagen;
    private JButton btnImagen, btnGuardar, btnCancelar;
    private File imagenPerfil;
    private int valoracionSeleccionada = 0;
    private JLabel[] estrellas = new JLabel[5];

    public AgregarRestaurante() {
        setTitle("Nuevo Restaurante");
        setSize(520, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));

        ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
        Color bgPanel = theme == ThemeManager.Theme.DARK ? new Color(44, 62, 80) : new Color(245, 247, 250);
        Color fgPanel = theme == ThemeManager.Theme.DARK ? Color.WHITE : new Color(44, 62, 80);
        Color borderColor = new Color(52, 152, 219);

        // Panel principal redondeado con sombra
        JPanel panel = UIUtils.crearPanelRedondeado(bgPanel, 32, 2, borderColor);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Título
        JLabel lblTitulo = new JLabel("Nuevo Restaurante");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 26));
        lblTitulo.setForeground(fgPanel);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(18));

        // Panel campos
        JPanel panelCampos = new JPanel();
        panelCampos.setOpaque(false);
        panelCampos.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // Nombre
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 16));
        lblNombre.setForeground(fgPanel);
        panelCampos.add(lblNombre, gbc);

        gbc.gridy++;
        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 15));
        txtNombre.setBackground(theme == ThemeManager.Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
        txtNombre.setForeground(fgPanel);
        txtNombre.setCaretColor(fgPanel);
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        txtNombre.setPreferredSize(new Dimension(250, 36));
        panelCampos.add(txtNombre, gbc);

        // Ubicación
        gbc.gridy++;
        JLabel lblUbicacion = new JLabel("Ubicación:");
        lblUbicacion.setFont(new Font("Arial", Font.PLAIN, 16));
        lblUbicacion.setForeground(fgPanel);
        panelCampos.add(lblUbicacion, gbc);

        gbc.gridy++;
        txtUbicacion = new JTextField();
        txtUbicacion.setFont(new Font("Arial", Font.PLAIN, 15));
        txtUbicacion.setBackground(theme == ThemeManager.Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
        txtUbicacion.setForeground(fgPanel);
        txtUbicacion.setCaretColor(fgPanel);
        txtUbicacion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        txtUbicacion.setPreferredSize(new Dimension(250, 36));
        panelCampos.add(txtUbicacion, gbc);

        panelCampos.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(panelCampos);
        panel.add(Box.createVerticalStrut(18));

        // Imagen
        lblImagen = new JLabel("Sin imagen", JLabel.CENTER);
        lblImagen.setPreferredSize(new Dimension(160, 120));
        lblImagen.setFont(new Font("Arial", Font.ITALIC, 14));
        lblImagen.setForeground(theme == ThemeManager.Theme.DARK ? new Color(120, 120, 120) : new Color(160, 160, 160));
        lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblImagen.setBorder(BorderFactory.createLineBorder(borderColor, 1, true));
        panel.add(lblImagen);

        btnImagen = UIUtils.crearBotonRedondeado("Seleccionar Imagen", borderColor, 18);
        ThemeManager.setComponentTheme(btnImagen, theme);
        btnImagen.setFont(new Font("Arial", Font.BOLD, 14));
        btnImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnImagen.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnImagen.addActionListener(e -> seleccionarImagen());
        panel.add(Box.createVerticalStrut(6));
        panel.add(btnImagen);
        panel.add(Box.createVerticalStrut(14));

        // Panel de estrellas (valoración inicial) centrado SIEMPRE
        JPanel panelEstrellasWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelEstrellasWrapper.setOpaque(false);

        JPanel panelEstrellas = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panelEstrellas.setOpaque(false);
        JLabel lblValoracion = new JLabel("Valoración inicial:");
        lblValoracion.setFont(new Font("Arial", Font.PLAIN, 16));
        lblValoracion.setForeground(fgPanel);
        panelEstrellas.add(lblValoracion);

        for (int i = 0; i < 5; i++) {
            estrellas[i] = new JLabel("☆");
            estrellas[i].setFont(new Font("Segoe UI Symbol", Font.BOLD, 28));
            estrellas[i].setForeground(new Color(241, 196, 15));
            final int estrellaIndex = i;
            estrellas[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            estrellas[i].addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    valoracionSeleccionada = estrellaIndex + 1;
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
            panelEstrellas.add(estrellas[i]);
        }
        panelEstrellasWrapper.add(panelEstrellas);
        panel.add(panelEstrellasWrapper);
        panel.add(Box.createVerticalStrut(18));

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 0));
        panelBotones.setOpaque(false);

        btnGuardar = UIUtils.crearBotonRedondeado("Guardar", new Color(46, 204, 113), 24);
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 18));
        btnGuardar.setPreferredSize(new Dimension(170, 48));
        btnGuardar.setFocusable(true);
        btnGuardar.setEnabled(true);
        btnGuardar.addActionListener(e -> agregarRestaurante());
        panelBotones.add(btnGuardar);

        btnCancelar = UIUtils.crearBotonRedondeado("Cancelar", new Color(231, 76, 60), 24);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 18));
        btnCancelar.setPreferredSize(new Dimension(170, 48));
        btnCancelar.setFocusable(true);
        btnCancelar.setEnabled(true);
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(btnCancelar);

        panel.add(Box.createVerticalGlue());
        panel.add(panelBotones);
        panel.add(Box.createVerticalStrut(10));

        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void seleccionarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            imagenPerfil = fileChooser.getSelectedFile();
            ImageIcon icon = new ImageIcon(imagenPerfil.getAbsolutePath());
            lblImagen.setIcon(new ImageIcon(icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)));
            lblImagen.setText("");
        }
    }

    private void actualizarEstrellas() {
        for (int i = 0; i < 5; i++) {
            estrellas[i].setText(i < valoracionSeleccionada ? "★" : "☆");
        }
    }

    private void resaltarEstrellas(int hasta) {
        for (int i = 0; i < 5; i++) {
            estrellas[i].setText(i < hasta ? "★" : "☆");
        }
    }

    private void agregarRestaurante() {
        try {
            String nombre = txtNombre.getText().trim();
            String ubicacion = txtUbicacion.getText().trim();

            if (nombre.isEmpty() || ubicacion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (valoracionSeleccionada == 0) {
                JOptionPane.showMessageDialog(this, "Selecciona una valoración inicial con estrellas.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (new dao.RestauranteDAO().obtenerRestaurantePorNombre(nombre) != null) {
                JOptionPane.showMessageDialog(this, "Ya existe un restaurante con ese nombre.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            byte[] imagenBytes = null;
            if (imagenPerfil != null) {
                imagenBytes = Files.readAllBytes(imagenPerfil.toPath());
            }

            Restaurante restaurante = new Restaurante(0, nombre, ubicacion, (float) valoracionSeleccionada,
                    imagenBytes);
            boolean guardado = TurismoService.getInstance().agregarRestaurante(restaurante);

            if (guardado) {
                Restaurante restInsertado = new dao.RestauranteDAO().obtenerRestaurantePorNombre(nombre);
                if (restInsertado != null) {
                    int idUsuario = 1;
                    dao.ValoracionDAO valoracionDAO = new dao.ValoracionDAO();
                    valoracionDAO.registrarValoracionRestaurante(
                            new model.ValoracionRestaurante(0, idUsuario, restInsertado.getId(), valoracionSeleccionada,
                                    "Valoración inicial"));
                }
                JOptionPane.showMessageDialog(this, "Restaurante agregado correctamente.");
                dispose();

            } else {
                JOptionPane.showMessageDialog(this, "No se pudo agregar el restaurante.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar restaurante: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
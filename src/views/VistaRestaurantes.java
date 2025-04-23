package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import model.Restaurante;
import model.Usuario;
import services.TurismoService;

public class VistaRestaurantes extends JFrame {
    private static VistaRestaurantes instance;

    private JTable tablaRestaurantes;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregar, btnEliminar, btnVerDetalles, btnValorar, btnVolver;
    private Usuario usuario;

    public static VistaRestaurantes getInstance(Usuario usuario) {
        if (instance == null || !instance.isVisible()) {
            instance = new VistaRestaurantes(usuario);
        }
        instance.toFront();
        return instance;
    }

    public VistaRestaurantes(Usuario usuario) {
        this.usuario = usuario;
        inicializarComponentes();
        cargarRestaurantes();
        ThemeManager.setTheme(ThemeManager.getCurrentTheme(), this);
    }

    private void inicializarComponentes() {
        setTitle("Gestión de Restaurantes");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(44, 62, 80));
        JLabel lblTitulo = new JLabel("Gestión de Restaurantes");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);

        ImageIcon iconoAct = new ImageIcon("assets/carga.png");

        JButton btnActualizar = new JButton(iconoAct);
        btnActualizar.setFont(new Font("Arial", Font.BOLD, 14));
        btnActualizar.setBackground(new Color(52, 152, 219));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setPreferredSize(new Dimension(50, 30));
        btnActualizar.addActionListener(e -> cargarRestaurantes());
        panelTitulo.add(btnActualizar, BorderLayout.EAST);

        add(panelTitulo, BorderLayout.NORTH);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnas = { "ID", "Nombre", "Ubicación", "Valoración" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaRestaurantes = new JTable(modeloTabla);
        tablaRestaurantes.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollTabla = new JScrollPane(tablaRestaurantes);
        panelTabla.add(scrollTabla, BorderLayout.CENTER);
        add(panelTabla, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(236, 240, 241));

        btnAgregar = crearBoton("Agregar Restaurante", new Color(52, 152, 219));
        btnEliminar = crearBoton("Eliminar Restaurante", new Color(231, 76, 60));
        btnVerDetalles = crearBoton("Ver Detalles", new Color(46, 204, 113));
        btnValorar = crearBoton("Valorar Restaurante", new Color(241, 196, 15));
        btnVolver = crearBoton("Volver al Menú", new Color(52, 152, 219));

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnVerDetalles);
        panelBotones.add(btnValorar);
        panelBotones.add(btnVolver);

        add(panelBotones, BorderLayout.SOUTH);

        // Acciones de botones
        btnAgregar.addActionListener(e -> new AgregarRestaurante().setVisible(true));
        btnEliminar.addActionListener(e -> eliminarRestaurante());
        btnVerDetalles.addActionListener(e -> verDetallesRestaurante());
        btnValorar.addActionListener(e -> valorarRestaurante());
        btnVolver.addActionListener(e -> {
            MenuPrincipal.getInstance(usuario).setVisible(true);
            dispose();
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                instance = null;
            }
        });

        setVisible(true);
    }

    private JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        return btn;
    }

    private void cargarRestaurantes() {
        modeloTabla.setRowCount(0);
        try {
            List<Restaurante> restaurantes = TurismoService.getInstance().obtenerRestaurantes();
            for (Restaurante r : restaurantes) {
                double media = TurismoService.getInstance().obtenerValoracionMediaRestaurante(r.getId());
                modeloTabla.addRow(new Object[] {
                        r.getId(), r.getNombre(), r.getUbicacion(), String.format("%.1f", media)
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar restaurantes: " + ex.getMessage());
        }
    }

    private void eliminarRestaurante() {
        int fila = tablaRestaurantes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un restaurante para eliminar.");
            return;
        }
        int idRestaurante = (int) modeloTabla.getValueAt(fila, 0);
        try {
            boolean eliminado = TurismoService.getInstance().eliminarRestaurante(idRestaurante);
            if (eliminado) {
                modeloTabla.removeRow(fila);
                TurismoService.getInstance().registrarActividad(usuario.getId(),
                        "Eliminó el restaurante con ID: " + idRestaurante);
                JOptionPane.showMessageDialog(this, "Restaurante eliminado.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar el restaurante.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void verDetallesRestaurante() {
        int fila = tablaRestaurantes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un restaurante.");
            return;
        }

        String nombre = (String) modeloTabla.getValueAt(fila, 1);
        String ubicacion = (String) modeloTabla.getValueAt(fila, 2);
        String valoracionStr = modeloTabla.getValueAt(fila, 3).toString();

        try {
            int idRestaurante = (int) modeloTabla.getValueAt(fila, 0);
            Restaurante restaurante = TurismoService.getInstance().obtenerRestaurantePorId(idRestaurante);

            ImageIcon icono;
            if (restaurante.getImagen() != null) {
                try {
                    java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(restaurante.getImagen());
                    BufferedImage original = javax.imageio.ImageIO.read(bais);
                    Image img = original.getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                    // imagen circular
                    BufferedImage circleBuffer = new BufferedImage(120, 120, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2 = circleBuffer.createGraphics();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, 120, 120));
                    g2.drawImage(img, 0, 0, 120, 120, null);
                    g2.setClip(null);
                    g2.setStroke(new BasicStroke(3));
                    g2.setColor(new Color(52, 152, 219));
                    g2.drawOval(2, 2, 116, 116);
                    g2.dispose();
                    icono = new ImageIcon(circleBuffer);
                } catch (Exception ex) {
                    icono = new ImageIcon("assets/imagen.png");
                }
            } else {
                icono = new ImageIcon("assets/imagen.png");
            }

            JLabel lblImagen = new JLabel(icono);
            lblImagen.setHorizontalAlignment(JLabel.CENTER);

            boolean dark = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK;
            Color fondo = dark ? new Color(44, 62, 80) : Color.WHITE;
            Color texto = dark ? Color.WHITE : new Color(44, 62, 80);

            JPanel panelDatos = new JPanel();
            panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));
            panelDatos.setBackground(fondo);
            panelDatos.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            JLabel lblNombre = new JLabel(nombre);
            lblNombre.setFont(new Font("Arial", Font.BOLD, 22));
            lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblNombre.setForeground(texto);

            JLabel lblUbicacion = new JLabel("Ubicación: " + ubicacion);
            lblUbicacion.setFont(new Font("Arial", Font.PLAIN, 16));
            lblUbicacion.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblUbicacion.setForeground(texto);

            JLabel lblValoracion = new JLabel("Valoración: " + valoracionStr + " ☆");
            lblValoracion.setFont(new Font("Segoe UI Symbol", Font.BOLD, 18));
            lblValoracion.setForeground(texto);
            lblValoracion.setAlignmentX(Component.CENTER_ALIGNMENT);

            panelDatos.add(Box.createVerticalStrut(10));
            panelDatos.add(lblNombre);
            panelDatos.add(Box.createVerticalStrut(10));
            panelDatos.add(lblUbicacion);
            panelDatos.add(Box.createVerticalStrut(10));
            panelDatos.add(lblValoracion);
            panelDatos.add(Box.createVerticalStrut(10));

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBackground(fondo);
            panel.add(lblImagen, BorderLayout.NORTH);
            panel.add(panelDatos, BorderLayout.CENTER);

            JButton btnOk = new JButton("Ok");
            btnOk.setFocusPainted(false);
            btnOk.setFont(new Font("Arial", Font.BOLD, dark ? 14 : 16));
            btnOk.setPreferredSize(new Dimension(dark ? 90 : 120, dark ? 32 : 40));
            btnOk.setForeground(Color.WHITE);
            Color azulClaro = new Color(52, 152, 219);
            btnOk.setBackground(azulClaro);
            btnOk.setContentAreaFilled(true);
            btnOk.setOpaque(true);
            btnOk.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnOk.setBorder(new javax.swing.border.CompoundBorder(
                    BorderFactory.createLineBorder(azulClaro, 2, true),
                    new javax.swing.border.EmptyBorder(8, 30, 8, 30)));
            if (dark) {
                btnOk.setBorder(new javax.swing.border.EmptyBorder(8, 30, 8, 30));
                btnOk.setContentAreaFilled(true);
                btnOk.setOpaque(true);
                btnOk.setBackground(azulClaro);
                btnOk.setForeground(Color.WHITE);
                btnOk.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btnOk.setBorder(BorderFactory.createLineBorder(azulClaro, 2, true));
            } else {
                btnOk.setBorder(new javax.swing.border.EmptyBorder(8, 30, 8, 30));
                btnOk.setContentAreaFilled(true);
                btnOk.setOpaque(true);
                btnOk.setBackground(azulClaro);
                btnOk.setForeground(Color.WHITE);
                btnOk.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                btnOk.setBorder(BorderFactory.createLineBorder(azulClaro, 2, true));
            }

            // Panel para el botón OK
            JPanel panelBoton = new JPanel();
            panelBoton.setBackground(fondo);
            panelBoton.setLayout(new FlowLayout(FlowLayout.CENTER, 0, dark ? 0 : 10)); // Sin margen extra en oscuro
            panelBoton.add(btnOk);

            JDialog dialog = new JDialog(this, "Detalles del Restaurante", true);
            dialog.setLayout(new BorderLayout());
            dialog.getContentPane().setBackground(fondo);
            dialog.add(panel, BorderLayout.CENTER);

            // Botón más cerca de la información en oscuro, y ventana más pequeña
            if (dark) {
                dialog.add(panelBoton, BorderLayout.SOUTH);
                dialog.setSize(340, 370); // Más pequeño en oscuro
            } else {
                dialog.add(panelBoton, BorderLayout.SOUTH);
                dialog.setSize(380, 420); // Tamaño original en claro
            }

            btnOk.addActionListener(e -> dialog.dispose());
            dialog.setLocationRelativeTo(this);
            dialog.setResizable(false);
            dialog.setVisible(true);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los detalles del restaurante: " + e.getMessage());
        }
    }

    private void valorarRestaurante() {
        int fila = tablaRestaurantes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un restaurante para valorar.");
            return;
        }
        int idRestaurante = (int) modeloTabla.getValueAt(fila, 0);
        String nombreRestaurante = (String) modeloTabla.getValueAt(fila, 1);

        ValorarRestaurantes ventana = new ValorarRestaurantes(nombreRestaurante, usuario);
        ventana.setVisible(true);

        ventana.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                try {
                    double media = services.TurismoService.getInstance()
                            .obtenerValoracionMediaRestaurante(idRestaurante);
                    modeloTabla.setValueAt(String.format("%.1f", media), fila, 3);
                } catch (Exception ex) {
                    // Ignorar error de actualización de media
                }
            }
        });
    }
}
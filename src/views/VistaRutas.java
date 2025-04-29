package views;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

import utils.I18n;
import utils.UIUtils;
import views.ThemeManager.RoundedBorder;
import dao.*;
import model.Ruta;
import model.Usuario;
import services.TurismoService;

public class VistaRutas extends JFrame {
    private static VistaRutas instance;

    private JTable tablaRutas;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregar, btnEliminar, btnVerDetalles, btnValorar, btnReservar, btnVolver, btnEditar;
    private Usuario usuario;
    private JPanel panelFiltrosLateral;
    private boolean filtrosVisibles = false;
    private JTextField txtBuscarNombre = new JTextField(14);
    private JSlider sliderPrecioMax = new JSlider(0, 500, 500);;
    private JLabel lblPrecioMax = new JLabel("Máximo: $500");;

    public static VistaRutas getInstance(Usuario usuario) {
        if (instance == null || !instance.isVisible()) {
            instance = new VistaRutas(usuario);
        }
        instance.toFront();
        return instance;
    }

    public VistaRutas(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Gestión de Rutas");
        setSize(600, 7000);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        inicializarComponentes();
        cargarRutas();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                instance = null;
            }
        });
        ThemeManager.setTheme(ThemeManager.getCurrentTheme(), this);
    }

    private void inicializarComponentes() {
        // Panel cabecera con título y botón de filtros
        JPanel panelCabecera = new JPanel(new BorderLayout());
        panelCabecera.setBackground(new Color(236, 240, 241));

        JLabel lblTitulo = new JLabel(I18n.t("titulo.rutas"), SwingConstants.CENTER);
        lblTitulo.setFont(ThemeManager.FUENTE_TITULO);
        lblTitulo.setForeground(new Color(44, 62, 80));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        JButton btnMostrarFiltros = new JButton("Filtros");
        btnMostrarFiltros.setFont(new Font("Arial", Font.BOLD, 14));
        btnMostrarFiltros.setBackground(new Color(52, 152, 219));
        btnMostrarFiltros.setForeground(Color.WHITE);
        btnMostrarFiltros.setFocusPainted(false);
        btnMostrarFiltros.setPreferredSize(new Dimension(100, 28));
        btnMostrarFiltros.addActionListener(e -> togglePanelFiltros());

        panelCabecera.add(lblTitulo, BorderLayout.CENTER);
        panelCabecera.add(btnMostrarFiltros, BorderLayout.EAST);

        // Panel principal con BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(new Color(236, 240, 241));

        // Panel de filtros lateral (inicialmente oculto)
        panelFiltrosLateral = crearPanelFiltrosLateral();
        panelFiltrosLateral.setVisible(false);
        panelPrincipal.add(panelFiltrosLateral, BorderLayout.WEST);

        // Tabla directamente en el centro del panel principal, sin panel extra
        String[] columnas = { "ID", "Nombre", "Descripción", "Precio", "Dificultad" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaRutas = new JTable(modeloTabla);

        // Permitir ordenar columnas haciendo clic en el encabezado
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(modeloTabla);
        tablaRutas.setRowSorter(sorter);

        tablaRutas.getTableHeader().setReorderingAllowed(false);
        tablaRutas.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaRutas.setRowHeight(22);
        JScrollPane scrollTabla = new JScrollPane(tablaRutas);
        scrollTabla.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        Color panelBotonesBg = ThemeManager.getCurrentTheme() == ThemeManager.Theme.DARK
                ? new Color(44, 62, 80)
                : new Color(220, 230, 241);
        panelBotones.setBackground(panelBotonesBg);

        btnAgregar = UIUtils.crearBotonRedondeado(I18n.t("boton.agregar"), new Color(52, 152, 219), 24);
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 18));
        btnAgregar.setPreferredSize(new Dimension(170, 48));
        ThemeManager.setComponentTheme(btnAgregar, ThemeManager.getCurrentTheme());

        btnEliminar = UIUtils.crearBotonRedondeado(I18n.t("boton.eliminar"), new Color(231, 76, 60), 24);
        btnEliminar.setFont(new Font("Arial", Font.BOLD, 18));
        btnEliminar.setPreferredSize(new Dimension(170, 48));
        ThemeManager.setComponentTheme(btnEliminar, ThemeManager.getCurrentTheme());

        btnVerDetalles = UIUtils.crearBotonRedondeado(I18n.t("boton.detalles"), new Color(46, 204, 113), 24);
        btnVerDetalles.setFont(new Font("Arial", Font.BOLD, 18));
        btnVerDetalles.setPreferredSize(new Dimension(170, 48));
        ThemeManager.setComponentTheme(btnVerDetalles, ThemeManager.getCurrentTheme());

        btnValorar = UIUtils.crearBotonRedondeado(I18n.t("boton.valorar"), new Color(241, 196, 15), 24);
        btnValorar.setFont(new Font("Arial", Font.BOLD, 18));
        btnValorar.setPreferredSize(new Dimension(170, 48));
        ThemeManager.setComponentTheme(btnValorar, ThemeManager.getCurrentTheme());

        btnReservar = UIUtils.crearBotonRedondeado(I18n.t("boton.reservar"), new Color(46, 204, 113), 24);
        btnReservar.setFont(new Font("Arial", Font.BOLD, 18));
        btnReservar.setPreferredSize(new Dimension(170, 48));
        ThemeManager.setComponentTheme(btnReservar, ThemeManager.getCurrentTheme());

        btnEditar = UIUtils.crearBotonRedondeado(I18n.t("boton.editar"), new Color(241, 196, 15), 24);
        btnEditar.setFont(new Font("Arial", Font.BOLD, 18));
        btnEditar.setPreferredSize(new Dimension(170, 48));
        ThemeManager.setComponentTheme(btnEditar, ThemeManager.getCurrentTheme());

        btnVolver = UIUtils.crearBotonRedondeado(I18n.t("boton.volver"), new Color(52, 152, 219), 24);
        btnVolver.setFont(new Font("Arial", Font.BOLD, 18));
        btnVolver.setPreferredSize(new Dimension(170, 48));
        ThemeManager.setComponentTheme(btnVolver, ThemeManager.getCurrentTheme());

        btnAgregar.setBorder(new RoundedBorder(panelBotonesBg, 3, 24));
        btnEliminar.setBorder(new RoundedBorder(panelBotonesBg, 3, 24));
        btnVerDetalles.setBorder(new RoundedBorder(panelBotonesBg, 3, 24));
        btnValorar.setBorder(new RoundedBorder(panelBotonesBg, 3, 24));
        btnReservar.setBorder(new RoundedBorder(panelBotonesBg, 3, 24));
        btnEditar.setBorder(new RoundedBorder(panelBotonesBg, 3, 24));
        btnVolver.setBorder(new RoundedBorder(panelBotonesBg, 3, 24));

        btnEliminar.setVisible(usuario.isAdmin());
        btnEditar.setVisible(usuario.isAdmin());

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnVerDetalles);
        panelBotones.add(btnValorar);
        panelBotones.add(btnReservar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnVolver);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        // Añadir cabecera y panel principal al frame
        setLayout(new BorderLayout());
        add(panelCabecera, BorderLayout.NORTH);
        add(panelPrincipal, BorderLayout.CENTER);

        // Listeners de botones igual que antes...
        btnAgregar.addActionListener(e -> {
            new AgregarRuta() {
                @Override
                public void dispose() {
                    super.dispose();
                    cargarRutas();
                }
            }.setVisible(true);
        });
        btnEliminar.addActionListener(e -> eliminarRuta());
        btnVerDetalles.addActionListener(e -> verDetallesRuta());
        btnValorar.addActionListener(e -> valorarRuta());
        btnReservar.addActionListener(e -> reservarRuta());
        btnVolver.addActionListener(e -> {
            new MenuPrincipal(usuario).setVisible(true);
            dispose();
        });
        btnEditar.addActionListener(e -> {
            int fila = tablaRutas.getSelectedRow();
            if (fila == -1) {
                UIUtils.mostrarError(this, I18n.t("mensaje.selecciona.ruta"));
                return;
            }
            int idRuta = (int) modeloTabla.getValueAt(fila, 0);
            Ruta ruta;
            try {
                ruta = TurismoService.getInstance().obtenerRutaPorId(idRuta);
            } catch (ClassNotFoundException ex) {
                UIUtils.mostrarError(this, I18n.t("mensaje.error.obtener.ruta") + ": " + ex.getMessage());
                return;
            }
            new EditarRuta(ruta) {
                @Override
                public void dispose() {
                    super.dispose();
                    cargarRutas();
                }
            }.setVisible(true);
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                instance = null;
            }
        });
        setVisible(true);
    }

    private JPanel crearPanelFiltrosLateral() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(225, 232, 239));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 2, new Color(52, 152, 219)));
        panel.setPreferredSize(new Dimension(270, 0));

        JLabel lblFiltros = new JLabel("Filtros de búsqueda");
        lblFiltros.setFont(new Font("Arial", Font.BOLD, 17));
        lblFiltros.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblFiltros.setBorder(BorderFactory.createEmptyBorder(18, 0, 18, 0));
        panel.add(lblFiltros);

        // Filtro nombre
        JLabel lblBuscar = new JLabel("🔍 Nombre:");
        lblBuscar.setFont(new Font("Dialog", Font.PLAIN, 15));
        lblBuscar.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblBuscar);

        txtBuscarNombre.setMaximumSize(new Dimension(220, 30));
        txtBuscarNombre.setFont(new Font("Arial", Font.PLAIN, 15));
        txtBuscarNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        panel.add(txtBuscarNombre);

        panel.add(Box.createVerticalStrut(16));

        // Filtro precio
        JLabel lblSlider = new JLabel("💰 Precio máximo:");
        lblSlider.setFont(new Font("Dialog", Font.PLAIN, 15));
        lblSlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblSlider);

        sliderPrecioMax.setMajorTickSpacing(200);
        sliderPrecioMax.setMinorTickSpacing(50);
        sliderPrecioMax.setPaintTicks(true);
        sliderPrecioMax.setPaintLabels(true);
        sliderPrecioMax.setBackground(new Color(225, 232, 239));
        sliderPrecioMax.setMaximumSize(new Dimension(220, 40));
        panel.add(sliderPrecioMax);

        lblPrecioMax.setFont(new Font("Arial", Font.PLAIN, 14));
        lblPrecioMax.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblPrecioMax);

        sliderPrecioMax.addChangeListener(e -> lblPrecioMax.setText("Máximo: $" + sliderPrecioMax.getValue()));

        panel.add(Box.createVerticalStrut(18));

        // Botón filtrar
        JButton btnFiltrar = new JButton("Aplicar filtros");
        btnFiltrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnFiltrar.setBackground(new Color(52, 152, 219));
        btnFiltrar.setForeground(Color.WHITE);
        btnFiltrar.setFocusPainted(false);
        btnFiltrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnFiltrar.setMaximumSize(new Dimension(180, 32));
        btnFiltrar.addActionListener(e -> {
            cargarRutas();
            togglePanelFiltros();
        });
        panel.add(btnFiltrar);

        panel.add(Box.createVerticalStrut(10));

        // Botón cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Arial", Font.PLAIN, 13));
        btnCerrar.setBackground(new Color(189, 195, 199));
        btnCerrar.setForeground(Color.BLACK);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrar.setMaximumSize(new Dimension(180, 28));
        btnCerrar.addActionListener(e -> togglePanelFiltros());
        panel.add(btnCerrar);

        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private void togglePanelFiltros() {
        filtrosVisibles = !filtrosVisibles;
        panelFiltrosLateral.setVisible(filtrosVisibles);
        revalidate();
        repaint();
    }

    private void cargarRutas() {
        int selectedRow = tablaRutas.getSelectedRow();
        Object selectedId = selectedRow != -1 ? modeloTabla.getValueAt(selectedRow, 0) : null;

        modeloTabla.setRowCount(0);
        try {
            String filtroNombre = txtBuscarNombre.getText().trim().toLowerCase();
            int precioMax = sliderPrecioMax.getValue();

            List<Ruta> rutas = TurismoService.getInstance().obtenerRutas();
            for (Ruta r : rutas) {
                boolean coincide = true;

                // Filtrar por nombre
                if (!filtroNombre.isEmpty() && !r.getNombre().toLowerCase().contains(filtroNombre)) {
                    coincide = false;
                }

                // Filtrar por precio máximo
                if (r.getPrecio() > precioMax) {
                    coincide = false;
                }

                if (coincide) {
                    modeloTabla.addRow(new Object[] {
                            r.getId(), r.getNombre(), r.getDescripcion(), r.getPrecio(), r.getDificultad()
                    });
                }
            }

            if (selectedId != null) {
                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                    if (modeloTabla.getValueAt(i, 0).equals(selectedId)) {
                        tablaRutas.setRowSelectionInterval(i, i);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar rutas: " + ex.getMessage());
        }
    }

    private void eliminarRuta() {
        int fila = tablaRutas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una ruta para eliminar.");
            return;
        }
        int idRuta = (int) modeloTabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres eliminar esta ruta?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION)
            return;
        try {
            boolean eliminado = TurismoService.getInstance().eliminarRuta(idRuta);
            if (eliminado) {
                cargarRutas(); // Actualiza la tabla
                JOptionPane.showMessageDialog(this, "Ruta eliminada.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar la ruta.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void verDetallesRuta() {
        int fila = tablaRutas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una ruta.");
            return;
        }

        try {
            int idRuta = (int) modeloTabla.getValueAt(fila, 0);
            Ruta ruta = TurismoService.getInstance().obtenerRutaPorId(idRuta);

            ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
            Color bgPanel = theme == ThemeManager.Theme.DARK ? new Color(44, 62, 80) : new Color(245, 247, 250);
            Color fgPanel = theme == ThemeManager.Theme.DARK ? Color.WHITE : new Color(44, 62, 80);
            Color btnBg = new Color(52, 152, 219);
            Color btnFg = Color.WHITE;

            JPanel panel = new JPanel(new BorderLayout(20, 20));
            panel.setBackground(bgPanel);
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(btnBg, 2, true),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)));

            // Imagen de la ruta redondeada o por defecto
            JLabel lblImagen = new JLabel();
            lblImagen.setHorizontalAlignment(JLabel.CENTER);
            lblImagen.setVerticalAlignment(JLabel.CENTER);
            lblImagen.setPreferredSize(new Dimension(220, 220));

            BufferedImage img;
            if (ruta.getImagen() != null) {
                img = ImageIO.read(new ByteArrayInputStream(ruta.getImagen()));
            } else {
                img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = img.createGraphics();
                g.setColor(new Color(220, 220, 220));
                g.fillOval(0, 0, 200, 200);
                g.setColor(new Color(160, 160, 160));
                g.setFont(new Font("Arial", Font.BOLD, 18));
                FontMetrics fm = g.getFontMetrics();
                String texto = "Sin imagen";
                int x = (200 - fm.stringWidth(texto)) / 2;
                int y = (200 - fm.getHeight()) / 2 + fm.getAscent();
                g.drawString(texto, x, y);
                g.dispose();
            }
            if (img != null) {
                img = resizeImage(img, 200, 200);
            } else {
                img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
            }

            // Convertir a circular
            int size = 200;
            BufferedImage circleBuffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = circleBuffer.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
            g2.drawImage(img, 0, 0, size, size, null);
            g2.setClip(null);
            g2.setStroke(new BasicStroke(4));
            g2.setColor(btnBg);
            g2.drawOval(2, 2, size - 4, size - 4);
            g2.dispose();
            lblImagen.setIcon(new ImageIcon(circleBuffer));
            panel.add(lblImagen, BorderLayout.WEST);

            // Panel de detalles con iconos y separación visual
            JPanel panelDetalles = new JPanel();
            panelDetalles.setLayout(new BoxLayout(panelDetalles, BoxLayout.Y_AXIS));
            panelDetalles.setBackground(bgPanel);

            JLabel lblNombre = new JLabel(" " + ruta.getNombre());
            lblNombre.setFont(new Font("Arial", Font.BOLD, 22));
            lblNombre.setForeground(fgPanel);

            JLabel lblDescripcion = new JLabel(
                    "<html><div style='width:260px;'><b>Descripción:</b> " + ruta.getDescripcion() + "</div></html>");
            lblDescripcion.setFont(new Font("Arial", Font.PLAIN, 15));
            lblDescripcion.setForeground(new Color(52, 73, 94));

            JLabel lblPrecio = new JLabel(" Precio: $" + ruta.getPrecio());
            lblPrecio.setFont(new Font("Arial", Font.BOLD, 16));
            lblPrecio.setForeground(new Color(39, 174, 96));

            JLabel lblDificultad = new JLabel(" Dificultad: " + ruta.getDificultad());
            lblDificultad.setFont(new Font("Arial", Font.BOLD, 16));
            lblDificultad.setForeground(new Color(243, 156, 18));

            panelDetalles.add(lblNombre);
            panelDetalles.add(Box.createVerticalStrut(12));
            panelDetalles.add(lblDescripcion);
            panelDetalles.add(Box.createVerticalStrut(18));
            panelDetalles.add(lblPrecio);
            panelDetalles.add(Box.createVerticalStrut(10));
            panelDetalles.add(lblDificultad);

            // Botón cerrar
            JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            panelBoton.setBackground(bgPanel);
            JButton btnCerrar = new JButton("Cerrar");
            btnCerrar.setBackground(btnBg);
            btnCerrar.setForeground(btnFg);
            btnCerrar.setFocusPainted(false);
            btnCerrar.setFont(new Font("Arial", Font.BOLD, 14));
            btnCerrar.addActionListener(e -> {
                Window w = SwingUtilities.getWindowAncestor(btnCerrar);
                if (w != null)
                    w.dispose();
            });
            panelBoton.add(btnCerrar);

            panelDetalles.add(Box.createVerticalStrut(18));
            panelDetalles.add(panelBoton);

            panel.add(panelDetalles, BorderLayout.CENTER);

            // Mostrar en un JDialog para mejor visual
            JDialog dialog = new JDialog(this, "Detalles de la Ruta", true);
            dialog.setContentPane(panel);
            dialog.pack();
            dialog.setLocationRelativeTo(this);
            dialog.setResizable(false);
            dialog.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar los detalles de la ruta: " + ex.getMessage());
        }
    }

    private void valorarRuta() {
        int fila = tablaRutas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una ruta para valorar.");
            return;
        }
        int idRuta = (int) modeloTabla.getValueAt(fila, 0);
        String nombreRuta = (String) modeloTabla.getValueAt(fila, 1);
        VistaValoraciones.getInstance(idRuta, nombreRuta, usuario).setVisible(true);
    }

    private void reservarRuta() {
        try {
            int fila = tablaRutas.getSelectedRow();
            if (fila == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona una ruta para reservar.");
                return;
            }

            String nombreRuta = (String) modeloTabla.getValueAt(fila, 1);

            RutaDAO rutaDAO = new RutaDAO();
            Ruta ruta = rutaDAO.obtenerRutaPorNombre(nombreRuta);

            if (ruta == null) {
                JOptionPane.showMessageDialog(this, "Ruta no encontrada.");
                return;
            }

            ReservarDAO dao = new ReservarDAO();
            boolean reservado = dao.reservarRuta(usuario.getId(), ruta.getId(), new Date());

            if (reservado) {
                TurismoService.getInstance().registrarActividad(usuario.getId(),
                        "Reservó la ruta: " + ruta.getNombre());
                JOptionPane.showMessageDialog(this, "Reserva realizada con éxito.");
                cargarRutas();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo realizar la reserva.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al reservar ruta: " + e.getMessage());
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resultingImage, 0, 0, null);
        g2d.dispose();
        return outputImage;
    }
}
package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
        btnActualizar.setPreferredSize(new Dimension(50,30));
        btnActualizar.addActionListener(e -> cargarRestaurantes());
        panelTitulo.add(btnActualizar, BorderLayout.EAST);

        add(panelTitulo, BorderLayout.NORTH);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnas = {"ID", "Nombre", "Ubicación", "Valoración"};
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
                modeloTabla.addRow(new Object[]{
                        r.getId(), r.getNombre(), r.getUbicacion(), r.getValoracion()
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
                TurismoService.getInstance().registrarActividad(usuario.getId(), "Eliminó el restaurante con ID: " + idRestaurante);
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
        float valoracion = (float) modeloTabla.getValueAt(fila, 3);

        try {
            int idRestaurante = (int) modeloTabla.getValueAt(fila, 0);
            Restaurante restaurante = TurismoService.getInstance().obtenerRestaurantePorId(idRestaurante);

            String detalles = "Nombre: " + nombre +
                            "\nUbicación: " + ubicacion +
                            "\nValoración: " + valoracion;

            ImageIcon icono = null;
            if (restaurante.getImagen() != null) {
                Image img = Toolkit.getDefaultToolkit().createImage(restaurante.getImagen());
                icono = new ImageIcon(img.getScaledInstance(200, 200, Image.SCALE_SMOOTH));
            }

            JLabel lblImagen = new JLabel(icono);
            lblImagen.setHorizontalAlignment(JLabel.CENTER);

            JOptionPane.showMessageDialog(this, detalles, "Detalles del Restaurante", JOptionPane.INFORMATION_MESSAGE);
            
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
        new ValorarRestaurantes(nombreRestaurante, usuario).setVisible(true);
    }
}
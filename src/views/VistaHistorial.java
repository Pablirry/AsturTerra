package views;

import model.Usuario;
import model.Historial;
import services.TurismoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VistaHistorial extends JDialog {
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;
    private Usuario usuario;

    public VistaHistorial(Usuario usuario) {
        super((Frame) null, "Historial de Actividad", true);
        this.usuario = usuario;
        setSize(700, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // Tema y colores
        ThemeManager.Theme theme = ThemeManager.getCurrentTheme();
        Color bgPanel = theme == ThemeManager.Theme.DARK ? new Color(44, 62, 80) : new Color(245, 247, 250);
        Color fgPanel = theme == ThemeManager.Theme.DARK ? Color.WHITE : new Color(44, 62, 80);
        Color borderColor = new Color(52, 152, 219);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgPanel);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setLayout(new BorderLayout(0, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 32, 24, 32));

        JLabel lblTitulo = new JLabel("Historial de Actividad");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(fgPanel);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblTitulo, BorderLayout.NORTH);

        // Tabla de historial
        modeloTabla = new DefaultTableModel(new Object[] { "Fecha", "Acción" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaHistorial = new JTable(modeloTabla);
        tablaHistorial.setFont(new Font("Arial", Font.PLAIN, 16));
        tablaHistorial.setRowHeight(28);
        tablaHistorial.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        tablaHistorial.getTableHeader().setBackground(borderColor);
        tablaHistorial.getTableHeader().setForeground(Color.WHITE);
        tablaHistorial.setSelectionBackground(new Color(52, 152, 219, 80));
        tablaHistorial.setSelectionForeground(fgPanel);
        tablaHistorial.setGridColor(borderColor);

        JScrollPane scroll = new JScrollPane(tablaHistorial);
        scroll.setBorder(BorderFactory.createLineBorder(borderColor, 2, true));
        panel.add(scroll, BorderLayout.CENTER);

        // Botón cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setFont(new Font("Arial", Font.BOLD, 16));
        btnCerrar.setBackground(borderColor);
        btnCerrar.setForeground(Color.WHITE);
        btnCerrar.setFocusPainted(false);
        btnCerrar.setBorder(BorderFactory.createEmptyBorder(8, 32, 8, 32));
        // Solo cierra el diálogo, no abre el menú principal
        btnCerrar.addActionListener(e -> this.dispose());

        JButton btnMenu = new JButton("Menú Principal");
        btnMenu.setFont(new Font("Arial", Font.BOLD, 16));
        btnMenu.setBackground(new Color(46, 204, 113));
        btnMenu.setForeground(Color.WHITE);
        btnMenu.setFocusPainted(false);
        btnMenu.setBorder(BorderFactory.createEmptyBorder(8, 32, 8, 32));
        btnMenu.addActionListener(e -> {
            this.dispose();
            MenuPrincipal.getInstance(usuario).setVisible(true);
        });

        JPanel panelBoton = new JPanel();
        panelBoton.setOpaque(false);
        panelBoton.add(btnMenu);
        panelBoton.add(btnCerrar);
        panel.add(panelBoton, BorderLayout.SOUTH);

        add(panel);

        cargarHistorial();
    }

    private void cargarHistorial() {
        modeloTabla.setRowCount(0);
        try {
            List<Historial> historial = TurismoService.getInstance().obtenerHistorialUsuario(usuario.getId());
            for (Historial h : historial) {
                modeloTabla.addRow(new Object[] { h.getFecha(), h.getAccion() });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + ex.getMessage());
        }
    }
}
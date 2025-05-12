package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import model.Restaurante;
import model.Usuario;
import services.TurismoService;

public class VistaRestaurantes extends JFrame {
    private JTable tablaRestaurantes;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregar, btnEditar, btnEliminar;
    private Usuario usuario;

    public VistaRestaurantes(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Gestión de Restaurantes");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inicializarComponentes();
        cargarRestaurantes();
    }

    private void inicializarComponentes() {
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Nombre", "Ubicación"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaRestaurantes = new JTable(modeloTabla);

        JScrollPane scrollPane = new JScrollPane(tablaRestaurantes);

        btnAgregar = new JButton("Agregar Restaurante");
        btnEditar = new JButton("Editar Restaurante");
        btnEliminar = new JButton("Eliminar Restaurante");

        btnAgregar.addActionListener(e -> {
            AgregarRestaurante ventana = new AgregarRestaurante(this);
            ventana.setVisible(true);
        });

        btnEditar.addActionListener(e -> editarRestauranteSeleccionado());
        btnEliminar.addActionListener(e -> eliminarRestauranteSeleccionado());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public void cargarRestaurantes() {
        modeloTabla.setRowCount(0);
        try {
            List<Restaurante> restaurantes = TurismoService.getInstance().obtenerRestaurantes();
            for (Restaurante r : restaurantes) {
                modeloTabla.addRow(new Object[]{
                    r.getId(),
                    r.getNombre(),
                    r.getUbicacion()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar restaurantes", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarRestauranteSeleccionado() {
        int fila = tablaRestaurantes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un restaurante para editar.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        EditarRestaurante ventana = new EditarRestaurante(this, id);
        ventana.setVisible(true);
    }

    private void eliminarRestauranteSeleccionado() {
        int fila = tablaRestaurantes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un restaurante para eliminar.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el restaurante?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                TurismoService.getInstance().eliminarRestaurante(id);
                cargarRestaurantes();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar restaurante", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
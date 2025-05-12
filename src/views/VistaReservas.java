package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import model.Reserva;
import model.Ruta;
import model.Usuario;
import services.TurismoService;

public class VistaReservas extends JFrame {
    private JTable tablaReservas;
    private DefaultTableModel modeloTabla;
    private JButton btnEliminar, btnConfirmar;
    private Usuario usuario;

    public VistaReservas(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Gestión de Reservas");
        setSize(900, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inicializarComponentes();
        cargarReservas();
    }

    private void inicializarComponentes() {
        modeloTabla = new DefaultTableModel(new Object[]{"ID", "Usuario", "Ruta", "Fecha", "Confirmada"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaReservas = new JTable(modeloTabla);

        JScrollPane scrollPane = new JScrollPane(tablaReservas);

        btnEliminar = new JButton("Eliminar Reserva");
        btnConfirmar = new JButton("Confirmar Reserva");

        btnEliminar.addActionListener(e -> eliminarReservaSeleccionada());
        btnConfirmar.addActionListener(e -> confirmarReservaSeleccionada());

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnConfirmar);
        panelBotones.add(btnEliminar);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    public void cargarReservas() {
        modeloTabla.setRowCount(0);
        try {
            List<Reserva> reservas = TurismoService.getInstance().obtenerReservas();
            for (Reserva r : reservas) {
                Usuario usuario = TurismoService.getInstance().obtenerUsuarioPorId(r.getIdUsuario());
                Ruta ruta = TurismoService.getInstance().obtenerRutaPorId(r.getIdRuta());
                modeloTabla.addRow(new Object[]{
                    r.getId(),
                    usuario != null ? usuario.getNombre() : "Desconocido",
                    ruta != null ? ruta.getNombre() : "Desconocida",
                    r.getFecha(),
                    r.isConfirmada() ? "Sí" : "No"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar reservas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarReservaSeleccionada() {
        int fila = tablaReservas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva para eliminar.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar la reserva?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                TurismoService.getInstance().eliminarReserva(id);
                cargarReservas();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar reserva", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void confirmarReservaSeleccionada() {
        int fila = tablaReservas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una reserva para confirmar.");
            return;
        }
        int id = (int) modeloTabla.getValueAt(fila, 0);
        try {
            TurismoService.getInstance().confirmarReserva(id);
            cargarReservas();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al confirmar reserva", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
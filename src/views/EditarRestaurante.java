package views;

import model.Restaurante;
import services.TurismoService;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.io.File;

public class EditarRestaurante extends JDialog {
    private JTextField txtNombre, txtUbicacion;
    private JLabel lblImagen;
    private JButton btnGuardar, btnCancelar, btnImagen;
    private File imagenRestaurante;
    private Restaurante restauranteOriginal;

    public EditarRestaurante(Restaurante restaurante) {
        super((Frame) null, "Editar Restaurante", true);
        this.restauranteOriginal = restaurante;
        setSize(400, 350);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        txtNombre = new JTextField(restaurante.getNombre());
        txtUbicacion = new JTextField(restaurante.getUbicacion());

        lblImagen = new JLabel("Imagen actual", JLabel.CENTER);
        lblImagen.setPreferredSize(new Dimension(120, 120));
        if (restaurante.getImagen() != null) {
            ImageIcon icon = new ImageIcon(restaurante.getImagen());
            lblImagen.setIcon(new ImageIcon(icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)));
        }
        btnImagen = new JButton("Cambiar Imagen");
        btnImagen.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                imagenRestaurante = fc.getSelectedFile();
                ImageIcon icon = new ImageIcon(imagenRestaurante.getAbsolutePath());
                lblImagen.setIcon(new ImageIcon(icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)));
            }
        });

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(Box.createVerticalStrut(8));
        panel.add(new JLabel("Ubicación:"));
        panel.add(txtUbicacion);
        panel.add(Box.createVerticalStrut(8));
        panel.add(lblImagen);
        panel.add(btnImagen);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        add(panel, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> dispose());
    }

    private void guardarCambios() {
        try {
            String nombre = txtNombre.getText().trim();
            String ubicacion = txtUbicacion.getText().trim();

            if (nombre.isEmpty() || ubicacion.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            byte[] imagenBytes = restauranteOriginal.getImagen();
            if (imagenRestaurante != null) {
                imagenBytes = Files.readAllBytes(imagenRestaurante.toPath());
            }

            Restaurante restauranteEditado = new Restaurante(
                restauranteOriginal.getId(),
                nombre,
                ubicacion,
                restauranteOriginal.getValoracion(), // Mantén la valoración
                imagenBytes
            );

            boolean actualizado = TurismoService.getInstance().actualizarRestaurante(restauranteEditado);
            if (actualizado) {
                JOptionPane.showMessageDialog(this, "Restaurante actualizado correctamente.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar el restaurante.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar restaurante: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
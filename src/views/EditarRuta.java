package views;

import model.Ruta;
import services.TurismoService;

import javax.swing.*;
import java.awt.*;
import java.nio.file.Files;
import java.io.File;

public class EditarRuta extends JDialog {
    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JTextField txtPrecio;
    private JComboBox<String> cmbDificultad;
    private JLabel lblImagen;
    private JButton btnGuardar, btnCancelar, btnImagen;
    private File imagenRuta;
    private Ruta rutaOriginal;

    public EditarRuta(Ruta ruta) {
        super((Frame) null, "Editar Ruta", true);
        this.rutaOriginal = ruta;
        setSize(400, 450);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        txtNombre = new JTextField(ruta.getNombre());
        txtDescripcion = new JTextArea(ruta.getDescripcion(), 3, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        txtPrecio = new JTextField(String.valueOf(ruta.getPrecio()));
        cmbDificultad = new JComboBox<>(new String[]{"Fácil", "Media", "Difícil"});
        cmbDificultad.setSelectedItem(ruta.getDificultad());

        lblImagen = new JLabel("Imagen actual", JLabel.CENTER);
        lblImagen.setPreferredSize(new Dimension(120, 120));
        if (ruta.getImagen() != null) {
            ImageIcon icon = new ImageIcon(ruta.getImagen());
            lblImagen.setIcon(new ImageIcon(icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)));
        }
        btnImagen = new JButton("Cambiar Imagen");
        btnImagen.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                imagenRuta = fc.getSelectedFile();
                ImageIcon icon = new ImageIcon(imagenRuta.getAbsolutePath());
                lblImagen.setIcon(new ImageIcon(icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)));
            }
        });

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(Box.createVerticalStrut(8));
        panel.add(new JLabel("Descripción:"));
        panel.add(new JScrollPane(txtDescripcion));
        panel.add(Box.createVerticalStrut(8));
        panel.add(new JLabel("Precio:"));
        panel.add(txtPrecio);
        panel.add(Box.createVerticalStrut(8));
        panel.add(new JLabel("Dificultad:"));
        panel.add(cmbDificultad);
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
            String descripcion = txtDescripcion.getText().trim();
            String precioStr = txtPrecio.getText().trim();
            String dificultad = (String) cmbDificultad.getSelectedItem();

            if (nombre.isEmpty() || descripcion.isEmpty() || precioStr.isEmpty() || dificultad == null) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double precio;
            try {
                precio = Double.parseDouble(precioStr);
                if (precio < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Precio no válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            byte[] imagenBytes = rutaOriginal.getImagen();
            if (imagenRuta != null) {
                imagenBytes = Files.readAllBytes(imagenRuta.toPath());
            }

            Ruta rutaEditada = new Ruta(
                rutaOriginal.getId(),
                nombre,
                descripcion,
                imagenBytes,
                precio,
                dificultad
            );

            boolean actualizado = TurismoService.getInstance().actualizarRuta(rutaEditada);
            if (actualizado) {
                JOptionPane.showMessageDialog(this, "Ruta actualizada correctamente.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo actualizar la ruta.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar ruta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
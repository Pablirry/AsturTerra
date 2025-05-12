package views;

import model.Restaurante;
import services.TurismoService;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;

public class EditarRestaurante extends JDialog {
    private JTextField txtNombre, txtUbicacion;
    private JLabel lblImagen;
    private JButton btnSeleccionarImagen, btnGuardar, btnCancelar;
    private byte[] imagenBytes = null;
    private int idRestaurante;
    private VistaRestaurantes padre;

    public EditarRestaurante(VistaRestaurantes padre, int idRestaurante) {
        super(padre, "Editar Restaurante", true);
        this.padre = padre;
        this.idRestaurante = idRestaurante;
        setSize(400, 300);
        setLocationRelativeTo(padre);
        inicializarComponentes();
        cargarDatos();
    }

    private void inicializarComponentes() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre = new JTextField();
        txtUbicacion = new JTextField();

        lblImagen = new JLabel("Sin imagen");
        btnSeleccionarImagen = new JButton("Seleccionar Imagen");
        btnSeleccionarImagen.addActionListener(e -> seleccionarImagen());

        btnGuardar = new JButton("Guardar Cambios");
        btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> dispose());

        gbc.gridx = 0; gbc.gridy = 0; panel.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1; panel.add(txtNombre, gbc);
        gbc.gridx = 0; gbc.gridy++; panel.add(new JLabel("Ubicación:"), gbc);
        gbc.gridx = 1; panel.add(txtUbicacion, gbc);
        gbc.gridx = 0; gbc.gridy++; panel.add(new JLabel("Imagen:"), gbc);
        gbc.gridx = 1; panel.add(lblImagen, gbc);
        gbc.gridx = 1; gbc.gridy++; panel.add(btnSeleccionarImagen, gbc);
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        panel.add(panelBotones, gbc);

        setContentPane(panel);
    }

    private void seleccionarImagen() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            lblImagen.setText(file.getName());
            try {
                imagenBytes = Files.readAllBytes(file.toPath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen.");
            }
        }
    }

    private void cargarDatos() {
        try {
            Restaurante restaurante = TurismoService.getInstance().obtenerRestaurantePorId(idRestaurante);
            if (restaurante == null) {
                JOptionPane.showMessageDialog(this, "No se encontró el restaurante.");
                dispose();
                return;
            }
            txtNombre.setText(restaurante.getNombre());
            txtUbicacion.setText(restaurante.getUbicacion());
            imagenBytes = restaurante.getImagen();
            lblImagen.setText(imagenBytes != null ? "Imagen cargada" : "Sin imagen");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar datos.");
            dispose();
        }
    }

    private void guardarCambios() {
        String nombre = txtNombre.getText().trim();
        String ubicacion = txtUbicacion.getText().trim();
        if (nombre.isEmpty() || ubicacion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.");
            return;
        }
        Restaurante restaurante = new Restaurante(idRestaurante, nombre, ubicacion, imagenBytes);
        try {
            TurismoService.getInstance().actualizarRestaurante(restaurante);
            padre.cargarRestaurantes();
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al guardar cambios.");
        }
    }
}
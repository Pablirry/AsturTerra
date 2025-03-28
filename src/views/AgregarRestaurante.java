package views;

import javax.swing.*;

import model.Restaurante;
import services.TurismoService;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AgregarRestaurante extends JFrame {

    private JTextField txtNombre, txtUbicacion;
    private JSpinner spnValoracion;
    private JButton btnSeleccionarImagen, btnAgregar, btnCancelar;
    private File imagenPerfil;

    public AgregarRestaurante() {
        setTitle("Agregar Restaurante");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(44, 62, 80));
        JLabel lblTitulo = new JLabel("Agregar Restaurante");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new GridLayout(5, 2, 10, 10));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField();
        JLabel lblUbicacion = new JLabel("Ubicación:");
        txtUbicacion = new JTextField();
        JLabel lblValoracion = new JLabel("Valoración:");
        spnValoracion = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        JLabel lblImg = new JLabel("Imagen:");
        btnSeleccionarImagen = new JButton("Seleccionar Imagen");

        panelContenido.add(lblNombre);
        panelContenido.add(txtNombre);
        panelContenido.add(lblUbicacion);
        panelContenido.add(txtUbicacion);
        panelContenido.add(lblValoracion);
        panelContenido.add(spnValoracion);
        panelContenido.add(lblImg);
        panelContenido.add(btnSeleccionarImagen);

        add(panelContenido, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(236, 240, 241));

        btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(new Color(46, 204, 113));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnAgregar);

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        btnSeleccionarImagen.addActionListener(e -> seleccionarImagen());
        btnAgregar.addActionListener(e -> agregarRestaurante());
        btnCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void seleccionarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            imagenPerfil = fileChooser.getSelectedFile();
        }
    }

    private void agregarRestaurante() {
        try {
            String nombre = txtNombre.getText().trim();
            String ubicacion = txtUbicacion.getText().trim();
            float valoracion = ((Integer) spnValoracion.getValue()).floatValue();
            byte[] imagenBytes = null;

            if (imagenPerfil != null) {
                imagenBytes = Files.readAllBytes(Paths.get(imagenPerfil.getAbsolutePath()));
            }

            Restaurante restaurante = new Restaurante(0, nombre, ubicacion, valoracion, imagenBytes);
            boolean guardado = TurismoService.getInstance().agregarRestaurante(restaurante);

            if (guardado) {
                JOptionPane.showMessageDialog(this, "Restaurante agregado correctamente.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo agregar el restaurante.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar restaurante: " + e.getMessage());
        }
    }
}

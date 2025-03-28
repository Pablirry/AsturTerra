package views;

import javax.swing.*;

import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import model.Ruta;
import services.TurismoService;


public class AgregarRuta extends JFrame {

    private JTextField txtNombre, txtDescripcion, txtPrecio;
    private JComboBox<String> cmbDificultad;
    private JButton btnSeleccionarImagen, btnAgregar, btnCancelar;
    private File imagenRuta;

    public AgregarRuta() {
        setTitle("Agregar Ruta");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(44, 62, 80));
        JLabel lblTitulo = new JLabel("Agregar Ruta");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Panel contenido
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new GridLayout(6, 2, 10, 10));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtNombre = new JTextField();
        txtDescripcion = new JTextField();
        txtPrecio = new JTextField();
        cmbDificultad = new JComboBox<>(new String[]{"Fácil", "Media", "Difícil"});
        btnSeleccionarImagen = new JButton("Seleccionar Imagen");

        panelContenido.add(new JLabel("Nombre:"));
        panelContenido.add(txtNombre);
        panelContenido.add(new JLabel("Descripción:"));
        panelContenido.add(txtDescripcion);
        panelContenido.add(new JLabel("Precio:"));
        panelContenido.add(txtPrecio);
        panelContenido.add(new JLabel("Dificultad:"));
        panelContenido.add(cmbDificultad);
        panelContenido.add(new JLabel("Imagen:"));
        panelContenido.add(btnSeleccionarImagen);

        add(panelContenido, BorderLayout.CENTER);

        // Panel botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panelBotones.setBackground(new Color(236, 240, 241));

        btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(new Color(46, 204, 113));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 14));

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(231, 76, 60));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));

        panelBotones.add(btnAgregar);
        panelBotones.add(btnCancelar);

        add(panelBotones, BorderLayout.SOUTH);

        // Eventos
        btnSeleccionarImagen.addActionListener(e -> seleccionarImagen());
        btnAgregar.addActionListener(e -> agregarRuta());
        btnCancelar.addActionListener(e -> dispose());

        setVisible(true);
    }

    private void seleccionarImagen() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            imagenRuta = fileChooser.getSelectedFile();
        }
    }

    private void agregarRuta() {
        try {
            String nombre = txtNombre.getText().trim();
            String descripcion = txtDescripcion.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            String dificultad = (String) cmbDificultad.getSelectedItem();
            byte[] imagenBytes = null;

            if (imagenRuta != null) {
                imagenBytes = Files.readAllBytes(imagenRuta.toPath());
            }

            Ruta ruta = new Ruta(0, nombre, descripcion, imagenBytes, precio, dificultad);
            boolean insertada = TurismoService.getInstance().agregarRuta(ruta);

            if (insertada) {
                JOptionPane.showMessageDialog(this, "Ruta agregada correctamente.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo agregar la ruta.");
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "El precio debe ser un número válido.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar ruta: " + ex.getMessage());
        }
    }
}
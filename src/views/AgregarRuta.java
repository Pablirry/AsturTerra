package views;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import controllers.RutaController;

public class AgregarRuta extends JFrame {

    private JTextField txtNombre, txtDescripcion, txtPrecio;
    private JComboBox<String> cmbDificultad;
    private JButton btnSeleccionarImagen, btnAgregar, btnCancelar;
    private JLabel lblImagen;
    private File imagenRuta;
    private RutaController rutaController;

    public AgregarRuta(RutaController rutaController) {
        this.rutaController = rutaController;

        setTitle("Agregar Ruta");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel de título
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(44, 62, 80));
        JLabel lblTitulo = new JLabel("Agregar Ruta");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        panelTitulo.add(lblTitulo);
        add(panelTitulo, BorderLayout.NORTH);

        // Panel de contenido
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new GridLayout(6, 2, 10, 10));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField();
        JLabel lblDescripcion = new JLabel("Descripción:");
        txtDescripcion = new JTextField();
        JLabel lblPrecio = new JLabel("Precio:");
        txtPrecio = new JTextField();
        JLabel lblDificultad = new JLabel("Dificultad:");
        cmbDificultad = new JComboBox<>(new String[]{"Fácil", "Media", "Difícil"});
        JLabel lblImagen = new JLabel("Imagen:");
        btnSeleccionarImagen = new JButton("Seleccionar Imagen");

        panelContenido.add(lblNombre);
        panelContenido.add(txtNombre);
        panelContenido.add(lblDescripcion);
        panelContenido.add(txtDescripcion);
        panelContenido.add(lblPrecio);
        panelContenido.add(txtPrecio);
        panelContenido.add(lblDificultad);
        panelContenido.add(cmbDificultad);
        panelContenido.add(lblImagen);
        panelContenido.add(btnSeleccionarImagen);

        add(panelContenido, BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
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

        // Eventos
        btnSeleccionarImagen.addActionListener(e -> seleccionarImagen());
        btnAgregar.addActionListener(e -> rutaController.agregarRuta());
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

    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtDescripcion() { return txtDescripcion; }
    public JTextField getTxtPrecio() { return txtPrecio; }
    public JComboBox<String> getCmbDificultad() { return cmbDificultad; }
    public JButton getBtnSeleccionarImagen() { return btnSeleccionarImagen; }
    public JButton getBtnAgregar() { return btnAgregar; }
    public JButton getBtnCancelar() { return btnCancelar; }
    public File getImagenRuta() { return imagenRuta; }
}
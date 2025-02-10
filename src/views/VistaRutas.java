package views;

import javax.swing.*;

public class VistaRutas extends JFrame {

	private JList<String> listaRutas;
    private JButton btnAgregar, btnEliminar, btnVerDetalles;

    public VistaRutas() {
        setTitle("Listado de Rutas");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        listaRutas = new JList<>();
        btnAgregar = new JButton("Agregar Ruta");
        btnEliminar = new JButton("Eliminar Ruta");
        btnVerDetalles = new JButton("Ver Detalles");

        add(new JScrollPane(listaRutas));
        add(btnAgregar);
        add(btnEliminar);
        add(btnVerDetalles);

        setVisible(true);
    }

    public JList<String> getListaRutas() { return listaRutas; }
    public JButton getBtnAgregar() { return btnAgregar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JButton getBtnVerDetalles() { return btnVerDetalles; }

}

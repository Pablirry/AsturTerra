package views;

import javax.swing.*;

public class VistaHistorial extends JFrame {

	private JList<String> listaHistorial;
    private JButton btnCerrar;

    public VistaHistorial() {
        setTitle("Historial de Actividades");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        listaHistorial = new JList<>();
        btnCerrar = new JButton("Cerrar");

        add(new JScrollPane(listaHistorial));
        add(btnCerrar);

        setVisible(true);
    }

    public JList<String> getListaHistorial() { return listaHistorial; }
    public JButton getBtnCerrar() { return btnCerrar; }

}

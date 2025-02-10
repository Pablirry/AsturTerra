package views;


import javax.swing.*;

public class VistaValoraciones extends JFrame {

	private JList<String> listaRutas;
    private JTextArea txtComentario;
    private JSpinner spinnerPuntuacion;
    private JButton btnEnviarValoracion;

    public VistaValoraciones() {
        setTitle("Valoración de Rutas");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        listaRutas = new JList<>();
        txtComentario = new JTextArea(3, 20);
        spinnerPuntuacion = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        btnEnviarValoracion = new JButton("Enviar Valoración");

        add(new JScrollPane(listaRutas));
        add(new JLabel("Puntuación (1-5):"));
        add(spinnerPuntuacion);
        add(new JLabel("Comentario:"));
        add(new JScrollPane(txtComentario));
        add(btnEnviarValoracion);

        setVisible(true);
    }

    public JList<String> getListaRutas() { return listaRutas; }
    public JTextArea getTxtComentario() { return txtComentario; }
    public JButton getBtnEnviarValoracion() { return btnEnviarValoracion; }

    public int getRutaSeleccionada() {
        int selectedIndex = listaRutas.getSelectedIndex();
        return (selectedIndex != -1) ? selectedIndex : -1; // Retorna el índice seleccionado o -1 si no hay selección
    }

    public int getPuntuacionSeleccionada() {
        return (int) spinnerPuntuacion.getValue(); // Obtiene el valor del spinner de puntuación
    }

}

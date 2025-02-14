package views;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import controllers.ValoracionController;

public class VistaValoraciones extends JFrame {

	private JList<String> listaRutas;
    private JTextArea txtComentario;
    private JSpinner spinnerPuntuacion;
    private JButton btnEnviarValoracion, btnVolver;
    private ValoracionController valoracionController;

    public VistaValoraciones() {
        setTitle("Valoración de Rutas");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        listaRutas = new JList<>();
        txtComentario = new JTextArea(3, 20);
        spinnerPuntuacion = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        btnEnviarValoracion = new JButton("Enviar Valoración");
        btnVolver = new JButton("Volver al Menú");

        add(new JScrollPane(listaRutas));
        add(new JLabel("Puntuación (1-5):"));
        add(spinnerPuntuacion);
        add(new JLabel("Comentario:"));
        add(new JScrollPane(txtComentario));
        add(btnEnviarValoracion);
        add(btnVolver);

        btnVolver.addActionListener(e -> {
            new MenuPrincipal();
            dispose();
        });

        btnEnviarValoracion.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int puntuacion = (int) spinnerPuntuacion.getValue();
                String comentario = txtComentario.getText();

                if (comentario.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "El comentario no puede estar vacío.");
                    return;
                }

                JOptionPane.showMessageDialog(null, "Valoración enviada correctamente.");
            }
        });

        setVisible(true);
    }

    public JList<String> getListaRutas() { return listaRutas; }
    public JTextArea getTxtComentario() { return txtComentario; }
    public JButton getBtnEnviarValoracion() { return btnEnviarValoracion; }
    public JButton getBtnVolver() { return btnVolver; }

    public int getRutaSeleccionada() {
        return listaRutas.getSelectedIndex();
    }

    public int getPuntuacionSeleccionada() {
        return (int) spinnerPuntuacion.getValue();
    }

}

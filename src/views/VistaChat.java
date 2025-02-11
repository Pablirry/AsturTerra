package views;

import javax.swing.*;
import java.awt.*;

public class VistaChat extends JFrame {

	private JTextArea txtMensajes;
    private JTextField txtMensaje;
    private JButton btnEnviar, btnVolver;

    public VistaChat() {
        setTitle("Chat de Soporte");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        txtMensajes = new JTextArea(10, 30);
        txtMensaje = new JTextField();
        btnEnviar = new JButton("Enviar");
        btnVolver = new JButton("Volver al Menú");

        add(new JScrollPane(txtMensajes));
        add(txtMensaje);
        add(btnEnviar);
        add(btnVolver);

        btnVolver.addActionListener(e -> {
            new MenuPrincipal();
            dispose();
        });

        setVisible(true);
    }

    public JButton getBtnVolver() { return btnVolver; }
    public JTextField getTxtMensaje() { return txtMensaje; }
    public JTextArea getTxtMensajes() { return txtMensajes; }
    public JButton getBtnEnviar() { return btnEnviar; }

}

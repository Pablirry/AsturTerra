package views;

import javax.swing.*;
import java.awt.*;

public class VistaChat extends JFrame {

	private JTextArea txtMensajes;
    private JTextField txtMensaje;
    private JButton btnEnviar;
    
    public VistaChat() {
        setTitle("Chat con Soporte");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        txtMensajes = new JTextArea();
        txtMensajes.setEditable(false);
        JScrollPane scrollMensajes = new JScrollPane(txtMensajes);

        txtMensaje = new JTextField();
        btnEnviar = new JButton("Enviar");

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.add(txtMensaje, BorderLayout.CENTER);
        panelInferior.add(btnEnviar, BorderLayout.EAST);

        add(scrollMensajes, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        setVisible(true);
    }

    public JTextField getTxtMensaje() { return txtMensaje; }
    public JTextArea getTxtMensajes() { return txtMensajes; }
    public JButton getBtnEnviar() { return btnEnviar; }

}

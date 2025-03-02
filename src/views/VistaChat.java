package views;

import javax.swing.*;
import java.awt.*;
import controllers.MensajeController;
import model.Usuario;

public class VistaChat extends JFrame {

    private JTextArea txtMensajes;
    private JTextField txtMensaje;
    private JButton btnEnviar, btnVolver;
    private Usuario usuario;
    private MensajeController mensajeController;

    public VistaChat(Usuario usuario) {
        this.usuario = usuario;
        inicializarComponentes();
        try {
            mensajeController = new MensajeController(this);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void inicializarComponentes() {
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
            MenuPrincipal.getInstance(usuario).setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    public JButton getBtnVolver() { return btnVolver; }
    public JTextField getTxtMensaje() { return txtMensaje; }
    public JTextArea getTxtMensajes() { return txtMensajes; }
    public JButton getBtnEnviar() { return btnEnviar; }
}
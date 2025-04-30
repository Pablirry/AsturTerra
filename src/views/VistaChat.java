package views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import model.Mensaje;
import model.Usuario;
import services.TurismoService;

public class VistaChat extends JFrame {

    private JTextArea txtMensajes;
    private JTextField txtMensaje;
    private JButton btnEnviar, btnVolver;
    private Usuario usuario;

    public VistaChat(Usuario usuario) {
        this.usuario = usuario;
        inicializarComponentes();
        cargarMensajes();
    }

    private void inicializarComponentes() {
        setTitle("Chat de Soporte");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        txtMensajes = new JTextArea();
        txtMensajes.setEditable(false);
        txtMensajes.setLineWrap(true);
        txtMensajes.setWrapStyleWord(true);
        JScrollPane scrollMensajes = new JScrollPane(txtMensajes);
        add(scrollMensajes, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout());
        txtMensaje = new JTextField();
        btnEnviar = new JButton("Enviar");
        btnVolver = new JButton("Volver");

        getRootPane().setDefaultButton(btnEnviar);

        panelInferior.add(txtMensaje, BorderLayout.CENTER);
        panelInferior.add(btnEnviar, BorderLayout.EAST);
        panelInferior.add(btnVolver, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);

        btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarMensaje();
            }
        });

        btnVolver.addActionListener(e -> {
            new MenuPrincipal(usuario).setVisible(true);
            dispose();
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void cargarMensajes() {
        try {
            List<Mensaje> mensajes = TurismoService.getInstance().obtenerMensajesUsuario(usuario.getId());
            txtMensajes.setText("");

            for (Mensaje m : mensajes) {
                txtMensajes.append("[" + m.getFecha() + "] " + m.getMensaje() + "\n");
                if (m.getRespuesta() != null && !m.getRespuesta().isEmpty()) {
                    txtMensajes.append("   Soporte: " + m.getRespuesta() + "\n");
                }
                txtMensajes.append("\n");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar mensajes: " + e.getMessage());
        }
    }

    private void enviarMensaje() {
        String mensajeTexto = txtMensaje.getText().trim();
        if (mensajeTexto.isEmpty()) {
            return;
        }

        try {
            Mensaje mensaje = new Mensaje();
            mensaje.setMensaje(mensajeTexto);
            mensaje.setRespuesta(null);
            mensaje.setFecha(new java.util.Date());

            boolean enviado = TurismoService.getInstance().enviarMensaje(usuario.getId(), mensaje);
            if (enviado) {
                txtMensaje.setText("");
                cargarMensajes();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo enviar el mensaje.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al enviar mensaje: " + e.getMessage());
        }
    }
}
package views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

import dao.MensajeDAO;
import model.Mensaje;
import model.Usuario;

import java.awt.*;

public class VistaSoporteAdmin extends JFrame {

    private JTable tablaMensajes;
    private JTextArea txtRespuesta;
    private JButton btnResponder;
    private Usuario usuario;
    private MensajeDAO mensajeDAO = new MensajeDAO();

    public VistaSoporteAdmin(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Soporte - responder Mensajes");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tabla de mensajes sin respuesta
        String[] columnas = {"ID", "Usuario", "Mensaje", "Fecha"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        tablaMensajes = new JTable(modelo);
        cargarMensajes(modelo);

        add(new JScrollPane(tablaMensajes), BorderLayout.CENTER);

        // Panel de respuesta
        JPanel panelRespuesta = new JPanel(new BorderLayout());
        txtRespuesta = new JTextArea(3, 40);
        btnResponder = new JButton("Responder");
        panelRespuesta.add(new JLabel("Respuesta:"), BorderLayout.NORTH);
        panelRespuesta.add(new JScrollPane(txtRespuesta), BorderLayout.CENTER);
        panelRespuesta.add(btnResponder, BorderLayout.SOUTH);

        add(panelRespuesta, BorderLayout.SOUTH);

        btnResponder.addActionListener(e -> responderMensaje(modelo));

        setVisible(true);
    }

    private void cargarMensajes(DefaultTableModel modelo) {
        try {
            modelo.setRowCount(0);
            List<Mensaje> mensajes = mensajeDAO.obtenerMensajesSinRespuesta();
            for (Mensaje m : mensajes) {
                modelo.addRow(new Object[]{m.getId(), m.getIdUsuario(), m.getMensaje(), m.getFecha()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar mensajes: " + e.getMessage());
        }
    }

    private void responderMensaje(DefaultTableModel modelo) {
        int fila = tablaMensajes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un mensaje.");
            return;
        }
        int idMensaje = (int) modelo.getValueAt(fila, 0);
        String respuesta = txtRespuesta.getText().trim();
        if (respuesta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Escribe una respuesta.");
            return;
        }
        try {
            boolean ok = mensajeDAO.responderMensaje(idMensaje, respuesta);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Respuesta enviada.");
                txtRespuesta.setText("");
                cargarMensajes(modelo);
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo responder.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}

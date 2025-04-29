package views;

import java.awt.*;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;
import javax.swing.border.AbstractBorder;

public class ThemeManager {
    public static final Color COLOR_PRIMARIO = new Color(52, 152, 219);
    public static final Color COLOR_SECUNDARIO = new Color(236, 240, 241);
    public static final Color COLOR_EXITO = new Color(46, 204, 113);
    public static final Color COLOR_ERROR = new Color(231, 76, 60);
    public static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 24);
    public static final Font FUENTE_NORMAL = new Font("Arial", Font.PLAIN, 15);

    public enum Theme {
        LIGHT, DARK
    }

    private static Theme currentTheme = Theme.LIGHT;

    public static void setTheme(Theme theme, Window rootWindow) {
        currentTheme = theme;
        try {
            if (theme == Theme.DARK) {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                UIManager.put("control", new Color(44, 62, 80));
                UIManager.put("info", new Color(44, 62, 80));
                UIManager.put("nimbusBase", new Color(44, 62, 80));
                UIManager.put("nimbusBlueGrey", new Color(44, 62, 80));
                UIManager.put("nimbusLightBackground", new Color(52, 73, 94));
                UIManager.put("text", Color.WHITE);
                UIManager.put("nimbusFocus", new Color(52, 152, 219));
            } else {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                UIManager.put("control", Color.WHITE);
                UIManager.put("info", Color.WHITE);
                UIManager.put("nimbusBase", new Color(52, 152, 219));
                UIManager.put("nimbusBlueGrey", new Color(236, 240, 241));
                UIManager.put("nimbusLightBackground", Color.WHITE);
                UIManager.put("text", Color.BLACK);
                UIManager.put("nimbusFocus", new Color(52, 152, 219));
            }
        } catch (Exception ignored) {
        }

        for (Window window : Window.getWindows()) {
            setComponentTheme(window, theme);
            SwingUtilities.updateComponentTreeUI(window);
        }
    }

    // Borde redondeado personalizado
    public static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int thickness;
        private final int radius;

        public RoundedBorder(Color color, int thickness, int radius) {
            this.color = color;
            this.thickness = thickness;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(x + thickness / 2, y + thickness / 2,
                    width - thickness, height - thickness, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(thickness, thickness, thickness, thickness);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = insets.top = insets.bottom = thickness;
            return insets;
        }
    }

    private static void setComponentTheme(Component comp, Theme theme) {
        if (comp instanceof JPanel) {
            if (theme == Theme.DARK) {
                comp.setBackground(new Color(52, 73, 94));
            } else {
                comp.setBackground(new Color(236, 240, 241));
            }
            // Borde redondeado para ambos modos
            Border rounded = new RoundedBorder(
                    theme == Theme.DARK ? new Color(33, 47, 60) : new Color(44, 62, 80),
                    3, 18);
            ((JPanel) comp).setBorder(rounded);
        }
        if (comp instanceof JLabel) {
            Color bg = comp.getBackground();
            if (theme == Theme.DARK) {
                ((JLabel) comp).setForeground(Color.WHITE);
            } else {
                if (bg != null && (bg.equals(Color.WHITE) || bg.equals(new Color(236, 240, 241)))) {
                    ((JLabel) comp).setForeground(new Color(44, 62, 80));
                } else {
                    ((JLabel) comp).setForeground(Color.BLACK);
                }
            }
        }
        if (comp instanceof JSlider) {
            JSlider slider = (JSlider) comp;
            if (theme == Theme.DARK) {
                slider.setBackground(new Color(52, 73, 94));
                slider.setForeground(Color.WHITE);
                slider.setOpaque(true);
                UIManager.put("Slider.trackColor", new Color(44, 62, 80));
                UIManager.put("Slider.thumb", new Color(189, 195, 199));
                UIManager.put("Slider.tickColor", Color.WHITE);
            } else {
                slider.setBackground(new Color(236, 240, 241));
                slider.setForeground(new Color(44, 62, 80));
                slider.setOpaque(true);
                UIManager.put("Slider.trackColor", new Color(189, 195, 199));
                UIManager.put("Slider.thumb", new Color(52, 152, 219));
                UIManager.put("Slider.tickColor", new Color(44, 62, 80));
            }
            SwingUtilities.updateComponentTreeUI(slider);
        }
        if (comp instanceof JTable) {
            JTable table = (JTable) comp;
            if (theme == Theme.DARK) {
                table.setBackground(new Color(52, 73, 94));
                table.setForeground(Color.WHITE);
                table.setSelectionBackground(new Color(33, 47, 60));
                table.setSelectionForeground(Color.WHITE);
                table.setGridColor(new Color(44, 62, 80));
            } else {
                table.setBackground(Color.WHITE);
                table.setForeground(Color.BLACK);
                table.setSelectionBackground(new Color(52, 152, 219));
                table.setSelectionForeground(Color.WHITE);
                table.setGridColor(new Color(236, 240, 241));
            }
            JTableHeader header = table.getTableHeader();
            if (header != null) {
                if (theme == Theme.DARK) {
                    header.setBackground(new Color(44, 62, 80));
                    header.setForeground(Color.WHITE);
                } else {
                    header.setBackground(new Color(236, 240, 241));
                    header.setForeground(new Color(44, 62, 80));
                }
            }
        }
        if (comp instanceof JButton) {
            JButton btn = (JButton) comp;
            // Colores de fondo para los estados
            Color normalBg = theme == Theme.DARK ? new Color(41, 128, 185) : new Color(52, 152, 219);
            Color hoverBg = theme == Theme.DARK ? new Color(52, 152, 219) : new Color(93, 173, 226); // Más claro en
                                                                                                     // hover
            Color pressedBg = theme == Theme.DARK ? new Color(33, 47, 60) : new Color(31, 97, 141); // Más oscuro al
                                                                                                    // pulsar

            // Borde: azul en oscuro, gris oscuro en claro
            Border rounded = new RoundedBorder(
                    theme == Theme.DARK ? new Color(52, 152, 219) : new Color(44, 62, 80),
                    2, 18);
            btn.setBorder(rounded);

            btn.setBackground(normalBg);
            btn.setForeground(theme == Theme.DARK ? Color.WHITE : Color.BLACK);
            btn.setFont(new Font("Dialog", Font.BOLD, 18));
            btn.setMargin(new Insets(12, 24, 12, 24));
            btn.setContentAreaFilled(false);
            btn.setOpaque(false);

            // Elimina listeners anteriores para evitar duplicados
            for (MouseListener ml : btn.getMouseListeners()) {
                if (ml.getClass().getName().contains("ButtonPressEffect")
                        || ml.getClass().getName().contains("MouseAdapter")) {
                    btn.removeMouseListener(ml);
                }
            }
            // Animación de hover y pulsado
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    btn.setBackground(hoverBg);
                    btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    btn.repaint();
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    btn.setBackground(normalBg);
                    btn.setCursor(Cursor.getDefaultCursor());
                    btn.repaint();
                }

                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    btn.setBackground(pressedBg);
                    btn.repaint();
                }

                @Override
                public void mouseReleased(java.awt.event.MouseEvent e) {
                    // Si el ratón sigue encima, vuelve a hover, si no a normal
                    Point p = e.getPoint();
                    if (p.x >= 0 && p.x < btn.getWidth() && p.y >= 0 && p.y < btn.getHeight()) {
                        btn.setBackground(hoverBg);
                    } else {
                        btn.setBackground(normalBg);
                    }
                    btn.repaint();
                }
            });
        }
        if (comp instanceof JTextField || comp instanceof JTextArea || comp instanceof JPasswordField) {
            comp.setBackground(theme == Theme.DARK ? new Color(44, 62, 80) : Color.WHITE);
            comp.setForeground(theme == Theme.DARK ? Color.WHITE : Color.BLACK);
            if (comp instanceof JTextComponent) {
                ((JTextComponent) comp).setCaretColor(theme == Theme.DARK ? Color.WHITE : Color.BLACK);
            }
        }
        if (comp instanceof JScrollPane) {
            comp.setBackground(theme == Theme.DARK ? new Color(44, 62, 80) : Color.WHITE);
            comp.setForeground(theme == Theme.DARK ? Color.WHITE : Color.BLACK);
        }
        if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                setComponentTheme(child, theme);
            }
        }
    }

    public static Theme getCurrentTheme() {
        return currentTheme;
    }
}

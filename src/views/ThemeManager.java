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
                // Colores base para modo oscuro
                UIManager.put("control", new Color(44, 62, 80));
                UIManager.put("info", new Color(44, 62, 80));
                UIManager.put("nimbusBase", new Color(44, 62, 80));
                UIManager.put("nimbusBlueGrey", new Color(44, 62, 80));
                UIManager.put("nimbusLightBackground", new Color(52, 73, 94));
                UIManager.put("text", Color.WHITE);
                UIManager.put("nimbusFocus", new Color(52, 152, 219));
                // Igualar bordes y fuentes al modo claro
                UIManager.put("Button.font", FUENTE_NORMAL);
                UIManager.put("Label.font", FUENTE_NORMAL);
                UIManager.put("TextField.font", FUENTE_NORMAL);
                UIManager.put("TextArea.font", FUENTE_NORMAL);
                UIManager.put("ComboBox.font", FUENTE_NORMAL);
                UIManager.put("TitledBorder.font", FUENTE_NORMAL);
                UIManager.put("Panel.background", new Color(44, 62, 80));
                UIManager.put("Button.background", COLOR_PRIMARIO);
                UIManager.put("Button.foreground", Color.WHITE);
                UIManager.put("TextField.background", new Color(52, 73, 94));
                UIManager.put("TextField.foreground", Color.WHITE);
                UIManager.put("TextArea.background", new Color(52, 73, 94));
                UIManager.put("TextArea.foreground", Color.WHITE);
                UIManager.put("ComboBox.background", new Color(52, 73, 94));
                UIManager.put("ComboBox.foreground", Color.WHITE);
                UIManager.put("ScrollPane.background", new Color(44, 62, 80));
                UIManager.put("ScrollPane.foreground", Color.WHITE);
                UIManager.put("TitledBorder.titleColor", COLOR_PRIMARIO);
            } else {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                UIManager.put("control", Color.WHITE);
                UIManager.put("info", Color.WHITE);
                UIManager.put("nimbusBase", new Color(52, 152, 219));
                UIManager.put("nimbusBlueGrey", new Color(236, 240, 241));
                UIManager.put("nimbusLightBackground", Color.WHITE);
                UIManager.put("text", Color.BLACK);
                UIManager.put("nimbusFocus", new Color(52, 152, 219));
                UIManager.put("Button.font", FUENTE_NORMAL);
                UIManager.put("Label.font", FUENTE_NORMAL);
                UIManager.put("TextField.font", FUENTE_NORMAL);
                UIManager.put("TextArea.font", FUENTE_NORMAL);
                UIManager.put("ComboBox.font", FUENTE_NORMAL);
                UIManager.put("TitledBorder.font", FUENTE_NORMAL);
                UIManager.put("Panel.background", Color.WHITE);
                UIManager.put("Button.background", COLOR_PRIMARIO);
                UIManager.put("Button.foreground", Color.WHITE);
                UIManager.put("TextField.background", Color.WHITE);
                UIManager.put("TextField.foreground", Color.BLACK);
                UIManager.put("TextArea.background", Color.WHITE);
                UIManager.put("TextArea.foreground", Color.BLACK);
                UIManager.put("ComboBox.background", Color.WHITE);
                UIManager.put("ComboBox.foreground", Color.BLACK);
                UIManager.put("ScrollPane.background", Color.WHITE);
                UIManager.put("ScrollPane.foreground", Color.BLACK);
                UIManager.put("TitledBorder.titleColor", COLOR_PRIMARIO);
            }
        } catch (Exception ignored) {
        }

        for (Window window : Window.getWindows()) {
            setComponentTheme(window, theme);
            SwingUtilities.updateComponentTreeUI(window);
        }
    }

    public static class RoundedButton extends JButton {
        private final int radius;

        public RoundedButton(String text, int radius) {
            super(text);
            this.radius = radius;
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g);
            g2.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getForeground());
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, radius, radius);
            g2.dispose();
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

    public static void setComponentTheme(Component comp, Theme theme) {
        // Paneles
        if (comp instanceof JPanel) {
            comp.setBackground(theme == Theme.DARK ? new Color(44, 62, 80) : Color.WHITE);
            Border rounded = new RoundedBorder(
                    theme == Theme.DARK ? new Color(41, 128, 185, 120) : new Color(41, 128, 185, 120),
                    2, 18);
            ((JPanel) comp).setBorder(rounded);
        }
        // Labels
        if (comp instanceof JLabel) {
            if (theme == Theme.DARK) {
                ((JLabel) comp).setForeground(Color.WHITE);
            } else {
                ((JLabel) comp).setForeground(new Color(44, 62, 80));
            }
        }
        // Sliders
        if (comp instanceof JSlider) {
            JSlider slider = (JSlider) comp;
            slider.setBackground(theme == Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
            slider.setForeground(theme == Theme.DARK ? Color.WHITE : Color.BLACK);
            slider.setOpaque(true);
        }
        // Tablas
        if (comp instanceof JTable) {
            JTable table = (JTable) comp;
            table.setBackground(theme == Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
            table.setForeground(theme == Theme.DARK ? Color.WHITE : Color.BLACK);
            table.setSelectionBackground(theme == Theme.DARK ? new Color(33, 47, 60) : new Color(52, 152, 219));
            table.setSelectionForeground(Color.WHITE);
            table.setGridColor(theme == Theme.DARK ? new Color(44, 62, 80) : new Color(236, 240, 241));
            JTableHeader header = table.getTableHeader();
            if (header != null) {
                header.setBackground(theme == Theme.DARK ? new Color(44, 62, 80) : new Color(236, 240, 241));
                header.setForeground(theme == Theme.DARK ? Color.WHITE : new Color(44, 62, 80));
            }
        }
        // Botones
        if (comp instanceof JButton) {
            JButton btn = (JButton) comp;
            Color colorBase = (Color) btn.getClientProperty("colorBase");
            if (colorBase == null) {
                colorBase = COLOR_PRIMARIO;
            }
            Color normalBg = colorBase;
            Color hoverBg = colorBase.brighter();
            Color pressedBg = colorBase.darker();

            Border rounded = new RoundedBorder(
                    theme == Theme.DARK ? colorBase : new Color(44, 62, 80),
                    2, 24
            );
            btn.setBorder(rounded);

            btn.setBackground(normalBg);
            btn.setForeground(Color.WHITE);
            btn.setFont(FUENTE_NORMAL);
            btn.setMargin(new Insets(12, 24, 12, 24));
            btn.setContentAreaFilled(true);
            btn.setOpaque(true);

            for (MouseListener ml : btn.getMouseListeners()) {
                btn.removeMouseListener(ml);
            }
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
        // Campos de texto
        if (comp instanceof JTextField || comp instanceof JTextArea || comp instanceof JPasswordField) {
            comp.setBackground(theme == Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
            comp.setForeground(theme == Theme.DARK ? Color.WHITE : Color.BLACK);
            if (comp instanceof JTextComponent) {
                ((JTextComponent) comp).setCaretColor(theme == Theme.DARK ? Color.WHITE : Color.BLACK);
            }
        }
        // ComboBox
        if (comp instanceof JComboBox) {
            comp.setBackground(theme == Theme.DARK ? new Color(52, 73, 94) : Color.WHITE);
            comp.setForeground(theme == Theme.DARK ? Color.WHITE : Color.BLACK);
        }
        // ScrollPane
        if (comp instanceof JScrollPane) {
            comp.setBackground(theme == Theme.DARK ? new Color(44, 62, 80) : Color.WHITE);
            comp.setForeground(theme == Theme.DARK ? Color.WHITE : Color.BLACK);
        }
        // Recursivo para hijos
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
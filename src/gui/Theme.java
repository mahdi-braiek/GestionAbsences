package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class Theme {

    public static final Color PRIMARY = new Color(0, 212, 255);
    public static final Color PRIMARY_DARK = new Color(0, 180, 216);
    public static final Color PRIMARY_LIGHT = new Color(102, 224, 255);

    public static final Color SECONDARY = new Color(139, 92, 246);
    public static final Color SECONDARY_DARK = new Color(109, 40, 217);
    public static final Color SECONDARY_LIGHT = new Color(167, 139, 250);

    public static final Color ACCENT = new Color(236, 72, 153);
    public static final Color ACCENT_LIGHT = new Color(244, 114, 182);

    public static final Color DANGER = new Color(251, 91, 91);
    public static final Color WARNING = new Color(251, 191, 36);
    public static final Color SUCCESS = new Color(52, 211, 153);
    public static final Color INFO = new Color(59, 130, 246);

    public static final Color BG_DARK = new Color(10, 10, 30);
    public static final Color BG_DARK_ALT = new Color(15, 23, 42);
    public static final Color BG_MEDIUM = new Color(22, 33, 62);
    public static final Color BG_LIGHT = new Color(30, 41, 82);
    public static final Color BG_CARD = new Color(25, 36, 72);
    public static final Color BG_GLASS = new Color(255, 255, 255, 8);
    public static final Color BG_GLASS_HOVER = new Color(255, 255, 255, 15);

    public static final Color TEXT_PRIMARY = new Color(248, 250, 252);
    public static final Color TEXT_SECONDARY = new Color(148, 163, 184);
    public static final Color TEXT_MUTED = new Color(100, 116, 139);
    public static final Color TEXT_GRADIENT_START = new Color(0, 212, 255);
    public static final Color TEXT_GRADIENT_END = new Color(139, 92, 246);

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 32);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADING = new Font("Segoe UI Semibold", Font.PLAIN, 16);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font("Segoe UI Semibold", Font.PLAIN, 14);
    public static final Font FONT_MONO = new Font("Consolas", Font.PLAIN, 13);

    public static final int BORDER_RADIUS = 16;
    public static final int PADDING = 20;
    public static final int BUTTON_HEIGHT = 48;

    public static final Color SHADOW_COLOR = new Color(0, 0, 0, 80);
    public static final Color GLOW_PRIMARY = new Color(0, 212, 255, 60);
    public static final Color GLOW_SECONDARY = new Color(139, 92, 246, 60);

    public static final Color BORDER = new Color(51, 65, 85);
    public static final Color BORDER_LIGHT = new Color(71, 85, 105);

    public static void applyGlobalTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        UIManager.put("Panel.background", BG_DARK);
        UIManager.put("TextField.background", BG_LIGHT);
        UIManager.put("TextField.foreground", TEXT_PRIMARY);
        UIManager.put("TextField.caretForeground", PRIMARY);
        UIManager.put("PasswordField.background", BG_LIGHT);
        UIManager.put("PasswordField.foreground", TEXT_PRIMARY);
        UIManager.put("PasswordField.caretForeground", PRIMARY);
        UIManager.put("ComboBox.background", BG_LIGHT);
        UIManager.put("ComboBox.foreground", TEXT_PRIMARY);
        UIManager.put("ComboBox.selectionBackground", PRIMARY);
        UIManager.put("ComboBox.selectionForeground", BG_DARK);
        UIManager.put("Table.background", BG_MEDIUM);
        UIManager.put("Table.foreground", TEXT_PRIMARY);
        UIManager.put("Table.gridColor", BORDER);
        UIManager.put("Table.selectionBackground", PRIMARY);
        UIManager.put("Table.selectionForeground", BG_DARK);
        UIManager.put("TableHeader.background", BG_LIGHT);
        UIManager.put("TableHeader.foreground", TEXT_PRIMARY);
        UIManager.put("ScrollPane.background", BG_DARK);
        UIManager.put("OptionPane.background", BG_MEDIUM);
        UIManager.put("OptionPane.messageForeground", TEXT_PRIMARY);
        UIManager.put("Button.select", PRIMARY_DARK);
    }

    public static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text) {
            private boolean isHovered = false;
            private float glowIntensity = 0f;

            {
                setContentAreaFilled(false);
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        isHovered = true;
                        repaint();
                    }

                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        isHovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (isHovered) {
                    g2d.setColor(new Color(0, 212, 255, 40));
                    g2d.fill(new RoundRectangle2D.Float(-3, -3, getWidth() + 6, getHeight() + 6, 20, 20));
                }

                GradientPaint gradient = new GradientPaint(
                        0, 0, PRIMARY,
                        getWidth(), getHeight(), isHovered ? ACCENT : SECONDARY);
                g2d.setPaint(gradient);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));

                g2d.setColor(TEXT_PRIMARY);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), x, y);

                g2d.dispose();
            }
        };

        button.setFont(FONT_BUTTON);
        button.setForeground(TEXT_PRIMARY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, BUTTON_HEIGHT));
        button.setOpaque(false);

        return button;
    }

    public static JButton createSecondaryButton(String text) {
        JButton button = new JButton(text) {
            private boolean isHovered = false;

            {
                setContentAreaFilled(false);
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        isHovered = true;
                        repaint();
                    }

                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        isHovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(
                        isHovered ? SUCCESS : new Color(SUCCESS.getRed(), SUCCESS.getGreen(), SUCCESS.getBlue(), 200));
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));

                g2d.setColor(BG_DARK);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), x, y);

                g2d.dispose();
            }
        };

        button.setFont(FONT_BUTTON);
        button.setForeground(BG_DARK);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, BUTTON_HEIGHT));
        button.setOpaque(false);

        return button;
    }

    public static JButton createDangerButton(String text) {
        JButton button = new JButton(text) {
            private boolean isHovered = false;

            {
                setContentAreaFilled(false);
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        isHovered = true;
                        repaint();
                    }

                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        isHovered = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (isHovered) {
                    g2d.setColor(new Color(251, 91, 91, 40));
                    g2d.fill(new RoundRectangle2D.Float(-3, -3, getWidth() + 6, getHeight() + 6, 20, 20));
                }

                g2d.setColor(isHovered ? new Color(239, 68, 68) : DANGER);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));

                g2d.setColor(TEXT_PRIMARY);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2d.drawString(getText(), x, y);

                g2d.dispose();
            }
        };

        button.setFont(FONT_BUTTON);
        button.setForeground(TEXT_PRIMARY);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, BUTTON_HEIGHT));
        button.setOpaque(false);

        return button;
    }

    public static JTextField createTextField() {
        JTextField field = new JTextField() {
            private boolean isFocused = false;

            {
                addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusGained(java.awt.event.FocusEvent e) {
                        isFocused = true;
                        repaint();
                    }

                    public void focusLost(java.awt.event.FocusEvent e) {
                        isFocused = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(BG_LIGHT);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));

                g2d.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (isFocused) {
                    g2d.setColor(PRIMARY);
                    g2d.setStroke(new BasicStroke(2));
                } else {
                    g2d.setColor(BORDER);
                    g2d.setStroke(new BasicStroke(1));
                }
                g2d.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 12, 12));

                g2d.dispose();
            }
        };

        field.setFont(FONT_REGULAR);
        field.setBackground(BG_LIGHT);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(PRIMARY);
        field.setOpaque(false);
        field.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        field.setPreferredSize(new Dimension(300, 50));
        return field;
    }

    public static JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField() {
            private boolean isFocused = false;

            {
                addFocusListener(new java.awt.event.FocusAdapter() {
                    public void focusGained(java.awt.event.FocusEvent e) {
                        isFocused = true;
                        repaint();
                    }

                    public void focusLost(java.awt.event.FocusEvent e) {
                        isFocused = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(BG_LIGHT);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));

                g2d.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (isFocused) {
                    g2d.setColor(PRIMARY);
                    g2d.setStroke(new BasicStroke(2));
                } else {
                    g2d.setColor(BORDER);
                    g2d.setStroke(new BasicStroke(1));
                }
                g2d.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 2, getHeight() - 2, 12, 12));

                g2d.dispose();
            }
        };

        field.setFont(FONT_REGULAR);
        field.setBackground(BG_LIGHT);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(PRIMARY);
        field.setOpaque(false);
        field.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        field.setPreferredSize(new Dimension(300, 50));
        return field;
    }

    public static JPanel createCardPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 40));
                g2d.fill(new RoundRectangle2D.Float(4, 4, getWidth() - 4, getHeight() - 4, 20, 20));

                g2d.setColor(BG_CARD);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 4, getHeight() - 4, 20, 20));

                g2d.setColor(BORDER);
                g2d.setStroke(new BasicStroke(1));
                g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 5, getHeight() - 5, 20, 20));

                g2d.dispose();
            }
        };

        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        return panel;
    }

    public static JPanel createGradientPanel(Color startColor, Color endColor) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                GradientPaint gradient = new GradientPaint(
                        0, 0, startColor,
                        getWidth(), getHeight(), endColor);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
    }

    public static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_TITLE);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    public static JLabel createGradientTitleLabel(String text) {
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                        0, 0, PRIMARY,
                        getWidth(), 0, SECONDARY);
                g2d.setPaint(gradient);
                g2d.setFont(getFont());

                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString(getText(), 0, fm.getAscent());

                g2d.dispose();
            }
        };
        label.setFont(FONT_TITLE);
        return label;
    }

    public static JLabel createSubtitleLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_SUBTITLE);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    public static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_REGULAR);
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    public static JLabel createSecondaryLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(FONT_SMALL);
        label.setForeground(TEXT_SECONDARY);
        return label;
    }

    public static <T> JComboBox<T> createComboBox() {
        JComboBox<T> comboBox = new JComboBox<T>() {
            @Override
            public void updateUI() {
                super.updateUI();
                setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
                    @Override
                    protected JButton createArrowButton() {
                        JButton btn = new JButton("▼");
                        btn.setContentAreaFilled(false);
                        btn.setFocusPainted(false);
                        btn.setBorder(BorderFactory.createEmptyBorder());
                        btn.setForeground(TEXT_PRIMARY);
                        return btn;
                    }

                    @Override
                    public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                        g.setColor(BG_LIGHT);
                        g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
                    }

                    @Override
                    protected void installDefaults() {
                        super.installDefaults();
                        comboBox.setBackground(BG_LIGHT);
                        comboBox.setForeground(TEXT_PRIMARY);
                        comboBox.setOpaque(true);
                    }
                });
            }
        };
        comboBox.setFont(FONT_REGULAR);
        comboBox.setBackground(BG_LIGHT);
        comboBox.setForeground(TEXT_PRIMARY);
        comboBox.setBorder(new LineBorder(BORDER, 1, true));
        comboBox.setPreferredSize(new Dimension(300, 50));

        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? PRIMARY : BG_LIGHT);
                setForeground(isSelected ? BG_DARK : TEXT_PRIMARY);
                setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                return this;
            }
        });

        return comboBox;
    }

    public static JSpinner createSpinner(SpinnerModel model) {
        JSpinner spinner = new JSpinner(model);
        spinner.setFont(FONT_REGULAR);
        spinner.setBackground(BG_LIGHT);
        spinner.setForeground(TEXT_PRIMARY);
        spinner.setBorder(new LineBorder(BORDER, 1, true));
        spinner.setPreferredSize(new Dimension(100, 50));

        JComponent editor = spinner.getEditor();
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) editor).getTextField();
            tf.setBackground(BG_LIGHT);
            tf.setForeground(TEXT_PRIMARY);
            tf.setCaretColor(PRIMARY);
        }

        return spinner;
    }

    public static void styleTable(JTable table) {
        table.setFont(FONT_REGULAR);
        table.setBackground(BG_MEDIUM);
        table.setForeground(TEXT_PRIMARY);
        table.setSelectionBackground(PRIMARY);
        table.setSelectionForeground(BG_DARK);
        table.setGridColor(BORDER);
        table.setRowHeight(48);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);

        table.getTableHeader().setFont(FONT_HEADING);
        table.getTableHeader().setBackground(BG_LIGHT);
        table.getTableHeader().setForeground(PRIMARY);
        table.getTableHeader().setBorder(new LineBorder(BORDER, 1));
        table.getTableHeader().setPreferredSize(new Dimension(0, 50));
    }

    public static JScrollPane createScrollPane(Component view) {
        JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.setBackground(BG_DARK);
        scrollPane.setBorder(new LineBorder(BORDER, 1, true));
        scrollPane.getViewport().setBackground(BG_MEDIUM);

        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = BORDER_LIGHT;
                this.trackColor = BG_DARK;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(thumbColor);
                g2d.fill(new RoundRectangle2D.Float(thumbBounds.x + 2, thumbBounds.y,
                        thumbBounds.width - 4, thumbBounds.height, 8, 8));
                g2d.dispose();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                return button;
            }
        });

        return scrollPane;
    }

    public static void showSuccess(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "✅ Succès", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showError(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "❌ Erreur", JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfo(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "ℹ️ Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public static boolean showConfirm(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, "❓ Confirmation",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
}

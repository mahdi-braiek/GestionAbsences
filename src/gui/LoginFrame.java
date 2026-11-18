package gui;

import dao.AuthentificationDAO;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import model.Utilisateur;

public class LoginFrame extends JFrame {

    private JTextField loginField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;

    private AuthentificationDAO authDAO;

    private Timer animationTimer;
    private float[] orbPositions = { 0, 0.33f, 0.66f };
    private float animationPhase = 0;

    public LoginFrame() {
        authDAO = new AuthentificationDAO();
        initializeUI();
        startBackgroundAnimation();
    }

    private void startBackgroundAnimation() {
        animationTimer = new Timer(50, e -> {
            animationPhase += 0.02f;
            if (animationPhase > 2 * Math.PI) {
                animationPhase = 0;
            }
            getContentPane().repaint();
        });
        animationTimer.start();
    }

    private void initializeUI() {
        setTitle("Gestion des Absences - Connexion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(10, 10, 30),
                        getWidth(), getHeight(), new Color(25, 15, 45));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                drawAnimatedOrbs(g2d);

                drawDecorationGrid(g2d);
            }

            private void drawAnimatedOrbs(Graphics2D g2d) {
                float x1 = (float) (150 + Math.sin(animationPhase) * 100);
                float y1 = (float) (200 + Math.cos(animationPhase * 0.7) * 80);
                drawOrb(g2d, x1, y1, 250, new Color(0, 212, 255, 40));

                float x2 = (float) (getWidth() - 200 + Math.sin(animationPhase * 0.8 + 2) * 80);
                float y2 = (float) (getHeight() - 180 + Math.cos(animationPhase * 0.6) * 60);
                drawOrb(g2d, x2, y2, 300, new Color(139, 92, 246, 35));

                float x3 = (float) (getWidth() / 2 + Math.sin(animationPhase * 0.5 + 4) * 120);
                float y3 = (float) (100 + Math.cos(animationPhase * 0.9) * 50);
                drawOrb(g2d, x3, y3, 180, new Color(236, 72, 153, 30));
            }

            private void drawOrb(Graphics2D g2d, float x, float y, float size, Color color) {
                RadialGradientPaint orbGradient = new RadialGradientPaint(
                        x, y, size,
                        new float[] { 0f, 0.5f, 1f },
                        new Color[] { color, new Color(color.getRed(), color.getGreen(), color.getBlue(), 20),
                                new Color(0, 0, 0, 0) });
                g2d.setPaint(orbGradient);
                g2d.fill(new Ellipse2D.Float(x - size, y - size, size * 2, size * 2));
            }

            private void drawDecorationGrid(Graphics2D g2d) {
                g2d.setColor(new Color(255, 255, 255, 5));
                g2d.setStroke(new BasicStroke(1));
                for (int i = 0; i < getWidth(); i += 50) {
                    g2d.drawLine(i, 0, i, getHeight());
                }
                for (int i = 0; i < getHeight(); i += 50) {
                    g2d.drawLine(0, i, getWidth(), i);
                }
            }
        };
        mainPanel.setLayout(new GridBagLayout());

        JPanel loginCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                for (int i = 0; i < 20; i++) {
                    g2d.setColor(new Color(0, 0, 0, 8 - i / 3));
                    g2d.fill(new RoundRectangle2D.Float(i, i, getWidth() - i * 2, getHeight() - i * 2, 30, 30));
                }

                g2d.setColor(new Color(22, 28, 56, 230));
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30));

                GradientPaint shine = new GradientPaint(
                        0, 0, new Color(255, 255, 255, 15),
                        0, 80, new Color(255, 255, 255, 0));
                g2d.setPaint(shine);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), 80, 30, 30));

                g2d.setColor(new Color(0, 212, 255, 50));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 3, getHeight() - 3, 30, 30));
            }
        };
        loginCard.setOpaque(false);
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setBorder(BorderFactory.createEmptyBorder(50, 60, 50, 60));
        loginCard.setPreferredSize(new Dimension(450, 580));

        JLabel iconLabel = new JLabel() {
            private float pulse = 0;
            {
                Timer pulseTimer = new Timer(50, e -> {
                    pulse += 0.05f;
                    if (pulse > Math.PI * 2)
                        pulse = 0;
                    repaint();
                });
                pulseTimer.start();

                Dimension size = new Dimension(140, 140);
                setPreferredSize(size);
                setMinimumSize(size);
                setMaximumSize(size);
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();
                int cx = w / 2;
                int cy = h / 2;

                float glowSize = (float) (5 + Math.sin(pulse) * 5);
                RadialGradientPaint glowPaint = new RadialGradientPaint(
                        cx, cy, 60 + glowSize,
                        new float[] { 0f, 1f },
                        new Color[] { new Color(0, 212, 255, 60), new Color(0, 0, 0, 0) });
                g2d.setPaint(glowPaint);
                g2d.fill(new Ellipse2D.Float(cx - 70, cy - 70, 140, 140));

                GradientPaint bgGradient = new GradientPaint(
                        cx - 40, cy - 40, Theme.PRIMARY,
                        cx + 40, cy + 40, Theme.SECONDARY);
                g2d.setPaint(bgGradient);
                g2d.fill(new Ellipse2D.Float(cx - 50, cy - 50, 100, 100));

                g2d.setColor(Color.WHITE);

                g2d.fill(new Ellipse2D.Float(cx - 14, cy - 18, 28, 28));

                g2d.setStroke(new BasicStroke(3));
                g2d.draw(new Arc2D.Float(cx - 28, cy - 8, 56, 50, 0, 180, Arc2D.OPEN));

                Polygon hat = new Polygon();
                hat.addPoint(cx, cy - 38);
                hat.addPoint(cx + 28, cy - 48);
                hat.addPoint(cx, cy - 58);
                hat.addPoint(cx - 28, cy - 48);
                g2d.fill(hat);

                g2d.dispose();
            }
        };
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Gestion des Absences") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                        0, 0, Theme.PRIMARY,
                        getWidth(), 0, Theme.SECONDARY);
                g2d.setPaint(gradient);
                g2d.setFont(getFont());

                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                g2d.drawString(getText(), x, fm.getAscent());

                g2d.dispose();
            }
        };
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setPreferredSize(new Dimension(350, 40));

        JLabel subtitleLabel = new JLabel("Connectez-vous à votre compte");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Theme.TEXT_SECONDARY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel loginFieldPanel = createFieldPanel("Identifiant", true);
        loginField = (JTextField) findTextField(loginFieldPanel);

        JPanel passwordFieldPanel = createFieldPanel("Mot de passe", false);
        passwordField = (JPasswordField) findTextField(passwordFieldPanel);

        loginButton = Theme.createPrimaryButton("Se connecter");
        loginButton.setMaximumSize(new Dimension(330, 52));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> effectuerConnexion());

        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    effectuerConnexion();
                }
            }
        });

        loginField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passwordField.requestFocus();
                }
            }
        });

        statusLabel = new JLabel(" ");
        statusLabel.setFont(Theme.FONT_SMALL);
        statusLabel.setForeground(Theme.DANGER);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginCard.add(iconLabel);
        loginCard.add(Box.createVerticalStrut(20));
        loginCard.add(titleLabel);
        loginCard.add(Box.createVerticalStrut(8));
        loginCard.add(subtitleLabel);
        loginCard.add(Box.createVerticalStrut(40));
        loginCard.add(loginFieldPanel);
        loginCard.add(Box.createVerticalStrut(20));
        loginCard.add(passwordFieldPanel);
        loginCard.add(Box.createVerticalStrut(12));
        loginCard.add(statusLabel);
        loginCard.add(Box.createVerticalStrut(25));
        loginCard.add(loginButton);

        mainPanel.add(loginCard);
        setContentPane(mainPanel);
    }

    private JPanel createFieldPanel(String labelText, boolean isTextField) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setMaximumSize(new Dimension(330, 80));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 12));
        label.setForeground(Theme.TEXT_SECONDARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        JComponent field;
        if (isTextField) {
            field = Theme.createTextField();
        } else {
            field = Theme.createPasswordField();
        }
        field.setMaximumSize(new Dimension(330, 50));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(8));
        panel.add(field);

        return panel;
    }

    private JComponent findTextField(JPanel panel) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof JTextField || comp instanceof JPasswordField) {
                return (JComponent) comp;
            }
        }
        return null;
    }

    private void effectuerConnexion() {
        String login = loginField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (login.isEmpty() || password.isEmpty()) {
            statusLabel.setText("⚠️ Veuillez remplir tous les champs");
            statusLabel.setForeground(Theme.WARNING);
            shakeComponent(loginButton);
            return;
        }

        loginButton.setEnabled(false);
        loginButton.setText("Connexion...");
        statusLabel.setText(" ");

        SwingWorker<Utilisateur, Void> worker = new SwingWorker<>() {
            @Override
            protected Utilisateur doInBackground() throws Exception {
                return authDAO.authentifier(login, password);
            }

            @Override
            protected void done() {
                try {
                    Utilisateur utilisateur = get();

                    if (utilisateur != null) {
                        statusLabel.setText("✅ Connexion réussie!");
                        statusLabel.setForeground(Theme.SUCCESS);

                        Timer timer = new Timer(500, e -> {
                            animationTimer.stop();
                            MainFrame mainFrame = new MainFrame(utilisateur);
                            mainFrame.setVisible(true);
                            dispose();
                        });
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        statusLabel.setText("❌ Identifiant ou mot de passe incorrect");
                        statusLabel.setForeground(Theme.DANGER);
                        shakeComponent(loginButton);
                        loginButton.setEnabled(true);
                        loginButton.setText("Se connecter");
                    }
                } catch (Exception e) {
                    statusLabel.setText("❌ Erreur de connexion à la base de données");
                    statusLabel.setForeground(Theme.DANGER);
                    loginButton.setEnabled(true);
                    loginButton.setText("Se connecter");
                    e.printStackTrace();
                }
            }
        };

        worker.execute();
    }

    private void shakeComponent(Component component) {
        final int distance = 8;
        final int cycles = 6;
        final int speed = 25;

        Point originalLocation = component.getLocation();

        Timer timer = new Timer(speed, null);
        final int[] count = { 0 };

        timer.addActionListener(e -> {
            if (count[0] >= cycles * 2) {
                component.setLocation(originalLocation);
                timer.stop();
            } else {
                int offset = (count[0] % 2 == 0) ? distance : -distance;
                component.setLocation(originalLocation.x + offset, originalLocation.y);
                count[0]++;
            }
        });

        timer.start();
    }

    public static void main(String[] args) {
        Theme.applyGlobalTheme();

        SwingUtilities.invokeLater(() -> {
            LoginFrame frame = new LoginFrame();
            frame.setVisible(true);
        });
    }
}

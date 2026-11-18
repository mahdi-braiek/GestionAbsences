package gui;

import dao.*;
import model.*;
import util.DBConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainFrame extends JFrame {

    private Utilisateur utilisateurConnecte;
    private JPanel contentPanel;
    private JPanel sidebarPanel;
    private JLabel userInfoLabel;
    private JLabel roleLabel;
    private JButton activeMenuButton = null;

    private AbsenceDAO absenceDAO;
    private EtudiantDAO etudiantDAO;
    private ClasseDAO classeDAO;
    private MatiereDAO matiereDAO;

    public MainFrame(Utilisateur utilisateur) {
        this.utilisateurConnecte = utilisateur;
        this.absenceDAO = new AbsenceDAO();
        this.etudiantDAO = new EtudiantDAO();
        this.classeDAO = new ClasseDAO();
        this.matiereDAO = new MatiereDAO();

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Gestion des Absences - " + utilisateurConnecte.getRole());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 850);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1100, 700));

        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                GradientPaint gradient = new GradientPaint(
                        0, 0, Theme.BG_DARK,
                        getWidth(), getHeight(), new Color(15, 15, 35));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        createSidebar();
        mainPanel.add(sidebarPanel, BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);

        showDashboard();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DBConnection.closeConnection();
            }
        });
    }

    private void createSidebar() {
        sidebarPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                g2d.setColor(new Color(18, 24, 50));
                g2d.fillRect(0, 0, getWidth(), getHeight());

                GradientPaint borderGradient = new GradientPaint(
                        getWidth() - 2, 0, Theme.PRIMARY,
                        getWidth() - 2, getHeight(), Theme.SECONDARY);
                g2d.setPaint(borderGradient);
                g2d.fillRect(getWidth() - 2, 0, 2, getHeight());
            }
        };
        sidebarPanel.setPreferredSize(new Dimension(280, 0));
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));

        JPanel userPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(0, 212, 255, 20),
                        getWidth(), getHeight(), new Color(139, 92, 246, 20));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(Theme.BORDER);
                g2d.fillRect(20, getHeight() - 1, getWidth() - 40, 1);
            }
        };
        userPanel.setOpaque(false);
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBorder(BorderFactory.createEmptyBorder(30, 25, 25, 25));
        userPanel.setMaximumSize(new Dimension(280, 160));
        userPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel iconLabel = new JLabel(getIconForRole()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 212, 255, 30));
                g2d.fillOval(-5, -5, 60, 60);

                g2d.dispose();
                super.paintComponent(g);
            }
        };
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 42));
        iconLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        userInfoLabel = new JLabel(utilisateurConnecte.getNomComplet());
        userInfoLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 18));
        userInfoLabel.setForeground(Theme.TEXT_PRIMARY);
        userInfoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        roleLabel = new JLabel(getRoleDisplayName());
        roleLabel.setFont(Theme.FONT_SMALL);
        roleLabel.setForeground(Theme.PRIMARY);
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        userPanel.add(iconLabel);
        userPanel.add(Box.createVerticalStrut(12));
        userPanel.add(userInfoLabel);
        userPanel.add(Box.createVerticalStrut(4));
        userPanel.add(roleLabel);

        sidebarPanel.add(userPanel);
        sidebarPanel.add(Box.createVerticalStrut(20));

        JPanel menuPanel = new JPanel();
        menuPanel.setOpaque(false);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        menuPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton dashboardBtn = createMenuButton("Tableau de bord", e -> showDashboard());
        menuPanel.add(dashboardBtn);
        menuPanel.add(Box.createVerticalStrut(8));

        switch (utilisateurConnecte.getRole()) {
            case "ETUDIANT":
                menuPanel.add(createMenuButton("Mon Profil", e -> showProfil()));
                menuPanel.add(Box.createVerticalStrut(8));
                menuPanel.add(createMenuButton("Mes absences", e -> showMesAbsences()));
                break;

            case "ENSEIGNANT":
                menuPanel.add(createMenuButton("Mon Profil", e -> showProfil()));
                menuPanel.add(Box.createVerticalStrut(8));
                menuPanel.add(createMenuButton("Enregistrer absence", e -> showEnregistrerAbsence()));
                menuPanel.add(Box.createVerticalStrut(8));
                menuPanel.add(createMenuButton("Absences par classe", e -> showAbsencesClasse()));
                break;

            case "RESPONSABLE":
                menuPanel.add(createMenuButton("Mon Profil", e -> showProfil()));
                menuPanel.add(Box.createVerticalStrut(8));
                menuPanel.add(createMenuButton("Toutes les absences", e -> showToutesAbsences()));
                menuPanel.add(Box.createVerticalStrut(8));
                menuPanel.add(createMenuButton("Par classe", e -> showAbsencesParClasse()));
                menuPanel.add(Box.createVerticalStrut(8));
                menuPanel.add(createMenuButton("Par étudiant", e -> showAbsencesParEtudiant()));
                menuPanel.add(Box.createVerticalStrut(8));
                menuPanel.add(createMenuButton("Justifier absence", e -> showJustifierAbsence()));
                menuPanel.add(Box.createVerticalStrut(8));
                menuPanel.add(createMenuButton("Annuler absence", e -> showAnnulerAbsence()));
                menuPanel.add(Box.createVerticalStrut(8));
                menuPanel.add(createMenuButton("Statistiques", e -> showStatistiques()));
                break;
        }

        sidebarPanel.add(menuPanel);

        sidebarPanel.add(Box.createVerticalGlue());

        JPanel logoutPanel = new JPanel();
        logoutPanel.setOpaque(false);
        logoutPanel.setLayout(new BoxLayout(logoutPanel, BoxLayout.Y_AXIS));
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 25, 15));
        logoutPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutPanel.setMaximumSize(new Dimension(280, 80));

        JButton logoutBtn = createMenuButton("Déconnexion", e -> seDeconnecter());
        logoutBtn.setForeground(Theme.DANGER);
        logoutPanel.add(logoutBtn);

        sidebarPanel.add(logoutPanel);
    }

    private JButton createMenuButton(String text, ActionListener action) {
        JButton button = new JButton(text) {
            private boolean isHovered = false;
            private boolean isActive = false;

            {
                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        isHovered = false;
                        repaint();
                    }
                });
            }

            public void setActive(boolean active) {
                this.isActive = active;
                repaint();
            }

            public boolean isActive() {
                return isActive;
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (isActive || isHovered) {
                    GradientPaint gradient = new GradientPaint(
                            0, 0, new Color(0, 212, 255, isActive ? 30 : 20),
                            getWidth(), 0, new Color(139, 92, 246, isActive ? 30 : 20));
                    g2d.setPaint(gradient);
                    g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));

                    if (isActive) {
                        GradientPaint accentGradient = new GradientPaint(
                                0, 0, Theme.PRIMARY,
                                0, getHeight(), Theme.SECONDARY);
                        g2d.setPaint(accentGradient);
                        g2d.fillRect(0, 4, 3, getHeight() - 8);
                    }
                }

                if (isActive) {
                    g2d.setColor(Theme.PRIMARY);
                    g2d.fillOval(15, getHeight() / 2 - 3, 6, 6);
                } else {
                    g2d.setColor(Theme.TEXT_SECONDARY);
                    g2d.drawOval(15, getHeight() / 2 - 3, 5, 5);
                }

                g2d.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(Theme.FONT_REGULAR);
        button.setForeground(Theme.TEXT_PRIMARY);
        button.setBackground(new Color(0, 0, 0, 0));
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(250, 48));
        button.setPreferredSize(new Dimension(250, 48));
        button.setBorder(BorderFactory.createEmptyBorder(12, 35, 12, 18));

        button.addActionListener(e -> {
            if (activeMenuButton != null) {
                try {
                    activeMenuButton.getClass().getMethod("setActive", boolean.class)
                            .invoke(activeMenuButton, false);
                } catch (Exception ex) {
                }
            }
            try {
                button.getClass().getMethod("setActive", boolean.class)
                        .invoke(button, true);
            } catch (Exception ex) {
            }
            activeMenuButton = button;
            action.actionPerformed(e);
        });

        return button;
    }

    private String getIconForRole() {
        switch (utilisateurConnecte.getRole()) {
            case "ETUDIANT":
                return "🎓";
            case "ENSEIGNANT":
                return "👨‍🏫";
            case "RESPONSABLE":
                return "👔";
            default:
                return "👤";
        }
    }

    private String getRoleDisplayName() {
        switch (utilisateurConnecte.getRole()) {
            case "ETUDIANT":
                return "Étudiant";
            case "ENSEIGNANT":
                return "Enseignant";
            case "RESPONSABLE":
                return "Responsable Administratif";
            default:
                return utilisateurConnecte.getRole();
        }
    }

    private void setContent(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createPageHeader(String title, String subtitle) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));

        JPanel titles = new JPanel();
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));
        titles.setOpaque(false);

        JLabel titleLabel = new JLabel(title) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                GradientPaint gradient = new GradientPaint(
                        0, 0, Theme.TEXT_PRIMARY,
                        getWidth() * 0.7f, 0, Theme.PRIMARY);
                g2d.setPaint(gradient);
                g2d.setFont(getFont());
                g2d.drawString(getText(), 0, g2d.getFontMetrics().getAscent());

                g2d.dispose();
            }
        };
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = Theme.createSecondaryLabel(subtitle);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        titles.add(titleLabel);
        titles.add(Box.createVerticalStrut(6));
        titles.add(subtitleLabel);

        header.add(titles, BorderLayout.WEST);

        return header;
    }

    private void showDashboard() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel header = createPageHeader("Tableau de bord", "Bienvenue, " + utilisateurConnecte.getPrenom() + " ! 👋");

        JButton refreshBtn = createIconButton("Actualiser");
        refreshBtn.addActionListener(e -> showDashboard());

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerRight.setOpaque(false);
        headerRight.add(refreshBtn);
        header.add(headerRight, BorderLayout.EAST);

        panel.add(header, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 25, 0));
        cardsPanel.setOpaque(false);

        if (utilisateurConnecte.getRole().equals("ETUDIANT")) {
            Etudiant etudiant = (Etudiant) utilisateurConnecte;
            Vector<Absence> absences = absenceDAO.getByEtudiant(etudiant.getId());
            long nonJustifiees = absences.stream().filter(a -> !a.isJustifiee()).count();
            long justifiees = absences.stream().filter(Absence::isJustifiee).count();

            cardsPanel.add(createStatCard("Total Absences", String.valueOf(absences.size()), Theme.PRIMARY,
                    Theme.SECONDARY));
            cardsPanel.add(createStatCard("Non Justifiées", String.valueOf(nonJustifiees), Theme.DANGER,
                    new Color(251, 91, 91, 180)));
            cardsPanel.add(createStatCard("Justifiées", String.valueOf(justifiees), Theme.SUCCESS,
                    new Color(52, 211, 153, 180)));
        } else if (utilisateurConnecte.getRole().equals("ENSEIGNANT")) {
            Enseignant ens = (Enseignant) utilisateurConnecte;
            Vector<Absence> absences = absenceDAO.getByEnseignant(ens.getId());
            Vector<Classe> classes = classeDAO.getByEnseignant(ens.getId());

            int totalEtudiants = 0;
            for (Classe c : classes) {
                totalEtudiants += etudiantDAO.getByClasse(c.getIdClasse()).size();
            }

            cardsPanel.add(createStatCard("Mes Enregistrements", String.valueOf(absences.size()), Theme.PRIMARY,
                    Theme.SECONDARY));
            cardsPanel.add(
                    createStatCard("Mes Classes", String.valueOf(classes.size()), Theme.SECONDARY, Theme.ACCENT));
            cardsPanel.add(createStatCard("Mes Étudiants", String.valueOf(totalEtudiants), Theme.INFO,
                    new Color(59, 130, 246, 180)));
        } else {
            Vector<Absence> allAbsences = absenceDAO.getAll();
            Vector<Classe> classes = classeDAO.getAll();
            Vector<Etudiant> etudiants = etudiantDAO.getAll();

            cardsPanel.add(createStatCard("Total Absences", String.valueOf(allAbsences.size()), Theme.PRIMARY,
                    Theme.SECONDARY));
            cardsPanel.add(
                    createStatCard("Classes", String.valueOf(classes.size()), Theme.SECONDARY, Theme.ACCENT));
            cardsPanel.add(createStatCard("Étudiants", String.valueOf(etudiants.size()), Theme.INFO,
                    new Color(59, 130, 246, 180)));
        }

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(cardsPanel, BorderLayout.NORTH);

        JPanel chartsContainer = new JPanel(new GridLayout(1, 1));
        chartsContainer.setOpaque(false);
        chartsContainer.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        if (utilisateurConnecte.getRole().equals("ETUDIANT")) {
            long nonJustifiees = ((Etudiant) utilisateurConnecte).getId() == 0 ? 0
                    : absenceDAO.getByEtudiant(utilisateurConnecte.getId()).stream().filter(a -> !a.isJustifiee())
                            .count();
            long justifiees = ((Etudiant) utilisateurConnecte).getId() == 0 ? 0
                    : absenceDAO.getByEtudiant(utilisateurConnecte.getId()).stream().filter(Absence::isJustifiee)
                            .count();

            chartsContainer.add(createPieChart("Répartition des Absences", justifiees, nonJustifiees));
        } else {
            Map<String, Integer> data = new java.util.HashMap<>();
            Vector<Classe> classesList = utilisateurConnecte.getRole().equals("ENSEIGNANT")
                    ? classeDAO.getByEnseignant(utilisateurConnecte.getId())
                    : classeDAO.getAll();

            for (Classe c : classesList) {
                int count = 0;
                Vector<Etudiant> etuds = etudiantDAO.getByClasse(c.getIdClasse());
                for (Etudiant e : etuds) {
                    count += absenceDAO.getByEtudiant(e.getId()).size();
                }
                data.put(c.getLibelle(), count);
            }
            chartsContainer.add(createBarChart("Absences par Classe", data));
        }

        JPanel recentPanel = createModernCardPanel();
        recentPanel.setLayout(new BorderLayout());

        JLabel recentTitle = Theme.createSubtitleLabel("📊 Dernières absences");
        recentTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));
        recentPanel.add(recentTitle, BorderLayout.NORTH);

        Vector<Absence> recentAbsences;
        if (utilisateurConnecte.getRole().equals("ETUDIANT")) {
            recentAbsences = absenceDAO.getByEtudiant(utilisateurConnecte.getId());
        } else if (utilisateurConnecte.getRole().equals("ENSEIGNANT")) {
            recentAbsences = absenceDAO.getByEnseignant(utilisateurConnecte.getId());
        } else {
            recentAbsences = absenceDAO.getAll();
        }

        if (recentAbsences.size() > 5) {
            recentAbsences = new Vector<>(recentAbsences.subList(0, 5));
        }

        JTable table = createAbsenceTable(recentAbsences);
        JScrollPane scrollPane = Theme.createScrollPane(table);
        recentPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(
                new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        JPanel mainContent = new JPanel(
                new BorderLayout(25, 25));
        mainContent.setOpaque(false);
        mainContent.add(chartsContainer, BorderLayout.WEST);
        mainContent.add(recentPanel, BorderLayout.CENTER);

        bottomPanel.add(mainContent, BorderLayout.CENTER);

        centerPanel.add(bottomPanel, BorderLayout.CENTER);
        panel.add(centerPanel, BorderLayout.CENTER);

        setContent(panel);
    }

    private JButton createIconButton(String tooltip) {
        JButton button = new JButton() {
            private boolean isHovered = false;

            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        isHovered = true;
                        repaint();
                    }

                    public void mouseExited(MouseEvent e) {
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
                    g2d.setColor(new Color(0, 212, 255, 30));
                    g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 10, 10));
                }

                g2d.setColor(Theme.TEXT_PRIMARY);
                g2d.setStroke(new BasicStroke(2));
                int cx = getWidth() / 2;
                int cy = getHeight() / 2;
                int r = 8;
                g2d.drawArc(cx - r, cy - r, 2 * r, 2 * r, 45, 270);
                g2d.drawLine(cx + r - 3, cy - 3, cx + r, cy);
                g2d.drawLine(cx + r + 3, cy - 3, cx + r, cy);

                g2d.dispose();
                super.paintComponent(g);
            }
        };

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(45, 45));
        button.setToolTipText(tooltip);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        return button;
    }

    private JPanel createModernCardPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 30));
                g2d.fill(new RoundRectangle2D.Float(4, 4, getWidth() - 4, getHeight() - 4, 20, 20));

                g2d.setColor(Theme.BG_CARD);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 4, getHeight() - 4, 20, 20));

                g2d.setColor(Theme.BORDER);
                g2d.setStroke(new BasicStroke(1));
                g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 5, getHeight() - 5, 20, 20));

                g2d.dispose();
            }

            {
                setOpaque(false);
                setBorder(BorderFactory.createEmptyBorder(22, 25, 22, 25));
            }
        };
    }

    private JPanel createStatCard(String title, String value, Color color1, Color color2) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(0, 0, 0, 40));
                g2d.fill(new RoundRectangle2D.Float(4, 4, getWidth() - 4, getHeight() - 4, 20, 20));

                g2d.setColor(Theme.BG_CARD);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 4, getHeight() - 4, 20, 20));

                GradientPaint accentGradient = new GradientPaint(
                        0, 0, new Color(color1.getRed(), color1.getGreen(), color1.getBlue(), 50),
                        getWidth(), 0, new Color(color2.getRed(), color2.getGreen(), color2.getBlue(), 25));
                g2d.setPaint(accentGradient);
                g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 4, 60, 20, 20));
                g2d.fillRect(0, 30, getWidth() - 4, 30);

                g2d.setColor(new Color(color1.getRed(), color1.getGreen(), color1.getBlue(), 100));
                g2d.setStroke(new BasicStroke(1));
                g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 5, getHeight() - 5, 20, 20));

                g2d.dispose();
            }
        };

        card.setOpaque(false);
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(200, 150));
        card.setBorder(BorderFactory.createEmptyBorder(22, 24, 22, 24));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JLabel titleLabel = Theme.createSecondaryLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 48));
        valueLabel.setForeground(color1);

        content.add(Box.createVerticalStrut(10));
        content.add(titleLabel);
        content.add(Box.createVerticalStrut(10));
        content.add(valueLabel);

        card.add(content, BorderLayout.CENTER);

        return card;
    }

    private void showMesAbsences() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel header = createPageHeader("Mes Absences", "Consultez toutes vos absences enregistrées");
        JButton refreshBtn = createIconButton("Actualiser");
        refreshBtn.addActionListener(e -> showMesAbsences());
        header.add(refreshBtn, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        Etudiant etudiant = (Etudiant) utilisateurConnecte;
        Vector<Absence> absences = absenceDAO.getByEtudiant(etudiant.getId());

        JPanel tablePanel = createModernCardPanel();
        tablePanel.setLayout(new BorderLayout());

        JTable table = createAbsenceTable(absences);
        JScrollPane scrollPane = Theme.createScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(tablePanel, BorderLayout.CENTER);
        setContent(panel);
    }

    private void showToutesAbsences() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel header = createPageHeader("Toutes les Absences", "Liste complète des absences du système");
        JButton refreshBtn = createIconButton("Actualiser");
        refreshBtn.addActionListener(e -> showToutesAbsences());
        header.add(refreshBtn, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        Vector<Absence> absences = absenceDAO.getAll();

        JPanel tablePanel = createModernCardPanel();
        tablePanel.setLayout(new BorderLayout());

        JTable table = createAbsenceTable(absences);
        JScrollPane scrollPane = Theme.createScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        panel.add(tablePanel, BorderLayout.CENTER);
        setContent(panel);
    }

    private void showAbsencesClasse() {
        showAbsencesParClasse();
    }

    private void showAbsencesParClasse() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        panel.add(createPageHeader("Absences par Classe", "Sélectionnez une classe pour voir ses absences"),
                BorderLayout.NORTH);

        JPanel filterPanel = createModernCardPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel label = Theme.createLabel("Classe :");
        JComboBox<Classe> classeCombo = Theme.createComboBox();

        Vector<Classe> classes = classeDAO.getAll();
        for (Classe c : classes) {
            classeCombo.addItem(c);
        }

        JButton searchBtn = Theme.createPrimaryButton("Afficher");
        searchBtn.setPreferredSize(new Dimension(140, 45));

        filterPanel.add(label);
        filterPanel.add(classeCombo);
        filterPanel.add(Box.createHorizontalStrut(15));
        filterPanel.add(searchBtn);

        JPanel tablePanel = createModernCardPanel();
        tablePanel.setLayout(new BorderLayout());

        JTable table = createAbsenceTable(new Vector<>());
        JScrollPane scrollPane = Theme.createScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        searchBtn.addActionListener(e -> {
            Classe selected = (Classe) classeCombo.getSelectedItem();
            if (selected != null) {
                Vector<Absence> absences = absenceDAO.getByClasse(selected.getIdClasse());
                updateAbsenceTable(table, absences);
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setOpaque(false);
        centerPanel.add(filterPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);
        setContent(panel);
    }

    private void showAbsencesParEtudiant() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        panel.add(createPageHeader("Absences par Étudiant", "Sélectionnez un étudiant pour voir ses absences"),
                BorderLayout.NORTH);

        JPanel filterPanel = createModernCardPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filterPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel label = Theme.createLabel("Étudiant :");
        JComboBox<Etudiant> etudiantCombo = Theme.createComboBox();

        Vector<Etudiant> etudiants = etudiantDAO.getAll();
        for (Etudiant e : etudiants) {
            etudiantCombo.addItem(e);
        }

        JButton searchBtn = Theme.createPrimaryButton("Afficher");
        searchBtn.setPreferredSize(new Dimension(140, 45));

        filterPanel.add(label);
        filterPanel.add(etudiantCombo);
        filterPanel.add(Box.createHorizontalStrut(15));
        filterPanel.add(searchBtn);

        JPanel tablePanel = createModernCardPanel();
        tablePanel.setLayout(new BorderLayout());

        JTable table = createAbsenceTable(new Vector<>());
        JScrollPane scrollPane = Theme.createScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        searchBtn.addActionListener(e -> {
            Etudiant selected = (Etudiant) etudiantCombo.getSelectedItem();
            if (selected != null) {
                Vector<Absence> absences = absenceDAO.getByEtudiant(selected.getId());
                updateAbsenceTable(table, absences);
            }
        });

        JPanel centerPanel = new JPanel(new BorderLayout(0, 20));
        centerPanel.setOpaque(false);
        centerPanel.add(filterPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);
        setContent(panel);
    }

    private void showEnregistrerAbsence() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        panel.add(createPageHeader("Feuille d'Appel", "Cochez les étudiants absents pour cette séance"),
                BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 20, 40));

        JPanel configPanel = createModernCardPanel();
        configPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Enseignant enseignant = (Enseignant) utilisateurConnecte;

        gbc.gridx = 0;
        gbc.gridy = 0;
        configPanel.add(Theme.createLabel("Classe :"), gbc);

        JComboBox<Classe> classeCombo = Theme.createComboBox();
        Vector<Classe> classes = classeDAO.getByEnseignant(enseignant.getId());
        for (Classe c : classes)
            classeCombo.addItem(c);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        configPanel.add(classeCombo, gbc);

        gbc.gridx = 2;
        gbc.weightx = 0;
        configPanel.add(Theme.createLabel("Matière :"), gbc);

        JComboBox<Matiere> matiereCombo = Theme.createComboBox();
        gbc.gridx = 3;
        gbc.weightx = 1.0;
        configPanel.add(matiereCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        configPanel.add(Theme.createLabel("N° Séance :"), gbc);

        JSpinner seanceSpinner = Theme.createSpinner(new SpinnerNumberModel(1, 1, 20, 1));
        gbc.gridx = 1;
        configPanel.add(seanceSpinner, gbc);

        contentPanel.add(configPanel, BorderLayout.NORTH);

        JPanel listPanel = createModernCardPanel();
        listPanel.setLayout(new BorderLayout());
        listPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel listTitle = Theme.createSubtitleLabel("Liste des étudiants");
        listPanel.add(listTitle, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(new Object[] { "ID", "Nom & Prénom", "Absent ?" }, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
        };

        JTable textTable = new JTable(model);
        textTable.setRowHeight(45);
        textTable.setFont(Theme.FONT_REGULAR);
        textTable.setBackground(Theme.BG_CARD);
        textTable.setForeground(Theme.TEXT_PRIMARY);
        textTable.setSelectionBackground(new Color(0, 212, 255, 30));
        textTable.setSelectionForeground(Theme.TEXT_PRIMARY);

        textTable.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBackground(Theme.BG_LIGHT);
                setForeground(Theme.TEXT_PRIMARY);
                setFont(Theme.FONT_HEADING);
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER),
                        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
                return this;
            }
        });
        textTable.getTableHeader().setPreferredSize(new Dimension(0, 45));
        textTable.setShowGrid(false);
        textTable.setIntercellSpacing(new Dimension(0, 0));

        textTable.getColumnModel().getColumn(2).setCellEditor(textTable.getDefaultEditor(Boolean.class));
        textTable.getColumnModel().getColumn(2).setCellRenderer(textTable.getDefaultRenderer(Boolean.class));

        textTable.getColumnModel().getColumn(0).setMinWidth(0);
        textTable.getColumnModel().getColumn(0).setMaxWidth(0);
        textTable.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = Theme.createScrollPane(textTable);
        listPanel.add(scrollPane, BorderLayout.CENTER);

        contentPanel.add(listPanel, BorderLayout.CENTER);

        JButton saveBtn = Theme.createPrimaryButton("Valider les absences");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setOpaque(false);
        btnPanel.add(saveBtn);
        contentPanel.add(btnPanel, BorderLayout.SOUTH);

        classeCombo.addActionListener(e -> {
            matiereCombo.removeAllItems();
            model.setRowCount(0);

            Classe selected = (Classe) classeCombo.getSelectedItem();
            if (selected != null) {
                Vector<Matiere> matieres = matiereDAO.getByEnseignantAndClasse(enseignant.getId(),
                        selected.getIdClasse());
                for (Matiere m : matieres)
                    matiereCombo.addItem(m);

                Vector<Etudiant> etudiants = etudiantDAO.getByClasse(selected.getIdClasse());
                for (Etudiant et : etudiants) {
                    model.addRow(new Object[] { et.getId(), et.getNomComplet(), false });
                }
            }
        });

        if (classeCombo.getItemCount() > 0)
            classeCombo.setSelectedIndex(0);

        saveBtn.addActionListener(e -> {
            Classe classe = (Classe) classeCombo.getSelectedItem();
            Matiere matiere = (Matiere) matiereCombo.getSelectedItem();
            int seance = (Integer) seanceSpinner.getValue();

            if (classe == null || matiere == null) {
                Theme.showError(this, "Sélectionnez une classe et une matière.");
                return;
            }

            int count = 0;
            boolean error = false;

            for (int i = 0; i < model.getRowCount(); i++) {
                Boolean isAbsent = (Boolean) model.getValueAt(i, 2);
                if (isAbsent != null && isAbsent) {
                    int idEtudiant = (Integer) model.getValueAt(i, 0);

                    Absence absence = new Absence(0, idEtudiant, enseignant.getId(), matiere.getIdMatiere(), seance,
                            new java.util.Date(), false);
                    if (absenceDAO.ajouter(absence)) {
                        count++;
                    } else {
                        error = true;
                    }
                }
            }

            if (count > 0) {
                String msg = count + " absence(s) enregistrée(s) avec succès !";
                if (error)
                    msg += "\n(Certaines erreurs sont survenues)";
                Theme.showSuccess(this, msg);
                for (int i = 0; i < model.getRowCount(); i++)
                    model.setValueAt(false, i, 2);
            } else if (!error) {
                Theme.showInfo(this, "Aucune absence cochée.");
            } else {
                Theme.showError(this, "Erreur lors de l'enregistrement.");
            }
        });

        panel.add(contentPanel, BorderLayout.CENTER);
        setContent(panel);
    }

    private void showJustifierAbsence() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        panel.add(createPageHeader("Justifier une Absence", "Sélectionnez une absence à justifier"),
                BorderLayout.NORTH);

        Vector<Absence> absences = absenceDAO.getAll();

        JPanel tablePanel = createModernCardPanel();
        tablePanel.setLayout(new BorderLayout());

        JTable table = createAbsenceTable(absences);
        JScrollPane scrollPane = Theme.createScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.setOpaque(false);

        JButton justifyBtn = Theme.createSecondaryButton("✅ Justifier l'absence");
        justifyBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                Theme.showError(this, "Veuillez sélectionner une absence");
                return;
            }

            int idAbsence = (Integer) table.getValueAt(selectedRow, 0);
            Absence absence = absenceDAO.getById(idAbsence);

            if (absence.isJustifiee()) {
                Theme.showInfo(this, "Cette absence est déjà justifiée");
                return;
            }

            if (Theme.showConfirm(this, "Voulez-vous vraiment justifier cette absence?")) {
                if (absenceDAO.justifier(idAbsence)) {
                    Theme.showSuccess(this, "Absence justifiée avec succès!");
                    showJustifierAbsence();
                } else {
                    Theme.showError(this, "Erreur lors de la justification");
                }
            }
        });

        buttonPanel.add(justifyBtn);
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(tablePanel, BorderLayout.CENTER);
        setContent(panel);
    }

    private void showAnnulerAbsence() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        panel.add(createPageHeader("Annuler une Absence", "Sélectionnez une absence à supprimer"),
                BorderLayout.NORTH);

        Vector<Absence> absences = absenceDAO.getAll();

        JPanel tablePanel = createModernCardPanel();
        tablePanel.setLayout(new BorderLayout());

        JTable table = createAbsenceTable(absences);
        JScrollPane scrollPane = Theme.createScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        buttonPanel.setOpaque(false);

        JButton deleteBtn = Theme.createDangerButton("❌ Supprimer l'absence");
        deleteBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                Theme.showError(this, "Veuillez sélectionner une absence");
                return;
            }

            int idAbsence = (Integer) table.getValueAt(selectedRow, 0);

            if (Theme.showConfirm(this, "Voulez-vous vraiment supprimer cette absence?")) {
                if (absenceDAO.supprimer(idAbsence)) {
                    Theme.showSuccess(this, "Absence supprimée avec succès!");
                    showAnnulerAbsence();
                } else {
                    Theme.showError(this, "Erreur lors de la suppression");
                }
            }
        });

        buttonPanel.add(deleteBtn);
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(tablePanel, BorderLayout.CENTER);
        setContent(panel);
    }

    private void showStatistiques() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        panel.add(createPageHeader("Statistiques", "Visualisez les statistiques d'absences"),
                BorderLayout.NORTH);

        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 25, 0));
        statsPanel.setOpaque(false);

        JPanel classeCard = createModernCardPanel();
        classeCard.setLayout(new BorderLayout());

        JLabel classeTitle = Theme.createSubtitleLabel("📊 Par Classe");
        classeTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 18, 0));
        classeCard.add(classeTitle, BorderLayout.NORTH);

        String[] classeHeaders = { "Classe", "Nb Absences" };
        DefaultTableModel classeModel = new DefaultTableModel(classeHeaders, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        Vector<Object[]> statsClasse = absenceDAO.getStatistiquesParClasse();
        for (Object[] stat : statsClasse) {
            classeModel.addRow(new Object[] { stat[1], stat[2] });
        }

        JTable classeTable = new JTable(classeModel);
        Theme.styleTable(classeTable);
        classeCard.add(Theme.createScrollPane(classeTable), BorderLayout.CENTER);

        JPanel etudiantCard = createModernCardPanel();
        etudiantCard.setLayout(new BorderLayout());

        JLabel etudiantTitle = Theme.createSubtitleLabel("👤 Par Étudiant");
        etudiantTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setOpaque(false);

        JComboBox<Classe> classeCombo = Theme.createComboBox();
        classeCombo.setPreferredSize(new Dimension(200, 40));
        Vector<Classe> classes = classeDAO.getAll();
        for (Classe c : classes) {
            classeCombo.addItem(c);
        }
        filterPanel.add(Theme.createLabel("Classe:"));
        filterPanel.add(classeCombo);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(etudiantTitle, BorderLayout.NORTH);
        headerPanel.add(filterPanel, BorderLayout.SOUTH);
        etudiantCard.add(headerPanel, BorderLayout.NORTH);

        String[] etudiantHeaders = { "Étudiant", "Nb Absences" };
        DefaultTableModel etudiantModel = new DefaultTableModel(etudiantHeaders, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable etudiantTable = new JTable(etudiantModel);
        Theme.styleTable(etudiantTable);
        etudiantCard.add(Theme.createScrollPane(etudiantTable), BorderLayout.CENTER);

        classeCombo.addActionListener(e -> {
            Classe selected = (Classe) classeCombo.getSelectedItem();
            if (selected != null) {
                etudiantModel.setRowCount(0);
                Vector<Object[]> statsEtudiant = absenceDAO.getStatistiquesParEtudiant(selected.getIdClasse());
                for (Object[] stat : statsEtudiant) {
                    etudiantModel.addRow(new Object[] { stat[0], stat[1] });
                }
            }
        });

        if (classeCombo.getItemCount() > 0) {
            classeCombo.setSelectedIndex(0);
        }

        statsPanel.add(classeCard);
        statsPanel.add(etudiantCard);

        panel.add(statsPanel, BorderLayout.CENTER);
        setContent(panel);
    }

    private JTable createAbsenceTable(Vector<Absence> absences) {
        String[] headers = { "ID", "Étudiant", "Matière", "Date", "Séance", "Statut" };
        DefaultTableModel model = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Absence a : absences) {
            model.addRow(new Object[] {
                    a.getIdAbsence(),
                    a.getPrenomEtudiant() + " " + a.getNomEtudiant(),
                    a.getLibelleMatiere(),
                    sdf.format(a.getDateAbsence()),
                    a.getNumSeance(),
                    a.getStatut()
            });
        }

        JTable table = new JTable(model);
        Theme.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                String status = (String) value;
                if (!isSelected) {
                    if (status != null && status.contains("Justifiée")) {
                        setForeground(Theme.SUCCESS);
                    } else {
                        setForeground(Theme.DANGER);
                    }
                }
                setHorizontalAlignment(CENTER);

                return c;
            }
        });

        return table;
    }

    private void updateAbsenceTable(JTable table, Vector<Absence> absences) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Absence a : absences) {
            model.addRow(new Object[] {
                    a.getIdAbsence(),
                    a.getPrenomEtudiant() + " " + a.getNomEtudiant(),
                    a.getLibelleMatiere(),
                    sdf.format(a.getDateAbsence()),
                    a.getNumSeance(),
                    a.getStatut()
            });
        }
    }

    private void showProfil() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        panel.add(createPageHeader("Mon Profil", "Consultez vos informations personnelles"),
                BorderLayout.NORTH);

        JPanel card = createModernCardPanel();
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(18, 25, 18, 25);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel avatarLabel = new JLabel(getIconForRole());
        avatarLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 4;
        gbc.insets = new Insets(18, 25, 18, 50);
        card.add(avatarLabel, gbc);
        gbc.gridheight = 1;
        gbc.insets = new Insets(18, 25, 18, 25);

        String[][] infos = {
                { "Prénom", utilisateurConnecte.getPrenom() },
                { "Nom", utilisateurConnecte.getNom() },
                { "Identifiant", utilisateurConnecte.getLogin() },
                { "Rôle", getRoleDisplayName() }
        };

        for (int i = 0; i < infos.length; i++) {
            gbc.gridx = 1;
            gbc.gridy = i;
            JLabel label = Theme.createSecondaryLabel(infos[i][0]);
            card.add(label, gbc);

            gbc.gridx = 2;
            JLabel value = new JLabel(infos[i][1]);
            value.setFont(Theme.FONT_HEADING);
            value.setForeground(Theme.TEXT_PRIMARY);
            card.add(value, gbc);
        }

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapper.setOpaque(false);
        wrapper.add(card);

        panel.add(wrapper, BorderLayout.CENTER);
        setContent(panel);
    }

    private void seDeconnecter() {
        if (Theme.showConfirm(this, "Voulez-vous vraiment vous déconnecter?")) {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            dispose();
        }
    }

    private JPanel createPieChart(String title, long justifiees, long nonJustifiees) {
        JPanel panel = createModernCardPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(300, 250));

        JLabel titleLabel = Theme.createSubtitleLabel(title);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel chart = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                long total = justifiees + nonJustifiees;
                if (total == 0)
                    return;

                int width = getWidth();
                int height = getHeight();
                int diameter = Math.min(width, height) - 40;
                int x = (width - diameter) / 2;
                int y = (height - diameter) / 2;

                int angleJustif = (int) (360 * justifiees / total);

                g2d.setColor(Theme.SUCCESS);
                g2d.fillArc(x, y, diameter, diameter, 90, angleJustif);

                g2d.setColor(Theme.DANGER);
                g2d.fillArc(x, y, diameter, diameter, 90 + angleJustif, 360 - angleJustif);

                g2d.setColor(Theme.BG_CARD);
                int innerD = diameter / 2;
                g2d.fillOval(x + diameter / 4, y + diameter / 4, innerD, innerD);

                drawLegend(g2d, "Justifiées: " + justifiees, Theme.SUCCESS, 20, height - 20);
                drawLegend(g2d, "Non Justifiées: " + nonJustifiees, Theme.DANGER, width - 130, height - 20);
            }

            private void drawLegend(Graphics2D g2, String text, Color color, int x, int y) {
                g2.setColor(color);
                g2.fillOval(x, y - 10, 10, 10);
                g2.setColor(Theme.TEXT_PRIMARY);
                g2.setFont(Theme.FONT_SMALL);
                g2.drawString(text, x + 15, y);
            }
        };
        chart.setOpaque(false);
        panel.add(chart, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBarChart(String title, Map<String, Integer> data) {
        JPanel panel = createModernCardPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(400, 250));

        JLabel titleLabel = Theme.createSubtitleLabel(title);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel chart = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = getWidth();
                int height = getHeight();
                int padding = 30;
                int barWidth = 40;
                int maxVal = data.values().stream().mapToInt(Integer::intValue).max().orElse(1);

                int x = padding;
                int i = 0;
                Color[] colors = { Theme.PRIMARY, Theme.SECONDARY, Theme.ACCENT, Theme.INFO, Theme.SUCCESS };

                for (Map.Entry<String, Integer> entry : data.entrySet()) {
                    int val = entry.getValue();
                    int barHeight = (int) ((double) val / maxVal * (height - 2 * padding - 20));

                    int y = height - padding - barHeight;

                    g2d.setColor(colors[i % colors.length]);
                    g2d.fillRoundRect(x, y, barWidth, barHeight, 5, 5);

                    g2d.setColor(Theme.TEXT_PRIMARY);
                    g2d.setFont(Theme.FONT_SMALL);
                    FontMetrics fm = g2d.getFontMetrics();
                    String valStr = String.valueOf(val);
                    g2d.drawString(valStr, x + (barWidth - fm.stringWidth(valStr)) / 2, y - 5);

                    String label = entry.getKey();
                    if (label.length() > 6)
                        label = label.substring(0, 5) + ".";
                    g2d.drawString(label, x + (barWidth - fm.stringWidth(label)) / 2, height - padding + 15);

                    x += barWidth + 20;
                    i++;
                }

                g2d.setColor(Theme.BORDER);
                g2d.drawLine(padding - 5, height - padding, width - padding, height - padding);
                g2d.drawLine(padding - 5, padding, padding - 5, height - padding);
            }
        };
        chart.setOpaque(false);
        panel.add(chart, BorderLayout.CENTER);
        return panel;
    }
}

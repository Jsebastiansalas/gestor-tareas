package com.gestor.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel sidebar;

    private static final Color BG_DARK = new Color(45, 52, 54);
    private static final Color BG_SIDEBAR = new Color(34, 40, 49);
    private static final Color ACCENT = new Color(0, 122, 204);
    private static final Color BG_CONTENT = new Color(241, 242, 246);
    private static final Color TEXT_WHITE = new Color(255, 255, 255);
    private static final Color TEXT_LIGHT = new Color(189, 195, 199);

    private JButton btnPersonas, btnEquipos, btnTareas;
    private JButton activeButton;

    public MainFrame() {
        setTitle("Gestor de Tareas - Scrum");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 750);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_CONTENT);

        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(BG_SIDEBAR);
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(new EmptyBorder(20, 0, 20, 0));

        JLabel appTitle = new JLabel("  GESTOR TAREAS");
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        appTitle.setForeground(ACCENT);
        appTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        appTitle.setBorder(new EmptyBorder(0, 15, 30, 0));
        sidebar.add(appTitle);

        btnPersonas = createSidebarButton("Personas");
        btnEquipos = createSidebarButton("Equipos");
        btnTareas = createSidebarButton("Tareas");

        sidebar.add(btnPersonas);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnEquipos);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(btnTareas);
        sidebar.add(Box.createVerticalGlue());

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BG_CONTENT);

        contentPanel.add(createPlaceholderPanel("Personas Panel"), "personas");
        contentPanel.add(createPlaceholderPanel("Equipos Panel"), "equipos");
        contentPanel.add(createPlaceholderPanel("Tareas Panel"), "tareas");

        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);

        btnPersonas.addActionListener(e -> showPanel("personas", btnPersonas));
        btnEquipos.addActionListener(e -> showPanel("equipos", btnEquipos));
        btnTareas.addActionListener(e -> showPanel("tareas", btnTareas));

        showPanel("personas", btnPersonas);
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(TEXT_LIGHT);
        btn.setBackground(BG_SIDEBAR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 45));
        btn.setPreferredSize(new Dimension(220, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(10, 25, 10, 15));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                if (btn != activeButton) {
                    btn.setBackground(new Color(50, 58, 69));
                }
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                if (btn != activeButton) {
                    btn.setBackground(BG_SIDEBAR);
                }
            }
        });

        return btn;
    }

    public void showPanel(String name, JButton selectedButton) {
        cardLayout.show(contentPanel, name);
        if (activeButton != null) {
            activeButton.setBackground(BG_SIDEBAR);
            activeButton.setForeground(TEXT_LIGHT);
        }
        activeButton = selectedButton;
        activeButton.setBackground(ACCENT);
        activeButton.setForeground(TEXT_WHITE);
    }

    private JPanel createPlaceholderPanel(String text) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_CONTENT);
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        label.setForeground(Color.GRAY);
        p.add(label, BorderLayout.CENTER);
        return p;
    }

    public void setPanel(String name, JPanel panel) {
        for (Component c : contentPanel.getComponents()) {
            if (c.getName() != null && c.getName().equals(name)) {
                contentPanel.remove(c);
                break;
            }
        }
        panel.setName(name);
        contentPanel.add(panel, name);
        cardLayout.show(contentPanel, name);
    }

    public static Color getBgDark() { return BG_DARK; }
    public static Color getBgSidebar() { return BG_SIDEBAR; }
    public static Color getAccent() { return ACCENT; }
    public static Color getBgContent() { return BG_CONTENT; }
    public static Color getTextWhite() { return TEXT_WHITE; }
    public static Color getTextLight() { return TEXT_LIGHT; }
}

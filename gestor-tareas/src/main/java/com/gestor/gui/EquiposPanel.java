package com.gestor.gui;

import com.gestor.model.Person;
import com.gestor.model.Team;
import com.gestor.model.TeamPerson;
import com.gestor.service.PersonService;
import com.gestor.service.TeamPersonService;
import com.gestor.service.TeamService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel de gestion de equipos con vista master-detail.
 * Usa un JSplitPane: tabla de equipos arriba y miembros del equipo
 * seleccionado abajo. Filtra personas ya asignadas (anti-duplicado).
 */
public class EquiposPanel extends JPanel {

    private final TeamService teamService = new TeamService();
    private final TeamPersonService teamPersonService = new TeamPersonService();
    private final PersonService personService = new PersonService();

    private JTable teamTable;
    private DefaultTableModel teamTableModel;
    private JTable memberTable;
    private DefaultTableModel memberTableModel;
    private JTextField txtSearch;
    private JLabel lblStatus;
    private List<Team> allTeams = new ArrayList<>();
    // Miembros del equipo actualmente seleccionado
    private List<TeamPerson> currentMembers = new ArrayList<>();

    private static final Color BG_CONTENT = MainFrame.getBgContent();
    private static final Color ACCENT = MainFrame.getAccent();
    private static final Color BG_SIDEBAR = MainFrame.getBgSidebar();
    private static final Color TEXT_WHITE = MainFrame.getTextWhite();

    public EquiposPanel(MainFrame frame) {
        setLayout(new BorderLayout());
        setBackground(BG_CONTENT);
        initUI();
        cargarEquipos();
    }

    // Construye la interfaz: toolbar, tabla de equipos, panel de miembros y split
    private void initUI() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BG_CONTENT);
        topPanel.setBorder(new EmptyBorder(15, 20, 10, 20));

        JLabel title = new JLabel("Gestion de Equipos");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(BG_SIDEBAR);
        topPanel.add(title, BorderLayout.NORTH);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        toolbar.setBackground(BG_CONTENT);

        JButton btnNew = createToolbarButton("+ Nuevo Equipo", new Color(46, 204, 113));
        JButton btnEdit = createToolbarButton("Editar", ACCENT);
        JButton btnDelete = createToolbarButton("Eliminar", new Color(231, 76, 60));
        JButton btnRefresh = createToolbarButton("Refrescar", new Color(149, 165, 166));

        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setPreferredSize(new Dimension(220, 32));
        txtSearch.putClientProperty("JTextField.placeholderText", "Buscar por nombre...");
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent e) {
                filterTable();
            }
        });

        toolbar.add(btnNew);
        toolbar.add(btnEdit);
        toolbar.add(btnDelete);
        toolbar.add(btnRefresh);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(new JLabel("Buscar: "));
        toolbar.add(txtSearch);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(BG_CONTENT);
        topContainer.add(topPanel, BorderLayout.NORTH);
        topContainer.add(toolbar, BorderLayout.CENTER);

        String[] teamColumns = {"ID", "Nombre", "Descripcion", "# Miembros"};
        teamTableModel = new DefaultTableModel(teamColumns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        teamTable = new JTable(teamTableModel);
        teamTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        teamTable.setRowHeight(30);
        teamTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        teamTable.setGridColor(new Color(220, 220, 220));
        teamTable.setShowGrid(true);
        teamTable.setIntercellSpacing(new Dimension(0, 1));
        teamTable.setSelectionBackground(ACCENT);
        teamTable.setSelectionForeground(TEXT_WHITE);
        teamTable.setFocusable(false);

        JTableHeader teamHeader = teamTable.getTableHeader();
        teamHeader.setFont(new Font("Segoe UI", Font.BOLD, 13));
        teamHeader.setBackground(new Color(230, 230, 230));
        teamHeader.setForeground(BG_SIDEBAR);
        teamHeader.setPreferredSize(new Dimension(teamHeader.getWidth(), 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        teamTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        teamTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        teamTable.getColumnModel().getColumn(0).setMaxWidth(60);

        teamTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        teamTable.getColumnModel().getColumn(2).setPreferredWidth(250);
        teamTable.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
        teamTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        teamTable.getColumnModel().getColumn(3).setMaxWidth(100);

        // Clic simple carga miembros; doble clic abre edicion del equipo
        teamTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editSelectedTeam();
                } else {
                    loadTeamMembers();
                }
            }
        });

        // Al cambiar la seleccion del equipo se recargan sus miembros
        teamTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadTeamMembers();
            }
        });

        JScrollPane teamScroll = new JScrollPane(teamTable);
        teamScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        teamScroll.getViewport().setBackground(Color.WHITE);

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setBackground(BG_CONTENT);
        topSection.add(topContainer, BorderLayout.NORTH);
        topSection.add(teamScroll, BorderLayout.CENTER);

        // Seccion inferior (detalle): miembros del equipo seleccionado
        JPanel memberPanel = new JPanel(new BorderLayout());
        memberPanel.setBackground(BG_CONTENT);
        memberPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ACCENT, 1, true),
                " Miembros del Equipo ",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 13), ACCENT));

        JPanel memberToolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        memberToolbar.setBackground(BG_CONTENT);

        JButton btnAddMember = createToolbarButton("+ Agregar Miembro", new Color(46, 204, 113));
        JButton btnRemoveMember = createToolbarButton("Remover Miembro", new Color(231, 76, 60));
        btnAddMember.setPreferredSize(new Dimension(160, 32));
        btnRemoveMember.setPreferredSize(new Dimension(160, 32));

        memberToolbar.add(btnAddMember);
        memberToolbar.add(btnRemoveMember);

        String[] memberColumns = {"ID", "Nombre Completo", "Email", "Rol"};
        memberTableModel = new DefaultTableModel(memberColumns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        memberTable = new JTable(memberTableModel);
        memberTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        memberTable.setRowHeight(30);
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        memberTable.setGridColor(new Color(220, 220, 220));
        memberTable.setShowGrid(true);
        memberTable.setIntercellSpacing(new Dimension(0, 1));
        memberTable.setSelectionBackground(ACCENT);
        memberTable.setSelectionForeground(TEXT_WHITE);
        memberTable.setFocusable(false);

        JTableHeader memberHeader = memberTable.getTableHeader();
        memberHeader.setFont(new Font("Segoe UI", Font.BOLD, 13));
        memberHeader.setBackground(new Color(230, 230, 230));
        memberHeader.setForeground(BG_SIDEBAR);
        memberHeader.setPreferredSize(new Dimension(memberHeader.getWidth(), 35));

        memberTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        memberTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        memberTable.getColumnModel().getColumn(0).setMaxWidth(60);

        memberTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        memberTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        memberTable.getColumnModel().getColumn(3).setPreferredWidth(120);

        memberTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showMemberDetails();
                }
            }
        });

        JScrollPane memberScroll = new JScrollPane(memberTable);
        memberScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        memberScroll.getViewport().setBackground(Color.WHITE);

        memberPanel.add(memberToolbar, BorderLayout.NORTH);
        memberPanel.add(memberScroll, BorderLayout.CENTER);

        // Split vertical: equipos arriba, miembros abajo
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topSection, memberPanel);
        splitPane.setDividerLocation(350);
        splitPane.setDividerSize(6);
        splitPane.setContinuousLayout(true);
        splitPane.setBorder(null);
        add(splitPane, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(new Color(230, 230, 230));
        statusPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        lblStatus = new JLabel("Listo");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(Color.GRAY);
        statusPanel.add(lblStatus);
        add(statusPanel, BorderLayout.SOUTH);

        btnNew.addActionListener(e -> showTeamDialog(null));
        btnEdit.addActionListener(e -> editSelectedTeam());
        btnDelete.addActionListener(e -> deleteSelectedTeam());
        btnRefresh.addActionListener(e -> cargarEquipos());
        btnAddMember.addActionListener(e -> showAddMemberDialog());
        btnRemoveMember.addActionListener(e -> removeSelectedMember());
    }

    // Crea un boton de toolbar con color propio y efecto hover
    private JButton createToolbarButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(TEXT_WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 32));
        btn.setBorder(new EmptyBorder(5, 12, 5, 12));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(bgColor);
            }
        });

        return btn;
    }

    // Carga los equipos desde la BD y limpia la tabla de miembros
    public void cargarEquipos() {
        try {
            allTeams = teamService.listarTodos();
            populateTeamTable(allTeams);
            lblStatus.setText("Equipos cargados: " + allTeams.size());
            currentMembers.clear();
            if (memberTableModel != null) {
                memberTableModel.setRowCount(0);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar equipos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            lblStatus.setText("Error al cargar equipos");
        }
    }

    // Llena la tabla de equipos incluyendo el conteo de miembros
    private void populateTeamTable(List<Team> teams) {
        teamTableModel.setRowCount(0);
        for (Team t : teams) {
            int memberCount = 0;
            try {
                memberCount = teamPersonService.obtenerMiembrosEquipo(t.getIdTeam()).size();
            } catch (Exception ignored) {
            }
            teamTableModel.addRow(new Object[]{
                    t.getIdTeam(),
                    t.getTeamName(),
                    t.getDescription(),
                    memberCount
            });
        }
    }

    // Filtra equipos en memoria por nombre o descripcion
    private void filterTable() {
        String query = txtSearch.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            populateTeamTable(allTeams);
            return;
        }
        List<Team> filtered = new ArrayList<>();
        for (Team t : allTeams) {
            if ((t.getTeamName() != null && t.getTeamName().toLowerCase().contains(query))
                    || (t.getDescription() != null && t.getDescription().toLowerCase().contains(query))) {
                filtered.add(t);
            }
        }
        populateTeamTable(filtered);
        lblStatus.setText("Resultados: " + filtered.size());
    }

    // Obtiene el equipo seleccionado en la tabla (o null si no hay)
    private Team getSelectedTeam() {
        int row = teamTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un equipo de la tabla",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        int id = (int) teamTableModel.getValueAt(row, 0);
        for (Team t : allTeams) {
            if (t.getIdTeam() == id) {
                return t;
            }
        }
        return null;
    }

    // Carga los miembros del equipo seleccionado en la tabla inferior
    private void loadTeamMembers() {
        Team team = getSelectedTeam();
        if (team == null) {
            memberTableModel.setRowCount(0);
            return;
        }
        try {
            currentMembers = teamPersonService.listarPorEquipo(team.getIdTeam());
            memberTableModel.setRowCount(0);
            for (TeamPerson tp : currentMembers) {
                Person p = tp.getPerson();
                if (p != null) {
                    String typeName = p.getTypePerson() != null ? p.getTypePerson().getTypeName() : "";
                    memberTableModel.addRow(new Object[]{
                            p.getIdPerson(),
                            p.getFirstName() + " " + p.getLastName(),
                            p.getEmail(),
                            typeName
                    });
                }
            }
            lblStatus.setText("Equipo: " + team.getTeamName() + " | Miembros: " + currentMembers.size());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar miembros: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editSelectedTeam() {
        Team team = getSelectedTeam();
        if (team != null) {
            showTeamDialog(team);
        }
    }

    private void deleteSelectedTeam() {
        Team team = getSelectedTeam();
        if (team == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Desea eliminar el equipo \"" + team.getTeamName() + "\"?\nSe eliminaran todas sus asignaciones.",
                "Confirmar eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            List<TeamPerson> members = teamPersonService.listarPorEquipo(team.getIdTeam());
            for (TeamPerson tp : members) {
                teamPersonService.remover(team.getIdTeam(), tp.getPerson().getIdPerson());
            }
            teamService.eliminar(team.getIdTeam());
            lblStatus.setText("Equipo eliminado: " + team.getTeamName());
            cargarEquipos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al eliminar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showTeamDialog(Team team) {
        boolean editing = team != null;
        String title = editing ? "Editar Equipo" : "Nuevo Equipo";

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(15, 15, 15, 15));
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField txtTeamName = new JTextField(editing ? team.getTeamName() : "", 25);
        JTextArea txtDescription = new JTextArea(editing ? team.getDescription() : "", 4, 25);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(txtDescription);
        descScroll.setPreferredSize(new Dimension(300, 80));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lbl1 = new JLabel("Nombre:");
        lbl1.setFont(labelFont);
        form.add(lbl1, gbc);
        gbc.gridx = 1;
        txtTeamName.setFont(fieldFont);
        form.add(txtTeamName, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel lbl2 = new JLabel("Descripcion:");
        lbl2.setFont(labelFont);
        form.add(lbl2, gbc);
        gbc.gridx = 1;
        form.add(descScroll, gbc);

        int result = JOptionPane.showConfirmDialog(
                this, form, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        try {
            if (editing) {
                team.setTeamName(txtTeamName.getText().trim());
                team.setDescription(txtDescription.getText().trim());
                teamService.actualizar(team);
                lblStatus.setText("Equipo actualizado: " + team.getTeamName());
            } else {
                Team newTeam = new Team();
                newTeam.setTeamName(txtTeamName.getText().trim());
                newTeam.setDescription(txtDescription.getText().trim());
                teamService.guardar(newTeam);
                lblStatus.setText("Equipo creado: " + newTeam.getTeamName());
            }
            cargarEquipos();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Validacion", JOptionPane.WARNING_MESSAGE);
            lblStatus.setText("Operacion cancelada");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            lblStatus.setText("Error al guardar");
        }
    }

    private void showAddMemberDialog() {
        Team team = getSelectedTeam();
        if (team == null) return;

        try {
            List<Person> allPersons = personService.listarTodos();
            List<Person> members = teamPersonService.obtenerMiembrosEquipo(team.getIdTeam());

            List<Person> available = new ArrayList<>();
            for (Person p : allPersons) {
                boolean isMember = false;
                for (Person m : members) {
                    if (m.getIdPerson() == p.getIdPerson()) {
                        isMember = true;
                        break;
                    }
                }
                if (!isMember) {
                    available.add(p);
                }
            }

            if (available.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay personas disponibles para agregar al equipo.",
                        "Aviso", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JComboBox<Person> cmbPersons = new JComboBox<>();
            for (Person p : available) {
                cmbPersons.addItem(p);
            }

            JPanel panel = new JPanel(new BorderLayout(0, 10));
            panel.setBorder(new EmptyBorder(10, 10, 10, 10));
            panel.setBackground(Color.WHITE);
            panel.add(new JLabel("Seleccione una persona:"), BorderLayout.NORTH);
            panel.add(cmbPersons, BorderLayout.CENTER);

            int result = JOptionPane.showConfirmDialog(
                    this, panel, "Agregar Miembro a \"" + team.getTeamName() + "\"",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result != JOptionPane.OK_OPTION) return;

            Person selected = (Person) cmbPersons.getSelectedItem();
            if (selected == null) return;

            TeamPerson tp = new TeamPerson();
            tp.setTeam(team);
            tp.setPerson(selected);
            tp.setJoinedAt(LocalDateTime.now());
            teamPersonService.asignar(tp);
            lblStatus.setText("Miembro agregado: " + selected.getFirstName() + " " + selected.getLastName());
            loadTeamMembers();
            cargarEquipos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al agregar miembro: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeSelectedMember() {
        Team team = getSelectedTeam();
        if (team == null) return;

        int row = memberTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un miembro de la tabla",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int personId = (int) memberTableModel.getValueAt(row, 0);
        String personName = (String) memberTableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Desea remover a \"" + personName + "\" del equipo \"" + team.getTeamName() + "\"?",
                "Confirmar remocion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            teamPersonService.remover(team.getIdTeam(), personId);
            lblStatus.setText("Miembro removido: " + personName);
            loadTeamMembers();
            cargarEquipos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al remover miembro: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showMemberDetails() {
        int row = memberTable.getSelectedRow();
        if (row < 0) return;

        int personId = (int) memberTableModel.getValueAt(row, 0);
        String name = (String) memberTableModel.getValueAt(row, 1);
        String email = (String) memberTableModel.getValueAt(row, 2);
        String rol = (String) memberTableModel.getValueAt(row, 3);

        String details = "ID: " + personId
                + "\nNombre: " + name
                + "\nEmail: " + email
                + "\nRol: " + (rol != null ? rol : "");

        JOptionPane.showMessageDialog(this,
                details, "Detalles del Miembro", JOptionPane.INFORMATION_MESSAGE);
    }
}

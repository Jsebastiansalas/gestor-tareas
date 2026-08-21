package com.gestor.gui;

import com.gestor.model.*;
import com.gestor.service.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TareasPanel extends JPanel {

    private final TaskService taskService = new TaskService();
    private final StatusTaskService statusTaskService = new StatusTaskService();
    private final TeamService teamService = new TeamService();
    private final PersonService personService = new PersonService();
    private final AssementTaskService assementTaskService = new AssementTaskService();
    private final TeamPersonService teamPersonService = new TeamPersonService();

    private List<Task> allTasks = new ArrayList<>();
    private List<StatusTask> allStatuses = new ArrayList<>();
    private List<Team> allTeams = new ArrayList<>();
    private List<Person> allPersons = new ArrayList<>();

    private JComboBox<Team> cmbFilterTeam;
    private JComboBox<StatusTask> cmbFilterStatus;
    private JComboBox<Person> cmbFilterPerson;

    private JPanel kanbanBoard;
    private JPanel columnTodo;
    private JPanel columnInProgress;
    private JPanel columnDone;

    private JPanel scrollTodo;
    private JPanel scrollInProgress;
    private JPanel scrollDone;

    private JLabel lblStatus;

    private Task selectedTask;

    private static final Color BG_CONTENT = MainFrame.getBgContent();
    private static final Color ACCENT = MainFrame.getAccent();
    private static final Color BG_SIDEBAR = MainFrame.getBgSidebar();
    private static final Color TEXT_WHITE = MainFrame.getTextWhite();
    private static final Color TEXT_LIGHT = MainFrame.getTextLight();

    private static final Color COLOR_TODO_BG = new Color(255, 249, 230);
    private static final Color COLOR_TODO_HEADER = new Color(241, 196, 15);
    private static final Color COLOR_PROGRESS_BG = new Color(232, 245, 253);
    private static final Color COLOR_PROGRESS_HEADER = new Color(52, 152, 219);
    private static final Color COLOR_DONE_BG = new Color(232, 249, 232);
    private static final Color COLOR_DONE_HEADER = new Color(46, 204, 113);
    private static final Color COLOR_CARD = Color.WHITE;
    private static final Color COLOR_CARD_SELECTED = new Color(0, 122, 204, 40);
    private static final Color COLOR_CARD_BORDER = new Color(220, 220, 220);

    public TareasPanel(MainFrame frame) {
        setLayout(new BorderLayout());
        setBackground(BG_CONTENT);
        initUI();
        cargarTareas();
    }

    private void initUI() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BG_CONTENT);
        topPanel.setBorder(new EmptyBorder(15, 20, 5, 20));

        JLabel title = new JLabel("Gestion de Tareas");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(BG_SIDEBAR);
        topPanel.add(title, BorderLayout.NORTH);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        toolbar.setBackground(BG_CONTENT);

        JButton btnNew = createToolbarButton("+ Nueva Tarea", new Color(46, 204, 113));
        JButton btnEdit = createToolbarButton("Editar", ACCENT);
        JButton btnDelete = createToolbarButton("Eliminar", new Color(231, 76, 60));
        JButton btnRefresh = createToolbarButton("Refrescar", new Color(149, 165, 166));

        toolbar.add(btnNew);
        toolbar.add(btnEdit);
        toolbar.add(btnDelete);
        toolbar.add(btnRefresh);
        toolbar.add(Box.createHorizontalStrut(20));

        cmbFilterTeam = new JComboBox<>();
        cmbFilterStatus = new JComboBox<>();
        cmbFilterPerson = new JComboBox<>();
        cmbFilterTeam.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbFilterStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbFilterPerson.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cmbFilterTeam.setPreferredSize(new Dimension(150, 30));
        cmbFilterStatus.setPreferredSize(new Dimension(150, 30));
        cmbFilterPerson.setPreferredSize(new Dimension(150, 30));

        toolbar.add(new JLabel("Equipo: "));
        toolbar.add(cmbFilterTeam);
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(new JLabel("Estado: "));
        toolbar.add(cmbFilterStatus);
        toolbar.add(Box.createHorizontalStrut(10));
        toolbar.add(new JLabel("Persona: "));
        toolbar.add(cmbFilterPerson);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(BG_CONTENT);
        topContainer.add(topPanel, BorderLayout.NORTH);
        topContainer.add(toolbar, BorderLayout.CENTER);

        add(topContainer, BorderLayout.NORTH);

        kanbanBoard = new JPanel(new GridLayout(1, 3, 10, 0));
        kanbanBoard.setBackground(BG_CONTENT);
        kanbanBoard.setBorder(new EmptyBorder(10, 20, 10, 20));

        columnTodo = createColumn("To Do", COLOR_TODO_HEADER, COLOR_TODO_BG);
        columnInProgress = createColumn("In Progress", COLOR_PROGRESS_HEADER, COLOR_PROGRESS_BG);
        columnDone = createColumn("Done", COLOR_DONE_HEADER, COLOR_DONE_BG);

        kanbanBoard.add(columnTodo);
        kanbanBoard.add(columnInProgress);
        kanbanBoard.add(columnDone);

        add(kanbanBoard, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(new Color(230, 230, 230));
        statusPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        lblStatus = new JLabel("Listo");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(Color.GRAY);
        statusPanel.add(lblStatus);
        add(statusPanel, BorderLayout.SOUTH);

        btnNew.addActionListener(e -> showTaskDialog(null));
        btnEdit.addActionListener(e -> editSelectedTask());
        btnDelete.addActionListener(e -> deleteSelectedTask());
        btnRefresh.addActionListener(e -> cargarTareas());

        cmbFilterTeam.addActionListener(e -> cargarTareas());
        cmbFilterStatus.addActionListener(e -> cargarTareas());
        cmbFilterPerson.addActionListener(e -> cargarTareas());
    }

    private JPanel createColumn(String headerText, Color headerColor, Color bgColor) {
        JPanel column = new JPanel(new BorderLayout());
        column.setBackground(bgColor);
        column.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        JLabel header = new JLabel(headerText, SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setForeground(TEXT_WHITE);
        header.setBackground(headerColor);
        header.setOpaque(true);
        header.setPreferredSize(new Dimension(0, 36));
        column.add(header, BorderLayout.NORTH);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(bgColor);
        cardPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JScrollPane scroll = new JScrollPane(cardPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(bgColor);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        column.add(scroll, BorderLayout.CENTER);

        if (headerText.equals("To Do")) scrollTodo = cardPanel;
        else if (headerText.equals("In Progress")) scrollInProgress = cardPanel;
        else scrollDone = cardPanel;

        return column;
    }

    private JPanel createTaskCard(Task task) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(COLOR_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_CARD_BORDER),
                new EmptyBorder(10, 12, 10, 12)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        card.setPreferredSize(new Dimension(0, 110));
        card.setMinimumSize(new Dimension(0, 100));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setName("card_" + task.getIdTask());

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        JLabel lblTitle = new JLabel(task.getTitle());
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(BG_SIDEBAR);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(lblTitle);

        infoPanel.add(Box.createVerticalStrut(4));

        String teamName = task.getTeam() != null ? task.getTeam().getTeamName() : "Sin equipo";
        JLabel lblTeam = new JLabel(teamName);
        lblTeam.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblTeam.setForeground(new Color(120, 120, 120));
        lblTeam.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPanel.add(lblTeam);

        infoPanel.add(Box.createVerticalStrut(4));

        String assignedNames = getAssignedNames(task.getIdTask());
        if (!assignedNames.isEmpty()) {
            JLabel lblAssigned = new JLabel(assignedNames);
            lblAssigned.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            lblAssigned.setForeground(new Color(100, 100, 100));
            lblAssigned.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPanel.add(lblAssigned);
        }

        card.add(infoPanel, BorderLayout.CENTER);

        if (task.getStatusTask() == null || task.getStatusTask().getStatusOrder() < 3) {
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            btnPanel.setOpaque(false);
            JButton btnMove = new JButton("Move Right \u2192");
            btnMove.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            btnMove.setForeground(ACCENT);
            btnMove.setBorderPainted(false);
            btnMove.setContentAreaFilled(false);
            btnMove.setFocusPainted(false);
            btnMove.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnMove.addActionListener(e -> moveTaskRight(task));
            btnPanel.add(btnMove);
            card.add(btnPanel, BorderLayout.SOUTH);
        }

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                selectCard(card, task);
            }
        });

        return card;
    }

    private String getAssignedNames(int idTask) {
        try {
            List<AssementTask> assignments = assementTaskService.listarPorTarea(idTask);
            List<String> names = new ArrayList<>();
            for (AssementTask at : assignments) {
                if (at.getPerson() != null) {
                    names.add(at.getPerson().getFirstName() + " " + at.getPerson().getLastName());
                }
            }
            return String.join(", ", names);
        } catch (Exception e) {
            return "";
        }
    }

    private void selectCard(JPanel card, Task task) {
        selectedTask = task;
        clearCardSelection(scrollTodo);
        clearCardSelection(scrollInProgress);
        clearCardSelection(scrollDone);
        card.setBackground(new Color(200, 225, 255));
        for (Component c : card.getComponents()) {
            if (c instanceof JPanel) {
                c.setBackground(new Color(200, 225, 255));
            }
        }
    }

    private void clearCardSelection(JPanel column) {
        for (Component c : column.getComponents()) {
            if (c instanceof JPanel) {
                JPanel p = (JPanel) c;
                p.setBackground(COLOR_CARD);
                for (Component inner : p.getComponents()) {
                    if (inner instanceof JPanel) {
                        inner.setBackground(COLOR_CARD);
                    }
                }
            }
        }
    }

    private void moveTaskRight(Task task) {
        if (task.getStatusTask() == null) return;
        int currentOrder = task.getStatusTask().getStatusOrder();
        StatusTask nextStatus = findStatusByOrder(currentOrder + 1);
        if (nextStatus == null) return;

        try {
            taskService.cambiarEstado(task.getIdTask(), nextStatus.getIdStatusTask());
            cargarTareas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cambiar estado: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private StatusTask findStatusByOrder(int order) {
        for (StatusTask st : allStatuses) {
            if (st.getStatusOrder() == order) return st;
        }
        return null;
    }

    public void cargarTareas() {
        try {
            allStatuses = statusTaskService.listarOrdernados();
            allTeams = teamService.listarTodos();
            allPersons = personService.listarTodos();

            populateFilterCombos();

            Team filterTeam = (Team) cmbFilterTeam.getSelectedItem();
            StatusTask filterStatus = (StatusTask) cmbFilterStatus.getSelectedItem();
            Person filterPerson = (Person) cmbFilterPerson.getSelectedItem();

            if (filterTeam != null && filterStatus != null) {
                allTasks = taskService.listarPorEquipoYEstado(filterTeam.getIdTeam(), filterStatus.getIdStatusTask());
            } else if (filterTeam != null) {
                allTasks = taskService.listarPorEquipo(filterTeam.getIdTeam());
            } else if (filterStatus != null) {
                allTasks = taskService.listarPorEstado(filterStatus.getIdStatusTask());
            } else if (filterPerson != null) {
                allTasks = taskService.listarPorPersona(filterPerson.getIdPerson());
            } else {
                allTasks = taskService.listarTodos();
            }

            renderKanban();
            updateStatusBar();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar tareas: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateFilterCombos() {
        cmbFilterTeam.removeActionListener(cmbFilterTeam.getActionListeners().length > 0 ? cmbFilterTeam.getActionListeners()[0] : null);
        cmbFilterStatus.removeActionListener(cmbFilterStatus.getActionListeners().length > 0 ? cmbFilterStatus.getActionListeners()[0] : null);
        cmbFilterPerson.removeActionListener(cmbFilterPerson.getActionListeners().length > 0 ? cmbFilterPerson.getActionListeners()[0] : null);

        Object prevTeam = cmbFilterTeam.getSelectedItem();
        Object prevStatus = cmbFilterStatus.getSelectedItem();
        Object prevPerson = cmbFilterPerson.getSelectedItem();

        cmbFilterTeam.removeAllItems();
        cmbFilterTeam.addItem(null);
        for (Team t : allTeams) cmbFilterTeam.addItem(t);

        cmbFilterStatus.removeAllItems();
        cmbFilterStatus.addItem(null);
        for (StatusTask s : allStatuses) cmbFilterStatus.addItem(s);

        cmbFilterPerson.removeAllItems();
        cmbFilterPerson.addItem(null);
        for (Person p : allPersons) cmbFilterPerson.addItem(p);

        restoreComboSelection(cmbFilterTeam, prevTeam);
        restoreComboSelection(cmbFilterStatus, prevStatus);
        restoreComboSelection(cmbFilterPerson, prevPerson);

        cmbFilterTeam.addActionListener(e -> cargarTareas());
        cmbFilterStatus.addActionListener(e -> cargarTareas());
        cmbFilterPerson.addActionListener(e -> cargarTareas());
    }

    private void restoreComboSelection(JComboBox combo, Object prev) {
        if (prev == null) {
            combo.setSelectedIndex(0);
            return;
        }
        for (int i = 0; i < combo.getItemCount(); i++) {
            Object item = combo.getItemAt(i);
            if (item == null && prev == null) { combo.setSelectedIndex(i); return; }
            if (item != null && item.equals(prev)) { combo.setSelectedIndex(i); return; }
        }
        combo.setSelectedIndex(0);
    }

    private void renderKanban() {
        scrollTodo.removeAll();
        scrollInProgress.removeAll();
        scrollDone.removeAll();

        for (Task task : allTasks) {
            if (task.getStatusTask() == null) continue;
            int order = task.getStatusTask().getStatusOrder();
            JPanel card = createTaskCard(task);
            if (order == 1) {
                scrollTodo.add(card);
                scrollTodo.add(Box.createVerticalStrut(5));
            } else if (order == 2) {
                scrollInProgress.add(card);
                scrollInProgress.add(Box.createVerticalStrut(5));
            } else {
                scrollDone.add(card);
                scrollDone.add(Box.createVerticalStrut(5));
            }
        }

        scrollTodo.revalidate();
        scrollTodo.repaint();
        scrollInProgress.revalidate();
        scrollInProgress.repaint();
        scrollDone.revalidate();
        scrollDone.repaint();
    }

    private void updateStatusBar() {
        int todoCount = 0, progressCount = 0, doneCount = 0;
        for (Task t : allTasks) {
            if (t.getStatusTask() == null) continue;
            int order = t.getStatusTask().getStatusOrder();
            if (order == 1) todoCount++;
            else if (order == 2) progressCount++;
            else doneCount++;
        }
        lblStatus.setText("Total: " + allTasks.size() +
                "  |  To Do: " + todoCount +
                "  |  In Progress: " + progressCount +
                "  |  Done: " + doneCount);
    }

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

    private void editSelectedTask() {
        if (selectedTask == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una tarea para editar",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        showTaskDialog(selectedTask);
    }

    private void deleteSelectedTask() {
        if (selectedTask == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una tarea para eliminar",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Desea eliminar la tarea \"" + selectedTask.getTitle() + "\"?",
                "Confirmar eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            taskService.eliminar(selectedTask.getIdTask());
            selectedTask = null;
            lblStatus.setText("Tarea eliminada correctamente");
            cargarTareas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al eliminar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showTaskDialog(Task task) {
        boolean editing = task != null;
        String dialogTitle = editing ? "Editar Tarea" : "Nueva Tarea";

        if (allTeams.isEmpty() || allStatuses.isEmpty() || allPersons.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Debe haber al menos un equipo, un estado y una persona creados.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(15, 15, 15, 15));
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField txtTitle = new JTextField(editing ? task.getTitle() : "", 25);
        JTextArea txtDescription = new JTextArea(editing ? (task.getDescription() != null ? task.getDescription() : "") : "", 4, 25);
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        JScrollPane descScroll = new JScrollPane(txtDescription);

        JComboBox<Team> cmbTeam = new JComboBox<>();
        for (Team t : allTeams) cmbTeam.addItem(t);
        if (editing && task.getTeam() != null) {
            selectComboItem(cmbTeam, task.getTeam().getIdTeam());
        }

        JComboBox<StatusTask> cmbStatus = new JComboBox<>();
        for (StatusTask s : allStatuses) cmbStatus.addItem(s);
        if (editing && task.getStatusTask() != null) {
            selectComboItem(cmbStatus, task.getStatusTask().getIdStatusTask());
        }

        JComboBox<Person> cmbCreatedBy = new JComboBox<>();
        for (Person p : allPersons) cmbCreatedBy.addItem(p);
        if (editing && task.getCreatedBy() != null) {
            selectComboItem(cmbCreatedBy, task.getCreatedBy().getIdPerson());
        }

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lbl1 = new JLabel("Titulo:");
        lbl1.setFont(labelFont);
        form.add(lbl1, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        txtTitle.setFont(fieldFont);
        form.add(txtTitle, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        JLabel lbl2 = new JLabel("Descripcion:");
        lbl2.setFont(labelFont);
        form.add(lbl2, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.BOTH;
        form.add(descScroll, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        JLabel lbl3 = new JLabel("Equipo:");
        lbl3.setFont(labelFont);
        form.add(lbl3, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        cmbTeam.setFont(fieldFont);
        form.add(cmbTeam, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        JLabel lbl4 = new JLabel("Estado:");
        lbl4.setFont(labelFont);
        form.add(lbl4, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        cmbStatus.setFont(fieldFont);
        form.add(cmbStatus, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        JLabel lbl5 = new JLabel("Creado por:");
        lbl5.setFont(labelFont);
        form.add(lbl5, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        cmbCreatedBy.setFont(fieldFont);
        form.add(cmbCreatedBy, gbc);

        JPanel assignmentPanel = null;
        DefaultTableModel assignmentModel = null;
        JTable assignmentTable = null;

        if (editing) {
            assignmentModel = new DefaultTableModel(
                    new Object[]{"ID", "Persona", "Rol", "Asignado"}, 0) {
                public boolean isCellEditable(int row, int column) { return false; }
            };
            assignmentTable = new JTable(assignmentModel);
            assignmentTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            assignmentTable.setRowHeight(25);
            assignmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JTableHeader aHeader = assignmentTable.getTableHeader();
            aHeader.setFont(new Font("Segoe UI", Font.BOLD, 12));
            aHeader.setBackground(new Color(230, 230, 230));
            aHeader.setPreferredSize(new Dimension(aHeader.getWidth(), 30));

            assignmentTable.getColumnModel().getColumn(0).setPreferredWidth(30);
            assignmentTable.getColumnModel().getColumn(0).setMaxWidth(50);
            assignmentTable.getColumnModel().getColumn(1).setPreferredWidth(150);
            assignmentTable.getColumnModel().getColumn(2).setPreferredWidth(100);
            assignmentTable.getColumnModel().getColumn(3).setPreferredWidth(130);

            JScrollPane aScroll = new JScrollPane(assignmentTable);
            aScroll.setPreferredSize(new Dimension(400, 120));

            JButton btnAddAssignment = createToolbarButton("+ Asignar Persona", new Color(46, 204, 113));
            btnAddAssignment.setPreferredSize(new Dimension(160, 28));
            JButton btnRemoveAssignment = createToolbarButton("Quitar", new Color(231, 76, 60));
            btnRemoveAssignment.setPreferredSize(new Dimension(100, 28));

            JPanel aButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
            aButtons.setOpaque(false);
            aButtons.add(btnAddAssignment);
            aButtons.add(btnRemoveAssignment);

            assignmentPanel = new JPanel(new BorderLayout());
            assignmentPanel.setOpaque(false);
            assignmentPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

            JLabel lblAssignments = new JLabel("Personas Asignadas:");
            lblAssignments.setFont(labelFont);
            assignmentPanel.add(lblAssignments, BorderLayout.NORTH);
            assignmentPanel.add(aScroll, BorderLayout.CENTER);
            assignmentPanel.add(aButtons, BorderLayout.SOUTH);

            gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH;
            form.add(assignmentPanel, gbc);

            loadAssignmentsIntoTable(assignmentModel, task.getIdTask());

            final DefaultTableModel finalModel = assignmentModel;
            final JTable finalTable = assignmentTable;
            btnAddAssignment.addActionListener(e -> showAddAssignmentDialog(task, finalModel));
            btnRemoveAssignment.addActionListener(e -> {
                int selRow = finalTable.getSelectedRow();
                if (selRow < 0) {
                    JOptionPane.showMessageDialog(this,
                            "Seleccione una asignacion para quitar",
                            "Aviso", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                int idAssement = (int) finalModel.getValueAt(selRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Desea quitar esta asignacion?",
                        "Confirmar", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;
                try {
                    assementTaskService.remover(idAssement);
                    loadAssignmentsIntoTable(finalModel, task.getIdTask());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error al quitar asignacion: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }

        int result = JOptionPane.showConfirmDialog(
                this, form, dialogTitle,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        try {
            Team selectedTeam = (Team) cmbTeam.getSelectedItem();
            StatusTask selectedStatus = (StatusTask) cmbStatus.getSelectedItem();
            Person selectedPerson = (Person) cmbCreatedBy.getSelectedItem();

            if (editing) {
                task.setTitle(txtTitle.getText().trim());
                task.setDescription(txtDescription.getText().trim());
                task.setTeam(selectedTeam);
                task.setStatusTask(selectedStatus);
                task.setCreatedBy(selectedPerson);
                taskService.actualizar(task);
                lblStatus.setText("Tarea actualizada: " + task.getTitle());
            } else {
                Task newTask = new Task();
                newTask.setTitle(txtTitle.getText().trim());
                newTask.setDescription(txtDescription.getText().trim());
                newTask.setTeam(selectedTeam);
                newTask.setStatusTask(selectedStatus);
                newTask.setCreatedBy(selectedPerson);
                taskService.guardar(newTask);
                lblStatus.setText("Tarea creada: " + newTask.getTitle());
            }
            selectedTask = null;
            cargarTareas();
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

    private void loadAssignmentsIntoTable(DefaultTableModel model, int idTask) {
        model.setRowCount(0);
        try {
            List<AssementTask> assignments = assementTaskService.listarPorTarea(idTask);
            for (AssementTask at : assignments) {
                String personName = "";
                if (at.getPerson() != null) {
                    personName = at.getPerson().getFirstName() + " " + at.getPerson().getLastName();
                }
                model.addRow(new Object[]{
                        at.getIdAssementTask(),
                        personName,
                        at.getRoleInTask() != null ? at.getRoleInTask() : "",
                        at.getAssignedAt() != null ? at.getAssignedAt().toString().replace("T", " ") : ""
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar asignaciones: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showAddAssignmentDialog(Task task, DefaultTableModel assignmentModel) {
        if (task.getTeam() == null || task.getTeam().getIdTeam() <= 0) {
            JOptionPane.showMessageDialog(this,
                    "La tarea debe tener un equipo asignado primero",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Person> teamMembers;
        try {
            teamMembers = teamPersonService.obtenerMiembrosEquipo(task.getTeam().getIdTeam());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al obtener miembros del equipo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (teamMembers.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay miembros en este equipo. Asigne personas al equipo primero.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        JComboBox<Person> cmbPerson = new JComboBox<>();
        for (Person p : teamMembers) cmbPerson.addItem(p);
        cmbPerson.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JTextField txtRole = new JTextField("", 20);
        txtRole.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lblPerson = new JLabel("Persona:");
        lblPerson.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(lblPerson, gbc);
        gbc.gridx = 1;
        panel.add(cmbPerson, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lblRole = new JLabel("Rol en tarea:");
        lblRole.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(lblRole, gbc);
        gbc.gridx = 1;
        panel.add(txtRole, gbc);

        int result = JOptionPane.showConfirmDialog(
                this, panel, "Asignar Persona",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        try {
            Person selectedPerson = (Person) cmbPerson.getSelectedItem();
            String role = txtRole.getText().trim();

            AssementTask at = new AssementTask();
            at.setTask(task);
            at.setPerson(selectedPerson);
            at.setRoleInTask(role);

            assementTaskService.asignar(at);
            loadAssignmentsIntoTable(assignmentModel, task.getIdTask());
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Validacion", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al asignar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void selectComboItem(JComboBox combo, int id) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            Object item = combo.getItemAt(i);
            if (item == null) continue;
            if (item instanceof Team && ((Team) item).getIdTeam() == id) {
                combo.setSelectedIndex(i);
                return;
            }
            if (item instanceof StatusTask && ((StatusTask) item).getIdStatusTask() == id) {
                combo.setSelectedIndex(i);
                return;
            }
            if (item instanceof Person && ((Person) item).getIdPerson() == id) {
                combo.setSelectedIndex(i);
                return;
            }
        }
    }
}

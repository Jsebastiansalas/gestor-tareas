package com.gestor.gui;

import com.gestor.model.Person;
import com.gestor.model.TypePerson;
import com.gestor.service.PersonService;
import com.gestor.service.TypePersonService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel de gestion de personas (CRUD completo).
 * Incluye tabla de registros, busqueda en tiempo real y formulario
 * modal para crear/editar con combo de roles (tipos de persona).
 */
public class PersonasPanel extends JPanel {

    private final PersonService personService = new PersonService();
    private final TypePersonService typePersonService = new TypePersonService();

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JLabel lblStatus;
    // Cache en memoria para la busqueda sin recargar de la BD
    private List<Person> allPersons = new ArrayList<>();
    private List<TypePerson> allTypes = new ArrayList<>();

    private static final Color BG_CONTENT = MainFrame.getBgContent();
    private static final Color ACCENT = MainFrame.getAccent();
    private static final Color BG_SIDEBAR = MainFrame.getBgSidebar();
    private static final Color TEXT_WHITE = MainFrame.getTextWhite();

    public PersonasPanel(MainFrame frame) {
        setLayout(new BorderLayout());
        setBackground(BG_CONTENT);
        initUI();
        loadData();
    }

    // Construye la interfaz: titulo, toolbar, tabla y barra de estado
    private void initUI() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BG_CONTENT);
        topPanel.setBorder(new EmptyBorder(15, 20, 10, 20));

        JLabel title = new JLabel("Gestion de Personas");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(BG_SIDEBAR);
        topPanel.add(title, BorderLayout.NORTH);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 10));
        toolbar.setBackground(BG_CONTENT);

        JButton btnNew = createToolbarButton("+ Nueva Persona", new Color(46, 204, 113));
        JButton btnEdit = createToolbarButton("Editar", ACCENT);
        JButton btnDelete = createToolbarButton("Eliminar", new Color(231, 76, 60));
        JButton btnRefresh = createToolbarButton("Refrescar", new Color(149, 165, 166));

        txtSearch = new JTextField(20);
        txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtSearch.setPreferredSize(new Dimension(220, 32));
        txtSearch.putClientProperty("JTextField.placeholderText", "Buscar por nombre...");
        // Busqueda en tiempo real al escribir en el campo de texto
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

        add(topContainer, BorderLayout.NORTH);

        String[] columns = {"ID", "Nombre", "Apellido", "Email", "Rol"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setGridColor(new Color(220, 220, 220));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(ACCENT);
        table.setSelectionForeground(TEXT_WHITE);
        table.setFocusable(false);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(230, 230, 230));
        header.setForeground(BG_SIDEBAR);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(0).setMaxWidth(60);

        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);

        // Doble clic sobre una fila abre el dialogo de edicion
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editSelectedPerson();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(new Color(230, 230, 230));
        statusPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        lblStatus = new JLabel("Listo");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblStatus.setForeground(Color.GRAY);
        statusPanel.add(lblStatus);
        add(statusPanel, BorderLayout.SOUTH);

        btnNew.addActionListener(e -> showPersonDialog(null));
        btnEdit.addActionListener(e -> editSelectedPerson());
        btnDelete.addActionListener(e -> deleteSelectedPerson());
        btnRefresh.addActionListener(e -> loadData());
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

    // Carga personas y roles desde la BD y llena la tabla
    private void loadData() {
        try {
            allPersons = personService.listarTodos();
            allTypes = typePersonService.listarTodos();
            populateTable(allPersons);
            lblStatus.setText("Registros cargados: " + allPersons.size());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar datos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            lblStatus.setText("Error al cargar datos");
        }
    }

    // Vuelca la lista de personas en el modelo de la tabla
    private void populateTable(List<Person> persons) {
        tableModel.setRowCount(0);
        for (Person p : persons) {
            String typeName = p.getTypePerson() != null ? p.getTypePerson().getTypeName() : "";
            tableModel.addRow(new Object[]{
                    p.getIdPerson(),
                    p.getFirstName(),
                    p.getLastName(),
                    p.getEmail(),
                    typeName
            });
        }
    }

    // Filtra la tabla en memoria por nombre, apellido o email
    private void filterTable() {
        String query = txtSearch.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            populateTable(allPersons);
            return;
        }
        List<Person> filtered = new ArrayList<>();
        for (Person p : allPersons) {
            if ((p.getFirstName() != null && p.getFirstName().toLowerCase().contains(query))
                    || (p.getLastName() != null && p.getLastName().toLowerCase().contains(query))
                    || (p.getEmail() != null && p.getEmail().toLowerCase().contains(query))) {
                filtered.add(p);
            }
        }
        populateTable(filtered);
        lblStatus.setText("Resultados: " + filtered.size());
    }

    // Obtiene la persona seleccionada en la tabla (o null si no hay)
    private Person getSelectedPerson() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una persona de la tabla",
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        for (Person p : allPersons) {
            if (p.getIdPerson() == id) {
                return p;
            }
        }
        return null;
    }

    // Abre el dialogo de edicion para la persona seleccionada
    private void editSelectedPerson() {
        Person person = getSelectedPerson();
        if (person != null) {
            showPersonDialog(person);
        }
    }

    // Elimina la persona seleccionada tras confirmacion del usuario
    private void deleteSelectedPerson() {
        Person person = getSelectedPerson();
        if (person == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
                "Desea eliminar a " + person.getFirstName() + " " + person.getLastName() + "?",
                "Confirmar eliminacion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            personService.eliminar(person.getIdPerson());
            lblStatus.setText("Persona eliminada correctamente");
            loadData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al eliminar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Dialogo modal para crear o editar una persona con combo de roles
    private void showPersonDialog(Person person) {
        boolean editing = person != null;
        String title = editing ? "Editar Persona" : "Nueva Persona";

        if (allTypes.isEmpty()) {
            try {
                allTypes = typePersonService.listarTodos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Error al cargar tipos de persona: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        if (allTypes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay tipos de persona disponibles. Cree un tipo primero.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(15, 15, 15, 15));
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField txtFirstName = new JTextField(editing ? person.getFirstName() : "", 20);
        JTextField txtLastName = new JTextField(editing ? person.getLastName() : "", 20);
        JTextField txtEmail = new JTextField(editing ? person.getEmail() : "", 20);
        JComboBox<TypePerson> cmbType = new JComboBox<>();

        for (TypePerson tp : allTypes) {
            cmbType.addItem(tp);
        }

        if (editing && person.getTypePerson() != null) {
            for (int i = 0; i < cmbType.getItemCount(); i++) {
                if (cmbType.getItemAt(i).getIdTypePerson() == person.getTypePerson().getIdTypePerson()) {
                    cmbType.setSelectedIndex(i);
                    break;
                }
            }
        }

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel lbl1 = new JLabel("Nombre:");
        lbl1.setFont(labelFont);
        form.add(lbl1, gbc);
        gbc.gridx = 1;
        txtFirstName.setFont(fieldFont);
        form.add(txtFirstName, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        JLabel lbl2 = new JLabel("Apellido:");
        lbl2.setFont(labelFont);
        form.add(lbl2, gbc);
        gbc.gridx = 1;
        txtLastName.setFont(fieldFont);
        form.add(txtLastName, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        JLabel lbl3 = new JLabel("Email:");
        lbl3.setFont(labelFont);
        form.add(lbl3, gbc);
        gbc.gridx = 1;
        txtEmail.setFont(fieldFont);
        form.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        JLabel lbl4 = new JLabel("Rol:");
        lbl4.setFont(labelFont);
        form.add(lbl4, gbc);
        gbc.gridx = 1;
        cmbType.setFont(fieldFont);
        form.add(cmbType, gbc);

        int result = JOptionPane.showConfirmDialog(
                this, form, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) return;

        try {
            TypePerson selectedType = (TypePerson) cmbType.getSelectedItem();
            if (editing) {
                person.setFirstName(txtFirstName.getText().trim());
                person.setLastName(txtLastName.getText().trim());
                person.setEmail(txtEmail.getText().trim());
                person.setTypePerson(selectedType);
                personService.actualizar(person);
                lblStatus.setText("Persona actualizada: " + person.getFirstName() + " " + person.getLastName());
            } else {
                Person newPerson = new Person();
                newPerson.setFirstName(txtFirstName.getText().trim());
                newPerson.setLastName(txtLastName.getText().trim());
                newPerson.setEmail(txtEmail.getText().trim());
                newPerson.setTypePerson(selectedType);
                personService.guardar(newPerson);
                lblStatus.setText("Persona creada: " + newPerson.getFirstName() + " " + newPerson.getLastName());
            }
            loadData();
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
}

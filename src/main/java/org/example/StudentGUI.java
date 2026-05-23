package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StudentGUI extends JFrame {

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField nameField, emailField,
            courseField, gradeField, searchField;
    private DatabaseManager db = new DatabaseManager();

    public StudentGUI() {
        db.connect();

        setTitle("ShadowFox Student Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table
        String[] columns = {"ID", "Name", "Email",
                "Course", "Grade"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setBackground(
                Color.decode("#3F51B5"));
        table.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(table);

        // Double click to load
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        nameField.setText(
                                tableModel.getValueAt(
                                        row, 1).toString());
                        emailField.setText(
                                tableModel.getValueAt(
                                        row, 2).toString());
                        courseField.setText(
                                tableModel.getValueAt(
                                        row, 3).toString());
                        gradeField.setText(
                                tableModel.getValueAt(
                                        row, 4).toString());
                    }
                }
            }
        });

        // Search bar
        searchField = new JTextField(20);
        JButton searchBtn = new JButton("🔍 Search");
        JButton showAllBtn = new JButton("📋 Show All");
        searchBtn.setBackground(Color.decode("#FF9800"));
        searchBtn.setForeground(Color.WHITE);
        showAllBtn.setBackground(Color.decode("#607D8B"));
        showAllBtn.setForeground(Color.WHITE);
        searchBtn.addActionListener(e -> searchStudent());
        showAllBtn.addActionListener(e -> refreshTable());

        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(Color.decode("#E8EAF6"));
        searchPanel.add(new JLabel("Search by Name:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(showAllBtn);

        // Form
        nameField = new JTextField(12);
        emailField = new JTextField(12);
        courseField = new JTextField(12);
        gradeField = new JTextField(12);

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder(
                "Student Details"));
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Course:"));
        formPanel.add(courseField);
        formPanel.add(new JLabel("Grade:"));
        formPanel.add(gradeField);

        // Buttons
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");

        addBtn.setBackground(Color.decode("#4CAF50"));
        addBtn.setForeground(Color.WHITE);
        updateBtn.setBackground(Color.decode("#2196F3"));
        updateBtn.setForeground(Color.WHITE);
        deleteBtn.setBackground(Color.decode("#f44336"));
        deleteBtn.setForeground(Color.WHITE);
        clearBtn.setBackground(Color.decode("#9E9E9E"));
        clearBtn.setForeground(Color.WHITE);

        addBtn.addActionListener(e -> addStudent());
        updateBtn.addActionListener(e -> updateStudent());
        deleteBtn.addActionListener(e -> deleteStudent());
        clearBtn.addActionListener(e -> clearFields());

        JPanel btnPanel = new JPanel();
        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        btnPanel.add(clearBtn);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        bottomPanel.add(btnPanel, BorderLayout.SOUTH);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshTable();
        setVisible(true);
    }

    private void addStudent() {
        try {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String course = courseField.getText().trim();
            double grade = Double.parseDouble(
                    gradeField.getText().trim());

            if (name.isEmpty() || email.isEmpty()
                    || course.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "❌ All fields are required!");
                return;
            }
            if (grade < 0 || grade > 100) {
                JOptionPane.showMessageDialog(this,
                        "❌ Grade must be between 0 and 100!");
                return;
            }

            boolean added = db.addStudent(
                    name, email, course, grade);
            if (added) {
                refreshTable();
                clearFields();
                JOptionPane.showMessageDialog(this,
                        "✅ Student added!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "❌ Invalid grade!");
        }
    }

    private void updateStudent() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a student to update!");
            return;
        }
        try {
            int id = Integer.parseInt(
                    tableModel.getValueAt(row, 0).toString());
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String course = courseField.getText().trim();
            double grade = Double.parseDouble(
                    gradeField.getText().trim());

            boolean updated = db.updateStudent(
                    id, name, email, course, grade);
            if (updated) {
                refreshTable();
                clearFields();
                JOptionPane.showMessageDialog(this,
                        "✅ Student updated!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "❌ Invalid grade!");
        }
    }

    private void deleteStudent() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a student to delete!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this student?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(
                    tableModel.getValueAt(row, 0).toString());
            boolean deleted = db.deleteStudent(id);
            if (deleted) {
                refreshTable();
                clearFields();
            }
        }
    }

    private void searchStudent() {
        String keyword = searchField.getText().trim();
        tableModel.setRowCount(0);
        for (Student s : db.searchByName(keyword)) {
            tableModel.addRow(new Object[]{
                    s.getId(), s.getName(), s.getEmail(),
                    s.getCourse(), s.getGrade()});
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Student s : db.getAllStudents()) {
            tableModel.addRow(new Object[]{
                    s.getId(), s.getName(), s.getEmail(),
                    s.getCourse(), s.getGrade()});
        }
    }

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        courseField.setText("");
        gradeField.setText("");
        searchField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentGUI::new);
    }
}
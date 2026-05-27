package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class StudentGUI extends JFrame {

    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField nameField, emailField,
            courseField, gradeField, searchField;
    private DatabaseManager db = new DatabaseManager();
    private JLabel avgLabel, highLabel, lowLabel;

    public StudentGUI() {
        db.connect();

        setTitle("ShadowFox Student Management");
        setSize(800, 680);
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

        // Row 1 - Search
        searchField = new JTextField(15);
        JButton searchBtn = new JButton("🔍 Search");
        JButton filterBtn = new JButton("📚 Filter Course");

        searchBtn.setBackground(Color.decode("#FF9800"));
        searchBtn.setForeground(Color.WHITE);
        filterBtn.setBackground(Color.decode("#9C27B0"));
        filterBtn.setForeground(Color.WHITE);

        searchBtn.addActionListener(e -> searchStudent());
        filterBtn.addActionListener(e -> filterByCourse());

        JPanel row1 = new JPanel();
        row1.setBackground(Color.decode("#E8EAF6"));
        row1.add(new JLabel("Search/Course:"));
        row1.add(searchField);
        row1.add(searchBtn);
        row1.add(filterBtn);

        // Row 2 - Action buttons
        JButton showAllBtn = new JButton("📋 Show All");
        JButton statsBtn = new JButton("📊 Statistics");
        JButton exportBtn = new JButton("📤 Export CSV");
        JButton importBtn = new JButton("📥 Import CSV");

        showAllBtn.setBackground(Color.decode("#607D8B"));
        showAllBtn.setForeground(Color.WHITE);
        statsBtn.setBackground(Color.decode("#009688"));
        statsBtn.setForeground(Color.WHITE);
        exportBtn.setBackground(Color.decode("#FF5722"));
        exportBtn.setForeground(Color.WHITE);
        importBtn.setBackground(Color.decode("#4CAF50"));
        importBtn.setForeground(Color.WHITE);

        showAllBtn.addActionListener(e -> refreshTable());
        statsBtn.addActionListener(e -> showStatistics());
        exportBtn.addActionListener(e -> exportCSV());
        importBtn.addActionListener(e -> importCSV());

        JPanel row2 = new JPanel();
        row2.setBackground(Color.decode("#E8EAF6"));
        row2.add(showAllBtn);
        row2.add(statsBtn);
        row2.add(exportBtn);
        row2.add(importBtn);

        JPanel searchPanel = new JPanel(new GridLayout(2, 1));
        searchPanel.setBackground(Color.decode("#E8EAF6"));
        searchPanel.add(row1);
        searchPanel.add(row2);

        // Stats panel
        avgLabel = new JLabel("Avg: 0.0");
        highLabel = new JLabel("Highest: 0.0");
        lowLabel = new JLabel("Lowest: 0.0");

        avgLabel.setFont(new Font("Arial", Font.BOLD, 13));
        highLabel.setFont(new Font("Arial", Font.BOLD, 13));
        lowLabel.setFont(new Font("Arial", Font.BOLD, 13));

        avgLabel.setForeground(Color.decode("#2196F3"));
        highLabel.setForeground(Color.decode("#4CAF50"));
        lowLabel.setForeground(Color.decode("#f44336"));

        JPanel statsPanel = new JPanel();
        statsPanel.setBackground(Color.decode("#E8EAF6"));
        statsPanel.add(avgLabel);
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(highLabel);
        statsPanel.add(Box.createHorizontalStrut(20));
        statsPanel.add(lowLabel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.SOUTH);

        // Form
        nameField = new JTextField(15);
        emailField = new JTextField(15);
        courseField = new JTextField(15);
        gradeField = new JTextField(15);

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

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshTable();
        updateStats();
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

            boolean added = db.addStudent(
                    name, email, course, grade);
            if (added) {
                refreshTable();
                updateStats();
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
                updateStats();
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
            db.deleteStudent(id);
            refreshTable();
            updateStats();
            clearFields();
        }
    }

    private void searchStudent() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            refreshTable();
            return;
        }
        tableModel.setRowCount(0);
        for (Student s : db.searchByName(keyword)) {
            tableModel.addRow(new Object[]{
                    s.getId(), s.getName(), s.getEmail(),
                    s.getCourse(), s.getGrade()});
        }
    }

    private void filterByCourse() {
        String course = searchField.getText().trim();
        if (course.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a course name to filter!");
            return;
        }
        tableModel.setRowCount(0);
        for (Student s : db.filterByCourse(course)) {
            tableModel.addRow(new Object[]{
                    s.getId(), s.getName(), s.getEmail(),
                    s.getCourse(), s.getGrade()});
        }
        double avg = db.getCourseAverage(course);
        JOptionPane.showMessageDialog(this,
                "Course: " + course +
                        "\nAverage Grade: " +
                        String.format("%.2f", avg));
    }

    private void showStatistics() {
        double avg = db.getAverageGrade();
        double high = db.getHighestGrade();
        double low = db.getLowestGrade();

        JOptionPane.showMessageDialog(this,
                "📊 Grade Statistics\n\n" +
                        "Average Grade: " +
                        String.format("%.2f", avg) + "\n" +
                        "Highest Grade: " + high + "\n" +
                        "Lowest Grade: " + low,
                "Statistics",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportCSV() {
        ArrayList<Student> students = db.getAllStudents();
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "❌ No students to export!");
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(
                new java.io.File("students.csv"));
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser
                    .getSelectedFile().getAbsolutePath();
            boolean exported = db.exportToCSV(
                    filename, students);
            if (exported) {
                JOptionPane.showMessageDialog(this,
                        "✅ Students exported to:\n" + filename);
            } else {
                JOptionPane.showMessageDialog(this,
                        "❌ Export failed!");
            }
        }
    }

    private void importCSV() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filename = fileChooser
                    .getSelectedFile().getAbsolutePath();
            int count = db.importFromCSV(filename);
            if (count > 0) {
                refreshTable();
                updateStats();
                JOptionPane.showMessageDialog(this,
                        "✅ Imported " + count + " students!");
            } else {
                JOptionPane.showMessageDialog(this,
                        "❌ No students imported!");
            }
        }
    }

    private void updateStats() {
        avgLabel.setText("Avg: " +
                String.format("%.2f", db.getAverageGrade()));
        highLabel.setText("Highest: " + db.getHighestGrade());
        lowLabel.setText("Lowest: " + db.getLowestGrade());
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Student s : db.getAllStudents()) {
            tableModel.addRow(new Object[]{
                    s.getId(), s.getName(), s.getEmail(),
                    s.getCourse(), s.getGrade()});
        }
        updateStats();
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
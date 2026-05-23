package org.example;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager {
    private static final String URL =
            "jdbc:sqlite:students.db";
    private Connection connection;

    // Connect to database
    public void connect() {
        try {
            connection = DriverManager.getConnection(URL);
            createTable();
            System.out.println("✅ Database connected!");
        } catch (SQLException e) {
            System.out.println("❌ Connection failed: "
                    + e.getMessage());
        }
    }

    // Create students table
    private void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS students (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                course TEXT NOT NULL,
                grade REAL NOT NULL
            )
            """;
        try {
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("❌ Table creation failed: "
                    + e.getMessage());
        }
    }

    // Add student
    public boolean addStudent(String name, String email,
                              String course, double grade) {
        String sql = "INSERT INTO students " +
                "(name, email, course, grade) " +
                "VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, course);
            pstmt.setDouble(4, grade);
            pstmt.executeUpdate();
            System.out.println("✅ Student added!");
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Add failed: "
                    + e.getMessage());
            return false;
        }
    }

    // Get all students
    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("course"),
                        rs.getDouble("grade")));
            }
        } catch (SQLException e) {
            System.out.println("❌ Fetch failed: "
                    + e.getMessage());
        }
        return students;
    }

    // Update student
    public boolean updateStudent(int id, String name,
                                 String email, String course,
                                 double grade) {
        String sql = "UPDATE students SET name=?, " +
                "email=?, course=?, grade=? WHERE id=?";
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, course);
            pstmt.setDouble(4, grade);
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
            System.out.println("✅ Student updated!");
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Update failed: "
                    + e.getMessage());
            return false;
        }
    }

    // Delete student
    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id=?";
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("✅ Student deleted!");
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Delete failed: "
                    + e.getMessage());
            return false;
        }
    }

    // Search by name
    public ArrayList<Student> searchByName(String keyword) {
        ArrayList<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE " +
                "LOWER(name) LIKE LOWER(?)";
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement(sql);
            pstmt.setString(1, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("course"),
                        rs.getDouble("grade")));
            }
        } catch (SQLException e) {
            System.out.println("❌ Search failed: "
                    + e.getMessage());
        }
        return students;
    }

    // Disconnect
    public void disconnect() {
        try {
            if (connection != null) connection.close();
            System.out.println("✅ Database disconnected!");
        } catch (SQLException e) {
            System.out.println("❌ Disconnect failed: "
                    + e.getMessage());
        }
    }
}
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

    // Get average grade
    public double getAverageGrade() {
        String sql = "SELECT AVG(grade) as avg FROM students";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble("avg");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        return 0.0;
    }

    // Get highest grade
    public double getHighestGrade() {
        String sql = "SELECT MAX(grade) as max FROM students";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble("max");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        return 0.0;
    }

    // Get lowest grade
    public double getLowestGrade() {
        String sql = "SELECT MIN(grade) as min FROM students";
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getDouble("min");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        return 0.0;
    }

    // Filter by course
    public ArrayList<Student> filterByCourse(String course) {
        ArrayList<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE " +
                "LOWER(course) LIKE LOWER(?)";
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement(sql);
            pstmt.setString(1, "%" + course + "%");
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
            System.out.println("❌ Filter failed: "
                    + e.getMessage());
        }
        return students;
    }

    // Get course average
    public double getCourseAverage(String course) {
        String sql = "SELECT AVG(grade) as avg FROM students " +
                "WHERE LOWER(course) = LOWER(?)";
        try {
            PreparedStatement pstmt =
                    connection.prepareStatement(sql);
            pstmt.setString(1, course);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("avg");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
        return 0.0;
    }

    // Export to CSV
    public boolean exportToCSV(String filename,
                               ArrayList<Student> students) {
        try {
            java.io.FileWriter writer =
                    new java.io.FileWriter(filename);
            writer.write("ID,Name,Email,Course,Grade\n");
            for (Student s : students) {
                writer.write(s.getId() + "," +
                        s.getName() + "," +
                        s.getEmail() + "," +
                        s.getCourse() + "," +
                        s.getGrade() + "\n");
            }
            writer.close();
            System.out.println("✅ Exported to " + filename);
            return true;
        } catch (Exception e) {
            System.out.println("❌ Export failed: "
                    + e.getMessage());
            return false;
        }
    }

    // Import from CSV
    public int importFromCSV(String filename) {
        int count = 0;
        try {
            java.io.BufferedReader reader =
                    new java.io.BufferedReader(
                            new java.io.FileReader(filename));
            String line;
            reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String name = parts[1].trim();
                    String email = parts[2].trim();
                    String course = parts[3].trim();
                    double grade = Double.parseDouble(
                            parts[4].trim());
                    boolean added = addStudent(
                            name, email, course, grade);
                    if (added) count++;
                }
            }
            reader.close();
            System.out.println("✅ Imported " + count
                    + " students!");
        } catch (Exception e) {
            System.out.println("❌ Import failed: "
                    + e.getMessage());
        }
        return count;
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
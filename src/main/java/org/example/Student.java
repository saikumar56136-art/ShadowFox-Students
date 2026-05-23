package org.example;

public class Student {
    private int id;
    private String name;
    private String email;
    private String course;
    private double grade;

    public Student(int id, String name,
                   String email, String course,
                   double grade) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.course = course;
        this.grade = grade;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getCourse() { return course; }
    public double getGrade() { return grade; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setCourse(String course) { this.course = course; }
    public void setGrade(double grade) { this.grade = grade; }

    @Override
    public String toString() {
        return "ID: " + id +
                " | Name: " + name +
                " | Email: " + email +
                " | Course: " + course +
                " | Grade: " + grade;
    }
}
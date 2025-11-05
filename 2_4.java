import java.sql.*;
import java.util.*;

// ---------- MODEL CLASSES ----------
class Student {
    int studentID;
    String name;
    String department;
    double marks;

    Student(int studentID, String name, String department, double marks) {
        this.studentID = studentID;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }
}

// ---------- CONTROLLER CLASS ----------
class DatabaseController {
    private static final String URL = "jdbc:mysql://localhost:3306/college_db";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password_here"; // Change this

    Connection conn;

    DatabaseController() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Database connected successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
        }
    }

    // PART A: Fetch Employee Data
    public void fetchEmployees() {
        System.out.println("\n--- Employee Records ---");
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Employee")) {
            while (rs.next()) {
                System.out.printf("EmpID: %d | Name: %s | Salary: %.2f%n",
                        rs.getInt("EmpID"), rs.getString("Name"), rs.getDouble("Salary"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching employees: " + e.getMessage());
        }
    }

    // PART B: CRUD on Product
    public void insertProduct(int id, String name, double price, int qty) {
        String sql = "INSERT INTO Product VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.setString(2, name);
            ps.setDouble(3, price);
            ps.setInt(4, qty);
            ps.executeUpdate();
            System.out.println("✅ Product added successfully!");
        } catch (SQLException e) {
            System.out.println("Error inserting product: " + e.getMessage());
        }
    }

    public void viewProducts() {
        System.out.println("\n--- Product List ---");
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Product")) {
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Price: %.2f | Quantity: %d%n",
                        rs.getInt("ProductID"), rs.getString("ProductName"),
                        rs.getDouble("Price"), rs.getInt("Quantity"));
            }
        } catch (SQLException e) {
            System.out.println("Error reading products: " + e.getMessage());
        }
    }

    public void updateProduct(int id, double newPrice) {
        String sql = "UPDATE Product SET Price=? WHERE ProductID=?";
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setDouble(1, newPrice);
                ps.setInt(2, id);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    conn.commit();
                    System.out.println("✅ Product updated successfully!");
                } else {
                    conn.rollback();
                    System.out.println("❌ Product not found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error updating product: " + e.getMessage());
        }
    }

    public void deleteProduct(int id) {
        String sql = "DELETE FROM Product WHERE ProductID=?";
        try {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    conn.commit();
                    System.out.println("✅ Product deleted successfully!");
                } else {
                    conn.rollback();
                    System.out.println("❌ Product not found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error deleting product: " + e.getMessage());
        }
    }

    // PART C: CRUD on Students (MVC Style)
    public void addStudent(Student s) {
        String sql = "INSERT INTO Student VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, s.studentID);
            ps.setString(2, s.name);
            ps.setString(3, s.department);
            ps.setDouble(4, s.marks);
            ps.executeUpdate();
            System.out.println("✅ Student added successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
        }
    }

    public void viewStudents() {
        System.out.println("\n--- Student Records ---");
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Student")) {
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Dept: %s | Marks: %.2f%n",
                        rs.getInt("StudentID"), rs.getString("Name"),
                        rs.getString("Department"), rs.getDouble("Marks"));
            }
        } catch (SQLException e) {
            System.out.println("Error reading students: " + e.getMessage());
        }
    }

    public void updateStudentMarks(int id, double newMarks) {
        String sql = "UPDATE Student SET Marks=? WHERE StudentID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, newMarks);
            ps.setInt(2, id);
            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("✅ Student marks updated!");
            else
                System.out.println("❌ Student not found.");
        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
        }
    }

    public void deleteStudent(int id) {
        String sql = "DELETE FROM Student WHERE StudentID=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println("✅ Student deleted successfully!");
            else
                System.out.println("❌ Student not found.");
        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
        }
    }
}

// ---------- MAIN (VIEW) ----------
public class Experiment2_4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        DatabaseController db = new DatabaseController();

        while (true) {
            System.out.println("\n========== MENU ==========");
            System.out.println("1. Fetch Employees (Part A)");
            System.out.println("2. Product CRUD Operations (Part B)");
            System.out.println("3. Student Management (MVC - Part C)");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1 -> db.fetchEmployees();
                case 2 -> {
                    System.out.println("\n1. Add Product\n2. View Products\n3. Update Product\n4. Delete Product");
                    System.out.print("Enter option: ");
                    int opt = sc.nextInt();
                    switch (opt) {
                        case 1 -> {
                            System.out.print("Enter ID, Name, Price, Quantity: ");
                            int id = sc.nextInt();
                            String name = sc.next();
                            double price = sc.nextDouble();
                            int qty = sc.nextInt();
                            db.insertProduct(id, name, price, qty);
                        }
                        case 2 -> db.viewProducts();
                        case 3 -> {
                            System.out.print("Enter ProductID & New Price: ");
                            int id = sc.nextInt();
                            double newPrice = sc.nextDouble();
                            db.updateProduct(id, newPrice);
                        }
                        case 4 -> {
                            System.out.print("Enter ProductID to delete: ");
                            int id = sc.nextInt();
                            db.deleteProduct(id);
                        }
                    }
                }
                case 3 -> {
                    System.out.println("\n1. Add Student\n2. View Students\n3. Update Marks\n4. Delete Student");
                    System.out.print("Enter option: ");
                    int opt = sc.nextInt();
                    switch (opt) {
                        case 1 -> {
                            System.out.print("Enter ID, Name, Department, Marks: ");
                            int id = sc.nextInt();
                            String name = sc.next();
                            String dept = sc.next();
                            double marks = sc.nextDouble();
                            db.addStudent(new Student(id, name, dept, marks));
                        }
                        case 2 -> db.viewStudents();
                        case 3 -> {
                            System.out.print("Enter StudentID & New Marks: ");
                            int id = sc.nextInt();
                            double marks = sc.nextDouble();
                            db.updateStudentMarks(id, marks);
                        }
                        case 4 -> {
                            System.out.print("Enter StudentID to delete: ");
                            int id = sc.nextInt();
                            db.deleteStudent(id);
                        }
                    }
                }
                case 4 -> {
                    System.out.println("Exiting program...");
                    System.exit(0);
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }
}

import java.io.*;
import java.util.*;

class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    int studentID;
    String name;
    String grade;

    Student(int studentID, String name, String grade) {
        this.studentID = studentID;
        this.name = name;
        this.grade = grade;
    }

    void display() {
        System.out.println("\n--- Student Details ---");
        System.out.println("ID: " + studentID);
        System.out.println("Name: " + name);
        System.out.println("Grade: " + grade);
    }
}

class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    int id;
    String name;
    String designation;
    double salary;

    Employee(int id, String name, String designation, double salary) {
        this.id = id;
        this.name = name;
        this.designation = designation;
        this.salary = salary;
    }

    void display() {
        System.out.println("\n--- Employee Details ---");
        System.out.println("ID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Designation: " + designation);
        System.out.println("Salary: " + salary);
    }
}

public class ComprehensiveJavaTasks {
    private static final String EMP_FILE = "employees.dat";
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int choice;
        do {
            System.out.println("\n====== Java Comprehensive Tasks ======");
            System.out.println("1. Sum of Integers using Autoboxing and Unboxing");
            System.out.println("2. Serialization and Deserialization of Student Object");
            System.out.println("3. Menu-Based Employee Management System");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = getIntInput();

            switch (choice) {
                case 1:
                    performAutoboxingSum();
                    break;
                case 2:
                    performStudentSerialization();
                    break;
                case 3:
                    employeeMenu();
                    break;
                case 4:
                    System.out.println("Exiting Program... Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice! Try again.");
            }
        } while (choice != 4);
    }

    // ========== PART (A): AUTOBOXING ==========
    private static void performAutoboxingSum() {
        System.out.println("\nEnter integers separated by space:");
        String input = sc.nextLine();
        if (input.isEmpty()) input = sc.nextLine();

        ArrayList<Integer> numbers = new ArrayList<>();
        for (String s : input.split(" ")) {
            Integer num = Integer.parseInt(s); // Autoboxing
            numbers.add(num);
        }

        int sum = 0;
        for (Integer n : numbers) sum += n; // Unboxing
        System.out.println("✅ Sum of integers: " + sum);
    }

    // ========== PART (B): SERIALIZATION ==========
    private static void performStudentSerialization() {
        System.out.print("\nEnter Student ID: ");
        int id = getIntInput();
        sc.nextLine(); // consume newline
        System.out.print("Enter Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Grade: ");
        String grade = sc.nextLine();

        Student s1 = new Student(id, name, grade);

        // Serialize
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("student.ser"))) {
            oos.writeObject(s1);
            System.out.println("✅ Student object serialized to student.ser");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Deserialize
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("student.ser"))) {
            Student s2 = (Student) ois.readObject();
            System.out.println("✅ Student object deserialized successfully:");
            s2.display();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // ========== PART (C): EMPLOYEE MANAGEMENT ==========
    private static void employeeMenu() {
        int choice;
        do {
            System.out.println("\n====== Employee Management System ======");
            System.out.println("1. Add Employee");
            System.out.println("2. Display All Employees");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");
            choice = getIntInput();

            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    displayEmployees();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        } while (choice != 3);
    }

    private static void addEmployee() {
        try (AppendableObjectOutputStream oos =
                     new AppendableObjectOutputStream(new FileOutputStream(EMP_FILE, true))) {
            System.out.print("Enter Employee ID: ");
            int id = getIntInput();
            sc.nextLine();
            System.out.print("Enter Name: ");
            String name = sc.nextLine();
            System.out.print("Enter Designation: ");
            String designation = sc.nextLine();
            System.out.print("Enter Salary: ");
            double salary = getDoubleInput();

            Employee emp = new Employee(id, name, designation, salary);
            oos.writeObject(emp);
            System.out.println("✅ Employee added successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void displayEmployees() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(EMP_FILE))) {
            System.out.println("\n--- Employee Records ---");
            while (true) {
                Employee emp = (Employee) ois.readObject();
                emp.display();
            }
        } catch (EOFException e) {
            System.out.println("✅ End of employee list reached.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("⚠️ No employee records found yet.");
        }
    }

    // Helper subclass to append serialized objects
    static class AppendableObjectOutputStream extends ObjectOutputStream {
        AppendableObjectOutputStream(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        protected void writeStreamHeader() throws IOException {
            File file = new File(EMP_FILE);
            if (file.length() == 0) {
                super.writeStreamHeader(); // write header if file empty
            } else {
                reset(); // skip writing header
            }
        }
    }

    // ====== Utility Input Methods ======
    private static int getIntInput() {
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }
        return sc.nextInt();
    }

    private static double getDoubleInput() {
        while (!sc.hasNextDouble()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }
        return sc.nextDouble();
    }
}

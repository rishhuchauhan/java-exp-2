import java.util.*;
import java.util.stream.*;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

// PART A: Employee class for sorting
class Employee {
    String name;
    int age;
    double salary;

    Employee(String name, int age, double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public String toString() {
        return String.format("Name: %s | Age: %d | Salary: %.2f", name, age, salary);
    }
}

// PART B: Student class for filtering and sorting
class Student {
    String name;
    double marks;

    Student(String name, double marks) {
        this.name = name;
        this.marks = marks;
    }

    public String toString() {
        return String.format("Name: %s | Marks: %.2f", name, marks);
    }
}

// PART C: Product class for stream grouping and analysis
class Product {
    String name;
    double price;
    String category;

    Product(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String toString() {
        return String.format("Name: %s | Price: %.2f | Category: %s", name, price, category);
    }
}

public class Experiment2_3 {

    public static void main(String[] args) {
        // ----------- PART A -----------
        System.out.println("===== Part A: Sorting Employees Using Lambda =====");
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("Rishav", 23, 40000));
        employees.add(new Employee("Aman", 25, 35000));
        employees.add(new Employee("Neha", 21, 45000));
        employees.add(new Employee("Priya", 24, 38000));

        System.out.println("\nOriginal List:");
        employees.forEach(System.out::println);

        // Sort by name
        employees.sort((e1, e2) -> e1.name.compareTo(e2.name));
        System.out.println("\nSorted by Name:");
        employees.forEach(System.out::println);

        // Sort by age
        employees.sort((e1, e2) -> Integer.compare(e1.age, e2.age));
        System.out.println("\nSorted by Age:");
        employees.forEach(System.out::println);

        // Sort by salary descending
        employees.sort((e1, e2) -> Double.compare(e2.salary, e1.salary));
        System.out.println("\nSorted by Salary (Descending):");
        employees.forEach(System.out::println);

        // ----------- PART B -----------
        System.out.println("\n===== Part B: Filtering & Sorting Students Using Streams =====");
        List<Student> students = Arrays.asList(
                new Student("Ravi", 82),
                new Student("Simran", 74),
                new Student("Kunal", 90),
                new Student("Isha", 68),
                new Student("Tina", 95)
        );

        System.out.println("\nStudents scoring above 75%, sorted by marks:");
        students.stream()
                .filter(s -> s.marks > 75)
                .sorted(Comparator.comparingDouble(s -> s.marks))
                .map(s -> s.name + " (" + s.marks + ")")
                .forEach(System.out::println);

        // ----------- PART C -----------
        System.out.println("\n===== Part C: Stream Operations on Product Dataset =====");
        List<Product> products = Arrays.asList(
                new Product("Laptop", 80000, "Electronics"),
                new Product("Smartphone", 60000, "Electronics"),
                new Product("Shoes", 3500, "Fashion"),
                new Product("Watch", 12000, "Fashion"),
                new Product("Mixer", 4500, "Home"),
                new Product("Fridge", 30000, "Home")
        );

        // Group by category
        Map<String, List<Product>> groupedByCategory =
                products.stream().collect(Collectors.groupingBy(p -> p.category));
        System.out.println("\nProducts Grouped by Category:");
        groupedByCategory.forEach((cat, list) -> {
            System.out.println(cat + ": " + list);
        });

        // Most expensive product in each category
        Map<String, Optional<Product>> mostExpensiveByCategory =
                products.stream().collect(Collectors.groupingBy(
                        p -> p.category,
                        Collectors.maxBy(Comparator.comparingDouble(p -> p.price))
                ));
        System.out.println("\nMost Expensive Product in Each Category:");
        mostExpensiveByCategory.forEach((cat, prod) ->
                System.out.println(cat + ": " + prod.get()));

        // Average price of all products
        double avgPrice = products.stream()
                .collect(Collectors.averagingDouble(p -> p.price));
        System.out.println("\nAverage Price of All Products: " + avgPrice);
    }
}

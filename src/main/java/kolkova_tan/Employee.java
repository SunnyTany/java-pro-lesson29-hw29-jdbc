package kolkova_tan;

public class Employee {
    private int id;
    private String name;
    private int age;
    private String position;
    private double salary;

    // Constructor for creating a new employee (without an ID, since the database will assign one itself)
    public Employee(String name, int age, String position, double salary) {
        this.name = name;
        this.age = age;
        this.position = position;
        this.salary = salary;
    }

    // Constructor for loading an employee from a database (with a known ID)
    public Employee(int id, String name, int age, String position, double salary) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.position = position;
        this.salary = salary;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getPosition() { return position; }
    public double getSalary() { return salary; }

    @Override
    public String toString() {
        return "Employee{" + "id=" + id + ", name='" + name + '\'' + ", age=" + age +
                ", position='" + position + '\'' + ", salary=" + salary + '}';
    }
}
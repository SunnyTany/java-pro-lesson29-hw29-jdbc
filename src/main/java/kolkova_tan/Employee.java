package kolkova_tan;

import java.math.BigDecimal;

public class Employee {
    // Спочатку йдуть посилання (references) для кращого memory footprint
    private String name;
    private String position;
    private BigDecimal salary;
    private Integer id;      // Клас-обгортка замість примітиву
    private int age;         // Примітив в кінці

    // Конструктор без ID (для створення нових)
    public Employee(String name, int age, String position, BigDecimal salary) {
        this.name = name;
        this.age = age;
        this.position = position;
        this.salary = salary;
    }

    // Конструктор з ID (для завантаження з БД)
    public Employee(Integer id, String name, int age, String position, BigDecimal salary) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.position = position;
        this.salary = salary;
    }

    // Геттери та сеттери
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public int getAge() { return age; }
    public String getPosition() { return position; }
    public BigDecimal getSalary() { return salary; }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", position='" + position + '\'' +
                ", salary=" + salary +
                '}';
    }
}
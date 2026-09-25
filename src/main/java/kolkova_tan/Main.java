package kolkova_tan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        EmployeeDAO employeeDAO = new EmployeeDAO();

        try {
            System.out.println("=== Крок 1: Додавання співробітників ===");
            Employee emp1 = new Employee("Іван Петров", 28, "Junior Developer", new BigDecimal("1200.00"));
            Employee emp2 = new Employee("Анна Сидорова", 32, "QA Engineer", new BigDecimal("1500.00"));

            // Після виконання цих методів в об'єктах emp1 та emp2 з'являться ID з бази даних!
            employeeDAO.addEmployee(emp1);
            employeeDAO.addEmployee(emp2);

            System.out.println("\n=== Крок 2: Виведення всіх співробітників ===");
            printEmployees(employeeDAO);

            System.out.println("\n=== Крок 3: Пошук по ID (Optional) ===");
            Optional<Employee> foundEmp = employeeDAO.getEmployeeById(emp1.getId());
            foundEmp.ifPresent(employee -> System.out.println("Знайдено співробітника: " + employee));

            System.out.println("\n=== Крок 4: Оновлення співробітника ===");
            if (foundEmp.isPresent()) {
                Employee toUpdate = foundEmp.get();
                // Змінюємо позицію та зарплату через конструктор/сеттери
                Employee updatedVersion = new Employee(
                        toUpdate.getId(),
                        toUpdate.getName(),
                        toUpdate.getAge(),
                        "Middle Developer",
                        new BigDecimal("2200.00")
                );
                employeeDAO.updateEmployee(updatedVersion);
            }

            System.out.println("\n=== Поточний список після змін ===");
            printEmployees(employeeDAO);

        } catch (DaoOperationException e) {
            System.err.println("Сталася помилка на рівні DAO: " + e.getMessage());
            if (e.getCause() != null) {
                System.err.println("Першопричина: " + e.getCause().getMessage());
            }
        }
    }

    private static void printEmployees(EmployeeDAO employeeDAO) {
        ArrayList<Employee> currentList = employeeDAO.getAllEmployees();
        if (currentList.isEmpty()) {
            System.out.println("База даних пуста.");
        } else {
            for (Employee emp : currentList) {
                System.out.println(emp);
            }
        }
    }
}
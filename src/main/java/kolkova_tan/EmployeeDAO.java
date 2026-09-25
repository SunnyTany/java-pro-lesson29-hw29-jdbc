package kolkova_tan;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class EmployeeDAO {

    public void addEmployee(Employee employee) {
        String sql = "INSERT INTO employees (name, age, position, salary) VALUES (?, ?, ?, ?)";

        // Вказуємо, що хочемо отримати назад згенеровані ключі
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURNED_GENERATED_KEYS)) {

            pstmt.setString(1, employee.getName());
            pstmt.setInt(2, employee.getAge());
            pstmt.setString(3, employee.getPosition());
            pstmt.setBigDecimal(4, employee.getSalary());

            pstmt.executeUpdate();

            // Отримуємо згенерований ID
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    employee.setId(generatedKeys.getInt(1));
                } else {
                    throw new DaoOperationException("Не вдалося отримати згенерований ID для співробітника.");
                }
            }
            System.out.println("[Успіх] Співробітник доданий з ID " + employee.getId() + ": " + employee.getName());

        } catch (SQLException e) {
            throw new DaoOperationException("Не вдалося додати співробітника: " + employee.getName(), e);
        }
    }

    // Використовуємо ArrayList як тип повернення за вимогою оптимізації CPU
    public ArrayList<Employee> getAllEmployees() {
        ArrayList<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                employees.add(new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("position"),
                        rs.getBigDecimal("salary")
                ));
            }
        } catch (SQLException e) {
            throw new DaoOperationException("Не вдалося отримати список співробітників", e);
        }
        return employees;
    }

    // Повертаємо Optional для безпечної обробки відсутності результату
    public Optional<Employee> getEmployeeById(int id) {
        String sql = "SELECT * FROM employees WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Employee employee = new Employee(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("age"),
                            rs.getString("position") != null ? rs.getString("position") : "Не вказана",
                            rs.getBigDecimal("salary")
                    );
                    return Optional.of(employee);
                }
            }
        } catch (SQLException e) {
            throw new DaoOperationException("Помилка під час пошуку співробітника з id " + id, e);
        }
        return Optional.empty();
    }

    public void updateEmployee(Employee employee) {
        String sql = "UPDATE employees SET name = ?, age = ?, position = ?, salary = ? WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, employee.getName());
            pstmt.setInt(2, employee.getAge());
            pstmt.setString(3, employee.getPosition());
            pstmt.setBigDecimal(4, employee.getSalary());
            pstmt.setInt(5, employee.getId());

            int updatedRows = pstmt.executeUpdate();

            // Якщо жоден рядок не оновився, кидаємо помилку (виправлено помилку в рев'ю)
            if (updatedRows == 0) {
                throw new DaoOperationException("Співробітника з ID " + employee.getId() + " не знайдено для оновлення.");
            }

            System.out.println("[Успіх] Дані співробітника з ID " + employee.getId() + " оновлено.");

        } catch (SQLException e) {
            throw new DaoOperationException("Не вдалося оновити дані співробітника з ID " + employee.getId(), e);
        }
    }

    public void deleteEmployee(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int deletedRows = pstmt.executeUpdate();

            if (deletedRows == 0) {
                throw new DaoOperationException("Співробітника з ID " + id + " не знайдено для видалення.");
            }

            System.out.println("[Успіх] Співробітник з ID " + id + " видалений.");
        } catch (SQLException e) {
            throw new DaoOperationException("Не вдалося видалити співробітника з ID " + id, e);
        }
    }
}
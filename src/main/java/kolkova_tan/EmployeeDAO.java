package kolkova_tan;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    public void addEmployee(Employee employee) {
        String sql = "INSERT INTO employees (name, age, position, salary) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employee.getName());
            pstmt.setInt(2, employee.getAge());
            pstmt.setString(3, employee.getPosition());
            pstmt.setDouble(4, employee.getSalary());
            pstmt.executeUpdate();
            System.out.println("[Успех] Сотрудник добавлен: " + employee.getName());
        } catch (SQLException e) {
            System.err.println("[Ошибка] Не удалось добавить сотрудника: " + e.getMessage());
        }
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
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
                        rs.getDouble("salary")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[Ошибка] Не удалось получить список: " + e.getMessage());
        }
        return employees;
    }

    public Employee getEmployeeById(int id) {
        String sql = "SELECT * FROM employees WHERE id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Employee(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("age"),
                            // Позиция может быть null в БД, обрабатываем аккуратно
                            rs.getString("position") != null ? rs.getString("position") : "Не указана",
                            rs.getDouble("salary")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[Ошибка] Не удалось найти сотрудника с id " + id + ": " + e.getMessage());
        }
        return null;
    }

    public void updateEmployee(Employee employee) {
        String sql = "UPDATE employees SET name = ?, age = ?, position = ?, salary = ? WHERE id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, employee.getName());
            pstmt.setInt(2, employee.getAge());
            pstmt.setString(3, employee.getPosition());
            pstmt.setDouble(4, employee.getSalary());
            pstmt.setInt(5, employee.getId());
            pstmt.executeUpdate();
            System.out.println("[Успех] Данные сотрудника с ID " + employee.getId() + " обновлены.");
        } catch (SQLException e) {
            System.err.println("[Ошибка] Не удалось обновить данные: " + e.getMessage());
        }
    }

    public void deleteEmployee(int id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("[Успех] Сотрудник с ID " + id + " удален.");
        } catch (SQLException e) {
            System.err.println("[Ошибка] Не удалось удалить сотрудника: " + e.getMessage());
        }
    }
}
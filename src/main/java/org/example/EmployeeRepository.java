package org.example;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Employee> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM employees LIMIT 5",
                (rs, rowNum) -> {
                    Employee emp = new Employee();
                    emp.setEmpNo(rs.getInt("emp_no"));
                    emp.setFirstName(rs.getString("first_name"));
                    emp.setLastName(rs.getString("last_name"));
                    emp.setGender(rs.getString("gender"));
                    emp.setHireDate(rs.getDate("hire_date"));
                    return emp;
                }
        );
    }
}

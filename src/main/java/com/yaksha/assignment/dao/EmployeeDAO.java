package com.yaksha.assignment.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.yaksha.assignment.models.Employee;

public class EmployeeDAO {

	private JdbcTemplate jdbcTemplate;

	public EmployeeDAO(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// Insert Employee
	public void insertEmployee(Employee employee) {
		// Perform business validation (negative salary check)
		if (employee.getSalary() < 0) {
			throw new IllegalArgumentException("Salary cannot be negative");
		}
		String sql = "INSERT INTO Employee (name, department, salary) VALUES (?, ?, ?)";
		jdbcTemplate.update(sql, employee.getName(), employee.getDepartment(), employee.getSalary());
	}

	// Retrieve all employees
	public List<Employee> getAllEmployees() {
		String sql = "SELECT * FROM Employee";
		return jdbcTemplate.query(sql, new RowMapper<Employee>() {
			@Override
			public Employee mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
				Employee employee = new Employee();
				employee.setId(rs.getInt("id"));
				employee.setName(rs.getString("name"));
				employee.setDepartment(rs.getString("department"));
				employee.setSalary(rs.getDouble("salary"));
				return employee;
			}
		});
	}

	// Retrieve employee by ID
	public Employee getEmployeeById(int id) {
		String sql = "SELECT * FROM Employee WHERE id = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { id }, new RowMapper<Employee>() {
			@Override
			public Employee mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
				Employee employee = new Employee();
				employee.setId(rs.getInt("id"));
				employee.setName(rs.getString("name"));
				employee.setDepartment(rs.getString("department"));
				employee.setSalary(rs.getDouble("salary"));
				return employee;
			}
		});
	}
}
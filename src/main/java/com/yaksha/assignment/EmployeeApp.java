package com.yaksha.assignment;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.yaksha.assignment.config.AppConfig;
import com.yaksha.assignment.dao.EmployeeDAO;
import com.yaksha.assignment.models.Employee;

public class EmployeeApp {

	public static void main(String[] args) {
		// Create an application context based on AppConfig
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

		// Retrieve the EmployeeDAO bean
		EmployeeDAO employeeDAO = context.getBean(EmployeeDAO.class);

		// Create an employee and insert it into the database
		Employee employee = new Employee("John Doe", "IT", 50000);
		employeeDAO.insertEmployee(employee);

		// Retrieve all employees and display them
		System.out.println("Employees in database:");
		employeeDAO.getAllEmployees().forEach(emp -> System.out.println(emp));

		// Retrieve a specific employee by ID
		Employee retrievedEmployee = employeeDAO.getEmployeeById(1);
		System.out.println("Employee with ID 1: " + retrievedEmployee);

		// Retrieve all employees and display them
		System.out.println("Employees in database:");
		employeeDAO.getAllEmployees().forEach(emp -> System.out.println(emp));

		// Close the context
		context.close();
	}
}
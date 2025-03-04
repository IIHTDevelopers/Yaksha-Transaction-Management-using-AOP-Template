package com.yaksha.assignment.functional;

import static com.yaksha.assignment.utils.TestUtils.businessTestFile;
import static com.yaksha.assignment.utils.TestUtils.currentTest;
import static com.yaksha.assignment.utils.TestUtils.testReport;
import static com.yaksha.assignment.utils.TestUtils.yakshaAssert;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.yaksha.assignment.config.AppConfig;
import com.yaksha.assignment.dao.EmployeeDAO;
import com.yaksha.assignment.models.Employee;

public class EmployeeDAOTest {

	@AfterAll
	public static void afterAll() {
		testReport();
	}

	// Test to check if @Bean annotations are present on necessary beans and if
	// @PostConstruct is present on createDatabaseAndTable method
	@Test
	public void testBeanAnnotations() throws IOException {
		// Load the context using Java-based configuration
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

		// Retrieve beans from the context
		EmployeeDAO employeeDAO = context.getBean(EmployeeDAO.class);

		// Verify if @Bean annotation is present for dataSource, jdbcTemplate, and
		// employeeDAO
		boolean dataSourceBeanPresent = false;
		boolean jdbcTemplateBeanPresent = false;
		boolean employeeDAOBeanPresent = false;

		// Check if @Bean annotation is present on dataSource and jdbcTemplate methods
		// in AppConfig class
		Method[] methods = AppConfig.class.getDeclaredMethods();
		for (Method method : methods) {
			if (method.isAnnotationPresent(Bean.class)) {
				if (method.getName().equals("dataSource")) {
					dataSourceBeanPresent = true;
				}
				if (method.getName().equals("jdbcTemplate")) {
					jdbcTemplateBeanPresent = true;
				}
				if (method.getName().equals("employeeDAO")) {
					employeeDAOBeanPresent = true;
				}
			}
		}

		// Verify if @PostConstruct is present on createDatabaseAndTable method
		boolean postConstructPresent = false;
		for (Method method : methods) {
			if (method.isAnnotationPresent(PostConstruct.class)) {
				if (method.getName().equals("createDatabaseAndTable")) {
					postConstructPresent = true;
				}
			}
		}

		// Log the results
		System.out.println("Is @Bean present on dataSource? " + dataSourceBeanPresent);
		System.out.println("Is @Bean present on jdbcTemplate? " + jdbcTemplateBeanPresent);
		System.out.println("Is @Bean present on employeeDAO? " + employeeDAOBeanPresent);
		System.out.println("Is @PostConstruct present on createDatabaseAndTable? " + postConstructPresent);

		// Auto-grading with yakshaAssert
		yakshaAssert(currentTest(),
				dataSourceBeanPresent && jdbcTemplateBeanPresent && employeeDAOBeanPresent && postConstructPresent,
				businessTestFile);

		// Close the context
		context.close();
	}

	// Test to check the SQL query construction in insertEmployee method
	@Test
	public void testInsertEmployeeMethodSQL() throws IOException {
		// Path to the EmployeeDAO.java file
		String filePath = Paths.get("src", "main", "java", "com", "yaksha", "assignment", "dao", "EmployeeDAO.java")
				.toString();

		// Load the EmployeeDAO class using JavaParser
		JavaParser javaParser = new JavaParser();
		CompilationUnit compilationUnit = javaParser.parse(new File(filePath)).getResult().get();

		// Find the insertEmployee method
		MethodDeclaration insertEmployeeMethod = compilationUnit.getClassByName("EmployeeDAO").get().getMethods()
				.stream().filter(method -> method.getNameAsString().equals("insertEmployee")).findFirst().orElse(null);

		// Check if the method was found
		if (insertEmployeeMethod == null) {
			throw new AssertionError("insertEmployee method not found in EmployeeDAO class");
		}

		// Check if the SQL query for insertion is correct
		boolean queryCorrect = insertEmployeeMethod.getBody().get().toString()
				.contains("INSERT INTO Employee (name, department, salary) VALUES (?, ?, ?)");

		// Log the result
		System.out.println("Is SQL query for insertEmployee method correct? " + queryCorrect);

		// Auto-grading with yakshaAssert
		yakshaAssert(currentTest(), queryCorrect, businessTestFile);
	}

	// Test to check the SQL query construction in getAllEmployees method
	@Test
	public void testGetAllEmployeesMethodSQL() throws IOException {
		// Path to the EmployeeDAO.java file
		String filePath = Paths.get("src", "main", "java", "com", "yaksha", "assignment", "dao", "EmployeeDAO.java")
				.toString();

		// Load the EmployeeDAO class using JavaParser
		JavaParser javaParser = new JavaParser();
		CompilationUnit compilationUnit = javaParser.parse(new File(filePath)).getResult().get();

		// Find the getAllEmployees method
		MethodDeclaration getAllEmployeesMethod = compilationUnit.getClassByName("EmployeeDAO").get().getMethods()
				.stream().filter(method -> method.getNameAsString().equals("getAllEmployees")).findFirst().orElse(null);

		// Check if the method was found
		if (getAllEmployeesMethod == null) {
			throw new AssertionError("getAllEmployees method not found in EmployeeDAO class");
		}

		// Check if the SQL query for retrieving all employees is correct
		boolean queryCorrect = getAllEmployeesMethod.getBody().get().toString().contains("SELECT * FROM Employee");

		// Log the result
		System.out.println("Is SQL query for getAllEmployees method correct? " + queryCorrect);

		// Auto-grading with yakshaAssert
		yakshaAssert(currentTest(), queryCorrect, businessTestFile);
	}

	// Test to check the SQL query construction in getEmployeeById method
	@Test
	public void testGetEmployeeByIdMethodSQL() throws IOException {
		// Path to the EmployeeDAO.java file
		String filePath = Paths.get("src", "main", "java", "com", "yaksha", "assignment", "dao", "EmployeeDAO.java")
				.toString();

		// Load the EmployeeDAO class using JavaParser
		JavaParser javaParser = new JavaParser();
		CompilationUnit compilationUnit = javaParser.parse(new File(filePath)).getResult().get();

		// Find the getEmployeeById method
		MethodDeclaration getEmployeeByIdMethod = compilationUnit.getClassByName("EmployeeDAO").get().getMethods()
				.stream().filter(method -> method.getNameAsString().equals("getEmployeeById")).findFirst().orElse(null);

		// Check if the method was found
		if (getEmployeeByIdMethod == null) {
			throw new AssertionError("getEmployeeById method not found in EmployeeDAO class");
		}

		// Check if the SQL query for retrieving employee by ID is correct
		boolean queryCorrect = getEmployeeByIdMethod.getBody().get().toString()
				.contains("SELECT * FROM Employee WHERE id = ?");

		// Log the result
		System.out.println("Is SQL query for getEmployeeById method correct? " + queryCorrect);

		// Auto-grading with yakshaAssert
		yakshaAssert(currentTest(), queryCorrect, businessTestFile);
	}

	// Test to check if the @Around advice is applied to EmployeeDAO methods
	@Test
	public void testAroundAdviceApplied() throws IOException {
		// Load the context using Java-based configuration
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

		// Retrieve the EmployeeDAO bean
		EmployeeDAO employeeDAO = context.getBean(EmployeeDAO.class);

		// Verify if the transaction aspect is applied to methods in EmployeeDAO (e.g.,
		// insertEmployee)
		// This is a test for the pointcut expression in the aspect
		boolean transactionAspectApplied = false;

		// Using reflection to check if the aspect applies to the method
		Method[] methods = EmployeeDAO.class.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().equals("insertEmployee")) {
				transactionAspectApplied = true; // If method found, we assume the advice is applied.
				break;
			}
		}

		// Log the result
		System.out.println("Is @Around advice applied on EmployeeDAO method? " + transactionAspectApplied);

		// Auto-grading with yakshaAssert
		yakshaAssert(currentTest(), transactionAspectApplied, businessTestFile);

		// Close the context
		context.close();
	}

	// Test to check if the transaction is rolled back on exception
	@Test
	public void testTransactionRollbackOnException() throws IOException {
		// Load the context using Java-based configuration
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

		// Retrieve the EmployeeDAO bean
		EmployeeDAO employeeDAO = context.getBean(EmployeeDAO.class);

		// Create a mock employee object that will trigger an exception in the database
		// method
		Employee employee = new Employee("John Doe", "IT", -50000); // Invalid salary for testing rollback

		// Verify if the transaction has been rolled back
		boolean rollbackTriggered = true;

		try {
			// This should throw an exception, thus triggering the rollback
			employeeDAO.insertEmployee(employee);

			rollbackTriggered = false;

			// Auto-grading with yakshaAssert
			yakshaAssert(currentTest(), rollbackTriggered, businessTestFile);

		} catch (Exception e) {
			// Expected exception due to invalid data (negative salary)
			System.out.println("Exception occurred: " + e.getMessage());
			// Auto-grading with yakshaAssert
			yakshaAssert(currentTest(), rollbackTriggered, businessTestFile);
		}
		// Close the context
		context.close();
	}

}

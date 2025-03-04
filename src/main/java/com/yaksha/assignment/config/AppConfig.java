package com.yaksha.assignment.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import com.yaksha.assignment.dao.EmployeeDAO;

@Configuration
@EnableTransactionManagement
public class AppConfig {

	// Create and configure the data source to directly connect to employeedb
	@Bean
	public DataSource dataSource() {
		System.out.println("Initializing DataSource...");
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		// Directly connect to the employeedb database
		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataSource.setUrl(
				"jdbc:mysql://localhost:3306/employeedb?createDatabaseIfNotExist=true&useUnicode=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
		dataSource.setUsername("root");
		dataSource.setPassword("root");
		System.out.println("DataSource Initialized with URL: " + dataSource.getUrl());
		return dataSource;
	}

	// Create and configure JdbcTemplate using the data source
	@Bean
	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
		System.out.println("Initializing JdbcTemplate...");
		return new JdbcTemplate(dataSource);
	}

	// Ensure EmployeeDAO is available as a bean in the application context
	@Bean
	public EmployeeDAO employeeDAO(JdbcTemplate jdbcTemplate) {
		System.out.println("Creating EmployeeDAO bean...");
		return new EmployeeDAO(jdbcTemplate);
	}

	@Bean
	@Autowired
	public PlatformTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Bean
	@Autowired
	public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
		return new TransactionTemplate(transactionManager);
	}

	// Ensure the database and table are created at the very first step
	@PostConstruct
	public void createDatabaseAndTable() {
		System.out.println("Executing @PostConstruct: Creating Database and Table...");

		JdbcTemplate jdbcTemplate = jdbcTemplate(dataSource());

		// Create database query (it will not fail if already exists)
		String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS employeedb";
		// Create Employee table query
		String createTableSQL = "CREATE TABLE IF NOT EXISTS Employee (" + "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "name VARCHAR(255) NOT NULL, " + "department VARCHAR(255) NOT NULL, " + "salary DOUBLE NOT NULL)";

		try (Connection connection = jdbcTemplate.getDataSource().getConnection();
				Statement statement = connection.createStatement()) {

			// Logging the connection success
			System.out.println("Connected to the database successfully.");

			// Create the database if it does not exist
			System.out.println("Executing SQL: " + createDatabaseSQL);
			statement.executeUpdate(createDatabaseSQL);
			System.out.println("Database created or already exists.");

			// Create the Employee table if it doesn't exist
			System.out.println("Executing SQL: " + createTableSQL);
			statement.executeUpdate(createTableSQL);
			System.out.println("Table 'Employee' created or already exists.");

		} catch (SQLException e) {
			System.out.println("Error occurred during database or table creation.");
			e.printStackTrace();
		}
	}
}
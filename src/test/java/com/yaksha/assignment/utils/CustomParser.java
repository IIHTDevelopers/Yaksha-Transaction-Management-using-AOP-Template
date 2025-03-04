package com.yaksha.assignment.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;

public class CustomParser {

	/**
	 * Checks if the class contains the required class-level annotation.
	 * 
	 * @param filePath        - Full path to the class file.
	 * @param classAnnotation - The annotation to check for in the class
	 *                        (e.g., @SpringBootApplication).
	 * @return true if the class has the annotation, false otherwise.
	 * @throws IOException
	 */
	public static boolean checkClassAnnotation(String filePath, String classAnnotation) throws IOException {
		System.out.println("Checking class-level annotation in file: " + filePath);

		// Load class content
		String classContent = loadClassContent(filePath);
		if (classContent == null) {
			System.out.println("Error: Failed to load class content from file: " + filePath);
			return false;
		}

		// Parse the class content using JavaParser
		JavaParser javaParser = new JavaParser();
		Optional<CompilationUnit> optionalCompilationUnit = javaParser.parse(classContent).getResult();

		if (optionalCompilationUnit.isEmpty()) {
			System.out.println("Error: Failed to parse the class content from file: " + filePath);
			return false;
		}

		CompilationUnit compilationUnit = optionalCompilationUnit.get();

		// Check if the class contains the required annotation
		Optional<ClassOrInterfaceDeclaration> classDeclaration = compilationUnit.getClassByName(getClassName(filePath));

		if (classDeclaration.isEmpty()) {
			System.out.println("Error: Class not found in the provided file.");
			return false;
		}

		boolean hasClassAnnotation = classDeclaration.get().getAnnotations().stream()
				.anyMatch(annotation -> annotation.getNameAsString().equals(classAnnotation));

		if (!hasClassAnnotation) {
			System.out.println("Error: The class is missing the @" + classAnnotation + " annotation.");
			return false;
		}

		System.out.println("Class is annotated with @" + classAnnotation + " annotation.");
		return true;
	}

	/**
	 * Checks if any method contains the required method-level annotation.
	 * 
	 * @param filePath         - Full path to the class file.
	 * @param methodAnnotation - The annotation to check for in methods
	 *                         (e.g., @Autowired).
	 * @return true if any method contains the annotation, false otherwise.
	 * @throws IOException
	 */
	public static boolean checkMethodAnnotation(String filePath, String methodAnnotation) throws IOException {
		System.out.println("Checking method-level annotation in file: " + filePath);

		// Load class content
		String classContent = loadClassContent(filePath);
		if (classContent == null) {
			System.out.println("Error: Failed to load class content from file: " + filePath);
			return false;
		}

		// Parse the class content using JavaParser
		JavaParser javaParser = new JavaParser();
		Optional<CompilationUnit> optionalCompilationUnit = javaParser.parse(classContent).getResult();

		if (optionalCompilationUnit.isEmpty()) {
			System.out.println("Error: Failed to parse the class content from file: " + filePath);
			return false;
		}

		CompilationUnit compilationUnit = optionalCompilationUnit.get();

		// Check if any method contains the required annotation
		Optional<ClassOrInterfaceDeclaration> classDeclaration = compilationUnit.getClassByName(getClassName(filePath));

		if (classDeclaration.isEmpty()) {
			System.out.println("Error: Class not found in the provided file.");
			return false;
		}

		boolean hasMethodAnnotation = classDeclaration.get().getMethods().stream()
				.anyMatch(method -> method.getAnnotationByName(methodAnnotation).isPresent());

		if (!hasMethodAnnotation) {
			System.out.println("Error: No method is annotated with @" + methodAnnotation + " annotation.");
			return false;
		}

		System.out.println("Method is annotated with @" + methodAnnotation + " annotation.");
		return true;
	}

	/**
	 * Checks if any constructor contains the required constructor-level annotation.
	 * 
	 * @param filePath              - Full path to the class file.
	 * @param constructorAnnotation - The annotation to check for in constructors
	 *                              (e.g., @Autowired).
	 * @return true if any constructor contains the annotation, false otherwise.
	 * @throws IOException
	 */
	public static boolean checkConstructorAnnotation(String filePath, String constructorAnnotation) throws IOException {
		System.out.println("Checking constructor-level annotation in file: " + filePath);

		// Load class content
		String classContent = loadClassContent(filePath);
		if (classContent == null) {
			System.out.println("Error: Failed to load class content from file: " + filePath);
			return false;
		}

		// Parse the class content using JavaParser
		JavaParser javaParser = new JavaParser();
		Optional<CompilationUnit> optionalCompilationUnit = javaParser.parse(classContent).getResult();

		if (optionalCompilationUnit.isEmpty()) {
			System.out.println("Error: Failed to parse the class content from file: " + filePath);
			return false;
		}

		CompilationUnit compilationUnit = optionalCompilationUnit.get();

		// Check if any constructor contains the required annotation
		Optional<ClassOrInterfaceDeclaration> classDeclaration = compilationUnit.getClassByName(getClassName(filePath));

		if (classDeclaration.isEmpty()) {
			System.out.println("Error: Class not found in the provided file.");
			return false;
		}

		boolean hasConstructorAnnotation = classDeclaration.get().getConstructors().stream()
				.anyMatch(constructor -> constructor.getAnnotationByName(constructorAnnotation).isPresent());

		if (!hasConstructorAnnotation) {
			System.out.println("Error: No constructor is annotated with @" + constructorAnnotation + " annotation.");
			return false;
		}

		System.out.println("Constructor is annotated with @" + constructorAnnotation + " annotation.");
		return true;
	}

	/**
	 * Loads the content of the class file from the file path.
	 * 
	 * @param filePath Full path to the class file.
	 * @return The class content as a String.
	 * @throws IOException
	 */
	private static String loadClassContent(String filePath) throws IOException {
		// Create a File object from the provided file path
		File participantFile = new File(filePath);
		if (!participantFile.exists()) {
			System.out.println("Error: Class file not found: " + filePath);
			return null;
		}

		// Read the content of the file
		try (FileInputStream fileInputStream = new FileInputStream(participantFile)) {
			byte[] bytes = fileInputStream.readAllBytes();
			return new String(bytes, StandardCharsets.UTF_8);
		}
	}

	/**
	 * Checks if a field contains a specific annotation (e.g., @Autowired).
	 * 
	 * @param filePath   - Full path to the class file.
	 * @param fieldType  - The type of the field to check (e.g., "Order").
	 * @param annotation - The annotation to check for (e.g., "Autowired").
	 * @return true if the field has the annotation, false otherwise.
	 * @throws IOException
	 */
	public static boolean checkFieldAnnotation(String filePath, String fieldType, String annotation)
			throws IOException {
		System.out.println("Checking field-level annotation on field of type '" + fieldType + "' in file: " + filePath);

		// Load class content
		String classContent = loadClassContent(filePath);
		if (classContent == null) {
			System.out.println("Error: Failed to load class content from file: " + filePath);
			return false;
		}

		// Parse the class content using JavaParser
		JavaParser javaParser = new JavaParser();
		Optional<CompilationUnit> optionalCompilationUnit = javaParser.parse(classContent).getResult();

		if (optionalCompilationUnit.isEmpty()) {
			System.out.println("Error: Failed to parse the class content from file: " + filePath);
			return false;
		}

		CompilationUnit compilationUnit = optionalCompilationUnit.get();

		// Check if the class contains the required field annotation
		Optional<ClassOrInterfaceDeclaration> classDeclaration = compilationUnit.getClassByName(getClassName(filePath));

		if (classDeclaration.isEmpty()) {
			System.out.println("Error: Class not found in the provided file.");
			return false;
		}

		// Iterate over all fields in the class
		boolean hasFieldAnnotation = classDeclaration.get().getFields().stream()
				.flatMap(field -> field.getVariables().stream()) // Loop through variables of the field
				.filter(var -> var.getTypeAsString().equals(fieldType)) // Match field type (e.g., "Order")
				.anyMatch(field -> field.getParentNode().isPresent()
						&& field.getParentNode().get() instanceof FieldDeclaration
						&& ((FieldDeclaration) field.getParentNode().get()).getAnnotations().stream()
								.anyMatch(annotationNode -> annotationNode.getNameAsString().equals(annotation))); // Match
																													// annotation

		if (!hasFieldAnnotation) {
			System.out.println(
					"Error: The field of type '" + fieldType + "' is missing the @" + annotation + " annotation.");
			return false;
		}

		System.out.println("Field of type '" + fieldType + "' is annotated with @" + annotation + " annotation.");
		return true;
	}

	/**
	 * Checks if a constructor contains a specific annotation (e.g., @Value).
	 * 
	 * @param filePath              - Full path to the class file.
	 * @param constructorAnnotation - The annotation to check for in constructor
	 *                              parameters (e.g., "Value").
	 * @param parameterNames        - Names of the parameters to check.
	 * @return true if the constructor contains the annotation on the specified
	 *         parameters, false otherwise.
	 * @throws IOException
	 */
	public static boolean checkConstructorParameterAnnotation(String filePath, String constructorAnnotation,
			String... parameterNames) throws IOException {
		System.out.println(
				"Checking constructor parameters for annotation @" + constructorAnnotation + " in file: " + filePath);

		// Load class content
		String classContent = loadClassContent(filePath);
		if (classContent == null) {
			System.out.println("Error: Failed to load class content from file: " + filePath);
			return false;
		}

		// Parse the class content using JavaParser
		JavaParser javaParser = new JavaParser();
		Optional<CompilationUnit> optionalCompilationUnit = javaParser.parse(classContent).getResult();

		if (optionalCompilationUnit.isEmpty()) {
			System.out.println("Error: Failed to parse the class content from file: " + filePath);
			return false;
		}

		CompilationUnit compilationUnit = optionalCompilationUnit.get();

		// Check if the class contains the required constructor annotation
		Optional<ClassOrInterfaceDeclaration> classDeclaration = compilationUnit.getClassByName(getClassName(filePath));

		if (classDeclaration.isEmpty()) {
			System.out.println("Error: Class not found in the provided file.");
			return false;
		}

		// Iterate over constructors to find the matching annotations on parameters
		boolean hasConstructorAnnotation = classDeclaration.get().getConstructors().stream().anyMatch(constructor -> {
			// Check if constructor parameters have the specified annotation
			return constructor.getParameters().stream()
					.anyMatch(param -> parameterNames.length == 2 && param.getNameAsString().equals(parameterNames[0])
							&& param.getAnnotations().stream().anyMatch(
									annotation -> annotation.getNameAsString().equals(constructorAnnotation)));
		});

		if (!hasConstructorAnnotation) {
			System.out.println(
					"Error: The constructor is missing the @" + constructorAnnotation + " annotation on parameters.");
			return false;
		}

		System.out.println("Constructor parameters are annotated with @" + constructorAnnotation + " annotation.");
		return true;
	}

	/**
	 * Checks if any method contains the required method-level annotation with a
	 * specific value.
	 *
	 * @param filePath         - Full path to the class file.
	 * @param methodAnnotation - The annotation to check for in methods
	 *                         (e.g., @Qualifier).
	 * @param annotationValue  - The expected value of the annotation (e.g.,
	 *                         "payPalPaymentGateway").
	 * @return true if any method contains the annotation with the specific value,
	 *         false otherwise.
	 * @throws IOException
	 */
	public static boolean checkMethodAnnotationWithValue(String filePath, String methodAnnotation,
			String annotationValue) throws IOException {
		System.out.println("Checking method-level annotation with value in file: " + filePath);

		// Load class content
		String classContent = loadClassContent(filePath);
		if (classContent == null) {
			System.out.println("Error: Failed to load class content from file: " + filePath);
			return false;
		}

		// Parse the class content using JavaParser
		JavaParser javaParser = new JavaParser();
		Optional<CompilationUnit> optionalCompilationUnit = javaParser.parse(classContent).getResult();

		if (optionalCompilationUnit.isEmpty()) {
			System.out.println("Error: Failed to parse the class content from file: " + filePath);
			return false;
		}

		CompilationUnit compilationUnit = optionalCompilationUnit.get();

		// Check if any method contains the required annotation and value
		Optional<ClassOrInterfaceDeclaration> classDeclaration = compilationUnit.getClassByName(getClassName(filePath));

		if (classDeclaration.isEmpty()) {
			System.out.println("Error: Class not found in the provided file.");
			return false;
		}

		boolean hasMethodAnnotationWithValue = classDeclaration.get().getMethods().stream()
				.anyMatch(method -> method.getAnnotationByName(methodAnnotation).isPresent()
						&& method.getAnnotationByName(methodAnnotation).get().getChildNodes().stream()
								.anyMatch(node -> node.toString().contains(annotationValue)));

		if (!hasMethodAnnotationWithValue) {
			System.out.println("Error: No method is annotated with @" + methodAnnotation + " with value '"
					+ annotationValue + "'.");
			return false;
		}

		System.out.println("Method is annotated with @" + methodAnnotation + " with value '" + annotationValue + "'.");
		return true;
	}

	/**
	 * Extracts the class name from the file path (assumes the class name is the
	 * same as the file name).
	 * 
	 * @param filePath The path to the Java file.
	 * @return The class name (without package).
	 */
	private static String getClassName(String filePath) {
		// Extract class name from file path (assumes Java file name matches the class
		// name)
		String fileName = new File(filePath).getName();
		return fileName.substring(0, fileName.lastIndexOf('.'));
	}

	/**
	 * Checks if a specific method contains a parameter annotated with @Qualifier
	 * and with a specific value.
	 *
	 * @param filePath        - Full path to the class file.
	 * @param methodName      - The name of the method to check for the annotation.
	 * @param annotation      - The annotation to check for (e.g., "Qualifier").
	 * @param annotationValue - The expected value of the annotation (e.g.,
	 *                        "payPalPaymentGateway").
	 * @return true if the specified method contains a parameter with the @Qualifier
	 *         annotation and correct value, false otherwise.
	 * @throws IOException
	 */
	public static boolean checkMethodParameterAnnotationWithValue(String filePath, String methodName, String annotation,
			String annotationValue) throws IOException {
		System.out.println("Checking if method '" + methodName + "' has a parameter annotated with @" + annotation
				+ " with value: " + annotationValue);

		// Load class content
		String classContent = loadClassContent(filePath);
		if (classContent == null) {
			System.out.println("Error: Failed to load class content from file: " + filePath);
			return false;
		}

		// Parse the class content using JavaParser
		JavaParser javaParser = new JavaParser();
		Optional<CompilationUnit> optionalCompilationUnit = javaParser.parse(classContent).getResult();

		if (optionalCompilationUnit.isEmpty()) {
			System.out.println("Error: Failed to parse the class content from file: " + filePath);
			return false;
		}

		CompilationUnit compilationUnit = optionalCompilationUnit.get();

		// Check if the class contains the specified method
		Optional<ClassOrInterfaceDeclaration> classDeclaration = compilationUnit.getClassByName(getClassName(filePath));

		if (classDeclaration.isEmpty()) {
			System.out.println("Error: Class not found in the provided file.");
			return false;
		}

		// Check if the specified method contains a parameter annotated with @Qualifier
		// and the correct value
		boolean hasQualifierAnnotation = classDeclaration.get().getMethodsByName(methodName).stream()
				.flatMap(method -> method.getParameters().stream()) // Loop through method parameters
				.anyMatch(param -> param.getAnnotations().stream() // Check if parameter has the @Qualifier annotation
						.anyMatch(annotationNode -> annotationNode.getNameAsString().equals(annotation)
								&& annotationNode.getChildNodes().stream()
										.anyMatch(node -> node.toString().contains(annotationValue))));

		if (!hasQualifierAnnotation) {
			System.out.println("Error: The method '" + methodName + "' does not have a parameter annotated with @"
					+ annotation + " with value '" + annotationValue + "'.");
			return false;
		}

		System.out.println("Method '" + methodName + "' has a parameter annotated with @" + annotation + " with value '"
				+ annotationValue + "'.");
		return true;
	}

}

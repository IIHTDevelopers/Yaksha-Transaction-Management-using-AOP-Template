package com.yaksha.assignment.aspects;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@Aspect
public class TransactionAspect {

	private final TransactionTemplate transactionTemplate;

	public TransactionAspect(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	// Pointcut to apply to methods that require transactional behavior
	@Pointcut("execution(* com.yaksha.assignment.dao.EmployeeDAO.*(..))")
	public void employeeDAOOperations() {
	}

	// Around advice to handle transaction commit or rollback
	@Around("employeeDAOOperations()")
	public Object manageTransaction(org.aspectj.lang.ProceedingJoinPoint joinPoint) throws Throwable {
		return transactionTemplate.execute(new TransactionCallback<Object>() {
			@Override
			public Object doInTransaction(TransactionStatus status) {
				try {
					// Proceed with method execution
					Object result = joinPoint.proceed();
					System.out.println("Transaction committed successfully.");
					return result;
				} catch (Throwable ex) {
					// Handle exception and rollback
					System.out.println("Exception occurred, rolling back transaction.");
					status.setRollbackOnly();
					throw new RuntimeException("Transaction failed, rolled back.", ex);
				}
			}
		});
	}
}

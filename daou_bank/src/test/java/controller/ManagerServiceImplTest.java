package controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import exception.EmployeeCreationFailException;


class ManagerServiceImplTest {
	
	class ChildClass {
		
		public void myMethod() throws EmployeeCreationFailException {
			try {
				throw new EmployeeCreationFailException("정신차리자");
			} finally {	
				System.out.println("현영아");
			}
			
		}
	}
	
	@Test
	void test() {
		ChildClass mine = new ChildClass();
		try {
			mine.myMethod();
		} catch (EmployeeCreationFailException e) {
			e.printStackTrace();
		}
	}

}

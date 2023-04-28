package controller;

import dto.UserDTO;
import exception.DeleteEmployeeFailException;
import exception.EmployeeCreationFailException;
import exception.HandOverManagerException;

public interface ManagerService {
	public abstract int registerEmployee (UserDTO user) throws EmployeeCreationFailException;
	
	public abstract int handOverManager (UserDTO user, String targetEmployee) throws HandOverManagerException;
	
	
	public abstract int deleteEmployee (String targetEmployee) throws DeleteEmployeeFailException;
}

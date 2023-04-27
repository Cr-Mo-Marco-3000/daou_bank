package controller;

import dto.UserDTO;

public interface ManagerService {
	public abstract int registerEmployee (UserDTO user);
	
	public abstract int handOverManager (UserDTO user);
	
	public int handOverManager (UserDTO user, String targetEmployee);
}

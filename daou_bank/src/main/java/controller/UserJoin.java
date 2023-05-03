package controller;

import java.util.List;

import dto.AccountDTO;
import dto.UserDTO;

public interface UserJoin {
	
	public void userSignup();
	public void userLogin(UserDTO loginedUser);
}
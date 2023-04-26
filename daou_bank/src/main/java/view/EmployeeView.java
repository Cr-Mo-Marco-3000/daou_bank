package view;

import java.util.Scanner;

import controller.ManagerServiceImpl;
import dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeView {
	private UserDTO loginedUser; 	// 로그인 한 후, 로그인한 유저정보를 EmployeeView에 넣어놓고 사용합니다.
	
	public void EmployeeMenu () {
		Scanner scan = new Scanner(System.in);
		ManagerServiceImpl impl = new ManagerServiceImpl();
		impl.registerEmployee(loginedUser);
	}
}

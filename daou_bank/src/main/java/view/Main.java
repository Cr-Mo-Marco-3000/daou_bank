package view;

import java.util.Scanner;

import dto.UserDTO;

public class Main {
	
	public static void main(String[] args) {
		
		// 임시 유저
		UserDTO tempUser = new UserDTO(1, "bizyoung93", "123123", "Employee", "김현영", 20);
		
		Scanner scan = new Scanner(System.in);
		System.out.println("임시 메뉴를 입력하세요(1: 관리자 로그인, 2: 고객 로그인, 0: 종료)");
		int select = scan.nextInt();
		
		if (select == 1) {
			EmployeeView empView = new EmployeeView(tempUser);
			empView.EmployeeMenu();
		} else if (select == 2) {
			System.out.println("고객 로그인은 종오가 구현합니다.");
		} else if (select == 0) {
			System.out.println("서비스를 종료합니다.");
			System.exit(0);
		}
		
		
	}
}
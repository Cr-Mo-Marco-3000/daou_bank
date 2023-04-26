package view;

import java.util.Scanner;

import controller.UserATM_Impl;
import controller.UserJoin_Impl;
import model.BankAccount;
import model.User;

public class MenuImpl implements Menu {

	public static Scanner scan = new Scanner(System.in);
	
	private static MenuImpl menu = new MenuImpl();
	public static UserATM_Impl userImpl = new UserATM_Impl();
	public static UserJoin_Impl userJoin = new UserJoin_Impl();
	
	public static MenuImpl getInstance() {
		if(menu == null) {
			menu = new MenuImpl();
		}		
		return menu;
	}	
	
	@Override
	public void init() {
		User user = new User();
		user.getUserFile();
		
		BankAccount bank = new BankAccount();
		bank.getBankFile();
		loginMenu();
	}

	@Override
	public void loginMenu() {
		
		while(true) {
		
		System.out.println("");
		System.out.println("  ┏━━━━* Bank U ━━━━┓");
		System.out.println("  ┃                 *");
		System.out.println("  ┃ 1) 로그인");
		System.out.println("  ┃ 2) 회원가입");
		System.out.println("  ┃ 3) 회원목록 - test");
		System.out.println("  ┃ 0) 종료하기");
		System.out.println("  ┃     ");
		System.out.print("  ┃ 메뉴 입력 : ");
		String menu = scan.next();
		System.out.println("  ┃                 *");
		System.out.println("  ┗━━━━━━━━━━━━━━━━━┛\n");
		
			switch(menu) {
			
			case ("1"):
				userJoin.userLogin();
				break;
				
			case ("2"):
				userJoin.userJoin();
				break;
				
			case ("3"):
				userJoin.userList();
				break;
				
			case ("0"): 
				System.out.println("Good Bye *");	
				System.exit(0);return;
				
			default:
				System.out.println("다시 입력해주세요 :)");
			}	
		}
	}
	@Override
	public void userView() {
	
			while(true) {
			System.out.println("");
			System.out.println("  ┏━━━━* Bank U ━━━━┓");
			System.out.println("  ┃                 *");
			System.out.println("  ┃ 1) 계좌조회");
			System.out.println("  ┃ 2) 입금 ");
			System.out.println("  ┃ 3) 출금 ");
			System.out.println("  ┃ 4) 계좌이체");
			System.out.println("  ┃ 5) 통장정리");
			System.out.println("  ┃ 0) 로그아웃");
			System.out.println("  ┃     ");
			System.out.print("  ┃ 메뉴 입력 : ");
			String menu = scan.next();
			System.out.println("  ┃                 *");
			System.out.println("  ┗━━━━━━━━━━━━━━━━━┛\n");
			
			switch(menu) {
				
			case ("1"):
				userImpl.userBalance();
				break;
					
			case ("2"):
				userImpl.userDeposit();
				break;
			
			case ("3"):
				userImpl.userWithdraw();
				break;
				
			case ("4"):
				userImpl.userTransfer();
				break;
				
			case ("5"):
				userImpl.userHistory();
				break;
				
			case ("0"): 
				System.out.println("로그아웃 합니다.");
				loginMenu();
				break;
				
			default:
				System.out.println("없는 메뉴를 선택하셨습니다");
				userView();
			}	
		}
	}
}
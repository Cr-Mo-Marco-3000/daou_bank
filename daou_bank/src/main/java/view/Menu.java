package view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import controller.UserATM_Impl;
import controller.UserJoin_Impl;
import dto.AccountDTO;
import dto.UserDTO;
import model.BankAccount;
import model.User;

public class Menu {

	public static Scanner scan = new Scanner(System.in);
	
	public static UserATM_Impl userImpl = new UserATM_Impl();
	public static UserJoin_Impl userJoin = new UserJoin_Impl();
	
	// 로그인한 유저 데이터 담는 객체 생성
	private static Map<Integer, UserDTO> login_user_map;
	private static List<AccountDTO> login_user_account_list;
	

	
	// 싱글톤
	private static Menu menu = new Menu();
	public static Menu getInstance() {
		if(menu == null) {
			menu = new Menu();
		}		
		return menu;
	}	
	
	// 진입점
	public void init() {
		loginMenu();
	}

	public void loginMenu() {
		
//		UserDTO tempUser = new UserDTO(1, 
//				"bizyoung93", "123123", "Manager", "김현영", "1993-03-29");
		while(true) {
		
			System.out.println("");
			System.out.println("\t┏━━━* Daou_Bank ATM ━━━━┓");
			System.out.println("\t┃			┃");
			System.out.println("\t┗━━━━━━━━━━━━━━━━━━━━━━━┛");
			System.out.println("\t  ┃		      ┃");
			System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  *");
			System.out.println("\t  ┃ 1) 로그인");			
			System.out.println("\t  ┃ 2) 회원가입");
			System.out.println("\t  ┃ 0) 종료하기");
			System.out.println("\t  ┃     ");
			System.out.print("\t  ┃ 메뉴 입력 : ");
			String menu = scan.next();
			System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  ┃");
			System.out.println("\t  ┃                   *");
			System.out.println("\t  ┗━━━━━━━━━━━━━━━━━━━┛\n");
			System.out.println("");
		
		
			switch(menu) {
			
				case ("1"):
					userJoin.userLogin();
					break;
					
				case ("2"):
					userJoin.userSignup();
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
		
		// 일반 유저가 로그인하면, 보이는 메뉴입니다. 
		public void userView(Map<Integer, UserDTO> login_user_map, List<AccountDTO> login_user_account_list) {
					
				while(true) {
					System.out.println("");
					System.out.println("\t┏━━━* Daou_Bank ATM ━━━━┓");
					System.out.println("\t┃			┃");
					System.out.println("\t┗━━━━━━━━━━━━━━━━━━━━━━━┛");
					System.out.println("\t  ┃		      ┃");
					System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  *");
					System.out.println("\t  ┃ 1) 계좌조회");
					System.out.println("\t  ┃ 2) 입금 ");
					System.out.println("\t  ┃ 3) 출금 ");
					System.out.println("\t  ┃ 4) 계좌이체");
					System.out.println("\t  ┃ 5) 통장정리");
					System.out.println("\t  ┃ 0) 로그아웃");
					System.out.println("\t  ┃     ");
					System.out.print("\t  ┃ 메뉴 입력 : ");
					String menu = scan.next();
					System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  ┃");
					System.out.println("\t  ┃                   *");
					System.out.println("\t  ┗━━━━━━━━━━━━━━━━━━━┛\n");
					System.out.println("");
				
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
							userView(login_user_map,login_user_account_list);
					}	
				}
		}
		
		// 일반 직원이 로그인하면 보이는 뷰
		public void EmployeeView() {
			while(true) {
				System.out.println("");
				System.out.println("\t┏━━━* Daou_Bank ATM ━━━━┓");
				System.out.println("\t┃			┃");
				System.out.println("\t┗━━━━━━━━━━━━━━━━━━━━━━━┛");
				System.out.println("\t  ┃		      ┃");
				System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  *");
				System.out.println("\t  ┃ 1) 계좌조회");
				System.out.println("\t  ┃ 2) 입금 ");
				System.out.println("\t  ┃ 3) 출금 ");
				System.out.println("\t  ┃ 4) 계좌이체");
				System.out.println("\t  ┃ 5) 통장정리");
				System.out.println("\t  ┃ 0) 로그아웃");
				System.out.println("\t  ┃     ");
				System.out.print("\t  ┃ 메뉴 입력 : ");
				String menu = scan.next();
				System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  ┃");
				System.out.println("\t  ┃                   *");
				System.out.println("\t  ┗━━━━━━━━━━━━━━━━━━━┛\n");
				System.out.println("");
			
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
						EmployeeView();
				}	
			}
	}
		
		
		
}
package view;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import controller.ManagerServiceImpl;
import controller.UserATM_Impl;
import controller.UserJoin_Impl;
import dto.AccountDTO;
import dto.UserDTO;
import exception.EmployeeCreationFailException;
import exception.HandOverManagerException;


public class Menu {

	public static Scanner scan = new Scanner(System.in);
	
	public static UserATM_Impl userImpl = new UserATM_Impl();
	public static UserJoin_Impl userJoin = new UserJoin_Impl();
	
	// 로그인한 유저 데이터 담는 객체 생성
	private static UserDTO loginedUser;
	private static List<AccountDTO> login_User_account_list;

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
		

		while(true) {
			System.out.println("");
			System.out.println("\t┏━━━* Daou_Bank ATM ━━━━┓");
			System.out.println("\t┃			┃");
			System.out.println("\t┗━━━━━━━━━━━━━━━━━━━━━━━┛");
			System.out.println("\t  ┃		      ┃");
			System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  *");
			System.out.println("\t  ┃ 1) 로그인");			
			System.out.println("\t  ┃ 2) 회원가입");
			System.out.println("\t  ┃ 3) 관리자 로그인(임시)");
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
					userJoin.userLogin(loginedUser,login_User_account_list);

					break;
					
				case ("2"):
					userJoin.userSignup();
					break;
					
				case ("3"):
					loginedUser = new UserDTO(
							1, "bizyoung93", "123123", "Manager", "김현영", "1993/03/29");
					EmployeeView();
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

		public void userView(UserDTO userdto, List<AccountDTO> account_list) {

			loginedUser = userdto;
			login_User_account_list = account_list;
				
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
				System.out.println("\t  ┃ 6) 계좌 개설 요청");
				System.out.println("\t  ┃ 7) 마이 페이지");
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
						
					case ("6"):
						userImpl.createAccount(loginedUser.getUser_key());
						break;
						
					case ("7"):
						userImpl.showInfo(loginedUser,login_User_account_list);
					break;
					
					case ("0"): 
						System.out.println("로그아웃 합니다.");
						loginedUser = null;
						login_User_account_list = null;
						loginMenu();
						break;
					default:
						System.out.println("없는 메뉴를 선택하셨습니다");
						userView(loginedUser,login_User_account_list);
				}	
			}
		}
		
		// 직원이 로그인하면 보이는 뷰
		public void EmployeeView() {
			while(true) {
				System.out.println("");
				System.out.println("\t┏━━━* Daou_Bank ATM ━━━━┓");
				System.out.println("\t┃			┃");
				System.out.println("\t┗━━━━━━━━━━━━━━━━━━━━━━━┛");
				System.out.println("\t  ┃		      ┃");
				System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  *");
				System.out.println("\t  ┃ 1) 고객 정보 열람");
				System.out.println("\t  ┃ 2) 계좌 생성 요구 조회");
				// 직원이 매니저일 경우, 해당 메뉴들이 보입니다.
				if (loginedUser.getType() == "Manager") {
					System.out.println("\t  ┃ 3) 직원 등록");
					System.out.println("\t  ┃ 4) 관리자 권한 인계");
					System.out.println("\t  ┃ 5) 직원 삭제");
				}
				System.out.println("\t  ┃ 0) 로그아웃");
				System.out.println("\t  ┃     ");
				System.out.print("\t  ┃ 메뉴 입력 : ");
				int menu = scan.nextInt();
				System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  ┃");
				System.out.println("\t  ┃                   *");
				System.out.println("\t  ┗━━━━━━━━━━━━━━━━━━━┛\n");
				System.out.println("");
				
				if (menu == 1) {
					System.out.println("고객 정보 열람을 선택하셨습니다.");
				} else if (menu == 2) {
					System.out.println("계좌 생성 요구 조회를 선택하셨습니다.");
				} else if (menu == 3) {
					System.out.println("직원 등록을 선택하셨습니다.");
					ManagerServiceImpl service = new ManagerServiceImpl();
					try {
						service.registerEmployee(loginedUser);
					} catch (EmployeeCreationFailException e) {
						System.out.println(e.getMessage());
					}
				} else if (menu == 4) {
					System.out.println("관리자 권한 인계를 선택하셨습니다.");
					ManagerServiceImpl service = new ManagerServiceImpl();
					String targetEmployee = "bluefri0329";
					try {
						service.handOverManager(loginedUser, targetEmployee);
					} catch (HandOverManagerException e) {
						System.out.println(e.getMessage());
					}
				} else if (menu == 5) {
					System.out.println("직원 삭제를 선택하셨습니다.");
				} else if (menu == 0) {
					System.out.println("로그아웃을 선택하셨습니다.");
					loginedUser = null;
					login_User_account_list = null;
					loginMenu();
					return;
				} else {
					System.out.println("없는 메뉴를 선택하셨습니다. 다시 입력해 주세요");
				}

			}
		}
		
		
		
		
		
}
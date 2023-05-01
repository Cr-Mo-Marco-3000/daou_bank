package view;

import java.io.Console;
import java.util.List;
import java.util.Scanner;

import org.apache.ibatis.session.SqlSession;

import controller.EmployeeServiceImpl;
import controller.ManagerServiceImpl;
import controller.UserATM_Impl;
import controller.UserJoin_Impl;
import dto.AccountDTO;
import dto.UserDTO;
import exception.AccountRequestNotFoundException;
import exception.AllCustomerInfocheckFailException;
import exception.CustomerAccountApprovalException;
import exception.CustomerAccountRejectionException;
import exception.CustomerEnrolFailException;
import exception.CustomerInfocheckFailException;
import exception.DeleteEmployeeFailException;
import exception.DuplicateCustomerException;
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
					// 임시 회원 => 나중에 삭제 요망
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
						System.out.println("\t  ┃ 1) 계좌 생성 요구 조회");
						System.out.println("\t  ┃ 2) 고객 등록");
						System.out.println("\t  ┃ 3) 고객 정보 열람");
						System.out.println("\t  ┃ 4) 고객 계좌 생성 승인");
						System.out.println("\t  ┃ 5) 고객 계좌 생성 거절");
						System.out.println("\t  ┃ 6) 전체 회원 정보 조회");
						// 직원이 매니저일 경우, 해당 메뉴들이 보입니다.
						if (loginedUser.getType() == "Manager") {
							System.out.println("\t  ┃ 7) 직원 등록");
							System.out.println("\t  ┃ 8) 관리자 권한 인계");
							System.out.println("\t  ┃ 9) 직원 삭제");
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
						    System.out.println("계좌 생성 요구 조회를 선택하셨습니다.");
						    EmployeeServiceImpl service = new EmployeeServiceImpl();
						    String isTemporary = scan.nextLine(); // isTemporary 변수에 값을 할당
						    try {
						        boolean isTemp = "1".equals(isTemporary) ? true : false;
						        service.getAccountRequests(isTemp ? "1" : "0"); // isTemp 값에 따라서 "temporary" 또는 "regular"을 인자로 전달
						    } catch (AccountRequestNotFoundException e) {
						        System.out.println(e.getMessage());
						    } catch (Exception e) {
						        System.out.println("잘못된 입력값입니다. 1 또는 0을 입력해주세요.");
						    }
						} else if (menu == 2) {
							System.out.println("고객 등록을 선택하셨습니다.");
							EmployeeServiceImpl service = new EmployeeServiceImpl();
							String user_id = scan.nextLine(); // user_id 변수에 값을 할당
							UserDTO user = new UserDTO(user_id); // UserDTO 객체 생성 후 user_id 값을 설정
							try {
								service.registerCustomer(user);
							} catch (CustomerEnrolFailException e) {
								System.out.println(e.getMessage());
							} catch (DuplicateCustomerException e) {
								System.out.println(e.getMessage()); 
							}
						
						} else if (menu == 3) {
							System.out.println("고객 정보 열람을 선택하셨습니다.");
							EmployeeServiceImpl service = new EmployeeServiceImpl();
							String user_id = scan.nextLine(); // user_id 변수에 값을 할당
							try {
								service.getCustomerById(user_id);
							} catch (CustomerInfocheckFailException e) {
								System.out.println(e.getMessage());
							}
						} else if (menu == 4) {
							System.out.println("고객 계좌 생성 승인을 선택하셨습니다.");
							EmployeeServiceImpl service = new EmployeeServiceImpl();
							String user_id = scan.nextLine(); // user_id 변수에 값을 할당
							try {
								service.approveCustomer(user_id);
							} catch (CustomerAccountApprovalException e) {
								System.out.println(e.getMessage());
							}
						} 
						else if (menu == 5) {
							System.out.println("고객 계좌 생성 거절을 선택하셨습니다.");
							String user_id = scan.nextLine(); // user_id 변수에 값을 할당
							EmployeeServiceImpl service = new EmployeeServiceImpl();
							try {
								service.rejectAccountRequest(user_id);
							} catch (CustomerAccountRejectionException  e) {
								System.out.println(e.getMessage());
							}
							
						} else if (menu == 6) {
							System.out.println("전체 회원 정보 조회을 선택하셨습니다.");
							EmployeeServiceImpl service = new EmployeeServiceImpl();
							try {
								service.getAllCustomers();
							} catch (AllCustomerInfocheckFailException e) {
								System.out.println(e.getMessage());
							}
						} else if (menu == 7) {
							System.out.println("직원 등록을 선택하셨습니다.");
							ManagerServiceImpl service = new ManagerServiceImpl();
							// 아이디
							String user_id = "";
							int checkNum = 2;
							while (checkNum == 2 || checkNum == 1) {
								if (checkNum == 2) {
									System.out.println("직원 아이디를 입력해주세요");
								} else { 
									System.out.println("중복되는 아이디가 존재합니다. 다시 입력해주세요");
								}
								user_id = scan.next();
								try {
									checkNum = service.isDuplicatedEmployee(user_id);
								} catch (EmployeeCreationFailException e) {
									System.out.println(e.getMessage());
								}
							};
							// 비밀번호
							Console console = System.console();
							String user_password = "";
							String user_password_confirm = "";
							int flag = 1;
							if (console == null) {			// eclipse로 실행했을 때 => console이 null로 들어감
								do {
									if (flag == 0) System.out.println("비밀번호가 일치하지 않습니다. 다시 입력해주세요");
									System.out.println("직원의 비밀번호를 입력해주세요");
									user_password = scan.next();
									System.out.println("직원의 비밀번호 확인을 입력해주세요");
									user_password_confirm = scan.next();
									flag = 0;
								} while (!user_password.equals(user_password_confirm));
							} else {
								do {
									if (flag == 0) System.out.println("비밀번호가 일치하지 않습니다. 다시 입력해주세요");
									user_password = new String(console.readPassword("직원의 비밀번호를 입력해주세요"));
									user_password_confirm = new String(console.readPassword("비밀번호 확인을 입력해주세요"));
									flag = 0;
								} while (!user_password.equals(user_password_confirm));
							}
							//이름
							System.out.println("직원의 이름을 입력해주세요");
							String user_name = scan.next();
							// 생일
							flag = 1;
							String user_birth_day ="";
							do {
								if (flag == 0) System.out.println("형식이 일치하지 않습니다. 다시 입력해주세요");
								System.out.println("직원의 생일을 입력해주세요(8자리)");
								user_birth_day = scan.next();
								flag = 1;
								// 조건을 만족하지 않았을 때 들어감 => 정규표현식
								if (!user_birth_day.matches("^[0-9]{8}$")) {
									flag = 0;
								}
							} while (flag == 0);
							
							UserDTO user = new UserDTO(-1, user_id, user_password, "Employee", user_name, user_birth_day);
							try {
								service.registerEmployee(user);
							} catch (EmployeeCreationFailException e) {
								System.out.println(e.getMessage());
							}
						} else if (menu == 8) {
							System.out.println("관리자 권한 인계를 선택하셨습니다.");
							ManagerServiceImpl service = new ManagerServiceImpl();
							System.out.println("인계하시려는 직원 아이디를 입력해주세요");
							String targetEmployee = scan.next();
							try {
								service.handOverManager(loginedUser, targetEmployee);
							} catch (HandOverManagerException e) {
								System.out.println(e.getMessage());
							}
							
						} else if (menu == 9) {
							System.out.println("직원 삭제를 선택하셨습니다.");
							ManagerServiceImpl service = new ManagerServiceImpl();
							System.out.println("삭제하시려는 직원 아이디를 입력해주세요");
							String targetEmployee = scan.next();
							try {
								service.deleteEmployee(targetEmployee);
							} catch (DeleteEmployeeFailException e) {
								System.out.println(e.getMessage());
							}
						}  else if (menu == 0) {
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
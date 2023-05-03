package view;

import java.io.Console;
import java.util.List;
import java.util.Scanner;

import controller.EmployeeServiceImpl;
import controller.ManagerServiceImpl;
import controller.UserATM_Impl;
import controller.UserJoin_Impl;
import dto.AccountDTO;
import dto.UserDTO;
import exception.AccountRequestNotFoundException;
import exception.CustomerAccountApprovalException;
import exception.CustomerAccountRejectionException;
import exception.CustomerInfocheckFailException;
import exception.DeleteEmployeeFailException;
import exception.EmployeeCreationFailException;
import exception.HandOverManagerException;


public class Menu {

	public static Scanner scan = new Scanner(System.in);
	public static UserATM_Impl userImpl = new UserATM_Impl();
	public static UserJoin_Impl userJoin = new UserJoin_Impl();
	
	// 로그인한 유저 데이터 담는 객체 생성
	private static UserDTO loginedUser = new UserDTO();

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
			System.out.println("\t┏━━━━━━━━━* Daou_Bank ATM ━━━━━━━━━┓");
			System.out.println("\t┃	      	 	      	   ┃");
			System.out.println("\t┃	        	   	   ┃");
			System.out.println("\t┃	   			   ┃");
			System.out.println("\t┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
			System.out.println("\t  ┃		                 ┃");
			System.out.println("\t  ┃  ━━━━━━━━━━━━━━━━━━━━━━━━━━  *");
			System.out.println("\t  ┃             *");
			System.out.println("\t  ┃ 1) 로그인");			
			System.out.println("\t  ┃ 2) 회원가입");
			System.out.println("\t  ┃ 0) 종료하기");
			System.out.println("\t  ┃     ");
			System.out.print("\t  ┃ 메뉴 입력 : ");
			String menu = scan.next();
			System.out.println("\t  ┃                 ");
			System.out.println("\t  ┃  ━━━━━━━━━━━━━━━━━━━━━━━━━━  ┃");
			System.out.println("\t  ┃                            * ┃");
			System.out.println("\t  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛\n");
			System.out.println("");
		
		
			switch(menu) {
			
				case ("1"):
					userJoin.userLogin(loginedUser);
					break;
					
				case ("2"):
					userJoin.userSignup();
					break;
					
				case ("0"): 
					loginedUser = null;
					System.out.println("Good Bye *");	
					System.exit(0);return;
					
				default:
					System.out.println("다시 입력해주세요 :)");
			}	
		
		}
	}
		
	// 일반 유저가 로그인하면, 보이는 메뉴입니다. 
	public void userView(UserDTO userdto) {

		loginedUser = userdto;
			
		while(true) {
			System.out.println("");
			System.out.println("\t┏━━━━━━━━━* Daou_Bank ATM ━━━━━━━━━┓");
			System.out.println("\t┃	      	 	      	   ┃");
			System.out.println("\t┃	        	   	   ┃");
			System.out.println("\t┃	   			   ┃");
			System.out.println("\t┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
			System.out.println("\t  ┃		                 ┃");
			System.out.println("\t  ┃  ━━━━━━━━━━━━━━━━━━━━━━━━━━  *");
			System.out.println("\t  ┃             *");
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
			System.out.println("\t  ┃                 ");
			System.out.println("\t  ┃  ━━━━━━━━━━━━━━━━━━━━━━━━━━  ┃");
			System.out.println("\t  ┃                            * ┃");
			System.out.println("\t  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛\n");
			System.out.println("");
		
			switch(menu) {
				case ("1"):
					userImpl.userBalance(loginedUser);
					break;
						
				case ("2"):
					userImpl.userDeposit(loginedUser);
					break;
				
				case ("3"):
					userImpl.userWithdraw(loginedUser);
					break;
				case ("4"):
					userImpl.userTransfer(loginedUser);
					break;
					
				case ("5"):
					userImpl.userHistory(loginedUser);
					break;
					
				case ("6"):
					userImpl.createAccount(loginedUser.getUser_key());
					break;
					
				case ("7"):
					userImpl.showInfo(loginedUser);
					break;
				
				case ("0"): 
					System.out.println("\t로그아웃 합니다.");
					loginedUser = null;
					loginMenu();
					break;
					
				default:
					System.out.println("\t없는 메뉴를 선택하셨습니다");
					userView(loginedUser);
			}	
		}
	} 
	 	
	// 직원이 로그인하면 보이는 뷰
	public void EmployeeView(UserDTO userdto) {
		loginedUser = userdto;
		while(true) {
			System.out.println("");
			System.out.println("\t┏━━━━━━━━━* Daou_Bank ATM ━━━━━━━━━┓");
			System.out.println("\t┃	      	 	      	   ┃");
			System.out.println("\t┃	        	   	   ┃");
			System.out.println("\t┃	   			   ┃");
			System.out.println("\t┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
			System.out.println("\t  ┃		                 ┃");
			System.out.println("\t  ┃  ━━━━━━━━━━━━━━━━━━━━━━━━━━  *");
			System.out.println("\t  ┃             *");
			System.out.println("\t  ┃ 1) 계좌 생성 요구 조회");
			System.out.println("\t  ┃ 2) 고객 정보 열람");
			System.out.println("\t  ┃ 3) 고객 계좌 생성 승인");
			// 직원이 매니저일 경우, 해당 메뉴들이 보입니다.
			if (loginedUser.getType().equals("Manager")) {
				System.out.println("\t  ┃ 4) 직원 등록");
				System.out.println("\t  ┃ 5) 관리자 권한 인계");
				System.out.println("\t  ┃ 6) 직원 삭제");
			}
			System.out.println("\t  ┃ 0) 로그아웃");
			System.out.println("\t  ┃     ");
			System.out.print("\t  ┃ 메뉴 입력 : ");
			int menu = scan.nextInt();
			System.out.println("\t  ┃                 ");
			System.out.println("\t  ┃  ━━━━━━━━━━━━━━━━━━━━━━━━━━  ┃");
			System.out.println("\t  ┃                            * ┃");
			System.out.println("\t  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛\n");
			System.out.println("");
			
 
			if (menu == 1) { 
			    System.out.println("\t=============================================");
			    System.out.println("\t계좌 생성 요구 조회를 선택하셨습니다.");
			    System.out.println("\t=============================================");
			    EmployeeServiceImpl service = new EmployeeServiceImpl();
			    // try 블록에서 AccountRequestNotFoundException 예외가 발생하지 않으므로, 해당 catch 블록은 실행되지 않는다.
			    // 따라서, 해당 catch 블록을 삭제한다.
			    List<AccountDTO> list = service.getAccountRequests();
			    System.out.println("\t요청 계좌 목록: ");
			    System.out.println("");
				System.out.println("\t┏━━━━━━━━━* Daou_Bank ATM ━━━━━━━━━┓");
				System.out.println("\t┃	      	 	      	   ┃");
				System.out.println("\t┃	     요청 계좌 목록	   	   ┃");
				System.out.println("\t┃	   			   ┃");
				System.out.println("\t┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
				System.out.println("\t  ┃		                 ┃");
				System.out.println("\t  ┃  ━━━━━━━━━━━━━━━━━━━━━━━━━━  *");
				System.out.println("\t  ┃             *");
				System.out.println("\t  ┃ ------------------------");
			    for (AccountDTO account : list) {
					System.out.println("\t  ┃ 계좌번호 : "+ account.getAccount_num());
					System.out.println("\t  ┃ 계좌생성일자 : "+ account.getCreate_date());
					System.out.println("\t  ┃ ------------------------");
			    }
				System.out.println("\t  ┃                 ");
				System.out.println("\t  ┃  ━━━━━━━━━━━━━━━━━━━━━━━━━━  ┃");
				System.out.println("\t  ┃                            * ┃");
				System.out.println("\t  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛\n");
				System.out.println("");
			}

		

			else if (menu == 2) {
				System.out.println("\t=============================================");
				System.out.println("\t고객 정보 열람을 선택하셨습니다.");
				System.out.println("\t=============================================");
				EmployeeServiceImpl service = new EmployeeServiceImpl();
				System.out.print("\t열람할 고객 ID를 입력하세요 : ");
			    String user_id = scan.next(); // user_id 변수에 값을 할당
				try { 
					UserDTO dto = service.getCustomerById(user_id);
					System.out.println("\t고객 정보: ");
					System.out.println("\t---------------------------------------------");
				    System.out.println("");
					System.out.println("\t┏━━━━━━━━━* Daou_Bank ATM ━━━━━━━━━┓");
					System.out.println("\t┃	      	 	      	   ┃");
					System.out.println("\t┃	     고객 정보 조회	   	   ┃");
					System.out.println("\t┃	   			   ┃");
					System.out.println("\t┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
					System.out.println("\t  ┃		                 ┃");
					System.out.println("\t  ┃  ━━━━━━━━━━━━━━━━━━━━━━━━━━  *");
					System.out.println("\t  ┃             *");
					System.out.println("\t  ┃ 고객 ID : "+ dto.getUser_id());
					System.out.println("\t  ┃ 고객 타입 : "+ dto.getType());
					System.out.println("\t  ┃ 고객 이름 : "+ dto.getName());
					System.out.println("\t  ┃ 고객 생년 월일 : "+ dto.getBirth_day());
					System.out.println("\t  ┃                 ");
					System.out.println("\t  ┃  ━━━━━━━━━━━━━━━━━━━━━━━━━━  ┃");
					System.out.println("\t  ┃                            * ┃");
					System.out.println("\t  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛\n");
					System.out.println("");
					System.out.println("\t---------------------------------------------");
				} catch (CustomerInfocheckFailException e) {
					System.out.println(e.getMessage());
				}
			} 
			else if (menu == 3) {
			    System.out.println("\t=============================================");
			    System.out.println("\t고객 계좌 생성 승인을 선택하셨습니다.");
			    System.out.println("\t=============================================");
			    EmployeeServiceImpl service = new EmployeeServiceImpl();
			    boolean isValidAccountNum = false;
			    String account_num = "";
			    while (!isValidAccountNum) {
			        System.out.print("\t승인할 계좌를 입력하세요 : "); // 110-484-423099
			        account_num = scan.next(); // 계좌번호를 입력받습니다.
			        // 계좌 번호의 양식이 맞는지 검사합니다.
			        if (account_num.matches("\\d{3}-\\d{3}-\\d{6}")) {
			            isValidAccountNum = true; // 계좌 번호의 양식이 맞으면 while 루프를 빠져나갑니다.
			            service.approveCustomer(account_num);
			        } else {
			            System.out.println("\t계좌 번호의 양식이 맞지 않습니다. 다시 입력하세요.");
			        }
			    }
			    System.out.println("\t승인이 완료되었습니다. (실제 계좌 생성: 1-> 0)");
			    System.out.println("");
				System.out.println("\t┏━━━━━━━━━* Daou_Bank ATM ━━━━━━━━━┓");
				System.out.println("\t┃	      	 	      	   ┃");
				System.out.println("\t┃	     승인 여부 확인	   	   ┃");
				System.out.println("\t┃	   			   ┃");
				System.out.println("\t┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
				System.out.println("\t  ┃		                 ┃");
				System.out.println("\t  ┃  ━━━━━━━━━━━━━━━━━━━━━━━━━━  *");
				System.out.println("\t  ┃             *");
			    System.out.println("\t  ┃ ------------------------");
			    System.out.println("\t  ┃ 계좌번호 : "+ account_num);
			    System.out.println("\t  ┃ ------------------------");
				System.out.println("\t  ┃                 ");
				System.out.println("\t  ┃  ━━━━━━━━━━━━━━━━━━━━━━━━━━  ┃");
				System.out.println("\t  ┃                            * ┃");
				System.out.println("\t  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛\n");
				System.out.println("");
		} else if (menu == 4) {
				System.out.println("\t직원 등록을 선택하셨습니다.");
				ManagerServiceImpl service = new ManagerServiceImpl();
				// 아이디
				String user_id = "";
				int checkNum = 2;
				while (checkNum == 2 || checkNum == 1) {
					if (checkNum == 2) {
						System.out.print("\t직원 아이디를 입력해주세요 : ");
					} else { 
						System.out.println("\t중복되는 아이디가 존재합니다. 다시 입력해주세요");
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
						if (flag == 0) System.out.println("\t비밀번호가 일치하지 않습니다. 다시 입력해주세요");
						System.out.print("\t직원의 비밀번호를 입력해주세요 : ");
						user_password = scan.next();
						System.out.print("\t직원의 비밀번호 확인을 입력해주세요 : ");
						user_password_confirm = scan.next();
						flag = 0;
					} while (!user_password.equals(user_password_confirm));
				} else {
					do {
						if (flag == 0) System.out.println("\t비밀번호가 일치하지 않습니다. 다시 입력해주세요");
						user_password = new String(console.readPassword("\t직원의 비밀번호를 입력해주세요 : "));
						user_password_confirm = new String(console.readPassword("\t비밀번호 확인을 입력해주세요 : "));
						flag = 0;
					} while (!user_password.equals(user_password_confirm));
				}
				//이름
				System.out.print("\t직원의 이름을 입력해주세요 : ");
				String user_name = scan.next();
				// 생일
				flag = 1;
				String user_birth_day ="";
				do {
					if (flag == 0) System.out.println("\t형식이 일치하지 않습니다. 다시 입력해주세요");
					System.out.print("\t직원의 생일을 입력해주세요(8자리) : ");
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
			} else if (menu == 5) {
				System.out.println("\t관리자 권한 인계를 선택하셨습니다.");
				ManagerServiceImpl service = new ManagerServiceImpl();
				System.out.print("\t인계하시려는 직원 아이디를 입력해주세요 : ");
				String targetEmployee = scan.next();
				try {
					service.handOverManager(loginedUser, targetEmployee);
				} catch (HandOverManagerException e) {
					System.out.println(e.getMessage());
				}
				
			} else if (menu == 6) {
				System.out.println("\t직원 삭제를 선택하셨습니다.");
				ManagerServiceImpl service = new ManagerServiceImpl();
				System.out.print("\t삭제하시려는 직원 아이디를 입력해주세요 : ");
				String targetEmployee = scan.next();
				try {
					service.deleteEmployee(targetEmployee);
				} catch (DeleteEmployeeFailException e) {
					System.out.println(e.getMessage());
				}
			}  else if (menu == 0) {
				System.out.println("\t로그아웃을 선택하셨습니다.");
				loginedUser = null;
				loginMenu();
				return;
			} else {
				System.out.println("\t없는 메뉴를 선택하셨습니다. 다시 입력해 주세요.");
			}
		}
	}
}
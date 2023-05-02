package controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import dao.DBDAO;
import dao.TransactionDAO;
import dao.BankDAO;
import dto.AccountDTO;
import dto.TransactionDTO;
import dto.UserDTO;
import view.Menu;

public class UserATM_Impl implements UserATM {

	public static String userId; 			// 유저 아이디
	public String bank; 			// 계좌조회 , 영수증 변수
	public int balance; 			// 잔고
	
	public static TransactionDTO transactionDTO = new TransactionDTO();
	
	
	public static DBDAO dbDAO = new DBDAO();
	public static BankDAO bankDAO = new BankDAO();
	public static TransactionDAO transactionDAO = new TransactionDAO();
	
	Menu menu = Menu.getInstance();
	
	
	// SQL 세션을 위한 기본설정 =============================================================================
	static SqlSessionFactory sqlSessionFactory;
	
	static {
		String resource = "mybatis/Configuration.xml";
		InputStream inputStream = null;
		try {
			inputStream = Resources.getResourceAsStream(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sqlSessionFactory =
		  new SqlSessionFactoryBuilder().build(inputStream);
	}
	// ----------------------------------------------------------------------------
	
	// 보유 계좌 가져오는 함수
	public List<AccountDTO> getAccountList(UserDTO loginedUser) {
		SqlSession session = sqlSessionFactory.openSession();
		DBDAO user_account_dao = new DBDAO();
		List<AccountDTO> login_User_account_list = user_account_dao.login_user_account(session, loginedUser.getUser_key());
		
		return login_User_account_list;
	}
	
	// 계좌 목록 출력 함수
	public void showAccount(UserDTO loginedUser) {
		List<AccountDTO> list = getAccountList(loginedUser);
		
		DecimalFormat df = new DecimalFormat("###,###");
		
		System.out.println("\t  [보유 계좌 목록]");
		for (AccountDTO dto : list) {
			this.balance = dto.getBalance();
			String formatMoney = df.format(balance);

			if (dto.getIs_temporary().equals("0")) {
				System.out.println("\t  ------------------------");
				System.out.println("\t  계좌번호 : "+ dto.getAccount_num());
				System.out.println("\t     성함 : "+ loginedUser.getName());
				System.out.println("\t     잔액 : "+ formatMoney+"원");
			}
		}
		System.out.println("\t  ------------------------");
	}
	
	
	// TransactionDTO 설정
	public TransactionDTO setTransactionDTO(int type, int amount, int balance, String accountNum, String sendName, String takeName, String accountCounterpartNum) {
		
		TransactionDTO dto = new TransactionDTO();

		dto.setTransaction_type(type);
		dto.setTransaction_amount(amount);
		dto.setTransaction_balance(balance);
		dto.setAccount_num(accountNum);
		dto.setTransaction_send_name(sendName);
		dto.setTransaction_take_name(takeName);
		dto.setAccount_counterpart_num(accountCounterpartNum);
				
		return dto;
	}
	
	@Override
	public void userBalance(UserDTO userDTO) {
		
		this.bank = "Balance";		
		userReceipt(userDTO);
	}

	@Override
	public void userWithdraw(UserDTO userDTO) {
		
		List<AccountDTO> list = getAccountList(userDTO);
		
		int balance = 0;
		
		showAccount(userDTO);
		
		System.out.print("\n\t 출금하실 계좌번호를 입력해주세요 [취소:0] :");
		String account_num = Menu.scan.next();
		if(account_num.equals("0")) return;
		
		System.out.print("\t 출금하실 금액을 입력해주세요. [취소:0] :");
		try {
			int money = Menu.scan.nextInt();
			if(Integer.toString(money).equals("0")) return;
			
			/* 금액이 0원이하이거나 1000원 단위가 아닐때 */
			if(money <= 0 || money % 1000 != 0) {
				System.out.println("\t 1000원 단위로 입력해주세요.");
				return;
			}		

			for(AccountDTO dto : list) {
				if (dto.getIs_temporary().equals("0") && account_num.equals(dto.getAccount_num())) {
					/* 금액이 모자랄때 */
					if(money > dto.getBalance()) {
						System.out.println("\t 잔액이 모자랍니다.");
						return;
					}
					
					/* 계좌 저장 */
					userAccount(dto.getAccount_num(), dto.getBalance() - money);	
					
					balance = dto.getBalance() - money;
				}
			}
		
			/* 거래 추가 */
			transactionDTO = setTransactionDTO(2, money, balance, account_num, "", "", "");
			transactionDAO.insert_transaction(transactionDTO);
			
			/* 영수증 */
			userReceipt(userDTO);
			
		}catch(Exception e) {
			System.out.println("\t 잘못 입력하셨습니다.");
			return;
		}		
	}

	@Override
	public void userDeposit(UserDTO loginedUser) {
		
		List<AccountDTO> list = getAccountList(loginedUser);
		
		int balance = 0;
		
		showAccount(loginedUser);
		
		System.out.print("\n\t 입금하실 계좌번호를 입력해주세요 [취소:0] :");
		String account_num = Menu.scan.next();
		if(account_num.equals("0")) return;
		
		System.out.print("\t 입금하실 금액을 입력해주세요 [취소:0] :");
		try {
			int money = Menu.scan.nextInt();
			if(Integer.toString(money).equals("0")) return;
			
			/* 금액이 0원이하이거나 1000원 단위가 아닐때 */
			if(money <= 0 || money % 1000 != 0) {
				System.out.println("\t 금액을 확인해주세요.");
				return;
			}		
			
			for(AccountDTO dto : list) {
				if (dto.getIs_temporary().equals("0") && account_num.equals(dto.getAccount_num())) {
					/* 금액이 모자랄때 */
					if(money > dto.getBalance()) {
						System.out.println("\t 잔액이 모자랍니다.");
						return;
					}
					
					/* 계좌 저장 */
					userAccount(dto.getAccount_num(), dto.getBalance() + money);	
					
					balance = dto.getBalance() + money;
				}
			}
			
			/* 거래 추가 */
			transactionDTO = setTransactionDTO(1, money, balance, account_num, "", "", "");
			transactionDAO.insert_transaction(transactionDTO);

			/* 영수증 */
			userReceipt(loginedUser);
			
		}catch(Exception e) {
			System.out.println("\t 잘못 입력하셨습니다.");
			return;
		}	
	}

	@Override
	public void userTransfer(UserDTO loginedUser) {
		
		List<AccountDTO> list = getAccountList(loginedUser);
		int transfer_user_key = 0;
		
		int balance1 = 0;
		int balance2 = 0;
		
		showAccount(loginedUser);
		
		System.out.print("\n\t 출금하실 계좌번호를 입력해주세요 [취소:0] :");
		String account_num = Menu.scan.next();
		if(account_num.equals("0")) return;
		
		System.out.print("\t 송금하실 계좌 번호를 입력하세요.");
		
		try {
			String opponentName = "";
			String name = loginedUser.getName();
			String transferAccountNum = Menu.scan.next();
			boolean check = false;

			SqlSession session = sqlSessionFactory.openSession();
			DBDAO dao = new DBDAO();
			List<UserDTO> transfer_user_list = dao.transfer_user_key(session, transferAccountNum);
			
			for(AccountDTO dto : list) {		
				if(dto.getIs_temporary().equals("0")) {
					
					/* 출금 계좌와 송금 계좌가 같을 시 */
					if (transferAccountNum.equals(account_num)) {
						System.out.println("\t 같은 계좌로의 송금은 불가합니다");
						break;
					}
					
					/* 정상 계좌가 아닌지 (name == null) 체크 후 넘기기 */
					for (UserDTO dto2 : transfer_user_list) {
						transfer_user_key = dto2.getUser_key();
						opponentName = dto2.getName();
						
						if (opponentName != null) check = true; 
						break;		
					}
				}
			}
			
			if(check == false) {
				System.out.println("\t 계좌번호를 확인해주세요."); 
				return;
			}
			
			System.out.println("");
			System.out.println("\t┏━━━* Daou_Bank ATM ━━━━┓");
			System.out.println("\t┃	     Transfer	┃");
			System.out.println("\t┗━━━━━━━━━━━━━━━━━━━━━━━┛");
			System.out.println("\t  ┃		      ┃");
			System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  *");
			System.out.println("\t  ┃                 *");
			System.out.println("\t  ┃ 받으실분의 계좌 : " + transferAccountNum);
			System.out.println("\t  ┃ 받으실분의 성함 : " + opponentName);
			System.out.println("\t  ┃                 *");
			System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  ┃");
			System.out.println("\t  ┃                   *");
			System.out.println("\t  ┗━━━━━━━━━━━━━━━━━━━┛\n");
			
			System.out.print("\t 송금하실 금액을 입력해주세요 [취소:0] :");
			
			try {
				int money = Menu.scan.nextInt();
				if(Integer.toString(money).equals("0")) return;
				
				/* 금액이 0원이하이거나 1000원 단위가 아닐때 */
				if(money <= 0 || money % 1000 != 0) {
					System.out.println("\t 1000원 단위로 입력해주세요.");
					return;
				}
				
				/* 금액이 모자랄때 */
				for(AccountDTO dto : list) {										
					if (dto.getIs_temporary().equals("0") && account_num.equals(dto.getAccount_num())) {
						if(money > dto.getBalance()) {
							System.out.println("\t 잔액이 모자랍니다.");
							return;
						}
					}
				}
				
				DecimalFormat df = new DecimalFormat("###,###");
				String formatMoney = df.format(money);
				
				System.out.println("");
				System.out.println("\t┏━━━* Daou_Bank ATM ━━━━┓");
				System.out.println("\t┃	     Transfer		┃");
				System.out.println("\t┗━━━━━━━━━━━━━━━━━━━━━━━┛");
				System.out.println("\t  ┃		      ┃");
				System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  *");
				System.out.println("\t  ┃                 *");
				System.out.println("\t  ┃ 받으실분의 계좌 : " + transferAccountNum);
				System.out.println("\t  ┃ 받으실분의 성함 : " + opponentName);
				System.out.println("\t  ┃ 송금 금액 : " + formatMoney + "원");
				System.out.println("\t  ┃                 *");
				System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  ┃");
				System.out.println("\t  ┃                   *");
				System.out.println("\t  ┗━━━━━━━━━━━━━━━━━━━┛\n");
				System.out.print("\t 송금 [1] 취소 [0] : ");
				
				String sel = Menu.scan.next();
				
				if(!sel.equals("1")) return;
				
				System.out.print("\t 비밀번호 : ");
				String pw = Menu.scan.next();
				
				for (AccountDTO dto : list) {
					if(!userCheckPassWord(pw, dto)) return;
				}
				
				/* 받는사람의 계좌, 금액 + */
				List<AccountDTO> transfer_user_account = dao.login_user_account(session, transfer_user_key);
				
				for(AccountDTO dto : transfer_user_account) {
					if (dto.getIs_temporary().equals("0") && transferAccountNum.equals(dto.getAccount_num())) {
						userAccount(dto.getAccount_num(), dto.getBalance() + money);	
						
						balance1 = dto.getBalance() + money;
					}
				}
				
				
				/* 보낸사람의 계좌, 금액 - */
				for(AccountDTO dto : list) {
					if (dto.getIs_temporary().equals("0") && account_num.equals(dto.getAccount_num())) {
						userAccount(dto.getAccount_num(), dto.getBalance() - money);	
						
						balance2 = dto.getBalance() - money;
					}
				}
				
				
				/* 거래 추가 */
				transactionDTO = setTransactionDTO(4, money, balance1, transferAccountNum, name, opponentName, account_num); // 4 : 이체-입금
				transactionDAO.insert_transaction(transactionDTO);

				transactionDTO = setTransactionDTO(3, money, balance2, account_num, name, opponentName, transferAccountNum); // 3 : 이체-송금
				transactionDAO.insert_transaction(transactionDTO);
				
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				/* 영수증 */
				userReceipt(loginedUser);
				
			} catch(Exception e) {
				e.printStackTrace();
				System.out.println("\t 잘못 입력하셨습니다.");
				return;
			}	
		
		} catch(Exception e) {
			System.out.println("\t 잘못 입력하셨습니다.");
		} 
		
		System.out.println();
	}

	@Override
	public void userHistory(UserDTO userDTO) {
		
		String name = "";
		String check = "";
		String account = "";
		
		/* 통장거래 내역 횟수를 확인할 변수 */
		int count = 1; 
		
		showAccount(userDTO);
		
		System.out.print("\n\t 통장정리할 계좌번호를 입력해주세요 [취소:0] :");
		String account_num = Menu.scan.next();
		if(account_num.equals("0")) return;
		
		List<TransactionDTO> transactionList = transactionDAO.transaction_history(account_num);
		
		try {
			
			for(TransactionDTO dto : transactionList) {	
				

				if (dto.getTransaction_type() == 1 || dto.getTransaction_type() == 2) {

					check = (dto.getTransaction_type() == 1) ? "입금" : "출금";
					name = "name : " + userDTO.getName();
					account = dto.getAccount_num();
				
				} else if (dto.getTransaction_type() == 3 || dto.getTransaction_type() == 4) {
					
					check = (dto.getTransaction_type() == 3) ? "이체-송금" : "이체-입금";
					name = "to : " + dto.getTransaction_take_name() + "\n\t  ┃  from : " + dto.getTransaction_send_name();
					account = (dto.getTransaction_type() == 3) ? dto.getAccount_num() + " -> " + dto.getAccount_counterpart_num() 
							 :  dto.getAccount_counterpart_num() + " -> " + dto.getAccount_num() ;
				}
				
				int cash = dto.getTransaction_amount();
				int balance = dto.getTransaction_balance();
				
				DecimalFormat df = new DecimalFormat("###,###");
				String formatCash = df.format(cash);
				String formatBalance = df.format(balance);
			
				
				System.out.printf("\n\t[%d] 번 거래내역\n",count);
				
				System.out.println("");
				System.out.println("\t┏━━━* Daou_Bank ATM ━━━━┓");
				System.out.println("\t┃	 " + check +  "    	┃");
				System.out.println("\t┗━━━━━━━━━━━━━━━━━━━━━━━┛");
				System.out.println("\t  ┃		      ┃");
				System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  *");
				System.out.println("\t  ┃                 *");
				System.out.println("\t  ┃    Date : "+ dto.getTransaction_datetime().substring(0, 10));
				System.out.println("\t  ┃    "+name);
				System.out.println("\t  ┃ Account : "+ account);
				System.out.println("\t  ┃  Amount : "+formatCash+"원");
				System.out.println("\t  ┃ ");
				System.out.println("\t  ┃ Balance : "+formatBalance+"원");
				System.out.println("\t  ┃                 *");
				System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  ┃");
				System.out.println("\t  ┃                   *");
				System.out.println("\t  ┗━━━━━━━━━━━━━━━━━━━┛\n");
				System.out.println("");
				
				/* 통장거래 내역 횟수를 확인할 변수 */
				count ++;
			}
		} catch (Exception e) {
			System.out.println("\t 거래 내역이 없습니다.");
			return;
		}		
	}

	@Override
	public void userAccount(String account, int balance) {		
		bankDAO.updateBalance(account, balance);
	}

	public void createAccount(int user_key) {
		
		SqlSession session = sqlSessionFactory.openSession();
		Scanner scan_pw = new Scanner(System.in);
		
		String input_pw;
		String input_pw_check;

		int input_pw_int=0;
		int input_pw_check_int=0;
		int n_sql_insert = 0;
		
		AccountDTO account_tmp = new AccountDTO();
		DBDAO db_create_dao = new DBDAO();
		
		do {
			System.out.println("개설할 계좌의 비밀번호를 입력해주세요");
			input_pw = scan_pw.next();			
			System.out.println("비밀번호 확인");
			input_pw_check = scan_pw.next();
			try {
				input_pw_int = Integer.parseInt(input_pw);
				input_pw_check_int = Integer.parseInt(input_pw_check);
			} catch(Exception e) {
				System.out.println("잘못된 비밀번호 형식입니다.");
			}
			if (input_pw_int != input_pw_check_int) {
				System.out.println("비밀번호가 일치하지 않습니다.");
			}
			else if (input_pw_int>9999 || input_pw_int < 0 ) {
				System.out.println("비밀번호는 4자리 정수 ( Ex) 0000 ) 형식으로 입력해주시기 바랍니다.");
			}
		} while(input_pw_int>9999 || input_pw_int < 0 || input_pw_int != input_pw_check_int); // do-while()문 end
				
		try {
			account_tmp = new AccountDTO(user_key, input_pw);
			account_tmp.setAccount_password(db_create_dao.Encryptonize_pw(""+account_tmp.getAccount_password(), db_create_dao.create_random_seed()));
			String account_tmp_num = db_create_dao.create_tmp_account_num(session, account_tmp);
			account_tmp.setAccount_num(account_tmp_num);
			n_sql_insert = db_create_dao.insert_account_db(session, account_tmp);
			if (n_sql_insert == 0) {
				System.out.println("계좌 개설 정보가 잘못되어, 개설요청이 정상적으로 이루어지지 않았습니다. 계좌 개설 요청 정보를 다시 입력해주세요.");
				session.rollback();
			}
			else {
				System.out.println("계좌 개설 요청이 완료되었습니다. \n");
				session.commit();
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("개설할 계좌의 정보가 잘못되었습니다. 다시 입력해주세요.");
		} finally {
			session.close();
		} // try {} end
		
	}

	@Override
	public boolean userCheckPassWord(String pw, AccountDTO accountDTO) {
		String accountPW = dbDAO.Decryptonize_pw(accountDTO.getAccount_password(), dbDAO.create_random_seed());
		
		if(accountPW.equals(pw)) {
			System.out.println("\t 송금 진행중입니다..");
			return true;
		}
		System.out.println("\t 비밀번호가 일치하지 않습니다.");
		return false;
	}

	@Override
	public void showInfo(UserDTO loginedUser) {
		int account_cnt = 0;
		int account_balance_total_sum = 0;		
		
		List<AccountDTO> list = getAccountList(loginedUser);
		
		DecimalFormat df = new DecimalFormat("###,###");
		
		System.out.printf("      [%s] 님의 마이 페이지 입니다.\n",loginedUser.getName());
		System.out.println("  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓* ");
		System.out.println("  ┃  개인 정보 ");
		System.out.println("  ┃  ");
		System.out.println("  ┃    Name : " + loginedUser.getName());
		System.out.printf("  ┃    Birth : %s 년 %s 월 %s 일\n",loginedUser.getBirth_day().substring(0,4), loginedUser.getBirth_day().substring(5,7),loginedUser.getBirth_day().substring(8,10));
		System.out.println("  ┃ ========================");
		System.out.println("  ┃  계좌 정보 ");
		System.out.println("  ┃  ");
		
		for(AccountDTO dto: list) {
			account_cnt+=1;
			account_balance_total_sum += dto.getBalance();
			if (dto.getIs_temporary().equals("0")) {
				System.out.printf("  ┃  %d 번째 계좌", account_cnt);
				System.out.println("  ┃  계좌번호 : " + dto.getAccount_num());
				System.out.println("  ┃  \t잔고 : " + df.format(dto.getBalance()));
				System.out.println("  ┃  \t생성날짜 : " + dto.getCreate_date().toString().substring(0, 10) );
				System.out.println("  ┃  - - - - - - - - - - - - - - -" );			
			}
		}
		System.out.println("  ┃ ========================");
		System.out.println("  ┃ ");
		System.out.println("  ┃  계좌 개설 요청 정보 ");
		System.out.println("  ┃  ");
		
		AtomicInteger tmp_index = new AtomicInteger();
		Predicate<AccountDTO> is_tmp_account = dto -> dto.getIs_temporary().equals("1");
		Consumer<AccountDTO> print_account_tmp = dto -> System.out.println("  ┃  " + (tmp_index.getAndIncrement()+1) + " 번째 생성 요청 ---\n  ┃    생성 요청 일자 : " + dto.getCreate_date().toString().substring(0, 10) + "\n  ┃  ");
		Stream <AccountDTO> account_stream = list.stream();
				
		account_stream.filter(is_tmp_account).forEach(print_account_tmp);
		
		System.out.println("  ┃ ========================");
		if (account_cnt == 0) {
			System.out.println("  ┃  계좌가 [텅] 비어있습니다.");
		}
		System.out.println("  ┃ ");
		System.out.println("  ┃    [ " + loginedUser.getName() + " ]님의 총 자산은 " + df.format(account_balance_total_sum) + "원 입니다." );
		System.out.println("  ┃ ");
		System.out.println("  ┃                 *");
		System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛\n");
		
	}
	
	@Override
	public void userReceipt(UserDTO userDTO) {
		
		List<AccountDTO> list = getAccountList(userDTO);

		this.bank = "Receipt";
		DecimalFormat df = new DecimalFormat("###,###");
		
		System.out.println("");
		System.out.println("\t┏━━━* Daou_Bank ATM ━━━━┓");
		System.out.println("\t┃	   " + bank + "	┃");
		System.out.println("\t┗━━━━━━━━━━━━━━━━━━━━━━━┛");
		System.out.println("\t  ┃		      ┃");
		System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  *");
		System.out.println("\t  ┃                 *");

		for (AccountDTO dto : list) {
			this.balance = dto.getBalance();
			String formatMoney = df.format(balance);

			if (dto.getIs_temporary().equals("0")) {
				System.out.println("\t  ┃ ------------------------");
				System.out.println("\t  ┃ 계좌번호 : "+ dto.getAccount_num());
				System.out.println("\t  ┃    성함 : "+ userDTO.getName());
				System.out.println("\t  ┃    잔액 : "+ formatMoney+"원");
			}
		}
		System.out.println("\t  ┃ ------------------------");
		System.out.println("\t  ┃ ");
		System.out.println("\t  ┃ [0] 확인 ");
		System.out.println("\t  ┃                 *");
		System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  ┃");
		System.out.println("\t  ┃                   *");
		System.out.println("\t  ┗━━━━━━━━━━━━━━━━━━━┛\n");
		System.out.println("");
		
		
		String sel = Menu.scan.next();
		if(sel.equals("0")) return;
	}
}
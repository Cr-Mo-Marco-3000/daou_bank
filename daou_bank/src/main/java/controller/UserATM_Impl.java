package controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import dao.DBDAO;
import dao.BankDAO;
import dto.AccountDTO;
import dto.TransactionDTO;
import dto.UserDTO;
import view.Menu;

public class UserATM_Impl implements UserATM {

	public String accountNumber; 	// 계좌번호
	public String userName; 		// 회원 이름
	public static String userId; 	// 회원 아이디
	public String userPw; 			// 회원 비번
	public String bank; 			// 계좌조회 , 영수증 변수
	public int balance; 			// 잔고
	public String recipientName;	// 수신인
	public int transactionType;		// 거래 유형
	
	public static UserATM_Impl userImpl = new UserATM_Impl();
	public static UserJoin_Impl userJoin = new UserJoin_Impl();
	
	public static UserDTO userDTO = new UserDTO();
	public static AccountDTO accountDTO = new AccountDTO();
	public static TransactionDTO transactionDTO = new TransactionDTO();
	
	public static DBDAO dbDAO = new DBDAO();
	public static BankDAO bankDAO = new BankDAO();
	
	Menu menu = Menu.getInstance();

	public static UserATM_Impl getInstance() {
		if(userImpl == null) {
			userImpl = new UserATM_Impl();
		}
		return userImpl;
	}
	
	
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
	
	
	@Override
	public void init() {
		this.accountNumber = accountDTO.getAccount_num();
		this.userName = userDTO.getName();
		this.userId = userDTO.getUser_id();
		this.userPw = userDTO.getUser_password();
		this.balance = accountDTO.getBalance();
	}
	
	@Override
	public void userBalance() {
		this.bank = "Balance";		
		userReceipt();
	}

	@Override
	public void userWithdraw() {
		
		System.out.print("출금하실 금액을 입력해주세요. [취소:0] :");
		try {
			int money = Menu.scan.nextInt();
			
			/* 금액이 0원이하이거나 1000원 단위가 아닐때 */
			if(money <= 0 || money % 1000 != 0) {
				System.out.println("1000원 단위로 입력해주세요.");
				return;
			}		
			/* 금액이 모자랄때 */
			if(money > balance) {
				System.out.println("잔액이 모자랍니다.");
				return;
			}
			/* 계좌 저장 */
			userAccount(accountNumber, balance-money);		
			
			/* 영수증 */
			userReceipt();
		}catch(Exception e) {
			System.out.println("잘못 입력하셨습니다.");
			return;
		}		
	}

	@Override
	public void userDeposit() {
		
		System.out.print("입금하실 금액을 입력해주세요 [취소:0] :");
		try {
			int money = Menu.scan.nextInt();
			
			/* 금액이 0원이하이거나 1000원 단위가 아닐때 */
			if(money <= 0 || money % 1000 != 0) {
				System.out.println("금액을 확인해주세요.");
				return;
			}		
			/* 계좌 저장 */
			userAccount(accountNumber, balance+money);	
			
			/* 영수증 */
			userReceipt();
			
		}catch(Exception e) {
			System.out.println("잘못 입력하셨습니다.");
			return;
		}	
	}

	@Override
	public void userTransfer() {
		
		System.out.println("입금하실 계좌를 입력하세요.");
		try {
			String name = "";
			String accountNum = Menu.scan.next();
			boolean check = false;
			
			for(AccountDTO key : dbDAO.login_user_account(null, userDTO)) {		
				if(accountNum.equals(key.getAccount_num())) {
					name = userDTO.getName();
					check = true; 
					break;		
				}
			}
			if(check == false) {
				System.out.println("계좌번호를 확인해주세요."); 
				return;
			}
			
			System.out.println("");
			System.out.println("\t┏━━━* Daou_Bank ATM ━━━━┓");
			System.out.println("\t┃	     Transfer	┃");
			System.out.println("\t┗━━━━━━━━━━━━━━━━━━━━━━━┛");
			System.out.println("\t  ┃		      ┃");
			System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  *");
			System.out.println("\t  ┃                 *");
			System.out.println("\t  ┃ 받으실분의 계좌 : " + accountDTO);
			System.out.println("\t  ┃ 받으실분의 성함 : " + name);
			System.out.println("\t  ┃                 *");
			System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  ┃");
			System.out.println("\t  ┃                   *");
			System.out.println("\t  ┗━━━━━━━━━━━━━━━━━━━┛\n");
			System.out.print("\t   입금하실 금액을 입력해주세요 [취소:0] :");
			
			try {
				int money = Menu.scan.nextInt();
					
				/* 금액이 0원이하이거나 1000원 단위가 아닐때 */
				if(money <= 0 || money % 1000 != 0) {
					System.out.println("1000원 단위로 입력해주세요.");
					return;
				}
				/* 금액이 모자랄때 */
				if(money > balance) {
					System.out.println("잔액이 모자랍니다.");
					return;
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
				System.out.println("\t  ┃ 받으실분의 계좌 : " + accountDTO);
				System.out.println("\t  ┃ 받으실분의 성함 : " + name);
				System.out.println("\t  ┃ 송금 금액 : " + formatMoney + "원");
				System.out.println("\t  ┃                 *");
				System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  ┃");
				System.out.println("\t  ┃                   *");
				System.out.println("\t  ┗━━━━━━━━━━━━━━━━━━━┛\n");
				System.out.print("\t   송금 [1] 취소 [0] : ");
				System.out.println("");
				
				String sel = Menu.scan.next();
				
				if(!sel.equals("1")) return;
				
				System.out.print("비밀번호 : ");
				String pw = Menu.scan.next();
				if(!userCheckPassWord(pw)) return;
				
				int cash = accountDTO.getBalance();
				
				/* 받는사람의 계좌, 금액 + */
				userAccount(accountNum, cash + money);	
				
				/* 보낸사람의 계좌, 금액 - */
				userAccount(accountDTO.getAccount_num(), balance - money); 
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				userReceipt();
			}catch(Exception e) {
				System.out.println("잘못 입력하셨습니다.");
				return;
			}	
		
		}catch(Exception e) {
			System.out.println("잘못 입력하셨습니다.");
		}
		System.out.println();
	}
	
	@Override
	public void userReceipt() {

		this.bank = "Receipt";
		DecimalFormat df = new DecimalFormat("###,###");
		String formatMoney = df.format(balance);
		
		System.out.println("");
		System.out.println("\t┏━━━* Daou_Bank ATM ━━━━┓");
		System.out.println("\t┃	     " + bank + "	┃");
		System.out.println("\t┗━━━━━━━━━━━━━━━━━━━━━━━┛");
		System.out.println("\t  ┃		      ┃");
		System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  *");
		System.out.println("\t  ┃                 *");
		System.out.println("\t  ┃ 계좌번호 : A"+accountNumber);
		System.out.println("\t  ┃    성함 : "+userName);
		System.out.println("\t  ┃    잔액 : "+formatMoney+"원");
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
	
	@Override
	public void userHistory() {
		
		try {
			
			/* 남아있던 잔액 을 비교하여 입금/출금 을 확인합니다. */
			int checkBalance = 0; 
			
			/* 통장거래 내역 횟수를 확인할 변수 */
			int count = 1; 
			
			/* 입금/출금 변수 */
			String check = "";
			String name = "";
			String account = transactionDTO.getAccount_num();
			
			for(TransactionDTO key : bankDAO.transactionHistory(userName)) {	
				
				int money = key.getTransaction_amount();
				int cash = 0;
				int date = key.getTransaction_datetime();

				if(checkBalance > money) {
					
					check = (key.getTransaction_type() == 1) ? "입금 : " : "출금 : ";
					name = "name : " + key.getTransaction_send_name();
					cash = -(checkBalance - money);
					
					break;		
					
				} else {
					check = (key.getTransaction_type() == 3) ? "이체-송금 : " : "이체-입금 : ";
					name = (key.getTransaction_type() == 3) ? "to : " + key.getTransaction_take_name() : "from" + key.getTransaction_send_name();
					cash = money - checkBalance;
				}

				DecimalFormat df = new DecimalFormat("###,###");
				String formatCash = df.format(cash);
				String formatBalance = df.format(money);
			
				
				System.out.printf("[%d] 번 거래내역\n",count);
				
				System.out.println("");
				System.out.println("\t┏━━━* Daou_Bank ATM ━━━━┓");
				System.out.println("\t┃	     " + check + "	┃");
				System.out.println("\t┗━━━━━━━━━━━━━━━━━━━━━━━┛");
				System.out.println("\t  ┃		      ┃");
				System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  *");
				System.out.println("\t  ┃                 *");
				System.out.println("\t  ┃    Date : "+date);
				System.out.println("\t  ┃    "+name);
				System.out.println("\t  ┃ Account : "+account);
				System.out.println("\t  ┃  Amount : "+formatCash+"원");
				System.out.println("\t  ┃ ");
				System.out.println("\t  ┃ Balance : "+formatBalance+"원");
				System.out.println("\t  ┃                 *");
				System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  ┃");
				System.out.println("\t  ┃                   *");
				System.out.println("\t  ┗━━━━━━━━━━━━━━━━━━━┛\n");
				System.out.println("");
				
				/* 남아있던 잔액 을 비교하여 입금/출금 을 확인합니다. */
				checkBalance = money;
				
				/* 통장거래 내역 횟수를 확인할 변수 */
				count ++;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("거래 내역이 없습니다.");
			return;
		}		
	}

	@Override
	public void userAccount(String account, int balance) {
		
		this.accountNumber = account;
		this.balance = balance;
		
		bankDAO.updateBalance(accountDTO);
		
		init();
	}

	public void createAccount(int user_key) {
		
		SqlSession session = sqlSessionFactory.openSession();
		Scanner scan_pw = new Scanner(System.in);
		
		int input_pw;
		int input_pw_check;
		AccountDTO account_tmp = null;
		System.out.println("개설할 계좌의 비밀번호를 입력해주세요");
		input_pw = scan_pw.nextInt();
		System.out.println("비밀번호 확인");
		input_pw_check = scan_pw.nextInt();
		
		if (input_pw != input_pw_check) {
			System.out.println("비밀번호가 일치하지 않습니다.");
		} else {			
			account_tmp = new AccountDTO(user_key, input_pw);
		}
		
		DBDAO db_create_dao = new DBDAO();
		int n = 0;
		n = db_create_dao.insert_account_db(session, account_tmp);
		if (n == 0) {
			//throw Exception()
		}
		else {
			System.out.println("계좌 개설 요청이 완료되었습니다. \n");
			session.commit();
		}
		session.close();
	}
	

	@Override
	public boolean userCheckPassWord(String pw) {
		if(userPw.equals(pw)) {
			System.out.println("송금 진행중입니다..");
			return true;
		}
		System.out.println("비밀번호가 일치하지 않습니다.");
		return false;
	}

	@Override
	public void showInfo(UserDTO loginedUser, List<AccountDTO> login_User_account_list) {
		int account_cnt = 0;
		int account_tmp_cnt = 0;
		int account_balance_total_sum = 0;		
		
		System.out.printf("      [%s] 님의 마이 페이지 입니다.\n",loginedUser.getName());
		System.out.println("  ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━* ");
		System.out.println("  ┃  개인 정보 ");
		System.out.println("  ┃  ");
		System.out.println("  ┃    Name : " + loginedUser.getName());
		System.out.printf("  ┃    Birth : %s 년 %s 월 %s 일\n",loginedUser.getBirth_day().substring(0,4), loginedUser.getBirth_day().substring(5,7),loginedUser.getBirth_day().substring(8,10));
		System.out.println("  ┃ ========================");
		System.out.println("  ┃  계좌 정보 ");
		System.out.println("  ┃  ");
		for(AccountDTO dto: login_User_account_list) {
			account_cnt+=1;
			account_balance_total_sum += dto.getBalance();
			System.out.printf("  ┃  %d 번째 계좌", account_cnt);
			System.out.println("  ┃  계좌번호 : " + dto.getAccount_num());
			System.out.println("  ┃  잔고 : " + dto.getBalance());
			System.out.println("  ┃  생성날짜 : " + dto.getCreate_date());
			System.out.println("  ┃  - - - - - - - - - - - - - - -" );			
		}
		if (account_cnt == 0)
			System.out.println("  ┃      계좌가 [텅] 비어있습니다.");
		System.out.println("  ┃ ========================");
		System.out.println("  ┃ ");
		System.out.println("  ┃    [ " + loginedUser.getName() + " ]님의 총 자산은 " + account_balance_total_sum + "원 입니다." );
		System.out.println("  ┃ ");
		System.out.println("  ┃                 *");
		System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛\n");
		
		
	}
	
	
	
	
}
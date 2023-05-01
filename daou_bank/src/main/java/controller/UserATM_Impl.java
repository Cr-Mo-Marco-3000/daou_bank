package controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
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
import dto.AccountDTO;
import dto.UserDTO;
import model.BankAccount;
import model.User;
import view.Menu;

public class UserATM_Impl implements UserATM {

	public int accountNumber; // 계좌번호
	public String userName; // 회원 이름
	public static String userId; // 회원 아이디
	public String userPw; // 회원 비번
	public String bank; // 계좌조회 , 영수증 변수
	public int balance; // 계좌잔액
	
	
	Menu menu = Menu.getInstance();
	
	private static UserATM_Impl userImpl = new UserATM_Impl();
	public static UserJoin_Impl userJoin = new UserJoin_Impl();
	
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
		// 로그인한 회원의 계좌 인스턴스를 생성할 때 필요한 값
		this.accountNumber = User.userMap.get(userId).getAccountNumber();
		this.userName = User.userMap.get(userId).getUserName();
		this.userPw = User.userMap.get(userId).getUserPw();
		this.balance = BankAccount.bankMap.get(accountNumber).getBalance();
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
		userAccount(accountNumber,userName,balance-money,userName);		
		
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
			userAccount(accountNumber,userName,balance+money,userName);		
			
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
			int account = Menu.scan.nextInt();
			boolean check = false;
			for(String key : User.userMap.keySet()) {			
				if(account == (User.userMap.get(key).getAccountNumber())) {
					name = User.userMap.get(key).getUserName();
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
			System.out.println("\t┃	     Transfer		┃");
			System.out.println("\t┗━━━━━━━━━━━━━━━━━━━━━━━┛");
			System.out.println("\t  ┃		      ┃");
			System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  *");
			System.out.println("\t  ┃                 *");
			System.out.println("\t  ┃ 받으실분의 계좌 : " + account);
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
				System.out.println("  ┏━━━━*Transfer━━━━┓");
				System.out.println("  ┃                 *");
				System.out.println("  ┃ 받으실분의 계좌 : " + account);
				System.out.println("  ┃ 받으실분의 성함 : " + name);
				System.out.println("  ┃ 송금 금액 : " + formatMoney + "원");
				System.out.println("  ┃                 *");
				System.out.println("  ┗━━━━━━━━━━━━━━━━━┛\n");
				System.out.print("   송금 [1] 취소 [0] :");
				String sel = Menu.scan.next();
				
				if(!sel.equals("1")) return;
				
				System.out.print("비밀번호 : ");
				String pw = Menu.scan.next();
				if(!userCheckPassWord(pw)) return;
				
				int cash = BankAccount.bankMap.get(account).getBalance();
				
				/* 받는사람의 계좌, 금액 + */
				userAccount(account,userName,cash + money,userName);	
				
				/* 보낸사람의 계좌, 금액 - */
				userAccount(accountNumber,userName,balance - money,name);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
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
		//menu.userView();		
	}

	@Override
	public void userReceipt() {

		this.bank = "Receipt";
		DecimalFormat df = new DecimalFormat("###,###");
		String formatMoney = df.format(balance);
		
		System.out.println("");
		System.out.println("  ┏━━━━* "+bank+"━━━━┓");
		System.out.println("  ┃                 *");
		System.out.println("  ┃ 계좌번호 : A"+accountNumber);
		System.out.println("  ┃    성함 : "+userName);
		System.out.println("  ┃    잔액 : "+formatMoney+"원");
		System.out.println("  ┃ ");
		System.out.println("  ┃ [0] 확인 ");
		System.out.println("  ┃                 *");
		System.out.println("  ┗━━━━━━━━━━━━━━━━━┛\n");
		
		String sel = Menu.scan.next();
		if(sel.equals("0")) return;
	}
	
	@Override
	public void userHistory() {
		
		Scanner scanFile;
		try {
			scanFile = new Scanner(new FileReader("bank.txt"));
			
			/* 남아있던 잔액 을 비교하여 입금/출금 을 확인합니다. */
			int checkBalance = 0; 
			/* 통장거래 내역 횟수를 확인할 변수 */
			int count = 1; 
			
			while(scanFile.hasNext()) {
				String[] line = scanFile.nextLine().split("@");
				if(accountNumber == Integer.parseInt(line[0])) {
					int money = Integer.parseInt(line[2]);
					
					/* 입금/출금 변수 */
					String check = "입금 : ";
					String name = line[1];
					int cash = 0;
					if(checkBalance > money) {		
						
						check = (!line[3].equals(userName)) ? "이체 : " : "출금 : ";
						name = (!line[3].equals(userName)) ? line[3] : name;	
						cash = -(checkBalance - money);
						
					}else {
						cash = money - checkBalance;
					}
					DecimalFormat df = new DecimalFormat("###,###");
					String formatCash = df.format(cash);
					String formatBalance = df.format(money);
				
					System.out.printf("[%d] 번 거래내역\n",count);
					System.out.println("  ┏━━━* "+check);
					System.out.println("  ┃ ");
					System.out.println("  ┃    Name : "+name);
					System.out.println("  ┃  Amount : "+formatCash+"원");
					System.out.println("  ┃ ");
					System.out.println("  ┃ Balance : "+formatBalance+"원");
					System.out.println("  ┃                 *");
					System.out.println("  ┗━━━━━━━━━━━━━━━━━┛\n");
					/* 남아있던 잔액 을 비교하여 입금/출금 을 확인합니다. */
					checkBalance = money;
					/* 통장거래 내역 횟수를 확인할 변수 */
					count ++;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void createAccount(int user_key) {
		
		SqlSession session = sqlSessionFactory.openSession();
		Scanner scan_pw = new Scanner(System.in);
		
		String input_pw;
		String input_pw_check;
		AccountDTO account_tmp = null;
		System.out.println("개설할 계좌의 비밀번호를 입력해주세요");
		input_pw = scan_pw.nextLine();
		System.out.println(input_pw);
		System.out.println("비밀번호 확인");
		input_pw_check = scan_pw.next();
		System.out.println(input_pw);
		
		if (input_pw.equals( input_pw_check )) {
			System.out.println("비밀번호가 일치하지 않습니다.");
		} else {			
			account_tmp = new AccountDTO(user_key, input_pw);
		}
		
		DBDAO db_create_dao = new DBDAO();

		System.out.println(account_tmp.getAccount_password());
		account_tmp.setAccount_password(db_create_dao.Encryptonize_pw(""+account_tmp.getAccount_password(), db_create_dao.create_random_seed()));
		System.out.println(account_tmp.getAccount_password());
		String account_tmp_num = db_create_dao.create_tmp_account_num(session, account_tmp);
		account_tmp.setAccount_num(account_tmp_num);
		
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
	public void userAccount(int account, String userName, int balance, String setName) {
		
		BankAccount bank = new BankAccount(account,userName,balance,setName);
		BankAccount.bankMap.put(account,bank);
		bank.setBankFile();	
		init();
		
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
			if (dto.getIs_temporary().equals("0")) {
				System.out.printf("  ┃  %d 번째 계좌", account_cnt);
				System.out.println("  ┃  계좌번호 : " + dto.getAccount_num());
				System.out.println("  ┃  잔고 : " + dto.getBalance());
				System.out.println("  ┃  생성날짜 : " + dto.getCreate_date());
				System.out.println("  ┃  - - - - - - - - - - - - - - -" );			
			}
		}
		System.out.println("  ┃ ========================");
		System.out.println("  ┃ ");
		System.out.println("  ┃  계좌 개설 요청 정보 ");

		AtomicInteger tmp_index = new AtomicInteger();
		Predicate<AccountDTO> is_tmp_account = dto -> dto.getIs_temporary().equals("1");
		Consumer<AccountDTO> print_account_tmp = dto -> System.out.println("  ┃  " + (tmp_index.getAndIncrement()+1) + "번쨰 생성 요청 ---\n  ┃    생성 요청 일자 : " + dto.getCreate_date().toString() + "\n");
		Stream <AccountDTO> account_stream = login_User_account_list.stream();
				
		account_stream.filter(is_tmp_account).forEach(print_account_tmp);
		
		System.out.println("  ┃ ========================");
		if (account_cnt == 0) {
			System.out.println("  ┃  계좌가 [텅] 비어있습니다.");
		}
		System.out.println("  ┃ ");
		System.out.println("  ┃    [ " + loginedUser.getName() + " ]님의 총 자산은 " + account_balance_total_sum + "원 입니다." );
		System.out.println("  ┃ ");
		System.out.println("  ┃                 *");
		System.out.println("  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛\n");
		
	}
	
}
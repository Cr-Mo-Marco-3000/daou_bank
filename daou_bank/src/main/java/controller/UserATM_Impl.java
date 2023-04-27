package controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.Scanner;

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
				System.out.println("\t┏━━━* Daou_Bank ATM ━━━━┓");
				System.out.println("\t┃	     Transfer		┃");
				System.out.println("\t┗━━━━━━━━━━━━━━━━━━━━━━━┛");
				System.out.println("\t  ┃		      ┃");
				System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  *");
				System.out.println("\t  ┃                 *");
				System.out.println("\t  ┃ 받으실분의 계좌 : " + account);
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
		System.out.println("\t┏━━━* Daou_Bank ATM ━━━━┓");
		System.out.println("\t┃	     " + bank + "		┃");
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
					
					System.out.println("");
					System.out.println("\t┏━━━* Daou_Bank ATM ━━━━┓");
					System.out.println("\t┃	     " + check + "		┃");
					System.out.println("\t┗━━━━━━━━━━━━━━━━━━━━━━━┛");
					System.out.println("\t  ┃		      ┃");
					System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  *");
					System.out.println("\t  ┃                 *");
					System.out.println("\t  ┃ ");
					System.out.println("\t  ┃    Name : "+name);
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
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
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
}
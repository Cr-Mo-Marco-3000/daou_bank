package controller;

import java.io.File;

import model.BankAccount;
import model.User;
import view.MenuImpl;

public class UserJoin_Impl implements UserJoin{

public static String userId;
private static UserJoin_Impl userJoin = new UserJoin_Impl();
public static UserATM_Impl userImpl = new UserATM_Impl();
MenuImpl menu = MenuImpl.getInstance();
	
	public static UserJoin_Impl getInstance() {
		if(userJoin == null) {
			userJoin = new UserJoin_Impl();
		}
		return userJoin;
	}
	
	@Override
	public void userJoin() {
		System.out.println("  ┏━━━━* Bank U ━━━━┓");
		System.out.println("  ┃                 *");
		System.out.println("  ┃   Join Account  ┃");
		System.out.println("  ┃                 ┃ ");
		System.out.println("  ┃  뒤로가기 [0]");
		System.out.print("  ┃  ID : ");
		String id = MenuImpl.scan.next();	
		if(id.equals("0")) return;
		if(User.userMap.containsKey(id)) {
			System.out.println("  ┃ 중복된 아이디입니다.");
			System.out.println("  ┃                 *");
			System.out.println("  ┗━━━━━━━━━━━━━━━━━┛\n");
			return;
		}
		System.out.print("  ┃  PW : ");
		String pw = MenuImpl.scan.next();
		if(pw.equals("0")) return;
		System.out.print("  ┃  이름을 입력하세요 :");
		String name = MenuImpl.scan.next();
		if(name.equals("0")) return;
		System.out.println("  ┃ 계좌를 생성 중입니다..");
		System.out.println("  ┃ ");

     /*회원가입 메소드 userJoin() 에서 아이디 중복검사를 거친후 최종적으로 계좌생성이되면*/
     /* 계좌번호 생성 */
		int account = 12345+User.userMap.size();
    /* 회원정보 저장 */ 
		User user = new User(id,account,pw,name);		
    /* 계좌정보 저장 */ 
		BankAccount bank = new BankAccount(account,name,0);
		BankAccount.bankMap.put(account,bank);
		bank.setBankFile();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		User.userMap.put(id,user);
		System.out.println(user.toString());
		
		user.setUserFile();
		
		File file = new File("account.txt");
		// 파일 유/무 판단 
		if (file.isFile()) {
			System.out.println("  ┃ 계좌가입이 완료되었습니다.");
			System.out.println("  ┃                 *");
			System.out.println("  ┗━━━━━━━━━━━━━━━━━┛\n");
		}		
	}

	@Override
	public void userLogin() {
		
		System.out.println("아이디를 입력하세요:");
		String id = MenuImpl.scan.next();
		System.out.println("비밀번호를 입력하세요:");
		String pw = MenuImpl.scan.next();
		if(User.userMap.containsKey(id) && pw.equals(User.userMap.get(id).getUserPw()) ){			
			System.out.println(id+"님 환영합니다.");		
			UserATM_Impl.userId = id;
			menu.userView();
		}else {
			System.out.println("아이디 & 비밀번호를 확인해주세요.");
		}
		return;
	}

	@Override
	public void userList() {
		/* 회원목록 */ 
		int count = 1;
		for(String key:User.userMap.keySet()) {		
			User value = User.userMap.get(key);	
			System.out.printf("[%d] 번 고객님\n",count);
			System.out.println(value.toList()); count++;
		}
	}
}
package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class User {

	public static Map<String, User> userMap = new HashMap<String, User>();
	
	private String userId; // 아이디
	private int accountNumber; // 계좌번호
	private String userPw; // 비번
	private String userName; // 이름

	public User(String userId, int accountNumber, String userPw, String userName) {
		this.userId = userId;
		this.accountNumber = accountNumber;
		this.userPw = userPw;
		this.userName = userName;
	}
	public User() {}
	
	public void getUserFile() {
		String path = "account.txt";
		String id,account,pw,name;
		
		try {
			Scanner scanFile = new Scanner(new FileReader(path));
			
			while(scanFile.hasNext()) {
				String[] line = scanFile.nextLine().split("@");
				id = line[0];
				account = line[1];
				pw = line[2];
				name = line[3];
				User user = new User(id,Integer.parseInt(account),pw,name);
				userMap.put(id,user);			
				
			}
			scanFile.close();
		}catch(Exception e) {
		}
	}
	
	public void setUserFile() {
		try (
				/*	이곳에 객체를 생성하면 try 종료 후 자동으로 close 처리가 됩니다.
				 *  true : 기존 파일에 이어서 작성 (default는 false 입니다.) - 덮어씌기가 됨
				 */
				FileWriter f_writer = new FileWriter("account.txt",true);
				BufferedWriter userWriter = new BufferedWriter( f_writer);
				
				)
		{
				userWriter.write(String.format("%s@%d@%s@%s\n",userId,accountNumber,userPw,userName));	
				userWriter.flush(); //버퍼의 내용을 파일에 쓰기
			
		} catch ( IOException e) {
			System.out.println(e);
		}
		
	}
	
	public static Map<String, User> getUserMap() {
		return userMap;
	}

	public static void setUserMap(Map<String, User> userMap) {
		User.userMap = userMap;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPw() {
		return userPw;
	}

	public void setUserPw(String userPw) {
		this.userPw = userPw;
	}

	// to String()
	public String toString() {
		
		return "  ┃ " + userName + "님 회원가입을 축하합니다 :)\n" + "  ┃ 계좌번호 : " + accountNumber;

	}

	public String toList() {
		String list ="  ┏━━━*User List━━━━┓";
		       list+="\n  ┃ ";
		       list+="\n  ┃ Id : "+ userId;
		       list+="\n  ┃ Pw : "+ userPw;
		       list+="\n  ┃ Ac : "+ accountNumber;
		       list+="\n  ┃                 *";
		       list+="\n  ┗━━━━━━━━━━━━━━━━━┛\n";
		    
		return list;
	}
}
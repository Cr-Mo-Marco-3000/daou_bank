package model;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class BankAccount {
//DTO (Data Transfer Object)

	public static Map<Integer, BankAccount> bankMap = new HashMap<Integer, BankAccount>();

	public int accountNumber; // 계좌번호
	public String userName; // 받는사람
	public int balance; // 계좌 금액
	public String setName; // 보내는사람 (계좌이체)
	
	public BankAccount() {}
	
	public BankAccount(int account, String name, int i){}

	public BankAccount(int accountNumber, String userName, int balance, String setName) {
		// 로그인한 회원의 계좌 인스턴스를 생성할 때 필요한 값
		this.accountNumber = accountNumber;
		this.userName = userName;
		this.balance = balance;
		this.setName = setName;
	}
	
	public void getBankFile() {
		String path = "bank.txt";
		
		try {
			Scanner scanFile = new Scanner(new FileReader(path));
			
			while(scanFile.hasNext()) {
				String[] line = scanFile.nextLine().split("@");
				accountNumber = Integer.parseInt(line[0]);
				userName = line[1];
				balance = Integer.parseInt(line[2]);	
				setName = line[3];
				BankAccount bank = new BankAccount(accountNumber,userName,balance,setName);
				bankMap.put(accountNumber,bank);			
				
			}
			scanFile.close();
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	public void setBankFile() {
		try (
				/*	이곳에 객체를 생성하면 try 종료 후 자동으로 close 처리가 됩니다.
				 *  true : 기존 파일에 이어서 작성 (default는 false 입니다.) - 덮어씌기가 됨
				 */
				FileWriter f_writer = new FileWriter("bank.txt",true);
				BufferedWriter userWriter = new BufferedWriter( f_writer);
				
				)
		{
				userWriter.write(String.format("%d@%s@%d@%s\n",accountNumber,userName,balance,setName));	
				userWriter.flush(); //버퍼의 내용을 파일에 쓰기
			
		} catch ( IOException e) {
			System.out.println(e);
		}
	}
	
	public static Map<Integer, BankAccount> getBankMap() {
		return bankMap;
	}


	public static void setBankMap(Map<Integer, BankAccount> bankMap) {
		BankAccount.bankMap = bankMap;
	}


	public int getAccountNumber() {
		return accountNumber;
	}


	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public int getBalance() {
		return balance;
	}


	public void setBalance(int balance) {
		this.balance = balance;
	}

	public String getSetName() {
		return userName;
	}


	public void setSetName(String setName) {
		this.setName = setName;
	}

	// to String()
	public String toString() {
		return "계좌번호 : "+accountNumber + "\n이름 : "+userName+"\n잔액 : " + balance + "원";

	}
}
package controller;

import java.util.List;

import dto.AccountDTO;
import dto.UserDTO;

public interface UserATM {

	public void userBalance(UserDTO userDTO); 		 //잔액확인
	public void userWithdraw(UserDTO userDTO); 		 //출금
	public void userDeposit(UserDTO loginedUser); 	 //입금
	public void userTransfer(UserDTO loginedUser); 	 //계좌이체
	public boolean userCheckPassWord(String userPw, AccountDTO accountDTO); //비밀번호확인
	public void userReceipt(UserDTO loginedUser); 	 //영수증
	public void userHistory(UserDTO loginedUser); 	 // 통장정리

	public void userAccount(String account, int balance);  // 입금 / 출금
	public void createAccount(int user_key); // 계좌 생성 요청
	
	public void showInfo(UserDTO loginedUser);

}

package controller;

import java.util.List;

import dto.AccountDTO;
import dto.UserDTO;

public interface UserATM {

	public void init();
	public void userBalance(); //잔액확인
	public void userWithdraw(); //출금
	public void userDeposit(); //입금
	public void userTransfer(); //계좌이체
	public boolean userCheckPassWord(String userPw); //비밀번호확인
	public void userReceipt(); //영수증
	public void userHistory(); //계좌거래목록
	
	public void userAccount(int account, String userName, int balance, String setName);  // 입금 / 출금
	public void createAccount(int user_key); // 계좌 생성 요청
	
	public void showInfo(UserDTO loginedUser, List<AccountDTO> login_User_account_list);

}

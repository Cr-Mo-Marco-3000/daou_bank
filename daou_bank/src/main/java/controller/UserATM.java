package controller;

import java.util.List;

import dto.AccountDTO;
import dto.UserDTO;

public interface UserATM {

	public void userBalance(UserDTO userDTO, List<AccountDTO> account_list); 			//잔액확인
	public void userWithdraw(UserDTO userDTO, List<AccountDTO> account_list); 		//출금
	public void userDeposit(UserDTO userDTO, List<AccountDTO> account_list); 			//입금
	public void userTransfer(UserDTO userDTO, List<AccountDTO> account_list); 		//계좌이체
	public boolean userCheckPassWord(String userPw); //비밀번호확인
	public void userReceipt(UserDTO userDTO, List<AccountDTO> account_list); 			//영수증
	public void userHistory(UserDTO userDTO, List<AccountDTO> account_list); 			// 통장정리
	
	public void userAccount(String account, int balance);  // 입금 / 출금
	public void createAccount(int user_key); // 계좌 생성 요청
	
	public void showInfo(UserDTO loginedUser, List<AccountDTO> login_User_account_list);

}

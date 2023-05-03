package dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import dto.AccountDTO;
import dto.UserDTO;
import exception.AccountRequestNotFoundException;
import exception.CustomerEnrolFailException;
import exception.DuplicateCustomerException;

public class EmployeeDAO {
	
	
	// 중복확인
	public int isDuplicatedCustomer (SqlSession session, String userId) {
		int n = 0;
		n = session.selectOne("mybatis.ManagerMapper.isDuplicatedCustomer", userId);
		return n;
	}

	// 계좌 생성 요구 조회 
	public List<AccountDTO> getAccountRequests(SqlSession session) {
	    List<AccountDTO> accountList = session.selectList("mybatis.EmployeeMapper.getAccountRequests");
	    return accountList;
	}
	
	// 고객 승인 
	public int approveCustomer(SqlSession session, String account_num) {
	    int count = session.update("mybatis.EmployeeMapper.approveCustomer", account_num);
	    return count;
	}
	
	// 고객 정보 열람 
	public UserDTO getCustomerById(SqlSession session, String user_id) {
		// getCustomerById라는 쿼리를 실행하여 user_id와 일치하는 고객 정보를 조회하고, 그 결과를 UserDTO 객체로 반환
		// selectOne 메서드는 org.apache.ibatis.session.SqlSession 인터페이스에 정의
		UserDTO user = session.selectOne("mybatis.EmployeeMapper.getCustomerById", user_id);
		return user;
	}
}
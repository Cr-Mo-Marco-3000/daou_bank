package dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import dto.AccountDTO;
import dto.UserDTO;
import exception.AccountRequestNotFoundException;
import exception.CustomerEnrolFailException;
import exception.DuplicateCustomerException;

public class EmployeeDAO {
	

	// 계좌 생성 요구 조회 
	public List<AccountDTO> getAccountRequests(SqlSession session) {
	    List<AccountDTO> accountList = null;
	    accountList = session.selectList("mybatis.EmployeeMapper.getAccountRequests");
	    return accountList;
	}
	


	// 고객 등록
	public int registerCustomer(SqlSession session, UserDTO user) throws CustomerEnrolFailException, DuplicateCustomerException {
	    try {
	        // 중복 여부 검사
	        int count = session.selectOne("mybatis.EmployeeMapper.isDuplicatedCustomer", user.getUserId());
	        if (count > 0) {
	            throw new DuplicateCustomerException("이미 등록된 고객입니다.");
	        }

	        // 고객 등록
	        // UserDTO 객체를 매개변수로 받아 새로운 고객을 등록 
	        // registerCustomer라는 쿼리를 실행하여 새로운 고객 정보를 고객 테이블에 삽입하고, 그 결과로 영향받은 레코드 수를 반환
	        int n = session.insert("mybatis.EmployeeMapper.registerCustomer", user);

	        // insert 메서드가 반환하는 값은 삽입된 레코드 수
	        // 이 값이 1이 아니면, 고객 등록에 실패한 것으로 판단하여 CustomerEnrolFailException 예외를 발생
	        if (n != 1) {
	            throw new CustomerEnrolFailException("고객 등록에 실패했습니다.");
	        }

	        return n;
	    } catch (DuplicateCustomerException e) {
	        throw e;
	    } catch (Exception e) {
	        throw new CustomerEnrolFailException("고객 등록에 실패했습니다.");
	    }
	}

	// 고객 승인 
	public int approveCustomer(SqlSession session, String user_id) {
	    int count = session.update("mybatis.EmployeeMapper.approveCustomer", user_id);
	    return count;
	}
	
	
	
	// 고객(계좌) 승인 거절
 	public int rejectAccountRequest(SqlSession session, String user_id){
 	    int result = 0;
 	    try {
 	        // Check if account request exists for the given user_id
 	        int count = session.selectOne("mybatis.EmployeeMapper.getAccountRequests", user_id);
 	        if (count != 1) {
 	            throw new AccountRequestNotFoundException("해당 계좌 생성 요청이 존재하지 않습니다.");
 	        }

 	        // Delete the account request
 	        result = session.delete("mybatis.EmployeeMapper.deleteAccountRequest", user_id);
 	        session.commit();

 	        System.out.println("계좌 생성 요청이 거절되었습니다.");
 	    } catch (AccountRequestNotFoundException e) {
 	        System.out.println(e.getMessage());
 	    } catch (Exception e) {
 	        System.out.println("계좌 생성 요청 거절 중 예외가 발생했습니다: " + e.getMessage());
 	    }
 	    return result;
 	}

	// 고객 정보 열람 
	public UserDTO getCustomerById(SqlSession session, String user_id) {
		UserDTO user = null;
		// getCustomerById라는 쿼리를 실행하여 user_id와 일치하는 고객 정보를 조회하고, 그 결과를 UserDTO 객체로 반환
		// selectOne 메서드는 org.apache.ibatis.session.SqlSession 인터페이스에 정의
		user = session.selectOne("mybatis.EmployeeMapper.getCustomerById", user_id);
		return user;
	}
	
	// 전체 회원 정보 조회
	public List<UserDTO> getAllCustomers(SqlSession session) {
		List<UserDTO> userList = null;
		// getAllCustomers라는 쿼리를 실행하여 고객 정보 전체를 조회하고, 그 결과를 List<UserDTO> 객체로 반환
		// selectList 메서드는 쿼리를 실행하여 결과값을 여러 개 가져올 때 사용하는 메서드 
		// 조회 결과를 List 컬렉션으로 반환
		userList = session.selectList("mybatis.EmployeeMapper.getAllCustomers");
		return userList;
	}
}
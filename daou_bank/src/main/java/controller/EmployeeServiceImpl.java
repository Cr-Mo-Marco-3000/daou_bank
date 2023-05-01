package controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import dao.EmployeeDAO;
import dto.UserDTO;
import exception.AccountRequestNotFoundException;
import exception.AllCustomerInfocheckFailException;
import exception.CustomerAccountApprovalException;
import exception.CustomerAccountRejectionException;
import exception.CustomerEnrolFailException;
import exception.CustomerInfocheckFailException;
import exception.DuplicateCustomerException;


public class EmployeeServiceImpl implements EmployeeService {

	// 기본설정
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
 	

 	
 

 	 @Override
     public List<UserDTO> getAccountRequests(String isTemporary) throws AccountRequestNotFoundException {
         try (SqlSession session = sqlSessionFactory.openSession()) {
             EmployeeDAO dao = new EmployeeDAO();
             List<UserDTO> userList = dao.getAccountRequests(session, isTemporary);
             if (userList == null || userList.isEmpty()) {
                 throw new AccountRequestNotFoundException("계좌 생성 요구 조회 실패: 조회된 데이터가 없습니다.");
             }
             return userList;
         }
     }
 	 
 	@Override
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
 	
 	 @Override
     public void approveCustomer(String user_id) throws CustomerAccountApprovalException {
         try (SqlSession session = sqlSessionFactory.openSession()) {
             EmployeeDAO dao = new EmployeeDAO();
             int count = dao.approveCustomer(session, user_id);
             if (count != 1) {
                 throw new CustomerAccountApprovalException("고객 계정 승인 실패: 대포통장 의심 거래자입니다. 다시 확인하세요. ");
             }
             session.commit();
         }
     }

 	@Override
 	public int rejectAccountRequest(SqlSession session, String user_id) {
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

 	
 	
 	@Override
 	public UserDTO getCustomerById(String user_id) throws CustomerInfocheckFailException {
 	    try (SqlSession session = sqlSessionFactory.openSession()) {
 	        EmployeeDAO dao = new EmployeeDAO();
 	        UserDTO user = dao.getCustomerById(session, user_id);
 	        if (user == null) {
 	            throw new CustomerInfocheckFailException("고객 정보 조회 실패: 해당 ID의 고객이 존재하지 않습니다.");
 	        }
 	        return user;
 	    }
 	}

 	@Override
 	public List<UserDTO> getAllCustomers() throws AllCustomerInfocheckFailException {
 	    try (SqlSession session = sqlSessionFactory.openSession()) {
 	        EmployeeDAO dao = new EmployeeDAO();
 	        List<UserDTO> userList = dao.getAllCustomers(session);
 	        if (userList == null || userList.isEmpty()) {
 	            throw new AllCustomerInfocheckFailException("모든 고객 정보 조회 실패: 조회된 데이터가 없습니다.");
 	        }
 	        return userList;
 	    }
 	}
}

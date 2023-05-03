package controller;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import dao.EmployeeDAO;
import dto.AccountDTO;
import dto.UserDTO;
import exception.AccountRequestNotFoundException;
import exception.AllCustomerInfocheckFailException;
import exception.CustomerAccountApprovalException;
import exception.CustomerAccountRejectionException;
import exception.CustomerEnrolFailException;
import exception.CustomerInfocheckFailException;
import exception.DuplicateCustomerException;
import exception.EmployeeCreationFailException;

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
 	

 
    // 계좌 생성 요청 조회 
 	@Override
 	public List<AccountDTO> getAccountRequests()  {
 	    SqlSession session = sqlSessionFactory.openSession();
 	    EmployeeDAO dao = new EmployeeDAO();
 	    List<AccountDTO> accountList = null;
 	    accountList = dao.getAccountRequests(session);
 	    try {
 	        if (accountList == null || accountList.isEmpty()) {
 	            throw new AccountRequestNotFoundException("계좌 생성 요구 조회 실패: 조회된 데이터가 없습니다.");
 	        }
 	        if (session != null) {
 	            session.commit();
 	        }
 	    } catch (AccountRequestNotFoundException e) {
 	        System.out.println(e.getMessage());
 	        if (session != null);
 	    } catch (Exception e) { 
 	        System.out.println("계좌 생성 요구 조회 중 예외가 발생했습니다: " + e.getMessage());
 	        if (session != null);
 	    } finally {
 	        if (session != null) {
 	            session.close();
 	        }
 	    }
 	    return accountList;
 	}

 	// 고객 정보 열람 
 	@Override
 	public UserDTO getCustomerById(String user_id) throws CustomerInfocheckFailException {
 	SqlSession session = sqlSessionFactory.openSession();
	 	try {
	 	EmployeeDAO dao = new EmployeeDAO();
	 	UserDTO user = dao.getCustomerById(session, user_id);
	 	if (user == null) {
	 		throw new CustomerInfocheckFailException("고객 정보 조회 실패: 해당 ID의 고객이 존재하지 않습니다.");
	 	}
	 	    session.commit();
	 		return user;
	 	} catch (CustomerInfocheckFailException e) {
	 		System.out.println( e.getMessage());
	 	throw e; // 예외를 호출한 측으로 
	 	} finally {
	 		session.close();
	 	}
 	}
  	    
 	
 	// 고객 계좌 승인
 	@Override
 	public void approveCustomer(String account_num)  {
 	SqlSession session = sqlSessionFactory.openSession();
 	try {
 		EmployeeDAO dao = new EmployeeDAO();
 		int count = dao.approveCustomer(session, account_num);
 		session.commit();
 	if (count != 1) {
 		throw new CustomerAccountApprovalException("고객 계정 승인 실패: 대포통장 의심 거래자입니다. 다시 확인하세요. ");
 	}
 		session.commit();
 	} catch (Exception e) {
 		session.rollback();
 	} finally {
 		session.close();
 	}
 	}  


	 	

	 	
}

package controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import dao.ManagerDAO;
import dto.UserDTO;
import exception.EmployeeCreationFailException;
import exception.HandOverManagerException;

public class ManagerServiceImpl {
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
	
	
	// 직원 등록
	public int registerEmployee (UserDTO user) throws EmployeeCreationFailException{
		int num = 0;
		SqlSession session = sqlSessionFactory.openSession();
		try {
			ManagerDAO dao = new ManagerDAO();
			// 중복 아이디 확인
			num = dao.isDuplicatedEmployee(session, user.getUser_id());
			if (num == 1) {
				throw new EmployeeCreationFailException("중복되는 아이디가 있습니다.");
			}
			// 직원 등록
			num = dao.registerEmployee(session, user);
			if (num == 0) {
				throw new EmployeeCreationFailException("직원 등록에 실패했습니다.");
			}
			session.commit();
		} finally {
			session.rollback();
			session.close();
		}
		return num;
	};
	
	// 관리자 권한 인계
	public int handOverManager (UserDTO user, String targetEmployee) throws HandOverManagerException {
		int num = 0;
		SqlSession session = sqlSessionFactory.openSession();
		try {
			ManagerDAO dao = new ManagerDAO();
			num = dao.isDuplicatedEmployee(session, targetEmployee);
			if (num == 0) {
				throw new HandOverManagerException("해당 아이디를 가진 직원이 없습니다.");
			}
			
			num = dao.verifyEmployee(session, targetEmployee);
			if (num == 0) {
				throw new HandOverManagerException("해당 유저는 일반 직원이 아닙니다.");
			}
			
			num = dao.makeManager(session, targetEmployee);
			if (num == 0) {
				throw new HandOverManagerException("직원을 관리자로 만들 수 없습니다.");
			}
			
			num = dao.giveUpManager(session, user.getUser_id());
			if (num == 0) {
				throw new HandOverManagerException("귀하의 신분 변경이 실패했습니다.");
			}
			session.commit();

		} finally {
			session.rollback();
			session.close();
		}
		return num;
	};
	
	// 직원 삭제
}

package controller;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import dao.ManagerDAO;
import dto.UserDTO;

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
	public int registerEmployee (UserDTO user) {
		int num = 0;
		SqlSession session = sqlSessionFactory.openSession();
		try {
			ManagerDAO dao = new ManagerDAO();
			num = dao.registerEmployee(session, user);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return num;
	};
	
	// 관리자 권한 인계
	
	
	// 직원 삭제
}

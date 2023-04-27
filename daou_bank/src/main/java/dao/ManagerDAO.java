package dao;

import org.apache.ibatis.session.SqlSession;

import dto.UserDTO;

public class ManagerDAO {
	
	// 중복확인
	public int isDuplicatedEmployee (SqlSession session, String userId) {
		int n = 0;
		n = session.selectOne("selectEmployeeId", userId);
		return n;
	}
	
	// 직원등록
	public int registerEmployee (SqlSession session, UserDTO user) {
		int n = 0;
		n = session.insert("mybatis.ManagerMapper.registerEmployee", user);
		return n;
	}
	
	// 직원이 일반 직원인지 확인
	public int verifyEmployee (SqlSession session, String targetEmployee) {
		int n = 0;
		n = session.selectOne("mybatis.ManagerMapper.verifyEmployee", targetEmployee);
		return n;
				
	}
	
	// 직원을 매니저로 바꿈
	public int makeManager(SqlSession session, String targetEmployee) {
		int n = 0;
		n = session.update("mybatis.ManagerMapper.makeManager", targetEmployee);
		return n;
	}
	
	// 직원을 일반 직원으로 바꿈
	public int giveUpManager(SqlSession session, String user) {
		int n = 0;
		n = session.update("mybatis.ManagerMapper.giveUpManager", user);
		return n;
	}
	
	// 직원을 삭제
	public int deleteEmployee(SqlSession session, String user) {
		int n = 0;
		n = session.delete("mybatis.ManagerMapper.deleteEmployee", user);
		return n;
	}
}

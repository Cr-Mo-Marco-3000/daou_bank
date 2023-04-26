package dao;

import org.apache.ibatis.session.SqlSession;

import dto.UserDTO;

public class ManagerDAO {
	public int registerEmployee (SqlSession session, UserDTO user) {
		int n = 0;
		n = session.insert("mybatis.ManagerMapper.registerEmployee", user);
		return 0;
	}
}

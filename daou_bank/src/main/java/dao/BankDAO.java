package dao;

import org.apache.ibatis.session.SqlSession;

public class BankDAO {
	
	public int deleteOutdatedTemporaryAccount (SqlSession session) {
		int n = 0;
		System.out.println("딜리트 시작");
		n = session.delete("mybatis.BankMapper.deleteOutdatedTemporaryAccount");
		System.out.println("딜리트 끝");
		return n;
	}
	
}

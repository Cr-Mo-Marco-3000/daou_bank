package controller;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import dao.BankDAO;

public class BankServiceImpl {
	
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
	
	public int deleteOutdatedTemporaryAccount () {
		int num=0;
		SqlSession session = sqlSessionFactory.openSession();
		try {
			BankDAO dao = new BankDAO();
			System.out.println(num + "되나?");
			num = dao.deleteOutdatedTemporaryAccount(session);
			session.commit();
			System.out.println("서비스 실행!");
		} finally {
			session.rollback();
			session.close();
		}
		return num;
	};
}

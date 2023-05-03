package dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import dto.AccountDTO;
import dto.TransactionDTO;
import dto.UserDTO;

public class BankDAO {

		// SQL 세션 생성 ===========================================
		static SqlSessionFactory sqlSessionFactory;
		static {
			String resource = "mybatis/Configuration.xml";
			InputStream inputStream = null;
			 try {
				inputStream = Resources.getResourceAsStream(resource);
			} catch (IOException e) {
				e.printStackTrace();
			}
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		
		}// static블럭 end
		
		
		public int deleteOutdatedTemporaryAccount (SqlSession session) {
			int n = 0;
			n = session.delete("mybatis.BankMapper.deleteOutdatedTemporaryAccount");
			return n;
		}

		// 계좌 잔액 업데이트
		public int updateBalance(String account, int balance) {
			SqlSession session = sqlSessionFactory.openSession();
			
			AccountDTO dto = new AccountDTO();
			
			dto.setAccount_num(account);
			dto.setBalance(balance);

			int result = session.update("updateBalance", dto);
			
			session.commit();
			
			return result;
		}
			
}
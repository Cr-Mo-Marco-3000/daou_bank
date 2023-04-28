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
	
		TransactionDTO dto = new TransactionDTO();

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
		
		
		// 총 거래 내역
		public List<TransactionDTO> transactionHistory(String name) {
			SqlSession session = sqlSessionFactory.openSession();
			
			List<TransactionDTO> history = session.selectList("transactionHistory", name);
			
			return history;
		}

		// 계좌 잔액 업데이트
		public int updateBalance(AccountDTO dto) {
			SqlSession session = sqlSessionFactory.openSession();
			
			int result = session.update("updateBalance", dto);
			
			return result;
		}
			
}

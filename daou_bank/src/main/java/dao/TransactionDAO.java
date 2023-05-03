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

public class TransactionDAO {

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
		public List<TransactionDTO> transaction_history(String account_num) {
			SqlSession session = sqlSessionFactory.openSession();
			
			List<TransactionDTO> history = session.selectList("transaction_history", account_num);
			
			return history;
		}

		// 거래 내역 삽입
		public void insert_transaction(TransactionDTO dto) {
			SqlSession session = sqlSessionFactory.openSession();
			
			int transaction = session.insert("insert_transaction", dto);
			
			session.commit();
		}
			
}
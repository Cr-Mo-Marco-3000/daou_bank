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
import dto.UserDTO;

public class DBDAO {

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
			sqlSessionFactory =
			  new SqlSessionFactoryBuilder().build(inputStream);
		
		}// static블럭 end
		
		// 동일한 아이디가 있는지 확인하는 메서드 (중복 ID 체크)
		public boolean check_Id(SqlSession session, String id) {
			boolean Is_in_userid = false;
			List<UserDTO> select_check_id = session.selectList("ID_dupli_check",id);
			if (select_check_id.size() != 0)
				Is_in_userid = true;
			return Is_in_userid;
		}
		
		// 로그인 정보가 DB에 존재하는지 확인하는 메서드
		public boolean check_login_user_db(SqlSession session, UserDTO dto) {
			
			boolean is_user_in_db = true;
			List<UserDTO>select_check_user = session.selectList("User_check", dto);	
			if (select_check_user.size() != 0)
				is_user_in_db = false;
			return is_user_in_db;
		}

		
		// 회원가입이 완료된 사용자의 정보를 DB에 저장하는 메서드
		public int insert_user_db(SqlSession session, UserDTO dto) {
			int n = 0;
			n = session.insert("User_sign_up", dto);
			session.commit();
			return n;
		}
		
		
		// 로그인 정보의 UserDTO를 반환하는 메서드
		public UserDTO login_user_info(SqlSession session, UserDTO dto) {
			
			UserDTO login_user_info = session.selectOne("Login_user_info_Map", dto);			
			return login_user_info;
		}
		
		// 로그인 정보의 [ Account0, Account1, ... ] 리스트를 반환하는 메서드
		public List<AccountDTO> login_user_account(SqlSession session, UserDTO dto) {
			
			List <AccountDTO> login_user_account_lst = session.selectList("Login_user_account_list",dto);
			return login_user_account_lst;
		}
		
		// 개설 요청 계좌를 DB에 저장하는 메서드
		public int insert_account_db(SqlSession session, AccountDTO dto) {
			int n = 0;
			n = session.insert("Account_create", dto);
			session.commit();
			return n;
		}
					
}

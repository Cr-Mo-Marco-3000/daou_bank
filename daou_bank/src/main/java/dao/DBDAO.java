package dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import dto.AccountDTO;
import dto.UserDTO;

public class DBDAO {

		// 아모와를 위한 랜덤 시드 생성
		private String seed_by_dba = "19860109";
		
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
		public boolean check_dupli_user_db(SqlSession session, UserDTO dto) {
			
			boolean is_user_in_db = false;
			System.out.println(dto);
			List<UserDTO>select_check_user = session.selectList("User_check_sign_up", dto);	
			System.out.println(select_check_user.size());
			if (select_check_user.size() != 0)
				is_user_in_db = true;
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
			
			UserDTO login_user_info = session.selectOne("Login_user_info", dto);			
			return login_user_info;
		}
		
		// 로그인 정보의 [ Account0, Account1, ... ] 리스트를 반환하는 메서드
		public List<AccountDTO> login_user_account(SqlSession session, UserDTO dto) {
			List <AccountDTO> login_user_account_lst = session.selectList("Login_user_account_list",dto);
			System.out.println(login_user_account_lst.toString());
			return login_user_account_lst;
		}
				
		// 로그인 정보의 [ tmp_Account0, tmp_Account1, ... ] 리스트를 반환하는 메서드
		public List<AccountDTO> login_user_tmp_account(SqlSession session, UserDTO dto) {
			List <AccountDTO> login_user_tmp_account_lst = session.selectList("Login_user_tmp_account_list",dto);
			System.out.println(login_user_tmp_account_lst.toString());
			return login_user_tmp_account_lst;
		}
		
		// 개설 요청 계좌를 DB에 저장하는 메서드
		public int insert_account_db(SqlSession session, AccountDTO dto) {
			int n = 0;
			n = session.insert("Account_create", dto);
			session.commit();
			return n;
		}
		
		// 개설 요청을 위한 임시 계좌번호를 생성하는 메서드
		public String create_tmp_account_num(SqlSession session, AccountDTO dto) {
			String tmp_account_num = "";
			tmp_account_num = session.selectOne("create_account_tmp_num", dto.getAccount_num());
			return tmp_account_num;
		}
		
		
	   // 암호화를 위한 Random Seed 발생
		  public int create_random_seed() {
			  int str_len = seed_by_dba.length();
			  int tmp=0;
			  String tmp_str = "";
			  tmp_str = str_len+""+seed_by_dba;
			  for (int c_idx = 0 ; c_idx < str_len;c_idx++) {
				  if (c_idx %2 ==0) {
					  tmp+=Integer.parseInt("" + tmp_str.charAt(c_idx));
				  }
			  }
			  while(tmp>10) {
				  tmp_str = "" + tmp;
				  tmp = 0;
				  for (int idx = 0 ; idx < tmp_str.length();idx++) {
					  tmp += Integer.parseInt("" + tmp_str.charAt(idx));
				  }
			  }
			  System.out.println(tmp);
			  return tmp;
		  }
		  
	   // 암호화하는 메서드
		  public String Encryptonize_pw(String input_pw, int seed) {
			  System.out.println(input_pw);
			  
			  char []ch_pw_token = input_pw.toCharArray();
			  String Encrypt_pw;
			  int idx = 0;
			  for (char token:ch_pw_token) {
				  ch_pw_token[idx]-=6;
				  idx++;
				  }
			  Encrypt_pw = String.valueOf(ch_pw_token);
			  System.out.println(Encrypt_pw);
			  
			  
			  return Encrypt_pw;
		  }
		  		  
	   // 복호화하는 메서드
		  public String Decryptonize_pw(String db_pw, int seed) {
			  char []ch_pw_token = db_pw.toCharArray();
			  String Decrypt_pw;
			  int idx = 0;
			  for (char token:ch_pw_token) {
				  ch_pw_token[idx]+=6;
				  idx++;
			  }
			  Decrypt_pw = String.valueOf(ch_pw_token);
			  return String.valueOf(Decrypt_pw );
		  }

	  
		
					
}

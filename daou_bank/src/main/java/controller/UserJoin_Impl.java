package controller;

import java.io.File;
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

import dao.DBDAO;
import dto.AccountDTO;
import dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.BankAccount;
import model.User;
import view.Menu;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserJoin_Impl implements UserJoin{

public static String userId;
private static UserJoin_Impl userJoin = new UserJoin_Impl();
public static UserATM_Impl userImpl = new UserATM_Impl();
Menu menu = Menu.getInstance();
	
	public static UserJoin_Impl getInstance() {
		if(userJoin == null) {
			userJoin = new UserJoin_Impl();
		}
		return userJoin;
	}
	
	// SQL 세션을 위한 기본설정 =============================================================================
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
	
	
	// ==================================================================================================================
	// 회원 가입 메서드
	@Override
	public void userSignup() {
		SqlSession session = sqlSessionFactory.openSession();
		
		System.out.println("");
		System.out.println("\t┏━━━* Daou_Bank ATM ━━━━┓");
		System.out.println("\t┃        회 원 가 입      	┃");
		System.out.println("\t┗━━━━━━━━━━━━━━━━━━━━━━━┛");
		System.out.println("\t  ┃		      ┃");
		System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  *");
		System.out.println("\t  ┃		      ┃");
		System.out.println("\t  ┃  뒤로가기 [0]");
		System.out.print("\t  ┃  ID : ");
		String id = Menu.scan.next();	
		if(id.equals("0")) return;
		DBDAO dbcheck = new DBDAO();
		if(dbcheck.check_Id(session, id)) {
			System.out.println("\t  ┃ 중복된 아이디입니다.");
			System.out.println("\t  ┃                 *");
			System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  ┃");
			System.out.println("\t  ┃                   *");
			System.out.println("\t  ┗━━━━━━━━━━━━━━━━━━━┛\n");
			return;
		}
		System.out.print("\t  ┃  PW : ");
		String pw = Menu.scan.next();
		if(pw.equals("0")) return;
		System.out.print("\t  ┃  이름을 입력하세요 : ");
		String name = Menu.scan.next();
		if(name.equals("0")) return;
		System.out.print("\t  ┃  생년월일을 입력하세요 : ");
		String birth_day = Menu.scan.next();
		if(birth_day.equals("0")) return;
		System.out.println("\t  ┃ ");

     /*회원가입 메소드 userSingup() 에서 아이디 중복검사를 거친후 최종적으로 계좌생성이되면*/
    /* 회원정보 저장 */ 
		UserDTO userdto = new UserDTO(id,pw,name,birth_day);
		
		if (dbcheck.check_dupli_user_db(session, userdto)) {
			System.out.println("\t  ┃ 이미 존재하는 회원입니다.");
			System.out.println("\t  ┃                 *");
			System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  ┃");
			System.out.println("\t  ┃                   *");
			System.out.println("\t  ┗━━━━━━━━━━━━━━━━━━━┛\n");
			return;
		}
		
		
		System.out.println("\t  ┃ 회원가입이 완료되었습니다.");
		System.out.println("\t  ┃                 *");
		System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  ┃");
		System.out.println("\t  ┃                   *");
		System.out.println("\t  ┗━━━━━━━━━━━━━━━━━━━┛\n");
		
	    /* 계좌정보 저장 */ 
		DBDAO db_dao = new DBDAO();
		System.out.println(userdto.getUser_password());
		userdto.setUser_password(db_dao.Encryptonize_pw(userdto.getUser_password(),db_dao.create_random_seed()));		
		System.out.println(userdto.getUser_password());
		
		db_dao.insert_user_db(session,userdto);
		session.close();
	}

	
	// ==================================================================================================================
	// 로그인
	@Override
	public void userLogin(UserDTO loginedUser, List<AccountDTO> login_User_account_list ) {
		SqlSession session = sqlSessionFactory.openSession();		
		
		System.out.println("아이디를 입력하세요:");
		String id = Menu.scan.next();
		System.out.println("비밀번호를 입력하세요:");
		String pw = Menu.scan.next();
		
		UserDTO userdto = new UserDTO(id,pw);
		DBDAO db_login_dao = new DBDAO();
		userdto.setUser_password(db_login_dao.Encryptonize_pw(userdto.getUser_password(), db_login_dao.create_random_seed()));
		loginedUser = db_login_dao.login_user_info(session, userdto);

		if(loginedUser != null ){	
			login_User_account_list = db_login_dao.login_user_account(session, loginedUser);
			System.out.println(" [ " + loginedUser.getName()+" ] 님 환영합니다.");
			UserATM_Impl.userId = id;
			menu.userView(loginedUser,login_User_account_list);
		}else {
			System.out.println("아이디 & 비밀번호를 확인해주세요.");
		}
		session.close();
		return;
	}
	
	@Override
	public void userList() {
		/* 회원목록 */ 
		int count = 1;
		for(String key:User.userMap.keySet()) {		
			User value = User.userMap.get(key);	
			System.out.printf("[%d] 번 고객님\n",count);
			System.out.println(value.toList()); count++;
		}
	}
	
	// ==================================================================================================================
	// DB_check
	
	
}
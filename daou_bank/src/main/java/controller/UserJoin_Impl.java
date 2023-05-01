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
		UserDTO userdto = new UserDTO();
		DBDAO db_dao = new DBDAO();
		
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
		
		if(db_dao.check_Id(session, id)) {
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

		
		try {
			/* 회원정보 저장 */ 
			userdto = new UserDTO(id,pw,name,birth_day);
			/*회원가입 메소드 userSingup() 에서 중복된 아이디가 있으면*/
			if (db_dao.check_dupli_user_db(session, userdto)) {
				System.out.println("\t  ┃ 이미 존재하는 회원입니다.");
				System.out.println("\t  ┃                 *");
				System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  ┃");
				System.out.println("\t  ┃                   *");
				System.out.println("\t  ┗━━━━━━━━━━━━━━━━━━━┛\n");
				return;
			}
			
			/*회원가입 메소드 userSingup() 에서 아이디 중복검사를 거친후 최종적으로 계좌생성이 되면*/
			System.out.println("\t  ┃ 회원가입이 완료되었습니다.");
			System.out.println("\t  ┃                 *");
			System.out.println("\t  ┃ ━━━━━━━━━━━━━━━━  ┃");
			System.out.println("\t  ┃                   *");
			System.out.println("\t  ┗━━━━━━━━━━━━━━━━━━━┛\n");

			/* 계좌정보 저장 */ 
			userdto.setUser_password(db_dao.Encryptonize_pw(userdto.getUser_password(),db_dao.create_random_seed()));		
			db_dao.insert_user_db(session,userdto);
			session.commit();
			
		} catch(Exception e) {
			System.out.println("회원가입 정보가 잘 못 되었습니다.");
			session.rollback();
		} finally {
			session.close();
		}
		
	}
	
	// ==================================================================================================================
	// 로그인
	@Override
	public void userLogin(UserDTO logined_User) {
		
		SqlSession session = sqlSessionFactory.openSession();		
		
		DBDAO user_login_dao = new DBDAO();
		UserDTO userdto;
		
		System.out.println("아이디를 입력하세요:");
		String id = Menu.scan.next();
		System.out.println("비밀번호를 입력하세요:");
		String pw = Menu.scan.next();
		try {
			userdto = new UserDTO(id,pw);
			userdto.setUser_password(user_login_dao.Encryptonize_pw(userdto.getUser_password(), user_login_dao.create_random_seed()));
			logined_User = user_login_dao.login_user_info(session, userdto);

			if(logined_User != null ){	
				System.out.println(" [ " + logined_User.getName()+" ] 님 환영합니다.");
				UserATM_Impl.userId = id;
				if (logined_User.getType().equals("Customer")) {
					menu.userView(logined_User);
				}
				else {
					menu.EmployeeView();
				}
			}else {
				System.out.println("아이디 & 비밀번호를 확인해주세요.");
			}
		} catch(Exception e) {
			System.out.println("로그인 정보가 잘못 되었습니다. 다시 입력 해주세요. ");
		} finally {
			session.close();
		}
		return;
		
	}
}
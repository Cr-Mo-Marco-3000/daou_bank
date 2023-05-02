package controller;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import dao.DBDAO;
import dto.UserDTO;
import view.Menu;

class UserJoin_ImplTest {

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
    } 
	
	@Test
	void test() {
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
			assertEquals(false, db_dao.check_dupli_user_db(session, userdto));
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
			System.out.println("회원가입 정보가 잘못되었습니다.");
			session.rollback();
		} finally {
			session.close();
		}

	}

}

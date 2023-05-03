package dto;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.io.Resources;
import org.junit.jupiter.api.Test;

/*
 * 비밀번호 암호화 테스트 메서드 2
 * 
 * */

class UserDTOTest {

	// SQL 세션 생성 ===========================================
	private static String Encryption_seed;
			
	static {
		Properties properties = new Properties();
		String resource_credential = "credential/Credential.properties";
		try {
			Reader reader = Resources.getResourceAsReader(resource_credential);
			properties.load(reader);
			Encryption_seed = properties.getProperty("credential.passwd_Encryption_seed");
		} catch(IOException e) {
			e.printStackTrace();
		}
	}// static블럭 end
	
	
	int create_random_seed() {
		  String seed = Encryption_seed.toString();
		  int str_len = seed.length();
		  int tmp=0;
		  String tmp_str = "";
		  tmp_str = str_len+seed;
		 
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
		  return tmp;
	  }
	
	   // 암호화하는 메서드
		  public String Encryptonize_pw(String input_pw, int seed) {
			  char []ch_pw_token = input_pw.toCharArray();
			  String Encrypt_pw;
			  int idx = 0;
			  for (char token:ch_pw_token) {
				  ch_pw_token[idx]-=(seed+idx);
				  idx++;
				  }
			  Encrypt_pw = String.valueOf(ch_pw_token);
			  
			  return Encrypt_pw;
		  }
		  		  
	   // 복호화하는 메서드
		  public String Decryptonize_pw(String db_pw, int seed) {
			  char []ch_pw_token = db_pw.toCharArray();
			  String Decrypt_pw;
			  int idx = 0;
			  for (char token:ch_pw_token) {
				  ch_pw_token[idx]+=(seed+idx);
				  idx++;
			  }
			  Decrypt_pw = String.valueOf(ch_pw_token);
			  return String.valueOf(Decrypt_pw );
		  }
	  
	  // 정상 출력 예정되는 값 "+,-+,-" 과 Encryptonize_pw 메서드를 통과한 값 비교
	  @Test
	  void testEncrypt () { // rnlduaQhWkr123
		  System.out.println("입력되는 암호 문자열:");
		  String checkString = "1234";
		  System.out.println(checkString);
		  System.out.println();
		  
		  System.out.println("암호화된 입력 암호:");
		  String armho = Encryptonize_pw(checkString, create_random_seed());
		  System.out.println(armho);
		  System.out.println();
		  
		  System.out.println("복호화된 암호화된 입력 암호:");
		  String bokho = Decryptonize_pw(Encryptonize_pw(checkString, create_random_seed()), create_random_seed());
		  System.out.println(bokho);
		  assertEquals(checkString, bokho);
	  }
}

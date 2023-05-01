package dto;

import java.util.Calendar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

	String account_num;		// primary_key
	String account_password;
	String is_temporary;
	int balance;
	String create_date;
	int user_key;			
	
	
	public AccountDTO(int user_key, String password) {
		
		Calendar cal = Calendar.getInstance();
		
		this.user_key = user_key;
		this.account_num = "000-000-000000";
		this.account_password = password;
		this.balance = 0;
		this.is_temporary = "1";
		this.create_date = "" + cal.get(Calendar.YEAR) + ( cal.get(Calendar.MONTH) + 1 <10? "0"+ (cal.get(Calendar.MONTH) + 1) : cal.get(Calendar.MONTH) + 1) + cal.get(Calendar.DAY_OF_MONTH);
	}

}
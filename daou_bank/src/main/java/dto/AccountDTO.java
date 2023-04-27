package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

	int account_num;		// primary_key
	int user_key;			
	int account_password;
	int balance;
	String is_temporary;
	String create_date;
	
}
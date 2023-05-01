package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserDTO {
	int user_key;		// primary_key
	String user_id;		// user_id
	String user_password;
	String type;
	String name;
	String birth_day;
	
	
	public UserDTO(String user_id, String user_password, String name, String birth_day) {
		this.user_key = 0;
		this.type = "Customer";
		this.user_id = user_id;
		this.user_password = user_password;
		this.name = name;
		this.birth_day = birth_day;
	}


	public UserDTO(String user_id, String user_password) {
		this.user_key = 0;
		this.type = "Customer";
		this.user_id = user_id;
		this.user_password = user_password;
		this.name = "";
		this.birth_day = "";
	}

	 
}
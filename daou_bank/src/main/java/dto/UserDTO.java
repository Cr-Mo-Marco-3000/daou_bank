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
	int age;
}

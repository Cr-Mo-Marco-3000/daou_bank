package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

	int transaction_key;		// primary_key
	int transaction_type;			
	int transaction_amount;
	int transaction_datetime;
	String account_num;
	String transaction_send_name;
	String transaction_take_name;
	String account_counterpart_num;
	
}
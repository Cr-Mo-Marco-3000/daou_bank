package controller;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import dto.AccountDTO;
import dto.UserDTO;
import exception.AccountRequestNotFoundException;
import exception.AllCustomerInfocheckFailException;
import exception.CustomerAccountApprovalException;
import exception.CustomerAccountRejectionException;
import exception.CustomerEnrolFailException;
import exception.CustomerInfocheckFailException;
import exception.DuplicateCustomerException;

public interface EmployeeService { 

	List<AccountDTO> getAccountRequests() throws AccountRequestNotFoundException; 
	
    int registerCustomer(UserDTO user) throws CustomerEnrolFailException, DuplicateCustomerException; 

    void approveCustomer(String user_id) throws CustomerAccountApprovalException;

    UserDTO getCustomerById(String user_id) throws CustomerInfocheckFailException;

    List<UserDTO> getAllCustomers() throws AllCustomerInfocheckFailException;

	int rejectAccountRequest(String user_id) throws CustomerAccountRejectionException;

}

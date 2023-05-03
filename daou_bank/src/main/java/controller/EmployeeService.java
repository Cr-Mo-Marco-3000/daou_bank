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
import exception.EmployeeCreationFailException;

public interface EmployeeService { 

	List<AccountDTO> getAccountRequests() throws AccountRequestNotFoundException; // 계좌 생성 요청 조회

    void approveCustomer(String user_id) throws CustomerAccountApprovalException; // 고객 계좌 승인 

    UserDTO getCustomerById(String user_id) throws CustomerInfocheckFailException; // 고객 정보 조회



}

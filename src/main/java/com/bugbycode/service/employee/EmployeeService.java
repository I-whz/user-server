package com.bugbycode.service.employee;

import com.bugbycode.module.employee.Employee;

public interface EmployeeService {

	public Employee queryByUserName(String username);
	
	public int insert(Employee emp);
	
	public void update(Employee emp);
}

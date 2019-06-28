package com.bugbycode.service.employee;

import java.util.List;
import java.util.Map;

import com.bugbycode.module.employee.Employee;

public interface EmployeeService {

	public Employee queryByUserName(String username);
	
	public int insert(Employee emp);
	
	public void update(Employee emp);
	
	public List<Employee> query(Map<String,Object> params);
}

package com.bugbycode.dao.employee;

import com.bugbycode.module.employee.Employee;

public interface EmployeeDao {

	public Employee queryByUserName(String username);

	public int insert(Employee emp);

	public void update(Employee emp);
}

package com.bugbycode.service.employee.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bugbycode.dao.employee.EmployeeDao;
import com.bugbycode.module.employee.Employee;
import com.bugbycode.service.employee.EmployeeService;

@Service("employeeService")
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeDao employeeDao;
	
	@Override
	public Employee queryByUserName(String username) {
		return employeeDao.queryByUserName(username);
	}

	@Override
	public int insert(Employee emp) {
		return employeeDao.insert(emp);
	}

	@Override
	public void update(Employee emp) {
		employeeDao.update(emp);
	}

	@Override
	public List<Employee> query(Map<String, Object> params) {
		return null;
	}

}

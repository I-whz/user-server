package com.bugbycode.dao.employee.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bugbycode.dao.base.BaseDao;
import com.bugbycode.dao.employee.EmployeeDao;
import com.bugbycode.module.employee.Employee;

@Repository("employeeDao")
public class EmployeeDaoImpl extends BaseDao implements EmployeeDao {

	@Override
	public Employee queryByUserName(String username) {
		return getSqlSession().selectOne("employee.queryByUserName",username);
	}

	@Override
	public int insert(Employee emp) {
		return getSqlSession().insert("employee.insert", emp);
	}

	@Override
	public void update(Employee emp) {
		getSqlSession().update("employee.update", emp);
	}

	@Override
	public List<Employee> query(Map<String, Object> params) {
		return getSqlSession().selectList("employee.query", params);
	}

	@Override
	public int count(Map<String, Object> params) {
		return getSqlSession().selectOne("employee.count", params);
	}

}

package com.bugbycode.service.organization.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.bugbycode.dao.employee.EmployeeDao;
import com.bugbycode.dao.organization.OrganizationDao;
import com.bugbycode.dao.role.RoleDao;
import com.bugbycode.module.employee.Employee;
import com.bugbycode.module.organization.Organization;
import com.bugbycode.service.organization.OrganizationService;

@Service("organizationService")
public class OrganizationServiceImpl implements OrganizationService{

	@Autowired
	private EmployeeDao employeeDao;
	
	@Autowired
	private OrganizationDao organizationDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Override
	public int insert(Organization ou) {
		String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Employee emp = employeeDao.queryByUserName(username);
		if(emp == null) {
			throw new AccessDeniedException("无权执行该操作");
		}
		ou.setCreateUserId(emp.getId());
		ou.setCreateTime(new Date());
		int rows = organizationDao.insert(ou);
		if(rows > 0) {
			Map<String,Object> params = new HashMap<String,Object>();
			params.put("empId", emp.getId());
			params.put("ouId", ou.getId());
			params.put("roleId", 1);
			roleDao.insertOuRel(params);
		}
		return rows;
	}

	@Override
	public Organization queryById(int organizationId) {
		return organizationDao.queryById(organizationId);
	}

	@Override
	public void update(Organization ou) {
		organizationDao.update(ou);
	}

	@Override
	public void delete(int organizationId) {
		organizationDao.delete(organizationId);
	}

	@Override
	public List<Organization> query(Map<String, Object> prarams) {
		return organizationDao.query(prarams);
	}

	@Override
	public int count(Map<String, Object> params) {
		return organizationDao.count(params);
	}

}

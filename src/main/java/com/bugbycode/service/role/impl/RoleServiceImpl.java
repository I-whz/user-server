package com.bugbycode.service.role.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.bugbycode.dao.employee.EmployeeDao;
import com.bugbycode.dao.role.RoleDao;
import com.bugbycode.module.employee.Employee;
import com.bugbycode.module.role.Role;
import com.bugbycode.service.role.RoleService;

@Service("roleService")
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private EmployeeDao employeeDao;
	
	@Override
	public List<Role> queryOuRole(int empId, int organizationId) {
		return roleDao.queryOuRole(empId, organizationId);
	}

	@Override
	public void checkRole(int organizationId,String grant) {
		String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Employee emp = employeeDao.queryByUserName(username);
		if(emp == null) {
			throw new AccessDeniedException("无权执行该操作");
		}
		List<Role> roleList = queryOuRole(emp.getId(),organizationId);
		if(CollectionUtils.isEmpty(roleList)) {
			throw new AccessDeniedException("无权执行该操作");
		}
		Set<String> grantSet = new HashSet<String>();
		if(!CollectionUtils.isEmpty(roleList)) {
			for(Role r : roleList) {
				grantSet.addAll(r.grantList());
			}
			for(String roleInfo : grantSet) {
				if(grant.equals(roleInfo)) {
					return;
				}
			}
		}
		throw new AccessDeniedException("无权执行该操作");
	}

	@Override
	public List<Role> query(Map<String, Object> params) {
		return roleDao.query(params);
	}

	@Override
	public int count(Map<String, Object> params) {
		return roleDao.count(params);
	}

	@Override
	public int insert(Role r) {
		return roleDao.insert(r);
	}

	@Override
	public void update(Role r) {
		roleDao.update(r);
	}

	@Override
	public void deleteById(int roleId) {
		roleDao.deleteById(roleId);
	}

	@Override
	public Role queryById(int roleId) {
		return roleDao.queryById(roleId);
	}

	@Override
	public Role queryByName(Map<String, Object> params) {
		return roleDao.queryByName(params);
	}

}

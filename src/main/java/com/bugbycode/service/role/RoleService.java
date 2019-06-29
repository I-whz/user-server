package com.bugbycode.service.role;

import java.util.List;

import com.bugbycode.module.role.Role;

public interface RoleService {

	public List<Role> queryOuRole(int empId,int organizationId);
	
	public void checkRole(int organizationId,String grant);
}

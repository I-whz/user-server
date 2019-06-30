package com.bugbycode.dao.role;

import java.util.List;
import java.util.Map;

import com.bugbycode.module.role.Role;

public interface RoleDao {

	public List<Role> queryOuRole(int empId,int organizationId);
	
	public void insertOuRel(Map<String,Object> params);
	
	public void exitOu(Map<String,Object> params);
	
	public void deleteOuRelByRole(int roleId);
	
	public void deleteOuRelByOuId(int organizationId);
}

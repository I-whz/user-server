package com.bugbycode.dao.role.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bugbycode.dao.base.BaseDao;
import com.bugbycode.dao.role.RoleDao;
import com.bugbycode.module.role.Role;

@Repository("roleDao")
public class RoleDaoImpl extends BaseDao implements RoleDao {

	@Override
	public List<Role> queryOuRole(int empId, int organizationId) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("empId", empId);
		param.put("organizationId", organizationId);
		return getSqlSession().selectList("role.queryOuRole", param);
	}

}

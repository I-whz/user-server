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

	@Override
	public void insertOuRel(Map<String, Object> params) {
		getSqlSession().insert("role.insertOuRel", params);
	}

	@Override
	public void exitOu(Map<String, Object> params) {
		getSqlSession().delete("role.exitOu", params);
	}

	@Override
	public void deleteOuRelByRole(int roleId) {
		getSqlSession().delete("role.deleteOuRelByRole", roleId);
	}

	@Override
	public void deleteOuRelByOuId(int organizationId) {
		getSqlSession().delete("role.deleteOuRelByOuId", organizationId);
	}

	@Override
	public void deleteByOuId(int organizationId) {
		getSqlSession().delete("role.deleteByOuId", organizationId);
	}

	@Override
	public Role queryById(int roleId) {
		return getSqlSession().selectOne("role.queryById", roleId);
	}

	@Override
	public List<Role> query(Map<String, Object> params) {
		return getSqlSession().selectList("role.query", params);
	}

	@Override
	public int count(Map<String, Object> params) {
		return getSqlSession().selectOne("role.count", params);
	}

	@Override
	public int insert(Role r) {
		return getSqlSession().insert("role.insert", r);
	}

	@Override
	public void update(Role r) {
		getSqlSession().update("role.update", r);
	}

	@Override
	public void deleteById(int roleId) {
		getSqlSession().delete("role.deleteById", roleId);
	}

	@Override
	public Role queryByName(Map<String, Object> params) {
		return getSqlSession().selectOne("role.queryByName", params);
	}

}

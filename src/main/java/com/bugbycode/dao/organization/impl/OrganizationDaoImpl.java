package com.bugbycode.dao.organization.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bugbycode.dao.base.BaseDao;
import com.bugbycode.dao.organization.OrganizationDao;
import com.bugbycode.module.organization.Organization;

@Repository("organizationDao")
public class OrganizationDaoImpl extends BaseDao implements OrganizationDao {

	@Override
	public Organization queryById(int organizationId) {
		return getSqlSession().selectOne("ou.queryById", organizationId);
	}

	@Override
	public int insert(Organization ou) {
		return getSqlSession().insert("ou.insert", ou);
	}

	@Override
	public void update(Organization ou) {
		getSqlSession().update("ou.update", ou);
	}

	@Override
	public void delete(int organizationId) {
		getSqlSession().delete("ou.delete", organizationId);
	}

	@Override
	public List<Organization> query(Map<String, Object> prarams) {
		return getSqlSession().selectList("ou.query", prarams);
	}

	@Override
	public int count(Map<String, Object> params) {
		return getSqlSession().selectOne("ou.count", params);
	}

}

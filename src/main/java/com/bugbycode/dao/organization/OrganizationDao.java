package com.bugbycode.dao.organization;

import java.util.List;
import java.util.Map;

import com.bugbycode.module.organization.Organization;

public interface OrganizationDao {

	public Organization queryById(int organizationId);
	
	public int insert(Organization ou);
	
	public void update(Organization ou);
	
	public void delete(int organizationId);
	
	public List<Organization> query(Map<String,Object> prarams);
	
	public int count(Map<String,Object> params);
}

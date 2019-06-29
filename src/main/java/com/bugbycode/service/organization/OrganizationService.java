package com.bugbycode.service.organization;

import java.util.List;
import java.util.Map;

import com.bugbycode.module.organization.Organization;

public interface OrganizationService {

	public int insert(Organization ou);
	
	public Organization queryById(int organizationId);
	
	public void update(Organization ou);
	
	public void delete(int organizationId);
	
	public List<Organization> query(Map<String,Object> prarams);
	
	public int count(Map<String,Object> params);
}

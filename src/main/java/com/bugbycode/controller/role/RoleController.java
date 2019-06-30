package com.bugbycode.controller.role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bugbycode.module.employee.Employee;
import com.bugbycode.module.organization.Organization;
import com.bugbycode.module.role.Role;
import com.bugbycode.service.employee.EmployeeService;
import com.bugbycode.service.organization.OrganizationService;
import com.bugbycode.service.role.RoleService;
import com.util.StringUtil;

@Validated
@RestController
@RequestMapping("/role")
public class RoleController {
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private RoleService roleService;
	
	@RequestMapping(method = {RequestMethod.GET},value = "/query")
	public List<Role> query(String keyWord,int organizationId,int offset,int limit){
		String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Employee emp = employeeService.queryByUserName(username);
		if(emp == null) {
			throw new AccessDeniedException("无权执行该操作");
		}
		
		Organization ou = organizationService.queryById(organizationId);
		if(ou == null || ou.getCreateUserId() != emp.getId()) {
			throw new AccessDeniedException("无权执行该操作");
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtil.isNotBlank(keyWord)) {
			params.put("keyword", keyWord);
		}
		params.put("ouId", organizationId);
		params.put("offset", offset);
		params.put("limit", limit);
		return roleService.query(params);
	}
	
	@RequestMapping(method = {RequestMethod.GET},value = "/count")
	public Map<String,?> count(String keyWord,int organizationId){
		Map<String,Integer> result = new HashMap<String,Integer>();
		String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Employee emp = employeeService.queryByUserName(username);
		if(emp == null) {
			throw new AccessDeniedException("无权执行该操作");
		}
		
		Organization ou = organizationService.queryById(organizationId);
		if(ou == null || ou.getCreateUserId() != emp.getId()) {
			throw new AccessDeniedException("无权执行该操作");
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtil.isNotBlank(keyWord)) {
			params.put("keyword", keyWord);
		}
		params.put("ouId", organizationId);
		result.put("count", roleService.count(params));
		return result;
	}
	
	
}

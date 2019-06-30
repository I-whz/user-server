package com.bugbycode.controller.role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotEmpty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
	
	@RequestMapping(method = {RequestMethod.POST},value = "/insert")
	public Map<String, ?> insert(@RequestBody @Validated Role role){
		Map<String,Object> result = new HashMap<String,Object>();
		String error = "success";
		String error_description = "新建角色成功";
		String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Employee emp = employeeService.queryByUserName(username);
		if(emp == null) {
			throw new AccessDeniedException("无权执行该操作");
		}
		
		Organization ou = organizationService.queryById(role.getOrganizationId());
		if(ou == null || ou.getCreateUserId() != emp.getId()) {
			throw new AccessDeniedException("无权执行该操作");
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("roleName", role.getName());
		params.put("organizationId", role.getOrganizationId());
		Role roleTmp = roleService.queryByName(params);
		if(roleTmp != null) {
			throw new AccessDeniedException("该角色名称已存在");
		}
		
		int row = roleService.insert(role);
		
		if(row > 0) {
			result.put("roleId", role.getId());
		}
		
		result.put("error", error);
		result.put("error_description", error_description);
		return result;
	}
	
	@RequestMapping(method = {RequestMethod.POST},value = "/update")
	public Map<String, ?> update(@RequestBody @Validated Role role){
		Map<String,Object> result = new HashMap<String,Object>();
		String error = "success";
		String error_description = "新建角色成功";
		String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Employee emp = employeeService.queryByUserName(username);
		if(emp == null) {
			throw new AccessDeniedException("无权执行该操作");
		}
		
		Organization ou = organizationService.queryById(role.getOrganizationId());
		if(ou == null || ou.getCreateUserId() != emp.getId()) {
			throw new AccessDeniedException("无权执行该操作");
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("roleName", role.getName());
		params.put("organizationId", role.getOrganizationId());
		Role roleTmp = roleService.queryByName(params);
		if(!(roleTmp == null || roleTmp.getId() == role.getId())) {
			throw new AccessDeniedException("该角色名称已存在");
		}
		
		roleService.update(role);
		
		result.put("error", error);
		result.put("error_description", error_description);
		return result;
	}
	
	@RequestMapping(method = {RequestMethod.POST},value = "/delete")
	public Map<String, ?> delete(int roleId){
		Map<String,Object> result = new HashMap<String,Object>();
		String error = "success";
		String error_description = "删除组织机构成功";
		String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Employee emp = employeeService.queryByUserName(username);
		if(emp == null) {
			throw new AccessDeniedException("无权执行该操作");
		}
		
		Role role = roleService.queryById(roleId);
		
		if(role == null) {
			throw new AccessDeniedException("无权执行该操作");
		}
		
		Organization ou = organizationService.queryById(role.getOrganizationId());
		if(ou == null || emp.getId() != ou.getCreateUserId()) {
			throw new AccessDeniedException("无权执行该操作");
		}
		
		roleService.deleteById(roleId);
		
		result.put("error", error);
		result.put("error_description", error_description);
		return result;
	}
	
	@RequestMapping(method = {RequestMethod.GET},value = "/queryById/{roleId}")
	public Role queryById(@PathVariable(name="roleId") int roleId) {
		String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Employee emp = employeeService.queryByUserName(username);
		if(emp == null) {
			throw new AccessDeniedException("无权执行该操作");
		}
		
		Role role = roleService.queryById(roleId);
		
		if(role == null) {
			throw new AccessDeniedException("无权执行该操作");
		}
		
		Organization ou = organizationService.queryById(role.getOrganizationId());
		if(ou == null || emp.getId() != ou.getCreateUserId()) {
			throw new AccessDeniedException("无权执行该操作");
		}
		
		return role;
	}
	
	@RequestMapping(method = {RequestMethod.GET},value = "/queryByName")
	public Role queryByName(
			@NotEmpty(message="角色名称不能为空")
			String name,int organizationId) {
		String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Employee emp = employeeService.queryByUserName(username);
		if(emp == null) {
			throw new AccessDeniedException("无权执行该操作");
		}
		Organization ou = organizationService.queryById(organizationId);
		if(ou == null || emp.getId() != ou.getCreateUserId()) {
			throw new AccessDeniedException("无权执行该操作");
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("roleName", name);
		params.put("organizationId", organizationId);
		return roleService.queryByName(params);
	}
}

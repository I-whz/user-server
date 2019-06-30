package com.bugbycode.controller.organization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
import com.bugbycode.service.employee.EmployeeService;
import com.bugbycode.service.organization.OrganizationService;
import com.util.StringUtil;

@Validated
@RestController
@RequestMapping("/organization")
public class OrganizationController {
	
	@Autowired
	private OrganizationService organizationService;
	
	@Autowired
	private EmployeeService employeeService;

	@RequestMapping(method = {RequestMethod.POST},value = "/insert")
	public Map<String, ?> insert(@RequestBody @Validated Organization ou){
		Map<String,Object> result = new HashMap<String,Object>();
		String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Employee emp = employeeService.queryByUserName(username);
		if(emp == null) {
			throw new AccessDeniedException("无权执行该操作");
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("empId", emp.getId());
		params.put("name", ou.getName());
		
		String error = "success";
		String error_description = "新建组织机构成功";
		if(organizationService.queryByName(params) != null) {
			throw new AccessDeniedException("该组织机构名称已存在");
		}
		int row = organizationService.insert(ou);
		if(row > 0) {
			result.put("organizationId", ou.getId());
		}
		result.put("error", error);
		result.put("error_description", error_description);
		return result;
	}
	
	@RequestMapping(method = {RequestMethod.GET},value = "/query")
	public List<Organization> query(String keyWord,int offset,int limit){
		String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Employee emp = employeeService.queryByUserName(username);
		if(emp == null) {
			throw new AccessDeniedException("无权执行该操作");
		}
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtil.isNotBlank(keyWord)) {
			params.put("keyword", keyWord);
		}
		params.put("empId", emp.getId());
		params.put("offset", offset);
		params.put("limit", limit);
		return organizationService.query(params);
	}
	
	@RequestMapping(method = {RequestMethod.GET},value = "/count")
	public Map<String,Integer> count(String keyWord){
		Map<String,Integer> result = new HashMap<String,Integer>();
		Map<String,Object> params = new HashMap<String,Object>();
		String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Employee emp = employeeService.queryByUserName(username);
		if(emp == null) {
			throw new AccessDeniedException("无权执行该操作");
		}
		if(StringUtil.isNotBlank(keyWord)) {
			params.put("keyword", keyWord);
		}
		params.put("empId", emp.getId());
		result.put("count", organizationService.count(params));
		return result;
	}
	
	@RequestMapping(method = {RequestMethod.POST},value = "/delete")
	public Map<String, ?> delete(int organizationId){
		Map<String,Object> result = new HashMap<String,Object>();
		String error = "success";
		String error_description = "删除组织机构成功";
		String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Employee emp = employeeService.queryByUserName(username);
		if(emp == null) {
			throw new AccessDeniedException("无权执行该操作");
		}
		Organization ou = organizationService.queryById(organizationId);
		if(ou == null || emp.getId() != ou.getCreateUserId()) {
			throw new AccessDeniedException("无权执行该操作");
		}
		organizationService.delete(organizationId);
		result.put("error", error);
		result.put("error_description", error_description);
		return result;
	}
	
	@RequestMapping(method = {RequestMethod.POST},value = "/update")
	public Map<String, ?> update(@RequestBody @Validated Organization ou){
		Map<String,Object> result = new HashMap<String,Object>();
		String error = "success";
		String error_description = "修改组织机构成功";
		String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Employee emp = employeeService.queryByUserName(username);
		if(emp == null) {
			throw new AccessDeniedException("无权执行该操作");
		}
		Organization backou = organizationService.queryById(ou.getId());
		if(backou == null || emp.getId() != backou.getCreateUserId()) {
			throw new AccessDeniedException("无权执行该操作");
		}
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("empId", emp.getId());
		params.put("name", ou.getName());
		backou = organizationService.queryByName(params);
		if(!(backou == null || backou.getId() == ou.getId())) {
			throw new AccessDeniedException("该组织机构名称已存在");
		}
		
		organizationService.update(ou);
		result.put("error", error);
		result.put("error_description", error_description);
		return result;
	}
	
	@RequestMapping(method = {RequestMethod.GET},value = "/queryById/{organizationId}")
	public Organization queryById(@PathVariable(name="organizationId") int organizationId) {
		String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Employee emp = employeeService.queryByUserName(username);
		if(emp == null) {
			throw new AccessDeniedException("无权执行该操作");
		}
		Organization ou = organizationService.queryById(organizationId);
		if(ou == null || emp.getId() != ou.getCreateUserId()) {
			throw new AccessDeniedException("无权执行该操作");
		}
		return ou;
	}
	
	@RequestMapping(method = {RequestMethod.GET},value = "/queryByName/{name}")
	public Organization queryByName(
			@Size(min=2,max=20,message="组织机构名称长度不能少于两个字符且不能超过二十个字符")
			@Pattern(regexp="^[A-Za-z0-9\\u4e00-\\u9fa5]*$",message="组织机构名称不能包含英文特殊字符")
			@PathVariable(name="name") String name) {
		String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Employee emp = employeeService.queryByUserName(username);
		if(emp == null) {
			throw new AccessDeniedException("无权执行该操作");
		}
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("name", name);
		params.put("empId", emp.getId());
		return organizationService.queryByName(params);
	}
}

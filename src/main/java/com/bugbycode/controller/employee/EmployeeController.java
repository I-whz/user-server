package com.bugbycode.controller.employee;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
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
import com.util.AESUtil;
import com.util.StringUtil;

@Validated
@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@RequestMapping(method = {RequestMethod.POST},value = "/insert")
	public Map<String, ?> insert(@RequestBody @Validated Employee emp){
		Map<String,Object> result = new HashMap<String,Object>();
		String error = "success";
		String error_description = "新建用户成功";
		String username = emp.getUsername();
		String password = emp.getPassword();
		if(StringUtil.isNotEmpty(password)) {
			emp.setPassword(AESUtil.encrypt(password));
		}
		Employee e = employeeService.queryByUserName(username);
		if(e != null) {
			throw new AccessDeniedException("添加用户失败，该用户名已存在");
		}
		
		emp.setCreateTime(new Date());
		
		int row = employeeService.insert(emp);
		if(row > 0) {
			result.put("empId", emp.getId());
		}
		result.put("error", error);
		result.put("error_description", error_description);
		return result;
	}
	
	@RequestMapping(method = {RequestMethod.GET},value = "/info")
	public Employee info() {
		String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		return employeeService.queryByUserName(username);
	}
	
	@RequestMapping(method = {RequestMethod.POST},value = "/update")
	public Map<String,?> update(@Validated @RequestBody Employee emp){
		String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Map<String,Object> result = new HashMap<String,Object>();
		String error = "success";
		String error_description = "修改用户成功";
		String password = emp.getPassword();
		Employee e = employeeService.queryByUserName(username);
		if(e == null) {
			throw new AccessDeniedException("修改用户失败");
		}else {
			emp.setId(e.getId());
			emp.setStatus(e.getStatus());
			if(StringUtil.isNotEmpty(password)) {
				password = AESUtil.encrypt(password);
				emp.setPassword(password);
			}else {
				emp.setPassword(e.getPassword());
			}
		}
		employeeService.update(emp);
		result.put("error", error);
		result.put("error_description", error_description);
		return result;
	}
	
	@RequestMapping(method = {RequestMethod.GET},value = "/queryByUserName/{username}")
	public Employee queryByUserName(
			@NotEmpty(message="用户名不能为空")
			@Size(min=2,max=20,message="用户名长度不能少于两个字符且不能超过二十个字符")
			@Pattern(regexp="^[A-Za-z0-9\\u4e00-\\u9fa5]*$",message="用户名不能包含除大小写字母、数字、下划线以及横杠以外的字符")
			@PathVariable(name = "username") 
			String username) {
		if(StringUtil.isEmpty(username)) {
			return null;
		}
		return employeeService.queryByUserName(username);
	}

	@RequestMapping(method = {RequestMethod.GET},value = "/query")
	public List<Employee> query(
			@Min(value = 1 ,message = "组织机构ID必须大于0")
			@Max(value = Integer.MAX_VALUE,message = ("组织机构ID不能超过" + Integer.MAX_VALUE))
			int organizationId,
			String keyWord,
			int offset,
			int limit){
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
		if(StringUtil.isNotEmpty(keyWord)) {
			params.put("keyword", keyWord);
		}
		params.put("organizationId", organizationId);
		params.put("offset", offset);
		params.put("limit", limit);
		return employeeService.query(params);
	}
	
	@RequestMapping(method = {RequestMethod.GET},value = "/count")
	public Map<String,Integer> count(
			@Min(value = 1 ,message = "组织机构ID必须大于0")
			@Max(value = Integer.MAX_VALUE,message = ("组织机构ID不能超过" + Integer.MAX_VALUE))
			int organizationId,
			String keyWord){
		String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
		Employee emp = employeeService.queryByUserName(username);
		if(emp == null) {
			throw new AccessDeniedException("无权执行该操作");
		}
		Organization ou = organizationService.queryById(organizationId);
		if(ou == null || emp.getId() != ou.getCreateUserId()) {
			throw new AccessDeniedException("无权执行该操作");
		}
		Map<String,Integer> result = new HashMap<String,Integer>();
		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtil.isNotEmpty(keyWord)) {
			params.put("keyword", keyWord);
		}
		params.put("organizationId", organizationId);
		result.put("count", employeeService.count(params));
		return result;
	}
}

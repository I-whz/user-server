package com.bugbycode.controller.employee;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
import com.bugbycode.service.employee.EmployeeService;
import com.util.AESUtil;
import com.util.StringUtil;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
	
	@Autowired
	private EmployeeService employeeService;
	
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
	public Map<String,?> update(@RequestBody Employee emp){
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
	public Employee queryByUserName(@PathVariable(name = "username") String username) {
		if(StringUtil.isEmpty(username)) {
			return null;
		}
		return employeeService.queryByUserName(username);
	}
	
	@RequestMapping(method = {RequestMethod.GET},value = "/query")
	public List<Employee> query(int organizationId,String keyWord,
			int startRow,int limit){
		return null;
	}
}

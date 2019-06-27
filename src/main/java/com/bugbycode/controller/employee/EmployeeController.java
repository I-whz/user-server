package com.bugbycode.controller.employee;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
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
	public Map<String, ?> insert(@RequestBody Employee emp){
		Map<String,Object> result = new HashMap<String,Object>();
		int code = 0;
		String message = "新建用户成功";
		String username = emp.getUsername();
		String password = emp.getPassword();
		String name = emp.getName();
		int status = emp.getStatus();
		String address = emp.getAddress();
		String zipCode = emp.getZipCode();
		int authType = emp.getAuthType();
		try {
			if(StringUtil.isBlank(name)) {
				throw new RuntimeException("请输入姓名");
			}
			if(StringUtil.isBlank(username)) {
				throw new RuntimeException("请输入用户名");
			}
			if(StringUtil.isEmpty(password)) {
				throw new RuntimeException("请输入密码");
			}
			
			emp.setPassword(AESUtil.encrypt(password));
			
			if(!(status == 0 || status == 1)) {
				throw new RuntimeException("用户状态错误");
			}
			
			Employee e = employeeService.queryByUserName(username);
			if(e != null) {
				throw new RuntimeException("该用户名已存在");
			}
			
			emp.setCreateTime(new Date());
			
			int row = employeeService.insert(emp);
			if(row > 0) {
				result.put("empId", emp.getId());
			}
		}catch (Exception e) {
			code = 1;
			message = e.getMessage();
		}
		result.put("code", code);
		result.put("message", message);
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
		int code = 0;
		String message = "修改用户成功";
		String password = emp.getPassword();
		Employee e = employeeService.queryByUserName(username);
		if(e == null) {
			code = 1;
			message = "修改用户失败";
		}else {
			emp.setId(e.getId());
			if(StringUtil.isNotEmpty(password)) {
				password = AESUtil.encrypt(password);
				emp.setPassword(password);
			}else {
				emp.setPassword(e.getPassword());
			}
		}
		employeeService.update(emp);
		result.put("code", code);
		result.put("message", message);
		return result;
	}
	
	@RequestMapping(method = {RequestMethod.GET},value = "/queryByUserName/{username}")
	public Employee queryByUserName(@PathVariable(name = "username") String username) {
		if(StringUtil.isEmpty(username)) {
			return null;
		}
		return employeeService.queryByUserName(username);
	}
}

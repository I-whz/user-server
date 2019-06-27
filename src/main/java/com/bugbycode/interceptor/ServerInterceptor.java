package com.bugbycode.interceptor;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.FilterInvocation;

import com.bugbycode.service.employee.EmployeeService;

public class ServerInterceptor implements Filter {

	private final Logger logger = LogManager.getLogger(ServerInterceptor.class);
	
	@Autowired
	private EmployeeService employeeService;
	
	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		FilterInvocation fi = new FilterInvocation(request, response, chain);
		logger.info(fi.getRequestUrl());
		logger.info(fi.getFullRequestUrl());
		chain.doFilter(request, response);
		//throw new AccessDeniedException("Access is denied");
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		
	}

}

package com.bugbycode.interceptor;

import java.io.IOException;
import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import com.bugbycode.module.employee.Employee;

@ControllerAdvice
public class FortRequestBodyAdvice implements RequestBodyAdvice {

	private final Logger logger = LogManager.getLogger(FortRequestBodyAdvice.class);
	
	@Autowired
	private HttpServletRequest request;
	
	@Override
	public boolean supports(MethodParameter methodParameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
		return inputMessage;
	}

	@Override
	public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
			Class<? extends HttpMessageConverter<?>> converterType) {
		logger.info(UrlUtils.buildRequestUrl(request));
		if(body instanceof Employee) {
			Employee emp = (Employee) body;
			String name = emp.getName();
			if(!GenericValidator.isBlankOrNull(name)){
				accessDenied("姓名不能为空");
			}
			if(!GenericValidator.maxLength(name, 20)) {
				accessDenied("姓名长度不能超过二十个字符");
			}
			if(!GenericValidator.minLength(name, 2)) {
				accessDenied("姓名长度至少");
			}
			if(!(GenericValidator.isBlankOrNull(emp.getEmail()) 
					|| GenericValidator.isEmail(emp.getEmail()))) {
				accessDenied("Email格式错误");
			}
		}
		return body;
	}

	@Override
	public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
			Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
		return body;
	}
	
	private void accessDenied(String message) {
		throw new AccessDeniedException(message);
	}

}

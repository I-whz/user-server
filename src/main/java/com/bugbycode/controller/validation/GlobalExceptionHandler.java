package com.bugbycode.controller.validation;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

	@Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, 
    		Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
		JSONObject errJson = new JSONObject();
		if(ex instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException mnv = (MethodArgumentNotValidException) ex;
			List<ObjectError> errList = mnv.getBindingResult().getAllErrors();
			JSONArray errArr = new JSONArray();
			for(ObjectError err : errList) {
				JSONObject errObj = new JSONObject();
				JSONObject objErr = JSONObject.parseObject(JSONObject.toJSON(err).toString());
				errObj.put("field", objErr.getString("field"));
				errObj.put("defaultMessage", objErr.getString("defaultMessage"));
				errArr.add(errObj);
			}
			errJson.put("error", "validator_error");
			errJson.put("error_description", errArr);
		}else {
			errJson.put("error", "server_error");
			errJson.put("error_description", "服务器内部错误");
		}
		return new ResponseEntity<Object>(errJson, status);
    }
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleValidException(ConstraintViolationException ex) {
		JSONObject errJson = new JSONObject();
		Set<ConstraintViolation<?>> set = ex.getConstraintViolations();
		JSONArray errArr = new JSONArray();
		for(ConstraintViolation<?> cv : set) {
			JSONObject errObj = new JSONObject();
			String property = cv.getPropertyPath().toString();
			int index = property.indexOf('.');
			if(index != -1) {
				index++;
				property = property.substring(index);
			}
			errObj.put("field", property);
			errObj.put("defaultMessage", cv.getMessageTemplate());
			errArr.add(errObj);
		}
		errJson.put("error", "validator_error");
		errJson.put("error_description", errArr);
		return new ResponseEntity<>(errJson, HttpStatus.BAD_REQUEST);
	}
}

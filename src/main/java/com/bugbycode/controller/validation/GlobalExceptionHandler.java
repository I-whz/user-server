package com.bugbycode.controller.validation;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
			errJson.put("code", "1");
			errJson.put("errors", errArr);
		}else {
			errJson.put("error", "server_error");
			errJson.put("error_description", ex.getMessage());
		}
		return new ResponseEntity<Object>(errJson, status);
    }
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleValidException(ConstraintViolationException ex) {
		ex.getConstraintViolations();
		return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
}

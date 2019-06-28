package com.bugbycode.controller.validation;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.alibaba.fastjson.JSONObject;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{

	@Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
		JSONObject errJson = new JSONObject();
		if(ex instanceof MethodArgumentNotValidException) {
			MethodArgumentNotValidException mnv = (MethodArgumentNotValidException) ex;
			FieldError fieldError = mnv.getBindingResult().getFieldError();
			errJson.put("error", "validation_failed");
			errJson.put("error_description", fieldError.getDefaultMessage());
		}else {
			errJson.put("error", "server_error");
			errJson.put("error_description", ex.getMessage());
		}
		return new ResponseEntity<Object>(errJson, status);
    }
}

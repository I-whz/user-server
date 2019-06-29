package com.bugbycode.controller.globla;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FortErrorController implements ErrorController {

	private static final String ERROR_PATH = "/error";
	
	@RequestMapping(value=ERROR_PATH)  
    public Map<String,String> handleError(Exception exception){  
		Map<String,String> result = new HashMap<String,String>();
		result.put("error", "server_error");
		result.put("error_description", "服务器内部错误");
        return result;
    }
	
	@Override
	public String getErrorPath() {
		return ERROR_PATH;
	}

}

package com.graduates.test.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> responeBuilder(
            String message, HttpStatus status, boolean isSuccess, Object responeObject
    )
    {
        Map<String, Object> respone=new HashMap<>();
        respone.put("success", isSuccess);
        respone.put("message", message);
        respone.put("status", status);
        respone.put("data", responeObject);
        return new ResponseEntity<>(respone,status);

    }

}

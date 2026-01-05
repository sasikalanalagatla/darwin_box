package com.hrms.darwinBox.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Object> handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        Object messageObj = request.getAttribute("javax.servlet.error.message");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", statusCode == null ? 500 : statusCode);
        body.put("path", requestUri == null ? request.getRequestURI() : requestUri);
        body.put("message", messageObj == null ? "Resource not found" : messageObj.toString());

        HttpStatus status = statusCode == null ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.valueOf(statusCode);
        return ResponseEntity.status(status).body(body);
    }
}

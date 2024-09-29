package com.example.tripplanner.member.controller.advice;

import com.example.tripplanner.member.exception.MemberTaskException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Log4j2
public class MemberControllerAdvice {

    @ExceptionHandler(MemberTaskException.class)
    public ResponseEntity<Map<String, Object>> handleTaskException(MemberTaskException ex){
        log.error(ex.getMessage());

        String msg = ex.getMsg();
        int status = ex.getCode();

        Map<String, Object> map = Map.of("code", status, "error", msg);
        return ResponseEntity.status(status).body(map);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException exception){
        log.info("handleAccessDeniedException ...................");

        Map<String, Object> errors = new HashMap<>();
        errors.put("message", exception.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.FORBIDDEN);
    }



}

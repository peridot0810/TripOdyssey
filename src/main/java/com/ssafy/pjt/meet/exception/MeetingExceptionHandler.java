package com.ssafy.pjt.meet.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ssafy.pjt.meet.dto.response.CommonResponse;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MeetingExceptionHandler {

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<CommonResponse<Void>> handleDuplicateKey(DuplicateKeyException e) {
    	e.printStackTrace();
    	return ResponseEntity.badRequest().body(new CommonResponse<>(
            false,
            "This schedule already exists.",
            null
        ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CommonResponse<Void>> handleConstraintViolation(DataIntegrityViolationException e) {
        e.printStackTrace();
    	return ResponseEntity.badRequest().body(new CommonResponse<>(
            false,
            "Database constraint violated.",
            null
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Void>> handleGeneric(Exception e) {
    	e.printStackTrace();
        return ResponseEntity.internalServerError().body(new CommonResponse<>(
            false,
            "Unexpected error occurred.",
            null
        ));
    }
}


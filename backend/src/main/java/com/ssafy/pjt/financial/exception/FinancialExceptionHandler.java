package com.ssafy.pjt.financial.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;



@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class FinancialExceptionHandler {

	
}

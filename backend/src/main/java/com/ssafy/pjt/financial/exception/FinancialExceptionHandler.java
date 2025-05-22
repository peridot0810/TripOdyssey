package com.ssafy.pjt.financial.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.ssafy.pjt.financial.controller.ExpenseTrackerController;
import com.ssafy.pjt.financial.controller.FinancialController;



@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class FinancialExceptionHandler {

	
}

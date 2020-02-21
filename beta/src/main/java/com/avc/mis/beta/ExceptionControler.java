/**
 * 
 */
package com.avc.mis.beta;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.dao.DataIntegrityViolationException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zvi
 *
 */
@ControllerAdvice
@Slf4j
public class ExceptionControler {

	@ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<String> handleDataIntegrityViolationException(Exception e){
        return error(HttpStatus.BAD_REQUEST, e);
    }
	
	
	
    private ResponseEntity<String> error(HttpStatus status, Exception e) {
        log.error("Exception : ", e);
        return ResponseEntity.status(status).body(e.getMessage());
    }
}

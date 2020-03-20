/**
 * 
 */
package com.avc.mis.beta;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.core.NestedRuntimeException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zvi
 *
 */
@ControllerAdvice
@Slf4j
public class ExceptionControler {

	//illegalStateexception shouldn't be fatal
	//illegalargumentexception should be sent as a neat message to the user
	
	@ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> handleDataIntegrityViolationException(IllegalArgumentException e){
        return error(HttpStatus.BAD_REQUEST, e);
    }
	
	@ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<String> handleDataIntegrityViolationException(NestedRuntimeException e){
        return error(HttpStatus.BAD_REQUEST, e.getRootCause());
    }
	
	@ExceptionHandler({InvalidDataAccessApiUsageException.class})
    public ResponseEntity<String> handleIllegalArgumentException(NestedRuntimeException e){
        return error(HttpStatus.BAD_REQUEST, e.getRootCause());
    }
	
	
	
    private ResponseEntity<String> error(HttpStatus status, Throwable e) {
        log.error("Exception : ", e);
        return ResponseEntity.status(status).body(e.getLocalizedMessage());
    }
}

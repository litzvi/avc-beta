/**
 * 
 */
package com.avc.mis.beta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolationException;

import org.springframework.core.NestedRuntimeException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Zvi
 *
 */
@ControllerAdvice
@Slf4j
public class ExceptionControler {
	
	/* 
	 * DAO#addEntity - 
	 * @throws IllegalArgumentException, EntityNotFoundException, EntityExistsException, TransactionRequiredException
	 *  
	 * ProcessInfoDAO -
	 * @throws NullPointerException if no such process type.
	 * @throws AccessControlException if no permission
	 * 
	 */
	
	//IllegalStateException - ProcessInfoReader

    private ResponseEntity<String> error(HttpStatus status, Throwable e) {
        log.error("Exception : ", e);
        return ResponseEntity.status(status).body(e.getLocalizedMessage());
    }
    
    private ResponseEntity<String> internalError(HttpStatus status, Throwable e) {
        log.error("Exception : ", e);
        return ResponseEntity.status(status).body("Internal server error, please contact system manager");
    }
	
	@ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> handleDataIntegrityViolationException(IllegalArgumentException e){
        return error(HttpStatus.BAD_REQUEST, e);
    }
	
//	//already handled in handleFatalException
//	@ExceptionHandler({IllegalStateException.class})
//    public ResponseEntity<String> handleDataIntegrityViolationException(IllegalStateException e){
//        return error(HttpStatus.BAD_REQUEST, e);
//    }
	
	@ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<String> handleDataIntegrityViolationException(NestedRuntimeException e){
        return error(HttpStatus.BAD_REQUEST, e.getRootCause());
    }
		
	@ExceptionHandler({InvalidDataAccessApiUsageException.class})
    public ResponseEntity<String> handleIllegalArgumentException(NestedRuntimeException e){
        return error(HttpStatus.BAD_REQUEST, e.getRootCause());
    }
	
	@ExceptionHandler({OptimisticLockingFailureException.class})
    public ResponseEntity<String> handleOptemisticLockingException(OptimisticLockingFailureException e){
        return error(HttpStatus.PRECONDITION_FAILED, e.getRootCause());
    }
	
	@ExceptionHandler({IllegalStateException.class})
    public ResponseEntity<String> handleFatalException(RuntimeException e){
        return internalError(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(
	  MethodArgumentNotValidException ex) {
	    Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<List<String>> handleValidationExceptions(
			ConstraintViolationException ex) {
		List<String> errors = ex.getConstraintViolations().stream()
				.map(v -> v.getMessage())
				.collect(Collectors.toList());
//	    List<String> errors = new ArrayList<>();
//	    ex.getConstraintViolations().forEach((error) -> {
//	        errors.add(error.getMessage());
//	    });
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
	}
}

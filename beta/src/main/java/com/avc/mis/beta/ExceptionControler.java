/**
 * 
 */
package com.avc.mis.beta;

import java.security.AccessControlException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TransactionRequiredException;
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
	 * ProcessInfoDAO
	 * @throws InvalidDataAccessApiUsageException
	 */
	
	//IllegalStateException - ProcessInfoReader
	//AccessControlException
	
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
	
	@ExceptionHandler({AccessControlException.class})
    public ResponseEntity<String> handleAccessControlException(AccessControlException e){
        return error(HttpStatus.FORBIDDEN, e);
    }
	
	@ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> handleDataIntegrityViolationException(IllegalArgumentException e){
        return error(HttpStatus.BAD_REQUEST, e);
    }	

	@ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e){
        return error(HttpStatus.NOT_FOUND, e);
    }
	
	@ExceptionHandler({EntityExistsException.class})
    public ResponseEntity<String> handleEntityExistsException(EntityExistsException e){
        return error(HttpStatus.CONFLICT, e);
    }
	
	/**
     * Getting suitable response entity for errors who's cause should be shown to user.
     * @param status http status of response
     * @param e the exception
     * @return ResponseEntity with http status and exception message in body.
     */
    private ResponseEntity<String> error(HttpStatus status, Throwable e) {
        log.error("Exception : ", e);
        return ResponseEntity.status(status).body(e.getLocalizedMessage());
    }
	
	@ExceptionHandler({IllegalStateException.class, NullPointerException.class, 
		TransactionRequiredException.class})
    public ResponseEntity<String> handleFatalException(RuntimeException e){
        return internalError(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }
	
//	@ExceptionHandler({NullPointerException.class})
//    public ResponseEntity<String> handleNullPointerException(NullPointerException e){
//        return internalError(HttpStatus.INTERNAL_SERVER_ERROR, e);
//    }
//	
//	@ExceptionHandler({TransactionRequiredException.class})
//    public ResponseEntity<String> handleTransactionRequiredException(TransactionRequiredException e){
//        return internalError(HttpStatus.INTERNAL_SERVER_ERROR, e);
//    }
    
    /**
     * Getting suitable response entity for internal server errors who's cause should not be shown to user.
     * @param status http status of response
     * @param e the exception
    * @return ResponseEntity with http status and generic message in body.
     */
    private ResponseEntity<String> internalError(HttpStatus status, Throwable e) {
        log.error("Exception : ", e);
        return ResponseEntity.status(status).body(
        		"Internal server error, please contact system manager\n");
    }
	
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
	
	
}

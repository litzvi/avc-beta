/**
 * 
 */
package com.avc.mis.beta;

import org.springframework.core.NestedRuntimeException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}

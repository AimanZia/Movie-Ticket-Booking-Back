package User.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerException {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<HttpStatus> resourceNotFoundExceptionHandler(ResourceNotFoundException ex)
    {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}

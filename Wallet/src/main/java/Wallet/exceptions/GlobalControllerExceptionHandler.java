package Wallet.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundExceptionHandler(ResourceNotFoundException ex)
    {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("message", ex.getMessage());
        response.put("resource", ex.getResourceName());
        response.put("field", ex.getFieldName());
        response.put("value", ex.getFieldValue());
        response.put("status", HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintViolationExceptionHandler(ConstraintViolationException cex){

        //ConstraintViolationException is thrown when validation is applied to individual method parameters, not on a DTO object annotated with @Valid.
        List<String> errors = cex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());

        return ResponseEntity.badRequest().body(Map.of("errors",errors));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException mex){
        //MethodArgumentNotValidException (which occurs when Bean Validation like @NotBlank, @Size, @Valid, etc. fails)
        Map<String,Object> response = new HashMap<>();
        Map<String,String> fieldErrors = new HashMap<>();

        mex.getBindingResult().getFieldErrors().forEach(error -> {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        });
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation failed");
        response.put("messages", fieldErrors);

        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}

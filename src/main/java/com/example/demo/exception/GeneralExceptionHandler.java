package com.example.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GeneralExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GeneralExceptionHandler.class); // logger

    // Fallback: qualquer outra exceção não tratada
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        logger.error("erro inesperado: ",ex); // logger

        ErrorResponseDTO error = new ErrorResponseDTO(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "Ocorreu um erro inesperado"
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    // Dispara quando @Valid falha (campos inválidos no DTO)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .toList();

        ErrorResponseDTO error = new ErrorResponseDTO(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Error",
            "Um ou mais campos estão inválidos",
            details
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Dispara quando o email já existe no cadastro
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
            HttpStatus.CONFLICT.value(),
            "Conflict",
            ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
    
    // invalid credentials when loggin in
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidCredentials(InvalidCredentialsException ex) {
    ErrorResponseDTO error = new ErrorResponseDTO(
        HttpStatus.UNAUTHORIZED.value(),
        "Unauthorized",
        ex.getMessage()
    );
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    // when trying to acess some route without the proper credentials
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthorizationDenied(AuthorizationDeniedException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
        HttpStatus.UNAUTHORIZED.value(), 
        "Authorization denied, try using a proper credential", 
        ex.getMessage()
    );
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
    // tried to get to users/id/ but id does not exist
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUsernameNOtFound(UsernameNotFoundException ex) {
        ErrorResponseDTO error = new ErrorResponseDTO(
        HttpStatus.NOT_FOUND.value(), 
        "id not found :(", 
        ex.getMessage()
    );
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

}
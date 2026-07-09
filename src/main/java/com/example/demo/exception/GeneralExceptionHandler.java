package com.example.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GeneralExceptionHandler {

        private static final Logger logger = LoggerFactory.getLogger(GeneralExceptionHandler.class); // logger

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
}
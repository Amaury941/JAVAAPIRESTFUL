package com.example.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestControllerAdvice
public class GeneralExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GeneralExceptionHandler.class); // logger

    private ResponseEntity<ErrorResponseDTO> build(HttpStatus status, String error, String message) {
        return build(status, error, message, null);
    }

    private ResponseEntity<ErrorResponseDTO> build(HttpStatus status, String error, String message, List<String> details) {
        ErrorResponseDTO body = new ErrorResponseDTO(status.value(), error, message, details);
        return ResponseEntity.status(status).body(body);
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

    // ===== Erros de validação de DTO (@Valid) =====
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .toList();

        return build(HttpStatus.BAD_REQUEST, "Validation Error",
            "Um ou mais campos estão inválidos", details);
    }

    // ===== JSON malformado no body da requisição =====
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleMalformedJson(HttpMessageNotReadableException ex) {
        logger.warn("JSON malformado recebido: {}", ex.getMessage());
        return build(HttpStatus.BAD_REQUEST, "Bad Request",
            "O corpo da requisição está mal formatado");
    }

    // ===== Parâmetro obrigatório faltando (ex: query param) =====
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDTO> handleMissingParams(MissingServletRequestParameterException ex) {
        return build(HttpStatus.BAD_REQUEST, "Bad Request",
            "Parâmetro obrigatório ausente: " + ex.getParameterName());
    }

    // ===== Tipo errado no path/param (ex: UUID inválido em /users/{id}) =====
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return build(HttpStatus.BAD_REQUEST, "Bad Request",
            "Valor inválido para o parâmetro: " + ex.getName());
    }

    // ===== Rota não existe (404) =====
    @ExceptionHandler({NoHandlerFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<ErrorResponseDTO> handleNotFound() {
        return build(HttpStatus.NOT_FOUND, "Not Found", "Rota não encontrada");
    }

    // ===== Método HTTP não suportado (ex: DELETE numa rota só de GET) =====
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        return build(HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed", "Esse método não é suportado");
    }

    // ===== Regras de negócio específicas do domínio =====
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        return build(HttpStatus.CONFLICT, "Conflict", ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleUserNotFound(UserNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    }

    @ExceptionHandler({InvalidCredentialsException.class, BadCredentialsException.class})
    public ResponseEntity<ErrorResponseDTO> handleInvalidCredentials(RuntimeException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Unauthorized", "Email ou senha inválidos");
    }

    // ===== Autorização negada (RBAC) =====
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDenied(AccessDeniedException ex) {
        return build(HttpStatus.FORBIDDEN, "Forbidden", "Você não tem permissão para acessar esse recurso");
    }
}
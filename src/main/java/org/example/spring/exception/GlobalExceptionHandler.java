package org.example.spring.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)

    public ResponseEntity<ErrorDto> handleReadableException(HttpMessageNotReadableException ex, HttpServletRequest request) {
        ErrorDto errorDto = ErrorDto.of(
                HttpStatus.BAD_REQUEST.name(),
                ErrorMessage.MALFORMED_JSON_REQUEST + ex

        );
        logError(HttpStatus.valueOf(errorDto.status()), request, errorDto);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)

    public ResponseEntity<ErrorDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {

        String requiredType = (ex.getRequiredType() != null)
                ? ex.getRequiredType().getSimpleName()
                : ErrorMessage.UNKNOWN_TYPE;

        String message = String.format(
                ErrorMessage.UNKNOWN_TYPE,
                ex.getName(),
                ex.getValue(),
                requiredType
        );

        ErrorDto errorDto = ErrorDto.of(HttpStatus.BAD_REQUEST.name(),
                message);
        logError(HttpStatus.valueOf(errorDto.status()), request, errorDto);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)

    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {

        ErrorDto errorDto = ErrorDto.of(
                HttpStatus.BAD_REQUEST.name(),
                ErrorMessage.VALIDATION_ERROR + ex
        );

        logError(HttpStatus.valueOf(errorDto.status()), request, errorDto);
        return ResponseEntity.badRequest().body(errorDto);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)

    public ResponseEntity<ErrorDto> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest request) {

        ErrorDto errorDto = ErrorDto.of(HttpStatus.METHOD_NOT_ALLOWED.name(),
                ErrorMessage.METHOD_NOT_ALLOWED + ex);

        logError(HttpStatus.valueOf(errorDto.status()), request, errorDto);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorDto);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)

    public ResponseEntity<ErrorDto> handleServletRequestParameter(MissingServletRequestParameterException ex, HttpServletRequest request) {

        ErrorDto errorDto = ErrorDto.of(HttpStatus.BAD_REQUEST.name(),
                ErrorMessage.MISSING_PARAMETER + ex);


        logError(HttpStatus.valueOf(errorDto.status()), request, errorDto);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

    @ExceptionHandler(NoResourceFoundException.class)

    public ResponseEntity<ErrorDto> handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest request) {

        ErrorDto errorDto = ErrorDto.of(HttpStatus.NOT_FOUND.name(),
                ErrorMessage.RESOURCE_NOT_FOUND + ex);

        logError(HttpStatus.valueOf(errorDto.status()), request, errorDto);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(EntityNotFoundException.class)

    public ResponseEntity<ErrorDto> handleNotFound(EntityNotFoundException ex, HttpServletRequest request) {

        ErrorDto errorDto = ErrorDto.of(HttpStatus.NOT_FOUND.name(),
                ex.getMessage()
        );

        logError(HttpStatus.valueOf(errorDto.status()), request, errorDto);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)

    public ResponseEntity<ErrorDto> handleUserAlready(UserAlreadyExistsException ex, HttpServletRequest request) {

        ErrorDto errorDto = ErrorDto.of(ex.getStatus().name(),
                ex.getMessage());

        logError(HttpStatus.valueOf(errorDto.status()), request, errorDto);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDto);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleAllExceptions(Exception ex, HttpServletRequest request) {

        ErrorDto error = ErrorDto.of(
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                ErrorMessage.INTERNAL_ERROR + ex
        );

        logError(HttpStatus.valueOf(error.status()), request, error);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    private void logError(HttpStatus status, HttpServletRequest request, ErrorDto error) {

        String method = request.getMethod();
        String url = request.getRequestURL().toString();

        log.warn("Status: [{} {}] [{}] | [{}] -> {}",
                status.value(),
                status.name(),
                method,
                url,
                error.message());
    }
}

package com.harsh.qrgenerator.exception;

import com.google.zxing.WriterException;
import com.harsh.qrgenerator.dto.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleValidation(BindException exception) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Please fix the highlighted fields.",
                fieldErrors
        );
    }

    @ExceptionHandler(InvalidLogoException.class)
    public ResponseEntity<ErrorResponse> handleInvalidLogo(InvalidLogoException exception) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                exception.getMessage(),
                Map.of("logo", exception.getMessage())
        );
    }

    @ExceptionHandler({WriterException.class, IOException.class})
    public ResponseEntity<ErrorResponse> handleQrGenerationFailure(Exception exception) {
        log.error("QR generation failed", exception);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "We could not generate the QR code. Please try again.",
                Map.of()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedFailure(Exception exception) {
        log.error("Unexpected application error", exception);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Something went wrong. Please try again.",
                Map.of()
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(
            HttpStatus status,
            String message,
            Map<String, String> fieldErrors
    ) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(
                        Instant.now(),
                        status.value(),
                        status.getReasonPhrase(),
                        message,
                        fieldErrors
                ));
    }
}

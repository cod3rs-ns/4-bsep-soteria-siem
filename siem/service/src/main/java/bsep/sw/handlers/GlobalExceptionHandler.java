package bsep.sw.handlers;


import bsep.sw.hateoas.ErrorResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleBaseException(final AccessDeniedException e) {
        final ErrorResponse response = new ErrorResponse("401", "Unauthorized", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleJSONBodyException(final ConstraintViolationException e) {
        final StringBuilder sb = new StringBuilder("error [");
        if (!e.getConstraintViolations().isEmpty()) {
            final ConstraintViolation violation = e.getConstraintViolations().iterator().next();
            sb.append(violation.getPropertyPath());
            sb.append(" - ");
            sb.append(violation.getMessage());
            sb.append("]");
        } else {
            sb.append("something went wrong]");
        }
        final ErrorResponse response = new ErrorResponse("400", "Invalid object format", sb.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleJSONBodyExceptionMissingField(final NullPointerException e) {
        final ErrorResponse response = new ErrorResponse("400", "Invalid object format", "Missing necessary field");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleJSONBodyUniqueException(final DataIntegrityViolationException e) {
        // do not expose intern to user - remove unique constraint name
        final String errorMessage = ExceptionUtils.getRootCause(e).getMessage();
        final String simplifiedMessage = StringUtils.substringBefore(errorMessage, " for key");

        final ErrorResponse response = new ErrorResponse("400", "Conflict", simplifiedMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJSONBodyUniqueException(final HttpMessageNotReadableException e) {
        final ErrorResponse response = new ErrorResponse("400", "Invalid object format", "Non-existing enumeration type");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleBaseException(final BadCredentialsException e) {
        final ErrorResponse response = new ErrorResponse("400", e.getMessage(), e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}

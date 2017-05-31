package bsep.sw.util;

import bsep.sw.hateoas.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class StandardResponses {
    protected ResponseEntity<ErrorResponse> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("STA-401", "Unauthorized", "Please login first."));
    }

    protected ResponseEntity<ErrorResponse> notFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("STA-404", "Not Found", "Requested entity does not exists."));
    }
}

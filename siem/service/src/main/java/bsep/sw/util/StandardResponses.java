package bsep.sw.util;

import bsep.sw.hateoas.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class StandardResponses {
    public ResponseEntity<ErrorResponse> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("401", "Unauthorized", "Please login first."));
    }
}

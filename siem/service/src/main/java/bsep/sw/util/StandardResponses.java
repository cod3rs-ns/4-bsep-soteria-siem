package bsep.sw.util;

import bsep.sw.hateoas.ErrorResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class StandardResponses {

    protected ResponseEntity<ErrorResponse> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("STA-401", "Unauthorized", "Please login first."));
    }

    protected ResponseEntity<ErrorResponse> notFound(final String entity) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("STA-404", "Not Found", StringUtils.capitalize(entity) + " does not exists."));
    }

}

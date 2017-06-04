package bsep.sw.hateoas.resource.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

public class ResponseMeta {

    @JsonProperty("created-at")
    private final DateTime createdAt;

    @JsonProperty("updated-at")
    private final DateTime updatedAt;

    @JsonProperty("created-by")
    private final String createdBy;

    @JsonProperty("updated-by")
    private final String updatedBy;

    public ResponseMeta(final DateTime createdAt, final DateTime updatedAt, final String createdBy, final String updatedBy) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public DateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

}

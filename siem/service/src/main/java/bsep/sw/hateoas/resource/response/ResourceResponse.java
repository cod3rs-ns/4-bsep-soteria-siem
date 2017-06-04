package bsep.sw.hateoas.resource.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class ResourceResponse {

    @JsonProperty("meta")
    protected ResponseMeta meta;

    @JsonProperty("data")
    protected ResponseData data;

    public ResourceResponse(final ResponseMeta meta, final ResponseData data) {
        this.meta = meta;
        this.data = data;
    }

    public ResourceResponse() {

    }

    public ResponseMeta getMeta() {
        return meta;
    }

    public ResponseData getData() {
        return data;
    }

}

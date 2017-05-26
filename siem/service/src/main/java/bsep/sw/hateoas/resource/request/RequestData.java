package bsep.sw.hateoas.resource.request;

import bsep.sw.hateoas.resource.response.ResourceResponseAttributes;

public class RequestData {

    private final String type;
    private final ResourceResponseAttributes attributes;

    public RequestData(final String type, final ResourceResponseAttributes attributes) {
        this.type = type;
        this.attributes = attributes;
    }

    public String getType() {
        return type;
    }

    public ResourceResponseAttributes getAttributes() {
        return attributes;
    }

}

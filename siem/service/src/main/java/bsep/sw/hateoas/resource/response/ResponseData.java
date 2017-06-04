package bsep.sw.hateoas.resource.response;

public class ResponseData {

    private final Long id;
    private final String type;
    private final ResourceResponseAttributes attributes;

    public ResponseData(final Long id, final String type, final ResourceResponseAttributes attributes) {
        this.id = id;
        this.type = type;
        this.attributes = attributes;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public ResourceResponseAttributes getAttributes() {
        return attributes;
    }

}

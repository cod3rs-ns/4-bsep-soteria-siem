package bsep.sw.hateoas.resource.request;

public abstract class ResourceRequest {

    private final RequestData data;

    public ResourceRequest(final RequestData data) {
        this.data = data;
    }

    public RequestData getData() {
        return data;
    }

}

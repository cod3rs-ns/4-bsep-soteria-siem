package bsep.sw.hateoas.resource.response;

import bsep.sw.domain.EntityMeta;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

public class ResourceResponseMeta {

    @JsonProperty("created-at")
    public DateTime createdAt;

    @JsonProperty("updated-at")
    public DateTime updatedAt;

    @JsonProperty("created-by")
    public String createdBy;

    @JsonProperty("updated-by")
    public String updatedBy;

    public static ResourceResponseMeta fromDomain(final EntityMeta entityMeta) {
        final ResourceResponseMeta meta = new ResourceResponseMeta();
        meta.createdAt = entityMeta.getCreatedAt();
        meta.createdBy = entityMeta.getCreatedBy();
        meta.updatedAt = entityMeta.getLastUpdate();
        meta.updatedBy = entityMeta.getUpdatedBy();
        return meta;
    }

}

package bsep.sw.hateoas;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

public class StandardResponseMeta {

    @JsonProperty("created-at")
    public final DateTime createdAt = new DateTime();

}

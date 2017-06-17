package bsep.sw.hateoas.alarm;


import bsep.sw.domain.Alarm;
import bsep.sw.domain.AlarmLevel;
import bsep.sw.hateoas.resource.response.ResourceResponseAttributes;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

public class AlarmResponseAttributes extends ResourceResponseAttributes {

    @JsonProperty("message")
    public String message;

    @JsonProperty("resolved")
    public Boolean resolved;

    @JsonProperty("resolved-at")
    public DateTime resolvedAt;

    @JsonProperty("resolved-by")
    public String resolvedBy;

    @JsonProperty("level")
    public AlarmLevel level;

    public static AlarmResponseAttributes fromDomain(final Alarm alarm) {
        final AlarmResponseAttributes attributes = new AlarmResponseAttributes();
        attributes.message = alarm.getMessage();
        attributes.resolved = alarm.getResolved();
        attributes.resolvedAt = alarm.getResolvedAt();
        attributes.resolvedBy = alarm.getResolvedBy();
        attributes.level = alarm.getLevel();
        return attributes;
    }

}

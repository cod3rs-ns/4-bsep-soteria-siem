package bsep.sw.hateoas.alarm_definition;

import bsep.sw.hateoas.relationships.RelationshipData;
import com.fasterxml.jackson.annotation.JsonProperty;

public class MultiRuleRelationshipData extends RelationshipData {

    @JsonProperty("repetition-trigger")
    public Integer repetitionTrigger;

    @JsonProperty("interval")
    public Integer interval;

    public MultiRuleRelationshipData(String type, String id, Integer repetitionTrigger, Integer interval) {
        super(type, id);
        this.repetitionTrigger = repetitionTrigger;
        this.interval = interval;
    }
}

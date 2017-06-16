package bsep.sw.hateoas.alarm_definition;

import bsep.sw.hateoas.relationships.RelationshipData;

public class MultiRuleRelationshipData extends RelationshipData {

    public Integer repetitionTrigger;
    public Integer interval;

    public MultiRuleRelationshipData(String type, String id, Integer repetitionTrigger, Integer interval) {
        super(type, id);
        this.repetitionTrigger = repetitionTrigger;
        this.interval = interval;
    }
}

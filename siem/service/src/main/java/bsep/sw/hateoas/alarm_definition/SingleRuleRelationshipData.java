package bsep.sw.hateoas.alarm_definition;

import bsep.sw.hateoas.relationships.RelationshipData;
import bsep.sw.rule_engine.FieldType;
import bsep.sw.rule_engine.MethodType;

public class SingleRuleRelationshipData extends RelationshipData {

    public MethodType method;
    public FieldType field;
    public String value;

    public SingleRuleRelationshipData(String type, String id, MethodType method, FieldType field, String value) {
        super(type, id);
        this.method = method;
        this.field = field;
        this.value = value;
    }

}

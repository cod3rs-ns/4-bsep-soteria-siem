package bsep.sw.hateoas.alarm_definition.request;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AlarmDefinitionRequestRelationships {

    @JsonProperty("single-rules")
    public List<SingleRuleRequest> singleRules;

    @JsonProperty("multi-rule")
    public MultiRuleRequest multiRule;

}

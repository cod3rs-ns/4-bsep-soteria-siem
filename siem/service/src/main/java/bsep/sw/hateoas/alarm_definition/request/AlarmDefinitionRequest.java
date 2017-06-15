package bsep.sw.hateoas.alarm_definition.request;

import bsep.sw.domain.AlarmDefinition;
import bsep.sw.domain.SingleRule;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;

public class AlarmDefinitionRequest {

    @JsonProperty("data")
    public AlarmDefinitionRequestData data;

    public AlarmDefinition toDomain() {
        final AlarmDefinition ad = new AlarmDefinition()
                .name(data.attributes.name)
                .description(data.attributes.description)
                .level(data.attributes.level)
                .message(data.attributes.message);

        final HashSet<SingleRule> rules = new HashSet<>();

        for (SingleRuleRequest sr : data.relationships.singleRules) {
            final SingleRule rule = new SingleRule()
                    .field(sr.field)
                    .method(sr.method)
                    .value(sr.value)
                    .definition(ad);
            System.out.println(rule);
            rules.add(rule);
        }
        ad.singleRules(rules);
        return ad;
    }

}

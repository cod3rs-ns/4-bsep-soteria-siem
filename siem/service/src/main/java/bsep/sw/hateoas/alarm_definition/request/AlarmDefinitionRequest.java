package bsep.sw.hateoas.alarm_definition.request;

import bsep.sw.domain.AlarmDefinition;
import bsep.sw.domain.AlarmDefinitionType;
import bsep.sw.domain.MultiRule;
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
                .message(data.attributes.message)
                .type(data.attributes.type);

        if (ad.getDefinitionType() == AlarmDefinitionType.MULTI) {
            final MultiRule mr = new MultiRule()
                    .definition(ad)
                    .repetitionTrigger(data.relationships.multiRule.repetitionTrigger)
                    .interval(data.relationships.multiRule.interval);

            final HashSet<SingleRule> rules = new HashSet<>();
            for (final SingleRuleRequest sr : data.relationships.singleRules) {
                final SingleRule rule = new SingleRule()
                        .field(sr.field)
                        .method(sr.method)
                        .value(sr.value)
                        .parent(mr);
                rules.add(rule);
            }
            mr.singleRules(rules);
            ad.multiRule(mr);
        } else {
            final HashSet<SingleRule> rules = new HashSet<>();
            for (final SingleRuleRequest sr : data.relationships.singleRules) {
                final SingleRule rule = new SingleRule()
                        .field(sr.field)
                        .method(sr.method)
                        .value(sr.value)
                        .definition(ad);
                rules.add(rule);
            }
            ad.singleRules(rules);
        }

        return ad;
    }

}

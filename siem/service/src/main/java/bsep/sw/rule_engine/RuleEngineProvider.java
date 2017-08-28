package bsep.sw.rule_engine;

import org.easyrules.api.RulesEngine;
import org.easyrules.core.RulesEngineBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RuleEngineProvider {

    private final RulesEngine engine;

    public RuleEngineProvider() {
        this.engine = RulesEngineBuilder
                .aNewRulesEngine()
                .named(UUID.randomUUID().toString())
                .withSkipOnFirstAppliedRule(false)
                .withSkipOnFirstNonTriggeredRule(false)
                .withSkipOnFirstFailedRule(false)
                .withSilentMode(false)
                .build();
    }


    public RulesEngine get() {
        return engine;
    }
}

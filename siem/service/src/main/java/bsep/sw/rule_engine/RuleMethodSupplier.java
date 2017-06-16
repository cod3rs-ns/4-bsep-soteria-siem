package bsep.sw.rule_engine;

import org.apache.commons.lang3.StringUtils;

import java.util.function.BiFunction;


public class RuleMethodSupplier {

    public BiFunction<String, String, Boolean> getMethod(final MethodType methodType) {
        switch (methodType) {
            case CONTAINS:
                return StringUtils::containsIgnoreCase;
            case EQUALS:
                return StringUtils::equalsIgnoreCase;
            case ENDS_WITH:
                return StringUtils::endsWithIgnoreCase;
            case STARTS_WITH:
                return StringUtils::startsWithIgnoreCase;
            case REGEX: // TODO
                return null;
            default:
                return null;
        }
    }

}

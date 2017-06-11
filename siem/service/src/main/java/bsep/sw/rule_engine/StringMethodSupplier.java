package bsep.sw.rule_engine;

import org.apache.commons.lang3.StringUtils;

import java.util.function.BiFunction;


class StringMethodSupplier {

    BiFunction<String, String, Boolean> getMethod(StringRuleTypes methodType) {
        switch (methodType) {
            case CONTAINS:
                return StringUtils::containsIgnoreCase;
            case EQUALS:
                return StringUtils::equalsIgnoreCase;
            case ENDSWITH:
                return StringUtils::endsWithIgnoreCase;
            case STARTSWITH:
                return StringUtils::startsWithIgnoreCase;
            default:
                return null;
        }
    }

}

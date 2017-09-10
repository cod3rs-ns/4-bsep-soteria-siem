package bsep.sw.rule_engine;

import org.apache.commons.lang3.StringUtils;

import java.util.function.BiFunction;
import java.util.regex.Pattern;


public class MethodSupplier {

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
            case REGEX:
                return this::checkRegex;
            default:
                return null;
        }
    }

    /**
     * Wrapper around method that check if value matches regex. Needed because of inverted parameters.
     *
     * @param field Field value to compare
     * @param regex Regex
     * @return True if matches, False otherwise
     */
    public Boolean checkRegex(final String field, final String regex) {
        return Pattern.compile(regex).matcher(field).matches();
    }

}

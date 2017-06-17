package bsep.sw.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class FilterExtractor {

    private FilterExtractor() {
        super();
    }

    public static Map<String, String[]> getFilterParams(final Map<String, String[]> params, final Set<String> supportedFilters) {
        final Map<String, String[]> filters = new HashMap<>();

        for (final String key: params.keySet()) {
            if (key.startsWith("filter[")) {
                final String name = key.substring(7, key.length() - 1);
                if (supportedFilters.contains(name)) {
                    filters.put(name, params.get(key)[0].split(","));
                }
            }
        }

        return filters;
    }

}

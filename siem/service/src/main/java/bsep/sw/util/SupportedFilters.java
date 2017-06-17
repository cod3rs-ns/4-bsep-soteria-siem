package bsep.sw.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SupportedFilters {

    private SupportedFilters() {
        super();
    }

    public static Set<String> SUPPORTED_LOG_FILTERS = new HashSet<>(Arrays.asList(
            "level", "from", "to", "message", "info.host", "info.pid", "info.gid", "info.uid", "info.source"
    ));

}

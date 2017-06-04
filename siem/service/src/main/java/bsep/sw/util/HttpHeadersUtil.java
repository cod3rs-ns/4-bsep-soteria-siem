package bsep.sw.util;

public enum HttpHeadersUtil {

    ACCESS_CONTROL_ALLOW_ORIGIN("Access-Control-Allow-Origin", "*"),
    ACCESS_CONTROL_ALLOW_METHODS("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE"),
    ACCESS_CONTROL_MAX_AGE("Access-Control-Max-Age", "3600"),
    ACCESS_CONTROL_ALLOW_HEADERS("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, X-Auth-Refreshed, X-Auth-Token"),
    ACCESS_CONTROL_EXPOSE_HEADERS("Access-Control-Expose-Headers", "X-Auth-Refreshed, X-Auth-Token"),
    X_AUTH_REFRESHED("X-Auth-Refreshed", "true"),
    X_AUTH_TOKEN("X-Auth-Token", null);

    private final String name;
    private final String value;

    HttpHeadersUtil(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}

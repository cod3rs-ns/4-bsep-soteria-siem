package bsep.sw.hateoas.log;

import bsep.sw.domain.Log;
import org.joda.time.DateTime;

import static bsep.sw.hateoas.ResourceTypes.LOGS_TYPE;

public class LogResponseData {

    public String type;
    public String id;
    public LogAttributes attributes;
    public LogResponseRelationships relationships;

    public static LogResponseData fromDomain(final Log log) {
        return new LogResponseData(LOGS_TYPE, log.getId(), createAttributes(log), createRelationships(log));
    }

    public LogResponseData(final String type, final String id, final LogAttributes attributes, final LogResponseRelationships relationships) {
        this.type = type;
        this.id = id;
        this.attributes = attributes;
        this.relationships = relationships;
    }

    private static LogAttributes createAttributes(final Log log) {
        return new LogAttributes()
                .level(log.getLevel().toString())
                .info(LogInfoHateoas.fromDomain(log.getInfo()))
                .time(new DateTime(log.getTime()))
                .message(log.getMessage());
    }

    private static LogResponseRelationships createRelationships(final Log log) {
        return LogResponseRelationships.fromDomain(log);
    }

}

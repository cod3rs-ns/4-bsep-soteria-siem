package bsep.sw.hateoas.log;

import bsep.sw.domain.Log;

import java.util.stream.Collectors;

import static bsep.sw.hateoas.ResourceTypes.LOGS_TYPE;

public class LogResponseData {

    private String type;
    private String id;
    private LogAttributes attributes;
    private LogResponseRelationships relationships;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

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
        final LogInfoHateoas info = new LogInfoHateoas();
        info.setHost(log.getInfo().getHost());
        info.setSource(log.getInfo().getSource());
        info.setGid(log.getInfo().getGid());
        info.setPid(log.getInfo().getPid());
        info.setUid(log.getInfo().getUid());
        info.setErrors(log.getInfo().getErrors().stream().map(LogErrorHateoas::fromDomain).collect(Collectors.toList()));

        return new LogAttributes()
                .level(log.getLevel().toString())
                .info(info)
                .time(log.getTime())
                .message(log.getMessage());
    }

    private static LogResponseRelationships createRelationships(final Log log) {
        return LogResponseRelationships.fromDomain(log);
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public LogAttributes getAttributes() {
        return attributes;
    }

    public void setAttributes(final LogAttributes attributes) {
        this.attributes = attributes;
    }

    public LogResponseRelationships getRelationships() {
        return relationships;
    }

    public void setRelationships(final LogResponseRelationships relationships) {
        this.relationships = relationships;
    }
}

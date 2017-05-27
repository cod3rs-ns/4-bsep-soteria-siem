package bsep.sw.hateoas.log;

import bsep.sw.domain.LogInfo;

import java.util.List;
import java.util.stream.Collectors;

class LogRequestInfo {

    private String host;
    private String source;
    private String pid;
    private String gid;
    private String uid;
    private List<LogRequestError> errors;

    public LogInfo toDomain() {
        return new LogInfo()
                .host(host)
                .source(source)
                .pid(pid)
                .gid(gid)
                .uid(uid)
                .errors(errors.stream().map(LogRequestError::toDomain).collect(Collectors.toList()));
    }

    public String getHost() {
        return host;
    }

    public void setHost(final String host) {
        this.host = host;
    }

    public String getSource() {
        return source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(final String pid) {
        this.pid = pid;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(final String gid) {
        this.gid = gid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(final String uid) {
        this.uid = uid;
    }

    public List<LogRequestError> getErrors() {
        return errors;
    }

    public void setErrors(final List<LogRequestError> errors) {
        this.errors = errors;
    }
}
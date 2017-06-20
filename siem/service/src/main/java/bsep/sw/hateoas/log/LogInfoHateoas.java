package bsep.sw.hateoas.log;

import bsep.sw.domain.LogInfo;
import bsep.sw.domain.PlatformType;

import java.util.List;
import java.util.stream.Collectors;

class LogInfoHateoas {

    public String host;
    public String source;
    public String platform;
    public String pid;
    public String gid;
    public String uid;
    public List<LogErrorHateoas> errors;

    public static LogInfoHateoas fromDomain(final LogInfo logInfo) {
        final LogInfoHateoas info = new LogInfoHateoas();
        info.host = logInfo.getHost();
        info.source = logInfo.getSource();
        info.platform = logInfo.getPlatform().toString();
        info.gid = logInfo.getGid();
        info.pid = logInfo.getPid();
        info.uid = logInfo.getUid();
        info.errors = (logInfo.getErrors().stream().map(LogErrorHateoas::fromDomain).collect(Collectors.toList()));

        return info;
    }

    public LogInfo toDomain() {
        return new LogInfo()
                .host(host)
                .source(source)
                .platform(PlatformType.valueOf(platform.toUpperCase()))
                .pid(pid)
                .gid(gid)
                .uid(uid)
                .errors(errors.stream().map(LogErrorHateoas::toDomain).collect(Collectors.toList()));
    }

}
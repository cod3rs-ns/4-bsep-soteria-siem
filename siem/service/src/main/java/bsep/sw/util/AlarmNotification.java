package bsep.sw.util;


import bsep.sw.domain.Alarm;
import bsep.sw.domain.Log;
import bsep.sw.domain.Project;
import bsep.sw.hateoas.PaginationLinks;
import bsep.sw.hateoas.alarm.AlarmResponse;
import bsep.sw.hateoas.log.LogCollectionResponse;
import bsep.sw.hateoas.project.ProjectResponse;

import java.io.Serializable;
import java.util.List;

public class AlarmNotification implements Serializable {

    public ProjectResponse project;
    public LogCollectionResponse logs;
    public AlarmResponse alarm;

    public AlarmNotification() {
    }

    public AlarmNotification(final Project project, final List<Log> logs, final Alarm alarm) {
        this.project = ProjectResponse.fromDomain(project);
        this.logs = LogCollectionResponse.fromDomain(logs, new PaginationLinks("non-existing"));
        this.alarm = AlarmResponse.fromDomain(alarm);
    }
}

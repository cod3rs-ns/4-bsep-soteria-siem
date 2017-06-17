package bsep.sw.util;


import bsep.sw.domain.Alarm;
import bsep.sw.domain.Log;
import bsep.sw.domain.Project;

import java.util.List;

public class AlarmNotification {

    private Project project;
    private List<Log> logs;
    private Alarm alarm;

    public AlarmNotification() {
    }

    public AlarmNotification(final Project project, final List<Log> logs, final Alarm alarm) {
        this.project = project;
        this.logs = logs;
        this.alarm = alarm;
    }

    public Project getProject() {

        return project;
    }

    public void setProject(final Project project) {
        this.project = project;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void setLog(final List<Log> logs) {
        this.logs = logs;
    }

    public Alarm getAlarm() {
        return alarm;
    }

    public void setAlarm(final Alarm alarm) {
        this.alarm = alarm;
    }
}

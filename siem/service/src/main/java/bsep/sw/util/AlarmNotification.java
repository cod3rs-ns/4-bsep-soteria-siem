package bsep.sw.util;


import bsep.sw.domain.Alarm;
import bsep.sw.domain.Log;
import bsep.sw.domain.Project;

public class AlarmNotification {

    private Project project;
    private Log log;
    private Alarm alarm;

    public AlarmNotification() {
    }

    public AlarmNotification(final Project project, final Log log, final Alarm alarm) {
        this.project = project;
        this.log = log;
        this.alarm = alarm;
    }

    public Project getProject() {

        return project;
    }

    public void setProject(final Project project) {
        this.project = project;
    }

    public Log getLog() {
        return log;
    }

    public void setLog(final Log log) {
        this.log = log;
    }

    public Alarm getAlarm() {
        return alarm;
    }

    public void setAlarm(final Alarm alarm) {
        this.alarm = alarm;
    }
}

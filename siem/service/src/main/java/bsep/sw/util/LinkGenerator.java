package bsep.sw.util;


import bsep.sw.domain.Agent;
import bsep.sw.domain.Alarm;
import bsep.sw.domain.AlarmDefinition;
import bsep.sw.domain.Project;

public class LinkGenerator {

    public static String generateAlarmDefinitionLink(final Alarm alarm) {
        return new StringBuilder()
                .append("/api/projects/")
                .append(alarm.getDefinition().getProject().getId())
                .append("/alarm-definitions/")
                .append(alarm.getDefinition().getId())
                .toString();
    }

    public static String generateAlarmDefinitionsLink(final Project project) {
        return new StringBuilder()
                .append("/api/projects/")
                .append(project.getId())
                .append("/alarm-definitions")
                .toString();
    }

    public static String generateProjectLink(final AlarmDefinition alarmDefinition) {
        return new StringBuilder()
                .append("/api/projects/")
                .append(alarmDefinition.getProject().getId()).toString();
    }

    public static String generateProjectLink(final Agent agent) {
        return new StringBuilder()
                .append("/api/projects/")
                .append(agent.getProject().getId()).toString();
    }

    public static String generateAlarmsLink(final AlarmDefinition alarmDefinition) {
        return new StringBuilder()
                .append("/api/projects/")
                .append(alarmDefinition.getProject().getId())
                .append("/alarms").toString();
    }

    public static String generateUserLink(final Project project) {
        return new StringBuilder()
                .append("/api/users/")
                .append(project.getOwner().getId())
                .toString();
    }

    public static String generateUsersLink(final Project project) {
        return new StringBuilder()
                .append("/api/projects/")
                .append(project.getId())
                .append("/members")
                .toString();
    }

    public static String generateAgentsLink(final Project project) {
        return new StringBuilder()
                .append("/api/projects/")
                .append(project.getId())
                .append("/agents")
                .toString();
    }

}

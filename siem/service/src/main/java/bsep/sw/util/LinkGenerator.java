package bsep.sw.util;


import bsep.sw.domain.Alarm;
import bsep.sw.domain.AlarmDefinition;

public class LinkGenerator {

    public static String generateAlarmDefinitionLink(final Alarm alarm) {
        return new StringBuilder()
                .append("/api/projects/")
                .append(alarm.getDefinition().getProject().getId())
                .append("/alarm-definitions/")
                .append(alarm.getDefinition().getId()).toString();
    }

    public static String generateProjectLink(final AlarmDefinition alarmDefinition) {
        return new StringBuilder()
                .append("/api/projects/")
                .append(alarmDefinition.getProject().getId()).toString();
    }


    public static String generateAlarmsLink(AlarmDefinition alarmDefinition) {
        return new StringBuilder()
                .append("/api/projects/")
                .append(alarmDefinition.getProject().getId())
                .append("/alarms").toString();
    }
}

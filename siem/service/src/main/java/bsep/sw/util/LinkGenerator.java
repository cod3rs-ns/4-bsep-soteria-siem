package bsep.sw.util;


import bsep.sw.domain.Alarm;

public class LinkGenerator {

    public static String generateAlarmDefinitionLink(final Alarm alarm) {
        final StringBuilder sb = new StringBuilder("/api/projects/");
        sb.append(alarm.getDefinition().getProject().getId());
        sb.append("/alarm-definitions/");
        sb.append(alarm.getDefinition().getId());
        return sb.toString();
    }

}

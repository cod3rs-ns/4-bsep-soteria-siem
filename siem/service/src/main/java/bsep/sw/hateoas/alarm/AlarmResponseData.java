package bsep.sw.hateoas.alarm;


import bsep.sw.domain.Alarm;
import bsep.sw.domain.Log;
import bsep.sw.hateoas.ResourceTypes;
import bsep.sw.hateoas.resource.response.ResourceResponseData;

public class AlarmResponseData extends ResourceResponseData {

    public static AlarmResponseData fromDomain(final Alarm alarm, final Log log) {
        final AlarmResponseData data = new AlarmResponseData();
        data.id = alarm.getId();
        data.type = ResourceTypes.ALARM_TYPE;
        data.attributes = AlarmResponseAttributes.fromDomain(alarm);
        data.relationships = AlarmResponseRelationships.fromDomain(alarm, log);
        return data;
    }

}

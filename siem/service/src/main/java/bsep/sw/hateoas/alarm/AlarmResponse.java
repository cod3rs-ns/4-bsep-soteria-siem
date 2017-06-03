package bsep.sw.hateoas.alarm;

import bsep.sw.domain.Alarm;
import bsep.sw.hateoas.resource.response.ResourceResponse;
import bsep.sw.hateoas.resource.response.ResourceResponseMeta;

public class AlarmResponse extends ResourceResponse {

    public static AlarmResponse fromDomain(final Alarm alarm) {
        final AlarmResponse response = new AlarmResponse();
        response.data = AlarmResponseData.fromDomain(alarm);
        response.meta = ResourceResponseMeta.fromDomain(alarm);
        return response;
    }

}

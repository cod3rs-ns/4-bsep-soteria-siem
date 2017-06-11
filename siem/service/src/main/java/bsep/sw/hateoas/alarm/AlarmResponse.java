package bsep.sw.hateoas.alarm;

import bsep.sw.domain.Alarm;
import bsep.sw.domain.Log;
import bsep.sw.hateoas.resource.response.ResourceResponse;
import bsep.sw.hateoas.resource.response.ResourceResponseMeta;

public class AlarmResponse extends ResourceResponse {

    public static AlarmResponse fromDomain(final Alarm alarm, final Log log) {
        final AlarmResponse response = new AlarmResponse();
        response.data = AlarmResponseData.fromDomain(alarm, log);
        response.meta = ResourceResponseMeta.fromDomain(alarm);
        return response;
    }

}

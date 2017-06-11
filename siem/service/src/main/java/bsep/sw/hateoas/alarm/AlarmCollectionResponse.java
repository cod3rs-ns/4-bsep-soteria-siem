package bsep.sw.hateoas.alarm;


import bsep.sw.domain.Alarm;
import bsep.sw.domain.Log;
import bsep.sw.hateoas.PaginationLinks;
import bsep.sw.hateoas.PaginationResponseMeta;
import bsep.sw.hateoas.resource.response.ResourceCollectionResponse;

import java.util.ArrayList;
import java.util.List;

public class AlarmCollectionResponse extends ResourceCollectionResponse {

    public static AlarmCollectionResponse fromDomain(final ArrayList<Log> logs, final List<Alarm> alarms, final PaginationLinks links) {
        final AlarmCollectionResponse response = new AlarmCollectionResponse();
        for (int i = 0; i < alarms.size(); i++) {
            response.data.add(AlarmResponseData.fromDomain(alarms.get(i), logs.get(i)));
        }
        response.links = links;
        response.meta = new PaginationResponseMeta(null, alarms.size(), null);
        return response;
    }

}

package bsep.sw.hateoas.alarm;


import bsep.sw.domain.Alarm;
import bsep.sw.hateoas.PaginationLinks;
import bsep.sw.hateoas.PaginationResponseMeta;
import bsep.sw.hateoas.resource.response.ResourceCollectionResponse;
import bsep.sw.repositories.LogsRepository;

import java.util.List;

public class AlarmCollectionResponse extends ResourceCollectionResponse {

    public static AlarmCollectionResponse fromDomain(final LogsRepository logs, final List<Alarm> alarms, final PaginationLinks links) {
        final AlarmCollectionResponse response = new AlarmCollectionResponse();
        for (Alarm alarm : alarms) {
            System.out.println(alarm.getLogId());
            System.out.println(logs.findOne(alarm.getLogId()));
            response.data.add(AlarmResponseData.fromDomain(alarm, logs.findOne(alarm.getLogId())));
        }
        response.links = links;
        response.meta = new PaginationResponseMeta(null, alarms.size(), null);
        return response;
    }

}

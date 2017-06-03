package bsep.sw.hateoas.alarm;


import bsep.sw.domain.Alarm;
import bsep.sw.hateoas.PaginationLinks;
import bsep.sw.hateoas.PaginationResponseMeta;
import bsep.sw.hateoas.resource.response.ResourceCollectionResponse;

import java.util.List;
import java.util.stream.Collectors;

public class AlarmCollectionResponse extends ResourceCollectionResponse {

    public static AlarmCollectionResponse fromDomain(final List<Alarm> alarms, final PaginationLinks links) {
        final AlarmCollectionResponse response = new AlarmCollectionResponse();
        response.data.addAll(alarms.stream().map(AlarmResponseData::fromDomain).collect(Collectors.toList()));
        response.links = links;
        response.meta = new PaginationResponseMeta(null, alarms.size(), null);
        return response;
    }

}

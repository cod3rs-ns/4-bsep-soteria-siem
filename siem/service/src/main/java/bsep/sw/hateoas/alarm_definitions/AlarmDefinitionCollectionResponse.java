package bsep.sw.hateoas.alarm_definitions;

import bsep.sw.domain.AlarmDefinition;
import bsep.sw.hateoas.PaginationLinks;
import bsep.sw.hateoas.PaginationResponseMeta;
import bsep.sw.hateoas.resource.response.ResourceCollectionResponse;

import java.util.List;
import java.util.stream.Collectors;

public class AlarmDefinitionCollectionResponse extends ResourceCollectionResponse {

    public static AlarmDefinitionCollectionResponse fromDomain(final List<AlarmDefinition> definitions, final PaginationLinks links) {
        final AlarmDefinitionCollectionResponse response = new AlarmDefinitionCollectionResponse();
        response.data.addAll(definitions.stream().map(AlarmDefinitionResponseData::fromDomain).collect(Collectors.toList()));
        response.links = links;
        response.meta = new PaginationResponseMeta(null, definitions.size(), null);
        return response;
    }

}

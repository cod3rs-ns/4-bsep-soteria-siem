package bsep.sw.hateoas.alarm_definitions;

import bsep.sw.domain.AlarmDefinition;
import bsep.sw.domain.Project;
import bsep.sw.hateoas.resource.response.ResourceResponse;
import bsep.sw.hateoas.resource.response.ResourceResponseMeta;

public class AlarmDefinitionResponse extends ResourceResponse {

    public static AlarmDefinitionResponse fromDomain(final AlarmDefinition definition) {
        final AlarmDefinitionResponse response = new AlarmDefinitionResponse();
        response.data = AlarmDefinitionResponseData.fromDomain(definition);
        response.meta = ResourceResponseMeta.fromDomain(definition);
        return response;
    }

}

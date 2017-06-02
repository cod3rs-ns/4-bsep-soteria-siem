package bsep.sw.hateoas.alarm_definitions;

import bsep.sw.domain.AlarmDefinition;
import bsep.sw.domain.Project;
import bsep.sw.hateoas.ResourceTypes;
import bsep.sw.hateoas.resource.response.ResourceResponseData;

public class AlarmDefinitionResponseData extends ResourceResponseData {

    public static AlarmDefinitionResponseData fromDomain(final AlarmDefinition definition) {
        final AlarmDefinitionResponseData data = new AlarmDefinitionResponseData();
        data.id = definition.getId();
        data.type = ResourceTypes.ALARM_DEFINITION_TYPE;
        data.attributes = AlarmDefinitionResponseAttributes.fromDomain(definition);
        return data;
    }

}

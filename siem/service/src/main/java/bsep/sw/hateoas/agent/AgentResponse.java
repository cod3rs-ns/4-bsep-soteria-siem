package bsep.sw.hateoas.agent;

import bsep.sw.domain.Agent;
import bsep.sw.hateoas.resource.response.ResourceResponse;
import bsep.sw.hateoas.resource.response.ResourceResponseMeta;

public class AgentResponse extends ResourceResponse {

    public static AgentResponse fromDomain(final Agent agent) {
        final AgentResponse response = new AgentResponse();
        response.data = AgentResponseData.fromDomain(agent);
        response.meta = ResourceResponseMeta.fromDomain(agent);
        return response;
    }

}

package bsep.sw.hateoas.agent;


import bsep.sw.domain.Agent;
import bsep.sw.hateoas.PaginationLinks;
import bsep.sw.hateoas.PaginationResponseMeta;
import bsep.sw.hateoas.resource.response.ResourceCollectionResponse;

import java.util.List;
import java.util.stream.Collectors;

public class AgentCollectionResponse extends ResourceCollectionResponse {

    public static AgentCollectionResponse fromDomain(final List<Agent> agents, final PaginationLinks links) {
        final AgentCollectionResponse response = new AgentCollectionResponse();
        response.data.addAll(agents.stream().map(AgentResponseData::fromDomain).collect(Collectors.toList()));
        response.links = links;
        response.meta = new PaginationResponseMeta(null, agents.size(), null);
        return response;
    }

}

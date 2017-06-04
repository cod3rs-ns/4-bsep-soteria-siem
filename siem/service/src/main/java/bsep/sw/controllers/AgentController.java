package bsep.sw.controllers;


import bsep.sw.domain.Agent;
import bsep.sw.domain.Project;
import bsep.sw.domain.User;
import bsep.sw.hateoas.PaginationLinks;
import bsep.sw.hateoas.agent.AgentCollectionResponse;
import bsep.sw.hateoas.agent.AgentResponse;
import bsep.sw.security.UserSecurityUtil;
import bsep.sw.services.AgentService;
import bsep.sw.services.ProjectService;
import bsep.sw.util.StandardResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AgentController extends StandardResponses {

    private final AgentService agentService;
    private final ProjectService projectService;
    private final UserSecurityUtil securityUtil;

    @Autowired
    public AgentController(final AgentService agentService,
                           final ProjectService projectService,
                           final UserSecurityUtil securityUtil) {
        this.agentService = agentService;
        this.projectService = projectService;
        this.securityUtil = securityUtil;
    }

    @GetMapping("/projects/{projectId}/agents")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority(T(bsep.sw.domain.UserRole).ADMIN, T(bsep.sw.domain.UserRole).OPERATOR)")
    public ResponseEntity<?> getProjectAgents(final HttpServletRequest request,
                                              @Valid @PathVariable final Long projectId) {
        final User user = securityUtil.getLoggedUser();

        final Project project = projectService.findByMembershipAndId(user, projectId);

        if (project == null) {
            return notFound("project");
        }

        final List<Agent> agents = agentService.findAllByProject(project);
        return ResponseEntity
                .ok()
                .body(AgentCollectionResponse.fromDomain(agents, new PaginationLinks(request.getRequestURL().toString())));
    }

    @GetMapping("/projects/{projectId}/agents/{agentId}")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority(T(bsep.sw.domain.UserRole).ADMIN, T(bsep.sw.domain.UserRole).OPERATOR)")
    public ResponseEntity<?> getProjectsAlarms(@Valid @PathVariable final Long projectId,
                                               @Valid @PathVariable final Long agentId) {
        final User user = securityUtil.getLoggedUser();

        final Project project = projectService.findByMembershipAndId(user, projectId);

        if (project == null) {
            return notFound("project");
        }

        final Agent agent = agentService.findOneByProjectAndId(project, agentId);
        return ResponseEntity
                .ok()
                .body(AgentResponse.fromDomain(agent));
    }

}

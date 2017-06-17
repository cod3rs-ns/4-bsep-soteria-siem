package bsep.sw.controllers;

import bsep.sw.domain.AlarmDefinition;
import bsep.sw.domain.Project;
import bsep.sw.domain.User;
import bsep.sw.hateoas.PaginationLinks;
import bsep.sw.hateoas.alarm_definition.AlarmDefinitionCollectionResponse;
import bsep.sw.hateoas.alarm_definition.AlarmDefinitionResponse;
import bsep.sw.hateoas.alarm_definition.request.AlarmDefinitionRequest;
import bsep.sw.security.UserSecurityUtil;
import bsep.sw.services.AlarmDefinitionService;
import bsep.sw.services.ProjectService;
import bsep.sw.util.StandardResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AlarmDefinitionController extends StandardResponses {

    private final AlarmDefinitionService alarmDefinitionService;
    private final ProjectService projectService;
    private final UserSecurityUtil securityUtil;

    @Autowired
    public AlarmDefinitionController(final AlarmDefinitionService alarmDefinitionService,
                                     final ProjectService projectService,
                                     final UserSecurityUtil securityUtil) {
        this.alarmDefinitionService = alarmDefinitionService;
        this.projectService = projectService;
        this.securityUtil = securityUtil;
    }

    @PostMapping("/projects/{projectId}/alarm-definitions")
    @ResponseBody
    @PreAuthorize("hasAuthority(T(bsep.sw.domain.UserRole).ADMIN)")
    public ResponseEntity<?> createAlarmDefinition(final HttpServletRequest request,
                                                   @Valid @PathVariable final Long projectId,
                                                   @Valid @RequestBody final AlarmDefinitionRequest alarmDefinitionRequest) throws URISyntaxException {
        final User user = securityUtil.getLoggedUser();

        final AlarmDefinition toSave = alarmDefinitionRequest.toDomain();
        toSave.setByAttributes(user);
        final Project parent = projectService.findByOwnerAndId(user, projectId);

        if (parent == null) {
            return notFound("project");
        }

        toSave.project(parent);

        final AlarmDefinition result = alarmDefinitionService.save(toSave);
        return ResponseEntity
                .created(new URI(request.getRequestURL().append("/").append(result.getId()).toString()))
                .body(AlarmDefinitionResponse.fromDomain(result));
    }

    @GetMapping("/projects/{projectId}/alarm-definitions")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority(T(bsep.sw.domain.UserRole).ADMIN, T(bsep.sw.domain.UserRole).OPERATOR, T(bsep.sw.domain.UserRole).FACEBOOK)")
    public ResponseEntity<?> getProjectAlarmDefinitions(final HttpServletRequest request,
                                                        @Valid @PathVariable final Long projectId) {
        final User user = securityUtil.getLoggedUser();

        final Project project = projectService.findByMembershipAndId(user, projectId);

        if (project == null) {
            return notFound("project");
        }

        final List<AlarmDefinition> definitions = alarmDefinitionService.findAllByProject(project);

        // TODO links
        return ResponseEntity
                .ok()
                .body(AlarmDefinitionCollectionResponse.fromDomain(definitions, new PaginationLinks(request.getRequestURL().toString(), "next", "prev")));
    }

    @GetMapping("/projects/{projectId}/alarm-definitions/{definitionId}")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority(T(bsep.sw.domain.UserRole).ADMIN, T(bsep.sw.domain.UserRole).OPERATOR, T(bsep.sw.domain.UserRole).FACEBOOK)")
    public ResponseEntity<?> getAlarmDefinition(@Valid @PathVariable final Long projectId,
                                                @Valid @PathVariable final Long definitionId) {
        final User user = securityUtil.getLoggedUser();

        final Project project = projectService.findByOwnerAndId(user, projectId);

        if (project == null) {
            return notFound("project");
        }

        final AlarmDefinition definition = alarmDefinitionService.findByProjectAndId(project, definitionId);

        if (definition == null) {
            return notFound("alarm-definition");
        }

        return ResponseEntity
                .ok()
                .body(AlarmDefinitionResponse.fromDomain(definition));
    }

    @DeleteMapping("/projects/{projectId}/alarm-definitions/{definitionId}")
    @PreAuthorize("hasAuthority(T(bsep.sw.domain.UserRole).ADMIN)")
    public ResponseEntity<?> deleteAlarmDefinition(@Valid @PathVariable final Long projectId,
                                                   @Valid @PathVariable final Long definitionId) {
        final User user = securityUtil.getLoggedUser();

        final Project project = projectService.findByOwnerAndId(user, projectId);

        if (project == null) {
            return notFound("project");
        }

        alarmDefinitionService.delete(definitionId);

        return ResponseEntity
                .noContent()
                .build();
    }

}

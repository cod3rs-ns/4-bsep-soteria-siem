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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

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
    @PreAuthorize("hasAuthority(T(bsep.sw.security.Privileges).WRITE_ALARM_DEFINITION)")
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
    @PreAuthorize("hasAuthority(T(bsep.sw.security.Privileges).READ_ALARM_DEFINITION)")
    public ResponseEntity<?> getProjectAlarmDefinitions(final HttpServletRequest request,
                                                        @Valid @PathVariable final Long projectId,
                                                        @RequestParam(value = "page[offset]", required = false, defaultValue = "0") final Integer offset,
                                                        @RequestParam(value = "page[limit]", required = false, defaultValue = "10") final Integer limit) {
        final User user = securityUtil.getLoggedUser();

        final Project project = projectService.findByMembershipAndId(user, projectId);

        if (project == null) {
            return notFound("project");
        }

        final Pageable pageable = new PageRequest(offset / limit, limit);
        final Page<AlarmDefinition> definitions = alarmDefinitionService.findAllByProject(project, pageable);

        final String baseUrl = request.getRequestURL().toString();
        final String self = String.format("%s?page[offset]=%d&page[limit]=%d", baseUrl, offset, limit);
        final String next = definitions.hasNext() ? String.format("%s?page[offset]=%d&page[limit]=%d", baseUrl, limit + offset, limit) : null;
        final String prev = (offset - limit >= 0) ? String.format("%s?page[offset]=%d&page[limit]=%d", baseUrl, offset - limit, limit) : null;

        return ResponseEntity
                .ok()
                .body(AlarmDefinitionCollectionResponse.fromDomain(definitions.getContent(), new PaginationLinks(self, next, prev)));
    }

    @GetMapping("/projects/{projectId}/alarm-definitions/{definitionId}")
    @ResponseBody
    @PreAuthorize("hasAuthority(T(bsep.sw.security.Privileges).READ_ALARM_DEFINITION)")
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
    @PreAuthorize("hasAuthority(T(bsep.sw.security.Privileges).REMOVE_ALARM_DEFINITION)")
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

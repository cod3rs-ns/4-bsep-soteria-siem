package bsep.sw.controllers;

import bsep.sw.domain.AlarmDefinition;
import bsep.sw.domain.Project;
import bsep.sw.domain.User;
import bsep.sw.hateoas.PaginationLinks;
import bsep.sw.hateoas.alarm_definitions.AlarmDefinitionCollectionResponse;
import bsep.sw.hateoas.alarm_definitions.AlarmDefinitionResponse;
import bsep.sw.hateoas.alarm_definitions.request.AlarmDefinitionRequest;
import bsep.sw.security.UserSecurityUtil;
import bsep.sw.services.AlarmDefinitionService;
import bsep.sw.services.ProjectService;
import bsep.sw.util.StandardResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public ResponseEntity<?> createAlarmDefinition(final HttpServletRequest request,
                                                   @PathVariable final Long projectId,
                                                   @RequestBody final AlarmDefinitionRequest alarmDefinitionRequest) throws URISyntaxException {
        final User user = securityUtil.getLoggedUser();

        if (user == null) {
            return unauthorized();
        }

        final AlarmDefinition toSave = alarmDefinitionRequest.toDomain();

        final Project parent = projectService.findByOwnerAndId(user, projectId);

        if (parent == null) {
            return notFound("project");
        }

        toSave.project(parent);

        final AlarmDefinition result = alarmDefinitionService.save(toSave);
        return ResponseEntity.created(new URI(request.getRequestURL().append("/").append(result.getId()).toString())).body(AlarmDefinitionResponse.fromDomain(result));
    }

    @GetMapping("/projects/{projectId}/alarm-definitions")
    @ResponseBody
    public ResponseEntity<?> getProjectAlarmDefinitions(final HttpServletRequest request,
                                                        @PathVariable final Long projectId) {
        final User user = securityUtil.getLoggedUser();

        if (user == null) {
            return unauthorized();
        }

        final Project project = projectService.findByOwnerAndId(user, projectId);

        if (project == null) {
            return notFound("project");
        }
        final List<AlarmDefinition> definitions = alarmDefinitionService.findAllByProject(project);

        return ResponseEntity.ok().body(AlarmDefinitionCollectionResponse.fromDomain(definitions, new PaginationLinks(request.getRequestURL().toString())));
    }

    @GetMapping("/projects/{projectId}/alarm-definitions/{definitionId}")
    @ResponseBody
    public ResponseEntity<?> getAlarmDefinition(final HttpServletRequest request,
                                                @PathVariable final Long projectId,
                                                @PathVariable final Long definitionId) {
        final User user = securityUtil.getLoggedUser();

        if (user == null) {
            return unauthorized();
        }

        final Project project = projectService.findByOwnerAndId(user, projectId);

        if (project == null) {
            return notFound("project");
        }

        final AlarmDefinition definition = alarmDefinitionService.findByProjectAndId(project, definitionId);

        return ResponseEntity.ok().body(AlarmDefinitionResponse.fromDomain(definition));
    }

    @DeleteMapping("/projects/{projectId}/alarm-definitions/{definitionId}")
    public ResponseEntity<?> deleteAlarmDefinition(@PathVariable final Long projectId,
                                                   @PathVariable final Long definitionId) {
        final User user = securityUtil.getLoggedUser();

        if (user == null) {
            return unauthorized();
        }

        final Project project = projectService.findByOwnerAndId(user, projectId);

        if (project == null) {
            return notFound("project");
        }

        alarmDefinitionService.delete(definitionId);

        return ResponseEntity.noContent().build();
    }

}

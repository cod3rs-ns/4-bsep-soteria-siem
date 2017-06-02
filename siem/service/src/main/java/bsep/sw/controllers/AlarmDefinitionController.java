package bsep.sw.controllers;

import bsep.sw.domain.AlarmDefinition;
import bsep.sw.domain.Project;
import bsep.sw.domain.User;
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

        final Project parent = projectService.findByUserAndId(user, projectId);

        if (parent == null) {
            return notFound();
        }

        toSave.project(parent);

        final AlarmDefinition result = alarmDefinitionService.save(toSave);
        return ResponseEntity.created(new URI(request.getRequestURL().append("/").append(result.getId()).toString())).body(AlarmDefinitionResponse.fromDomain(result));
    }

}

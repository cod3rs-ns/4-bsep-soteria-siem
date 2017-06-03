package bsep.sw.controllers;


import bsep.sw.domain.Alarm;
import bsep.sw.domain.Project;
import bsep.sw.domain.User;
import bsep.sw.hateoas.PaginationLinks;
import bsep.sw.hateoas.alarm.AlarmCollectionResponse;
import bsep.sw.hateoas.alarm.AlarmResponse;
import bsep.sw.security.UserSecurityUtil;
import bsep.sw.services.AlarmDefinitionService;
import bsep.sw.services.AlarmService;
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
public class AlarmController extends StandardResponses {

    private final AlarmService alarmService;
    private final AlarmDefinitionService alarmDefinitionService;
    private final ProjectService projectService;
    private final UserSecurityUtil securityUtil;

    @Autowired
    public AlarmController(final AlarmService alarmService,
                           final AlarmDefinitionService alarmDefinitionService,
                           final ProjectService projectService,
                           final UserSecurityUtil securityUtil) {
        this.alarmService = alarmService;
        this.alarmDefinitionService = alarmDefinitionService;
        this.projectService = projectService;
        this.securityUtil = securityUtil;
    }

    @GetMapping("/projects/{projectId}/alarms")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority(T(bsep.sw.domain.UserRole).ADMIN, T(bsep.sw.domain.UserRole).OPERATOR)")
    public ResponseEntity<?> getProjectsAlarms(final HttpServletRequest request,
                                               @Valid @PathVariable final Long projectId) {
        final User user = securityUtil.getLoggedUser();

        final Project project = projectService.findByMembershipAndId(user, projectId);

        if (project == null) {
            return notFound("project");
        }

        final List<Alarm> alarms = alarmService.findAllByProject(project);
        return ResponseEntity
                .ok()
                .body(AlarmCollectionResponse.fromDomain(alarms, new PaginationLinks(request.getRequestURL().toString())));
    }

    @GetMapping("/projects/{projectId}/alarms/{alarmId}")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority(T(bsep.sw.domain.UserRole).ADMIN, T(bsep.sw.domain.UserRole).OPERATOR)")
    public ResponseEntity<?> getProjectsAlarms(@Valid @PathVariable final Long projectId,
                                               @Valid @PathVariable final Long alarmId) {
        final User user = securityUtil.getLoggedUser();

        final Project project = projectService.findByMembershipAndId(user, projectId);

        if (project == null) {
            return notFound("project");
        }

        final Alarm alarm = alarmService.findOneByProjectAndId(project, alarmId);
        return ResponseEntity
                .ok()
                .body(AlarmResponse.fromDomain(alarm));
    }
    
}

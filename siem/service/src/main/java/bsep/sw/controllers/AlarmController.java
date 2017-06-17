package bsep.sw.controllers;


import bsep.sw.domain.Alarm;
import bsep.sw.domain.AlarmDefinition;
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
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final ProjectService projectService;
    private final AlarmDefinitionService alarmDefinitionService;
    private final UserSecurityUtil securityUtil;

    @Autowired
    public AlarmController(final AlarmService alarmService,
                           final ProjectService projectService,
                           final AlarmDefinitionService alarmDefinitionService,
                           final UserSecurityUtil securityUtil) {
        this.alarmService = alarmService;
        this.projectService = projectService;
        this.securityUtil = securityUtil;
        this.alarmDefinitionService = alarmDefinitionService;
    }

    @GetMapping("/projects/{projectId}/alarms")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority(T(bsep.sw.domain.UserRole).ADMIN, T(bsep.sw.domain.UserRole).OPERATOR, T(bsep.sw.domain.UserRole).FACEBOOK)")
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

    @GetMapping("/projects/{projectId}/alarm-definitions/{definitionId}/alarms")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority(T(bsep.sw.domain.UserRole).ADMIN, T(bsep.sw.domain.UserRole).OPERATOR)")
    public ResponseEntity<?> getDefinitionAlarms(final HttpServletRequest request,
                                                 @Valid @PathVariable final Long projectId,
                                                 @Valid @PathVariable final Long definitionId) {
        final User user = securityUtil.getLoggedUser();

        final Project project = projectService.findByMembershipAndId(user, projectId);

        if (project == null) {
            return notFound("project");
        }

        final AlarmDefinition definition = alarmDefinitionService.findByProjectAndId(project, definitionId);

        if (definition == null) {
            return notFound("definition");
        }

        final List<Alarm> alarms = alarmService.findAllByDefinition(definition);

        // TODO maybe pagination

        return ResponseEntity
                .ok()
                .body(AlarmCollectionResponse.fromDomain(alarms, new PaginationLinks(request.getRequestURL().toString())));
    }

    @GetMapping("/projects/{projectId}/alarms/{alarmId}")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority(T(bsep.sw.domain.UserRole).ADMIN, T(bsep.sw.domain.UserRole).OPERATOR, T(bsep.sw.domain.UserRole).FACEBOOK)")
    public ResponseEntity<?> getProjectsAlarms(@Valid @PathVariable final Long projectId,
                                               @Valid @PathVariable final Long alarmId) {
        final User user = securityUtil.getLoggedUser();

        final Project project = projectService.findByMembershipAndId(user, projectId);

        if (project == null) {
            return notFound("project");
        }

        final Alarm alarm = alarmService.findOneByProjectAndId(project, alarmId);

        if (alarm == null) {
            return notFound("alarm");
        }

        return ResponseEntity
                .ok()
                .body(AlarmResponse.fromDomain(alarm));
    }

    @GetMapping("/alarms")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority(T(bsep.sw.domain.UserRole).ADMIN, T(bsep.sw.domain.UserRole).OPERATOR, T(bsep.sw.domain.UserRole).FACEBOOK)")
    public ResponseEntity<?> getAlarmsResponsibleFor(final HttpServletRequest request,
                                                     @RequestParam(value = "page[offset]", required = false, defaultValue = "0") final Integer offset,
                                                     @RequestParam(value = "page[limit]", required = false, defaultValue = "10") final Integer limit,
                                                     @RequestParam(value = "filter[resolved]", required = false, defaultValue = "true") final Boolean resolved) {
        final User user = securityUtil.getLoggedUser();

        final Pageable pageable = new PageRequest(offset / limit, limit);
        final Page<Alarm> page = alarmService.findByUserAndStatus(user, resolved, pageable);

        final String baseUrl = request.getRequestURL().toString();
        final String self = String.format("%s?page[offset]=%d&page[limit]=%d&filter[resolved]=%b", baseUrl, offset, limit, resolved);
        final String next = page.hasNext() ? String.format("%s?page[offset]=%d&page[limit]=%d&filter[resolved]=%b", baseUrl, limit + offset, limit, resolved) : null;
        final String prev = (offset - limit >= 0) ? String.format("%s?page[offset]=%d&page[limit]=%d&filter[resolved]=%b", baseUrl, offset - limit, limit, resolved) : null;

        return ResponseEntity
                .ok()
                .body(AlarmCollectionResponse.fromDomain(page.getContent(), new PaginationLinks(self, next, prev)));
    }

    @PutMapping("/alarms/{alarmId}/resolve")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority(T(bsep.sw.domain.UserRole).ADMIN, T(bsep.sw.domain.UserRole).OPERATOR, T(bsep.sw.domain.UserRole).FACEBOOK)")
    public ResponseEntity<?> resolveAlarm(@Valid @PathVariable final Long alarmId) {
        final User user = securityUtil.getLoggedUser();

        // TODO should not be used findOne, in this case anyone can resolve alarm which ID is known
        final Alarm alarm = alarmService.findOne(alarmId);

        if (alarm == null) {
            notFound("alarm");
        }

        alarm.setResolved(true);
        alarm.setResolvedAt(new DateTime());
        alarm.setResolvedBy(user.getName());

        return ResponseEntity
                .ok()
                .body(AlarmResponse.fromDomain(alarmService.save(alarm)));
    }

}

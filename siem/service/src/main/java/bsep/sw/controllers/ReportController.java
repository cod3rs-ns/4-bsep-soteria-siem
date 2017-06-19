package bsep.sw.controllers;

import bsep.sw.domain.Project;
import bsep.sw.domain.User;
import bsep.sw.hateoas.reports.GlobalReport;
import bsep.sw.hateoas.reports.PieCollectionReport;
import bsep.sw.hateoas.reports.ReportRequest;
import bsep.sw.hateoas.reports.ReportRequestType;
import bsep.sw.security.UserSecurityUtil;
import bsep.sw.services.AlarmService;
import bsep.sw.services.LogsService;
import bsep.sw.services.ProjectService;
import bsep.sw.util.StandardResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class ReportController extends StandardResponses {

    final private LogsService logsService;
    final private AlarmService alarmService;
    final private ProjectService projectService;
    private final UserSecurityUtil securityUtil;

    @Autowired
    public ReportController(final LogsService logsService,
                            final AlarmService alarmService,
                            final ProjectService projectService,
                            final UserSecurityUtil securityUtil) {
        this.logsService = logsService;
        this.alarmService = alarmService;
        this.projectService = projectService;
        this.securityUtil = securityUtil;
    }

    @PostMapping("/projects/{projectId}/report")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority(T(bsep.sw.domain.UserRole).ADMIN, T(bsep.sw.domain.UserRole).OPERATOR, T(bsep.sw.domain.UserRole).FACEBOOK)")
    public ResponseEntity<?> createCustomReport(@PathVariable final Long projectId,
                                                @Valid @RequestBody final ReportRequest reportRequest) {
        final User user = securityUtil.getLoggedUser();
        final Project project = projectService.findByMembershipAndId(user, projectId);

        if (project == null) {
            return notFound("project");
        }
        final GlobalReport report;
        switch (reportRequest.entityType) {
            case LOGS:
                report = logsService.getReport(project, reportRequest);
                break;
            case ALARMS:
                report = alarmService.getReport(project, reportRequest);
                break;
            default:
                return notFound("report-entity-type");
        }


        return ResponseEntity.ok(report);
    }

    @GetMapping("/projects/{projectId}/std-reports/{reportType}")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority(T(bsep.sw.domain.UserRole).ADMIN, T(bsep.sw.domain.UserRole).OPERATOR, T(bsep.sw.domain.UserRole).FACEBOOK)")
    public ResponseEntity<?> retrieveAlarmReportLevels(@PathVariable final Long projectId,
                                                       @PathVariable final ReportRequestType reportType) {
        final User user = securityUtil.getLoggedUser();
        final Project project = projectService.findByMembershipAndId(user, projectId);

        if (project == null) {
            return notFound("project");
        }

        final PieCollectionReport report;
        switch (reportType) {
            case LOG_LEVELS:
                report = logsService.getReportLevels(project);
                break;
            case LOG_PLATFORMS:
                report = logsService.getReportPlatforms(project);
                break;
            case ALARM_LEVELS:
                report = alarmService.getReportLevels(project);
                break;
            case ALARM_RESOLVED:
                report = alarmService.getReportResolved(project);
                break;
            default:
                return notFound("report-type");
        }

        return ResponseEntity.ok(report);
    }
}

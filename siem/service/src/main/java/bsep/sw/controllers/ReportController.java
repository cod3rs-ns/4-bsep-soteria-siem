package bsep.sw.controllers;

import bsep.sw.domain.Project;
import bsep.sw.domain.User;
import bsep.sw.hateoas.reports.GlobalReport;
import bsep.sw.hateoas.reports.PieCollectionReport;
import bsep.sw.hateoas.reports.ReportRequest;
import bsep.sw.security.UserSecurityUtil;
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
    final private ProjectService projectService;
    private final UserSecurityUtil securityUtil;

    @Autowired
    public ReportController(final LogsService logsService,
                            final ProjectService projectService,
                            final UserSecurityUtil securityUtil) {
        this.logsService = logsService;
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

        final GlobalReport report = logsService.getReport(project, reportRequest);

        return ResponseEntity.ok(report);
    }

    @GetMapping("/projects/{projectId}/report/levels")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority(T(bsep.sw.domain.UserRole).ADMIN, T(bsep.sw.domain.UserRole).OPERATOR, T(bsep.sw.domain.UserRole).FACEBOOK)")
    public ResponseEntity<?> retrieveLevelsReport(@PathVariable final Long projectId) {
        final User user = securityUtil.getLoggedUser();
        final Project project = projectService.findByMembershipAndId(user, projectId);

        if (project == null) {
            return notFound("project");
        }

        final PieCollectionReport report = logsService.getReportLevels(project);

        return ResponseEntity.ok(report);
    }

    @GetMapping("/projects/{projectId}/report/platforms")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority(T(bsep.sw.domain.UserRole).ADMIN, T(bsep.sw.domain.UserRole).OPERATOR, T(bsep.sw.domain.UserRole).FACEBOOK)")
    public ResponseEntity<?> retrievePlatformsReport(@PathVariable final Long projectId) {
        final User user = securityUtil.getLoggedUser();
        final Project project = projectService.findByMembershipAndId(user, projectId);

        if (project == null) {
            return notFound("project");
        }

        final PieCollectionReport report = logsService.getReportPlatforms(project);

        return ResponseEntity.ok(report);
    }
}

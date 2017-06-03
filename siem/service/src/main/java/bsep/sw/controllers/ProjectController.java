package bsep.sw.controllers;

import bsep.sw.domain.Project;
import bsep.sw.domain.User;
import bsep.sw.hateoas.PaginationLinks;
import bsep.sw.hateoas.project.ProjectCollectionResponse;
import bsep.sw.hateoas.project.ProjectRequest;
import bsep.sw.hateoas.project.ProjectResponse;
import bsep.sw.security.UserSecurityUtil;
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
public class ProjectController extends StandardResponses {

    private final ProjectService projectService;
    private final UserSecurityUtil securityUtil;

    @Autowired
    public ProjectController(final ProjectService projectService, final UserSecurityUtil securityUtil) {
        this.projectService = projectService;
        this.securityUtil = securityUtil;
    }

    @PostMapping("/projects")
    @ResponseBody
    @PreAuthorize("hasAuthority(T(bsep.sw.domain.UserRole).ADMIN)")
    public ResponseEntity<?> createProject(final HttpServletRequest request,
                                           @Valid @RequestBody final ProjectRequest projectRequest) throws URISyntaxException {
        final User user = securityUtil.getLoggedUser();
        final Project toSave = projectRequest.toDomain();
        toSave.owner(user);
        toSave.getMembers().add(user);

        final Project result = projectService.save(toSave);

        return ResponseEntity
                .created(new URI(request.getRequestURL().append("/").append(result.getId()).toString()))
                .body(ProjectResponse.fromDomain(result));
    }

    @GetMapping("/projects/owned")
    @ResponseBody
    @PreAuthorize("hasAuthority(T(bsep.sw.domain.UserRole).ADMIN)")
    public ResponseEntity<?> getOwnedProjects(final HttpServletRequest request) {
        final User user = securityUtil.getLoggedUser();
        final List<Project> projects = projectService.findOwned(user);

        return ResponseEntity
                .ok()
                .body(ProjectCollectionResponse.fromDomain(projects, new PaginationLinks(request.getRequestURL().toString())));
    }

    @GetMapping("/projects/member-of")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority(T(bsep.sw.domain.UserRole).ADMIN, T(bsep.sw.domain.UserRole).OPERATOR)")
    public ResponseEntity<?> getMembershipProjects(final HttpServletRequest request) {
        final User user = securityUtil.getLoggedUser();
        final List<Project> projects = projectService.findAllByMembership(user);

        return ResponseEntity
                .ok()
                .body(ProjectCollectionResponse.fromDomain(projects, new PaginationLinks(request.getRequestURL().toString())));
    }

    @GetMapping("/projects/{projectId}")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority(T(bsep.sw.domain.UserRole).ADMIN, T(bsep.sw.domain.UserRole).OPERATOR)")
    public ResponseEntity<?> getProject(@Valid @PathVariable Long projectId) {
        final User user = securityUtil.getLoggedUser();

        final Project result = projectService.findByMembershipAndId(user, projectId);

        if (result == null) {
            return notFound("project");
        }

        return ResponseEntity.ok().body(ProjectResponse.fromDomain(result));
    }

    @DeleteMapping("/projects/{projectId}")
    @PreAuthorize("hasAuthority(T(bsep.sw.domain.UserRole).ADMIN)")
    public ResponseEntity<?> deleteProject(@Valid @PathVariable Long projectId) {
        final User user = securityUtil.getLoggedUser();

        if (!projectService.delete(user, projectId)) {
            return notFound("project");
        }

        return ResponseEntity.noContent().build();
    }

}

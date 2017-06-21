package bsep.sw.controllers;

import bsep.sw.domain.Project;
import bsep.sw.domain.User;
import bsep.sw.hateoas.ErrorResponse;
import bsep.sw.hateoas.PaginationLinks;
import bsep.sw.hateoas.project.ProjectCollectionResponse;
import bsep.sw.hateoas.project.ProjectResponse;
import bsep.sw.hateoas.project.request.ProjectRequest;
import bsep.sw.hateoas.user.UserCollectionResponse;
import bsep.sw.hateoas.user.UserResponse;
import bsep.sw.security.UserSecurityUtil;
import bsep.sw.services.ProjectService;
import bsep.sw.services.UserService;
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
public class ProjectController extends StandardResponses {

    private final ProjectService projectService;
    private final UserService userService;
    private final UserSecurityUtil securityUtil;

    @Autowired
    public ProjectController(final ProjectService projectService, final UserService userService, final UserSecurityUtil securityUtil) {
        this.projectService = projectService;
        this.userService = userService;
        this.securityUtil = securityUtil;
    }

    @PostMapping("/projects")
    @ResponseBody
    @PreAuthorize("hasAuthority(T(bsep.sw.security.Privileges).WRITE_PROJECT)")
    public ResponseEntity<?> createProject(final HttpServletRequest request,
                                           @Valid @RequestBody final ProjectRequest projectRequest) throws URISyntaxException {
        final User user = securityUtil.getLoggedUser();
        final Project toSave = projectRequest.toDomain();
        toSave.setByAttributes(user);
        toSave.owner(user);
        toSave.getMembers().add(user);

        final Project result = projectService.save(toSave);

        return ResponseEntity
                .created(new URI(request.getRequestURL().append("/").append(result.getId()).toString()))
                .body(ProjectResponse.fromDomain(result));
    }

    @GetMapping("/projects/owned")
    @ResponseBody
    @PreAuthorize("hasAuthority(T(bsep.sw.security.Privileges).READ_PROJECT)")
    public ResponseEntity<?> getOwnedProjects(final HttpServletRequest request,
                                              @RequestParam(value = "page[offset]", required = false, defaultValue = "0") final Integer offset,
                                              @RequestParam(value = "page[limit]", required = false, defaultValue = "2") final Integer limit) {
        final User user = securityUtil.getLoggedUser();

        final Pageable pageable = new PageRequest(offset / limit, limit);
        final Page<Project> page = projectService.findOwned(user, pageable);

        final String baseUrl = request.getRequestURL().toString();
        final String self = String.format("%s?page[offset]=%d&page[limit]=%d", baseUrl, offset, limit);
        final String next = page.hasNext() ? String.format("%s?page[offset]=%d&page[limit]=%d", baseUrl, limit + offset, limit) : null;
        final String prev = (offset - limit >= 0) ? String.format("%s?page[offset]=%d&page[limit]=%d", baseUrl, offset - limit, limit) : null;

        return ResponseEntity
                .ok()
                .body(ProjectCollectionResponse.fromDomain(page.getContent(), new PaginationLinks(self, next, prev)));
    }

    @GetMapping("/projects/member-of")
    @ResponseBody
    @PreAuthorize("hasAuthority(T(bsep.sw.security.Privileges).READ_PROJECT)")
    public ResponseEntity<?> getMembershipProjects(final HttpServletRequest request,
                                                   @RequestParam(value = "page[offset]", required = false, defaultValue = "0") final Integer offset,
                                                   @RequestParam(value = "page[limit]", required = false, defaultValue = "2") final Integer limit,
                                                   @RequestParam(value = "filter[owner]", required = false, defaultValue = "false") final Boolean owner) {
        final User user = securityUtil.getLoggedUser();

        final Pageable pageable = new PageRequest(offset / limit, limit);
        final Page<Project> page = projectService.findByMembership(user, owner, pageable);

        final String baseUrl = request.getRequestURL().toString();
        final String self = String.format("%s?page[offset]=%d&page[limit]=%d&filter[owner]=%b", baseUrl, offset, limit, owner);
        final String next = page.hasNext() ? String.format("%s?page[offset]=%d&page[limit]=%d&filter[owner]=%b", baseUrl, limit + offset, limit, owner) : null;
        final String prev = (offset - limit >= 0) ? String.format("%s?page[offset]=%d&page[limit]=%d&filter[owner]=%b", baseUrl, offset - limit, limit, owner) : null;

        return ResponseEntity
                .ok()
                .body(ProjectCollectionResponse.fromDomain(page.getContent(), new PaginationLinks(self, next, prev)));
    }


    @GetMapping("/projects/{projectId}")
    @ResponseBody
    @PreAuthorize("hasAuthority(T(bsep.sw.security.Privileges).READ_PROJECT)")
    public ResponseEntity<?> getProject(@Valid @PathVariable final Long projectId) {
        final User user = securityUtil.getLoggedUser();

        final Project result = projectService.findByMembershipAndId(user, projectId);

        if (result == null) {
            return notFound("project");
        }

        return ResponseEntity
                .ok()
                .body(ProjectResponse.fromDomain(result));
    }

    @DeleteMapping("/projects/{projectId}")
    @PreAuthorize("hasAuthority(T(bsep.sw.security.Privileges).REMOVE_PROJECT)")
    public ResponseEntity<?> deleteProject(@Valid @PathVariable final Long projectId) {
        final User user = securityUtil.getLoggedUser();

        if (!projectService.delete(user, projectId)) {
            return notFound("project");
        }

        return ResponseEntity
                .noContent()
                .build();
    }

    @GetMapping("/projects/{projectId}/users")
    @ResponseBody
    @PreAuthorize("hasAuthority(T(bsep.sw.security.Privileges).READ_PROJECT_COLLABORATORS)")
    public ResponseEntity<?> projectCollaborators(@Valid @PathVariable final Long projectId) {
        final User user = securityUtil.getLoggedUser();

        final Project project = projectService.findByMembershipAndId(user, projectId);
        if (project == null) {
            return notFound("project");
        }

        // TODO links
        return ResponseEntity
                .ok()
                .body(UserCollectionResponse.fromDomain(project.getMembers(), new PaginationLinks("self", "next")));
    }

    @PostMapping("/projects/{projectId}/users/{userId}")
    @ResponseBody
    @PreAuthorize("hasAuthority(T(bsep.sw.security.Privileges).WRITE_PROJECT_COLLABORATORS)")
    public ResponseEntity<?> addCollaborator(@Valid @PathVariable final Long projectId, @Valid @PathVariable final Long userId) {
        final User user = securityUtil.getLoggedUser();

        // TODO now when only admin can access this method should be used 'method' with owned
        final Project project = projectService.findByMembershipAndId(user, projectId);
        if (project == null) {
            return notFound("project");
        }

        final User collaborator = userService.findOne(userId);
        if (collaborator == null) {
            return notFound("user");
        }

        final Project alreadyCollaborate = projectService.findByMembershipAndId(collaborator, projectId);
        if (alreadyCollaborate != null) {
            return ResponseEntity.badRequest().body(new ErrorResponse("400", "User collaborator", "User is already collaborator!"));
        }

        userService.save(collaborator.addProject(project));
        projectService.save(project.addMember(collaborator));

        return ResponseEntity
                .ok()
                .body(UserResponse.fromDomain(collaborator));
    }

}

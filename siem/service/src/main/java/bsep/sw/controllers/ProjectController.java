package bsep.sw.controllers;

import bsep.sw.domain.Project;
import bsep.sw.domain.User;
import bsep.sw.hateoas.ErrorResponse;
import bsep.sw.hateoas.project.ProjectRequest;
import bsep.sw.hateoas.project.ProjectResponse;
import bsep.sw.security.UserSecurityUtil;
import bsep.sw.services.ProjectService;
import bsep.sw.util.StandardResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

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
    public ResponseEntity<?> createProject(final HttpServletRequest request,
                                           @RequestBody final ProjectRequest projectRequest) throws URISyntaxException {
        final User user = securityUtil.getLoggedUser();

        if (user == null) {
            return unauthorized();
        }

        final Project toSave = projectRequest.toDomain();
        toSave.owner(user);

        final Project result = projectService.save(toSave);

        return ResponseEntity.created(new URI(request.getRequestURL().append(result.getId()).toString())).body(ProjectResponse.fromDomain(result));
    }

    @GetMapping("/projects/{projectId}")
    @ResponseBody
    public ResponseEntity<?> getProject(final HttpServletRequest request,
                                        @PathVariable Long projectId) {
        final User user = securityUtil.getLoggedUser();

        if (user == null) {
            return unauthorized();
        }

        final Project result = projectService.findByUserAndId(user, projectId);

        if (result == null) {
            return notFound();
        }

        return ResponseEntity.ok().body(ProjectResponse.fromDomain(result));
    }

}

package bsep.sw.controllers;

import bsep.sw.domain.Project;
import bsep.sw.hateoas.project.ProjectRequest;
import bsep.sw.hateoas.project.ProjectResponse;
import bsep.sw.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(final ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/projects")
    @ResponseBody
    public ResponseEntity<ProjectResponse> createProject(@RequestBody ProjectRequest projectRequest) throws URISyntaxException {
        // TODO make real logic, this is example
        return ResponseEntity.ok().body(new ProjectResponse().fromDomain(new Project()));
    }

}

package bsep.sw.controllers;


import bsep.sw.domain.Agent;
import bsep.sw.domain.AgentConfig;
import bsep.sw.domain.Project;
import bsep.sw.domain.User;
import bsep.sw.hateoas.PaginationLinks;
import bsep.sw.hateoas.agent.AgentCollectionResponse;
import bsep.sw.hateoas.agent.AgentRequest;
import bsep.sw.hateoas.agent.AgentResponse;
import bsep.sw.hateoas.agent_config.AgentConfigRequest;
import bsep.sw.security.UserSecurityUtil;
import bsep.sw.services.AgentService;
import bsep.sw.services.ProjectService;
import bsep.sw.util.AgentKeys;
import bsep.sw.util.KeyStoreUtil;
import bsep.sw.util.StandardResponses;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api")
public class AgentController extends StandardResponses {

    private final AgentService agentService;
    private final ProjectService projectService;
    private final UserSecurityUtil securityUtil;
    private final KeyStoreUtil keyStoreUtil;

    @Autowired
    public AgentController(final AgentService agentService,
                           final ProjectService projectService,
                           final UserSecurityUtil securityUtil,
                           final KeyStoreUtil keyStoreUtil) {
        this.agentService = agentService;
        this.projectService = projectService;
        this.securityUtil = securityUtil;
        this.keyStoreUtil = keyStoreUtil;
    }

    @GetMapping("/projects/{projectId}/agents")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority(T(bsep.sw.domain.UserRole).ADMIN, T(bsep.sw.domain.UserRole).OPERATOR, T(bsep.sw.domain.UserRole).FACEBOOK)")
    public ResponseEntity<?> getProjectAgents(final HttpServletRequest request,
                                              @Valid @PathVariable final Long projectId,
                                              @RequestParam(value = "page[offset]", required = false, defaultValue = "0") final Integer offset,
                                              @RequestParam(value = "page[limit]", required = false, defaultValue = "6") final Integer limit) {
        final User user = securityUtil.getLoggedUser();

        final Project project = projectService.findByMembershipAndId(user, projectId);

        if (project == null) {
            return notFound("project");
        }

        final String baseUrl = request.getRequestURL().toString();
        final String self = String.format("%s?page[offset]=%d&page[limit]=%d", baseUrl, offset, limit);
        // FIXME 'next' should be 'null' if there's no data presented
        final String next = String.format("%s?page[offset]=%d&page[limit]=%d", baseUrl, limit + offset, limit);
        final String prev = (offset - limit >= 0) ? String.format("%s?page[offset]=%d&page[limit]=%d", baseUrl, offset - limit, limit) : null;

        final Pageable pageable = new PageRequest(offset / limit, limit);

        final List<Agent> agents = agentService.findAllByProject(project, pageable);
        return ResponseEntity
                .ok()
                .body(AgentCollectionResponse.fromDomain(agents, new PaginationLinks(self, next, prev)));
    }

    @PostMapping("/projects/{projectId}/agents")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority(T(bsep.sw.domain.UserRole).ADMIN, T(bsep.sw.domain.UserRole).OPERATOR, T(bsep.sw.domain.UserRole).FACEBOOK)")
    public ResponseEntity<?> addAgentToProject(@Valid @PathVariable final Long projectId, @RequestBody final AgentRequest request) {
        final User user = securityUtil.getLoggedUser();

        final Project project = projectService.findByMembershipAndId(user, projectId);

        if (project == null) {
            return notFound("project");
        }

        final Agent agent = agentService.save(request.toDomain().project(project));
        keyStoreUtil.generateAndSaveCertificate(agent.getId().toString());

        return ResponseEntity
                .ok()
                .body(AgentResponse.fromDomain(agent));
    }

    @GetMapping("/projects/{projectId}/agents/{agentId}")
    @ResponseBody
    @PreAuthorize("hasAnyAuthority(T(bsep.sw.domain.UserRole).ADMIN, T(bsep.sw.domain.UserRole).OPERATOR, T(bsep.sw.domain.UserRole).FACEBOOK)")
    public ResponseEntity<?> getProjectAgent(@Valid @PathVariable final Long projectId,
                                             @Valid @PathVariable final Long agentId) {
        final User user = securityUtil.getLoggedUser();

        final Project project = projectService.findByMembershipAndId(user, projectId);

        if (project == null) {
            return notFound("project");
        }

        final Agent agent = agentService.findOneByProjectAndId(project, agentId);

        if (agent == null) {
            return notFound("agent");
        }

        return ResponseEntity
                .ok()
                .body(AgentResponse.fromDomain(agent));
    }

    @PostMapping(value = "/agents", produces = "application/zip", consumes = "application/json")
    @PreAuthorize("hasAnyAuthority(T(bsep.sw.domain.UserRole).ADMIN, T(bsep.sw.domain.UserRole).OPERATOR)")
    public void downloadAgentWithConfiguration(final HttpServletResponse response, @RequestBody final AgentConfigRequest request) throws IOException {
        final Agent agent = agentService.findOne(request.getData().getAttributes().getAgentId());

        final boolean windows = "WINDOWS_AGENT".equalsIgnoreCase(request.getData().getAttributes().getOs());

        final ZipOutputStream zip = new ZipOutputStream(response.getOutputStream());

        // Set headers
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\"agent.zip\"");

        final AgentKeys keys = keyStoreUtil.findKeys(agent.getId().toString());
        final AgentConfig agentConfig = new AgentConfig(request.getData().getAttributes(), agent, keys);

        // Provide agent and config file to zip
        final ArrayList<File> files = new ArrayList<>(2);
        files.add(new File("agents/win.zip"));
        files.add(new File("README.md"));
        files.add(new File(windows ? agentConfig.toJsonFile() : agentConfig.toYmlFile()));

        // Add files to '.zip'
        for (final File file : files) {
            zip.putNextEntry(new ZipEntry(file.getName()));
            final FileInputStream fileInputStream = new FileInputStream(file);

            IOUtils.copy(fileInputStream, zip);

            fileInputStream.close();
            zip.closeEntry();
        }

        zip.close();
    }
}

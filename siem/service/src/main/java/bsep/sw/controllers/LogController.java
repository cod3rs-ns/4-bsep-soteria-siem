package bsep.sw.controllers;

import bsep.sw.domain.Log;
import bsep.sw.hateoas.PaginationLinks;
import bsep.sw.hateoas.log.LogCollectionResponse;
import bsep.sw.hateoas.log.LogRequest;
import bsep.sw.hateoas.log.LogResponse;
import bsep.sw.repositories.LogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class LogController {

    private LogsRepository logs;

    @Autowired
    public LogController(LogsRepository logs) {
        this.logs = logs;
    }

    @GetMapping("/projects/{projectId}/logs")
    public ResponseEntity<?> retrieveLogsForProject(final HttpServletRequest request,
                                                    @PathVariable("projectId") final Long project,
                                                    @RequestParam(value = "page[offset]", required = false, defaultValue = "0") Integer offset,
                                                    @RequestParam(value= "page[limit]", required = false, defaultValue = "10") Integer limit) {

        final String baseUrl = request.getRequestURL().toString();
        final String self = String.format("%s?page[offset]=%d&page[limit]=%d", baseUrl, offset, limit);
        final String next = String.format("%s?page[offset]=%d&page[limit]=%d", baseUrl, limit + offset, limit);

        final Pageable pageable = new PageRequest(offset/limit, limit);
        final PaginationLinks links = new PaginationLinks(self, next);

        return ResponseEntity.ok(LogCollectionResponse.fromDomain(logs.findByProject(project, pageable), links));
    }

    @PostMapping("/logs")
    @ResponseBody
    public ResponseEntity<?> storeLog(@RequestBody LogRequest request) {
        final Log log = request.toDomain()
                .id(UUID.randomUUID().toString());

        return ResponseEntity.ok(LogResponse.fromDomain(logs.save(log)));
    }

}

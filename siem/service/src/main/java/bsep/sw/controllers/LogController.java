package bsep.sw.controllers;

import bsep.sw.domain.Alarm;
import bsep.sw.domain.Log;
import bsep.sw.hateoas.ErrorResponse;
import bsep.sw.hateoas.PaginationLinks;
import bsep.sw.hateoas.log.LogCollectionResponse;
import bsep.sw.hateoas.log.LogRequest;
import bsep.sw.hateoas.log.LogResponse;
import bsep.sw.repositories.LogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class LogController {

    private final LogsRepository logs;
    private final SimpMessagingTemplate template;

    @Autowired
    public LogController(final LogsRepository logs, final SimpMessagingTemplate template) {
        this.logs = logs;
        this.template = template;
    }

    @GetMapping("/projects/{projectId}/logs")
    public ResponseEntity<?> retrieveLogsForProject(final HttpServletRequest request,
                                                    @PathVariable("projectId") final Long project,
                                                    @RequestParam(value = "page[offset]", required = false, defaultValue = "0") final Integer offset,
                                                    @RequestParam(value = "page[limit]", required = false, defaultValue = "10") final Integer limit) {

        final String baseUrl = request.getRequestURL().toString();
        final String self = String.format("%s?page[offset]=%d&page[limit]=%d", baseUrl, offset, limit);
        final String next = String.format("%s?page[offset]=%d&page[limit]=%d", baseUrl, limit + offset, limit);

        final Pageable pageable = new PageRequest(offset / limit, limit);
        final PaginationLinks links = new PaginationLinks(self, next);

        return ResponseEntity.ok(LogCollectionResponse.fromDomain(logs.findByProject(project, pageable), links));
    }

    @GetMapping("/logs/{logId}")
    public ResponseEntity<?> retrieveSingleLog(@PathVariable("logId") final String logId) {
        final Log log = logs.findOne(logId);
        if (log == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse("not exists", "Log does not exists", String.format("Log with id %s does not exists", logId)));
        }

        return ResponseEntity.ok(LogResponse.fromDomain(log));
    }

    @PostMapping("/logs")
    @ResponseBody
    public ResponseEntity<?> storeLog(@RequestBody final LogRequest request) {
        final Log log = request.toDomain()
                .id(UUID.randomUUID().toString());

        // Drools engine should return alarm that will be triggered
        final Alarm dummyAlarm = new Alarm()
                .message("Same user try to log 3 times in a row")
                .resolved(false);

        // Send notifications through socket
        template.convertAndSend("/publish/threat/" + log.getProject(), dummyAlarm);

        return ResponseEntity.ok(LogResponse.fromDomain(logs.save(log)));
    }

}

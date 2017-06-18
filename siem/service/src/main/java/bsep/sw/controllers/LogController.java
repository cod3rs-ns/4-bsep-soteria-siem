package bsep.sw.controllers;

import bsep.sw.domain.Log;
import bsep.sw.hateoas.PaginationLinks;
import bsep.sw.hateoas.log.LogCollectionResponse;
import bsep.sw.hateoas.log.LogRequest;
import bsep.sw.hateoas.log.LogResponse;
import bsep.sw.rule_engine.rules.RulesService;
import bsep.sw.services.LogsService;
import bsep.sw.util.CSRUtil;
import bsep.sw.util.FilterExtractor;
import bsep.sw.util.StandardResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

import static bsep.sw.util.SupportedFilters.SUPPORTED_LOG_FILTERS;

@RestController
@RequestMapping("/api")
public class LogController extends StandardResponses {

    private final LogsService logs;
    private final RulesService rulesService;
    private final CSRUtil csrUtil;

    @Autowired
    public LogController(final LogsService logs,
                         final RulesService rulesService,
                         final CSRUtil csrUtil) {
        this.logs = logs;
        this.rulesService = rulesService;
        this.csrUtil = csrUtil;
    }

    @GetMapping("/projects/{projectId}/logs")
    public ResponseEntity<?> retrieveLogsForProject(final HttpServletRequest request,
                                                    @PathVariable("projectId") final Long project,
                                                    @RequestParam(value = "page[offset]", required = false, defaultValue = "0") final Integer offset,
                                                    @RequestParam(value = "page[limit]", required = false, defaultValue = "10") final Integer limit) {

        final String baseUrl = request.getRequestURL().toString();
        final String self = String.format("%s?page[offset]=%d&page[limit]=%d", baseUrl, offset, limit);
        final String next = String.format("%s?page[offset]=%d&page[limit]=%d", baseUrl, limit + offset, limit);

        final Map<String, String[]> filters = FilterExtractor.getFilterParams(request.getParameterMap(), SUPPORTED_LOG_FILTERS);

        final PaginationLinks links = new PaginationLinks(self, next);

        return ResponseEntity
                .ok(LogCollectionResponse.fromDomain(logs.findByProject(project, filters, limit, offset), links));
    }

    @GetMapping("/logs/{logId}")
    public ResponseEntity<?> retrieveSingleLog(@PathVariable("logId") final String logId) {
        final Log log = logs.findOne(logId);
        if (log == null) {
            notFound("log");
        }

        return ResponseEntity.ok(LogResponse.fromDomain(log));
    }

    @PostMapping(value = "/logs/agent/{agentId}", consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> storeLog(@PathVariable("agentId") final Long agent,
                                      final HttpEntity<String> httpEntity) throws Exception {
        final String body = httpEntity.getBody();
        final LogRequest request = csrUtil.parseRequest(body, agent.toString());

        final Log log = request
                .toDomain()
                .id(UUID.randomUUID().toString());

        // TODO maybe not bad idea to check for project existence!!!

        final Log savedLog = logs.save(log);

        // push to rules evaluation
        rulesService.evaluateNewLog(savedLog);

        return ResponseEntity.ok(LogResponse.fromDomain(savedLog));
    }

}

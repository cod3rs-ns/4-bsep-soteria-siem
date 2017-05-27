package bsep.sw.controllers;

import bsep.sw.domain.Log;
import bsep.sw.hateoas.log.LogRequest;
import bsep.sw.repositories.LogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class LogController {

    private LogsRepository logs;

    @Autowired
    public LogController(LogsRepository logs) {
        this.logs = logs;
    }

    @PostMapping("/logs")
    @ResponseBody
    public ResponseEntity<?> storeLog(@RequestBody LogRequest request) {
        final Log log = request.toDomain()
                .id(UUID.randomUUID().toString());
        return ResponseEntity.ok(logs.save(log));
    }

}

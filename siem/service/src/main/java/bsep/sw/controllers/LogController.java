package bsep.sw.controllers;

import bsep.sw.repositories.LogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<?> storeLog() {
        // TODO Implement JSON API for Log storing
        return ResponseEntity.ok("");
    }

}

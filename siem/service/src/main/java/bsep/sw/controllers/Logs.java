package bsep.sw.controllers;

import bsep.sw.dao.LogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Logs {

    private LogsRepository logs;

    @Autowired
    public Logs(LogsRepository logs) {
        this.logs = logs;
    }

    @RequestMapping(
            value = "/api/logs",
            method = RequestMethod.POST
    )
    public void storeLog() {
        // TODO Implement JSON API for Log storing
    }

}

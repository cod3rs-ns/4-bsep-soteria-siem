package bsep.sw.rule_engine.rules;


import bsep.sw.domain.*;
import bsep.sw.repositories.AlarmedLogsRepository;
import bsep.sw.rule_engine.RuleEngineProvider;
import bsep.sw.services.AlarmDefinitionService;
import bsep.sw.services.AlarmService;
import bsep.sw.services.LogsService;
import bsep.sw.services.ProjectService;
import org.apache.log4j.Logger;
import org.easyrules.api.RulesEngine;
import org.easyrules.core.RulesEngineBuilder;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RulesService {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final SimpMessagingTemplate template;
    private final AlarmService alarmService;
    private final ProjectService projectService;
    private final AlarmDefinitionService alarmDefinitionService;
    private final LogsService logsService;
    private final AlarmedLogsRepository alarmedLogsRepository;
    private final RuleEngineProvider ruleEngineProvider;

    @Autowired
    public RulesService(final SimpMessagingTemplate template,
                        final AlarmService alarmService,
                        final ProjectService projectService,
                        final AlarmDefinitionService alarmDefinitionService,
                        final LogsService logsService,
                        final AlarmedLogsRepository alarmedLogsRepository,
                        final RuleEngineProvider ruleEngineProvider) {
        this.template = template;
        this.alarmService = alarmService;
        this.projectService = projectService;
        this.alarmDefinitionService = alarmDefinitionService;
        this.logsService = logsService;
        this.alarmedLogsRepository = alarmedLogsRepository;
        this.ruleEngineProvider = ruleEngineProvider;
    }

    public void evaluateNewLog(final Log log) {
        final RulesEngine rulesEngine = ruleEngineProvider.get();
        final Project project = projectService.findOne(log.getProject());
        final List<AlarmDefinition> definitions = alarmDefinitionService.findAllByProject(project);

        for (final AlarmDefinition definition : definitions) {
            if (definition.getDefinitionType() == AlarmDefinitionType.SINGLE) {
                final SingleLogRule logRule = new SingleLogRule(log, definition, projectService, alarmService, alarmDefinitionService, template);
                rulesEngine.registerRule(logRule);
            } else {
                final ArrayList<Log> logs = gatherNonProcessedLogs(definition);
                final MultiLogRule logRule = new MultiLogRule(logs, definition, projectService, alarmService, alarmDefinitionService, template, alarmedLogsRepository);
                rulesEngine.registerRule(logRule);
            }
        }
        rulesEngine.fireRules();
        rulesEngine.clearRules();
    }

    private ArrayList<Log> gatherNonProcessedLogs(final AlarmDefinition definition) {
        // retrieve all logs in interval
        final DateTime forwardLookup = DateTime.now().minus(definition.getMultiRule().getInterval() * 1000); // convert to millis
        final List<Log> allLogsInInterval = logsService.findByProjectAndTimeAfter(definition.getProject().getId(), forwardLookup.getMillis());

        // retrieve all logs that triggered Alarm within same definition
        final List<AlarmedLogs> alreadyApplied = alarmedLogsRepository.findAllByAlarmDefinitionId(definition.getId());
        final ArrayList<String> logIds = new ArrayList<>();
        alreadyApplied.forEach(al -> logIds.add(al.getLogId()));

        // remove all logs that have triggered alarms
        final ArrayList<Log> toRemove = new ArrayList<>();
        allLogsInInterval.forEach(log -> {
            for (String logId : logIds) {
                if (logId.equals(log.getId())) {
                    toRemove.add(log);
                    break;
                }
            }
        });
        allLogsInInterval.removeAll(toRemove);
        logger.info("Total number of logs to evaluate: " + allLogsInInterval.size());
        return new ArrayList<>(allLogsInInterval);
    }
}

package bsep.sw.rule_engine.rules;

import bsep.sw.domain.*;
import bsep.sw.repositories.AlarmedLogsRepository;
import bsep.sw.rule_engine.FieldSupplier;
import bsep.sw.rule_engine.FieldType;
import bsep.sw.rule_engine.RuleMethodSupplier;
import bsep.sw.services.AlarmDefinitionService;
import bsep.sw.services.AlarmService;
import bsep.sw.services.ProjectService;
import bsep.sw.util.AlarmNotification;
import org.apache.log4j.Logger;
import org.easyrules.core.BasicRule;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class MultiLogRule extends BasicRule {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final SimpMessagingTemplate template;

    // possibly should be injected
    private final ProjectService projectService;
    private final AlarmDefinitionService alarmDefinitionService;
    private final AlarmService alarmService;

    // perform rule on
    private final List<Log> logs;
    private final AlarmDefinition alarmDefinition;
    private final FieldSupplier fieldSupplier = new FieldSupplier();
    private final RuleMethodSupplier methodSupplier = new RuleMethodSupplier();

    private final List<AlarmedLogs> possibleTriggeredPairs = new ArrayList<>();
    private final AlarmedLogsRepository alarmedLogsRepository;

    private final List<FieldType> errorFields = Arrays.asList(FieldType.ERROR, FieldType.ERROR_NO, FieldType.ERROR_TYPE, FieldType.STACK);

    MultiLogRule(final List<Log> logs,
                 final AlarmDefinition alarmDefinition,
                 final ProjectService projectService,
                 final AlarmService alarmService,
                 final AlarmDefinitionService alarmDefinitionService,
                 final SimpMessagingTemplate template,
                 final AlarmedLogsRepository alarmedLogsRepository) {
        super(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        this.logs = logs;
        this.alarmDefinition = alarmDefinition;
        this.projectService = projectService;
        this.alarmService = alarmService;
        this.alarmDefinitionService = alarmDefinitionService;
        this.template = template;
        this.alarmedLogsRepository = alarmedLogsRepository;
    }


    @Override
    public boolean evaluate() {
        for (final Log log : logs) {
            for (final SingleRule rule : alarmDefinition.getMultiRule().getSingleRules()) {
                // specific check for list of errors, need at least one to match rule
                if (errorFields.contains(rule.getField())) {
                    Boolean atLeastOneMatchingRule = false;
                    for (LogError error : log.getInfo().getErrors()) {
                        if (methodSupplier
                                .getMethod(rule.getMethod())
                                .apply(fieldSupplier.getErrorField(log, rule.getField(), error).get(), rule.getValue())) {
                            atLeastOneMatchingRule = true;
                        }
                    }
                    if (!atLeastOneMatchingRule) {
                        break;
                    }
                } else {
                    if (!methodSupplier
                            .getMethod(rule.getMethod())
                            .apply(fieldSupplier.getField(log, rule.getField()).get(), rule.getValue())) {
                        break;
                    }
                }
            }
            possibleTriggeredPairs.add(new AlarmedLogs().definition(alarmDefinition.getId()).log(log.getId()));
        }
        final Integer totalMatchingLogs = possibleTriggeredPairs.size();
        return totalMatchingLogs >= alarmDefinition.getMultiRule().getRepetitionTrigger();
    }

    @Override
    public void execute() throws Exception {
        final Alarm alarm = new Alarm()
                .definition(alarmDefinition)
                .message(alarmDefinition.getMessage())
                .resolved(false)
                .level(alarmDefinition.getLevel());

        final ArrayList<LogAlarmPair> logPairs = new ArrayList<>();
        for (AlarmedLogs al : possibleTriggeredPairs) {
            logPairs.add(new LogAlarmPair().alarm(alarm).log(al.getLogId()));
        }
        alarm.logs(logPairs);

        logger.info(alarm);

        // save alarm and update definition stats
        final Alarm savedAlarm = alarmService.save(alarm);
        alarmDefinition.updateWithAlarm(savedAlarm);
        alarmDefinitionService.save(alarmDefinition);

        if (alarmDefinition.getDefinitionType() == AlarmDefinitionType.MULTI) {
            alarmedLogsRepository.save(possibleTriggeredPairs);
        }

        // Send notifications through socket
        final Project project = projectService.findOne(alarmDefinition.getProject().getId());
        for (final User user : project.getMembers()) {
            template.convertAndSend(
                    "/publish/threat/" + user.getUsername(),
                    new AlarmNotification(project, logs, alarm)); // TODO extend notification
        }
    }
}

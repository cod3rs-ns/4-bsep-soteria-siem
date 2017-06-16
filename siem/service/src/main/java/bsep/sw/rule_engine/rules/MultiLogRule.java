package bsep.sw.rule_engine.rules;

import bsep.sw.domain.*;
import bsep.sw.rule_engine.FieldSupplier;
import bsep.sw.rule_engine.RuleMethodSupplier;
import bsep.sw.services.AlarmDefinitionService;
import bsep.sw.services.AlarmService;
import bsep.sw.services.ProjectService;
import bsep.sw.util.AlarmNotification;
import org.apache.log4j.Logger;
import org.easyrules.core.BasicRule;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
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

    public MultiLogRule(final List<Log> logs,
                        final AlarmDefinition alarmDefinition,
                        final ProjectService projectService,
                        final AlarmService alarmService,
                        final AlarmDefinitionService alarmDefinitionService,
                        final SimpMessagingTemplate template) {
        super(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        this.logs = logs;
        this.alarmDefinition = alarmDefinition;
        this.projectService = projectService;
        this.alarmService = alarmService;
        this.alarmDefinitionService = alarmDefinitionService;
        this.template = template;
    }


    @Override
    public boolean evaluate() {
        for (final Log log : logs) {
            for (final SingleRule rule : alarmDefinition.getMultiRule().getSingleRules()) {
                if (!methodSupplier
                        .getMethod(rule.getMethod())
                        .apply(fieldSupplier.getField(log, rule.getField()).get(), rule.getValue())) {
                    break;
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
                .log("some_log"); // TODO extend to logs list

        logger.info(alarm);

        // save alarm and update definition stats
        final Alarm savedAlarm = alarmService.save(alarm);
        alarmDefinition.updateWithAlarm(savedAlarm);
        alarmDefinitionService.save(alarmDefinition);

        // Send notifications through socket
        final Project project = projectService.findOne(alarmDefinition.getProject().getId());
        for (final User user : project.getMembers()) {
            template.convertAndSend(
                    "/publish/threat/" + user.getUsername(),
                    new AlarmNotification(project, logs.get(0), alarm)); // TODO extend notification
        }
    }
}

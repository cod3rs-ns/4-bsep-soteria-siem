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
import org.joda.time.DateTime;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class SingleLogRule extends BasicRule {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final SimpMessagingTemplate template;

    // possibly should be injected
    private final ProjectService projectService;
    private final AlarmDefinitionService alarmDefinitionService;
    private final AlarmService alarmService;

    // perform rule on
    private final Log log;
    private final AlarmDefinition alarmDefinition;
    private final FieldSupplier fieldSupplier = new FieldSupplier();
    private final RuleMethodSupplier methodSupplier = new RuleMethodSupplier();

    public SingleLogRule(final Log log,
                         final AlarmDefinition alarmDefinition,
                         final ProjectService projectService,
                         final AlarmService alarmService,
                         final AlarmDefinitionService alarmDefinitionService,
                         final SimpMessagingTemplate template) {
        super(UUID.randomUUID().toString(), UUID.randomUUID().toString());
        this.log = log;
        this.alarmDefinition = alarmDefinition;
        this.projectService = projectService;
        this.alarmService = alarmService;
        this.alarmDefinitionService = alarmDefinitionService;
        this.template = template;
    }

    @Override
    public boolean evaluate() {
        for (final SingleRule rule : alarmDefinition.getSingleRules()) {
            if (!methodSupplier
                    .getMethod(rule.getMethod())
                    .apply(fieldSupplier.getField(log, rule.getField()).get(), rule.getValue())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void execute() throws Exception {
        final Alarm alarm = new Alarm()
                .definition(alarmDefinition)
                .message(alarmDefinition.getMessage())
                .resolved(false);

        final LogAlarmPair lap = new LogAlarmPair().alarm(alarm).log(log.getId());
        final ArrayList<LogAlarmPair> logPairs = new ArrayList<>();
        logPairs.add(lap);
        alarm.logs(logPairs);

        logger.info(alarm);

        // save alarm and update definition stats
        final Alarm savedAlarm = alarmService.save(alarm);
        alarmDefinition.updateWithAlarm(savedAlarm);
        alarmDefinitionService.save(alarmDefinition);

        // Send notifications through socket
        final Project project = projectService.findOne(log.getProject());
        for (final User user : project.getMembers()) {
            template.convertAndSend(
                    "/publish/threat/" + user.getUsername(),
                    new AlarmNotification(project, log, alarm));
        }
    }
}

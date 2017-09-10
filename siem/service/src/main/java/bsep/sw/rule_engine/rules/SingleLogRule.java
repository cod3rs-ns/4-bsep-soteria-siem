package bsep.sw.rule_engine.rules;

import bsep.sw.domain.*;
import bsep.sw.rule_engine.FieldSupplier;
import bsep.sw.rule_engine.FieldType;
import bsep.sw.rule_engine.MethodSupplier;
import bsep.sw.services.AlarmDefinitionService;
import bsep.sw.services.AlarmService;
import bsep.sw.services.ProjectService;
import bsep.sw.util.AlarmNotification;
import org.apache.log4j.Logger;
import org.easyrules.core.BasicRule;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;

public class SingleLogRule extends BasicRule {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final SimpMessagingTemplate template;

    private final ProjectService projectService;
    private final AlarmDefinitionService alarmDefinitionService;
    private final AlarmService alarmService;

    // perform rule on
    private final Log log;
    private final AlarmDefinition alarmDefinition;

    private final FieldSupplier fieldSupplier = new FieldSupplier();
    private final MethodSupplier methodSupplier = new MethodSupplier();

    private final List<FieldType> errorFields = Arrays.asList(FieldType.ERROR, FieldType.ERROR_NO, FieldType.ERROR_TYPE, FieldType.STACK);

    SingleLogRule(final Log log,
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
            // specific check for list of errors, need at least one to match rule
            if (errorFields.contains(rule.getField())) {
                Boolean atLeastOneMatchingRule = false;
                for (LogError error : log.getInfo().getErrors()) {
                    if (methodSupplier
                            .getMethod(rule.getMethod())
                            .apply(fieldSupplier.getErrorField(rule.getField(), error).get(), rule.getValue())) {
                        atLeastOneMatchingRule = true;
                    }
                }
                if (!atLeastOneMatchingRule) {
                    return false;
                }
            } else {
                if (!methodSupplier
                        .getMethod(rule.getMethod())
                        .apply(fieldSupplier.getField(log, rule.getField()).get(), rule.getValue())) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void execute() throws Exception {
        final Alarm alarm = new Alarm()
                .definition(alarmDefinition)
                .message(alarmDefinition.getMessage())
                .resolved(false)
                .level(alarmDefinition.getLevel());

        final LogAlarmPair lap = new LogAlarmPair()
                .alarm(alarm)
                .log(log.getId());
        alarm.getLogs().add(lap);

        // save alarm and update definition stats
        final Alarm savedAlarm = alarmService.save(alarm);
        alarmDefinition.updateWithAlarm(savedAlarm);
        alarmDefinitionService.save(alarmDefinition);

        // send notifications through socket
        final Project project = projectService.findOne(log.getProject());
        project.getMembers().forEach(u ->
                template.convertAndSend("/publish/threat/" + u.getUsername(),
                        new AlarmNotification(project, Collections.singletonList(log), alarm)));
    }
}

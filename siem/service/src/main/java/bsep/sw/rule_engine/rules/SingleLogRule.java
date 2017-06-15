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

import java.util.UUID;

public class SingleLogRule extends BasicRule {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final SimpMessagingTemplate template;
    // possibly should be injected
    private ProjectService projectService;
    private AlarmDefinitionService alarmDefinitionService;
    private AlarmService alarmService;
    // perform rule on
    private Log log;
    private AlarmDefinition alarmDefinition;
    private FieldSupplier fieldSupplier = new FieldSupplier();
    private RuleMethodSupplier methodSupplier = new RuleMethodSupplier();

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
            System.out.println(rule);
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
        // TODO real alarm name
        final Alarm alarm = new Alarm()
                .definition(alarmDefinition)
                .message("This happened! Within rule engine! Yeah madafaka!")
                .resolved(false)
                .log(log.getId());

        logger.info(alarm);

        // save alarm
        alarmService.save(alarm);
        alarmDefinition.getAlarms().add(alarm);
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

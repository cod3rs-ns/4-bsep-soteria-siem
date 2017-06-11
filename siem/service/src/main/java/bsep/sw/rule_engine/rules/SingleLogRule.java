package bsep.sw.rule_engine.rules;

import bsep.sw.domain.*;
import bsep.sw.rule_engine.FieldSupplier;
import bsep.sw.rule_engine.RuleMethodSupplier;
import bsep.sw.services.ProjectService;
import bsep.sw.util.AlarmNotification;
import org.easyrules.core.BasicRule;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public class SingleLogRule extends BasicRule {

    // possibly should be injected
    private ProjectService projectService;
    private final SimpMessagingTemplate template;

    // perform rule on
    private Log log;
    private AlarmDefinition alarmDefinition;
    private FieldSupplier fieldSupplier = new FieldSupplier();
    private RuleMethodSupplier methodSupplier = new RuleMethodSupplier();

    public SingleLogRule(final Log log,
                         final AlarmDefinition alarmDefinition,
                         final ProjectService projectService,
                         final SimpMessagingTemplate template) {
        this.log = log;
        this.alarmDefinition = alarmDefinition;
        this.projectService = projectService;
        this.template = template;
    }

    @Override
    public boolean evaluate() {
        for (SingleRule rule : alarmDefinition.getSingleRules()) {
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
        final Alarm alarm = new Alarm().definition(alarmDefinition).message("This happened! Within rule engine! Yeah madafaka!").resolved(false);
        System.out.println(alarm);
        alarmDefinition.getAlarms().add(alarm);
        final Project project = projectService.findOne(log.getProject());
        for (final User user : project.getMembers()) {
            // Send notifications through socket
            template.convertAndSend(
                    "/publish/threat/" + user.getUsername(),
                    new AlarmNotification(project, log, alarm));
        }
    }
}

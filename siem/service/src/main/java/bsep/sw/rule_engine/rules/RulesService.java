package bsep.sw.rule_engine.rules;


import bsep.sw.domain.AlarmDefinition;
import bsep.sw.domain.AlarmDefinitionType;
import bsep.sw.domain.Log;
import bsep.sw.domain.Project;
import bsep.sw.services.AlarmDefinitionService;
import bsep.sw.services.AlarmService;
import bsep.sw.services.ProjectService;
import org.easyrules.api.RulesEngine;
import org.easyrules.core.RulesEngineBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RulesService {

    private final SimpMessagingTemplate template;
    private final AlarmService alarmService;
    private final ProjectService projectService;
    private final AlarmDefinitionService alarmDefinitionService;

    @Autowired
    public RulesService(final SimpMessagingTemplate template,
                        final AlarmService alarmService,
                        final ProjectService projectService,
                        final AlarmDefinitionService alarmDefinitionService) {
        this.template = template;
        this.alarmService = alarmService;
        this.projectService = projectService;
        this.alarmDefinitionService = alarmDefinitionService;
    }

    public void evaluateNewLog(final Log log) {
        final RulesEngine rulesEngine = RulesEngineBuilder
                .aNewRulesEngine()
                .named(UUID.randomUUID().toString())
                .withSkipOnFirstAppliedRule(false)
                .withSkipOnFirstNonTriggeredRule(false)
                .withSkipOnFirstFailedRule(false)
                .withSilentMode(true)
                .build();

        final Project project = projectService.findOne(log.getProject());

        // don't risk
        if (project == null) return;

        final List<AlarmDefinition> definitions = alarmDefinitionService.findAllByProject(project);

        for (final AlarmDefinition definition : definitions) {
            if (definition.getDefinitionType() == AlarmDefinitionType.SINGLE) {
                final SingleLogRule logRule = new SingleLogRule(log, definition, projectService, alarmService, alarmDefinitionService, template);
                rulesEngine.registerRule(logRule);
            } else {
                // TODO gather logs
                final MultiLogRule logRule = new MultiLogRule(new ArrayList<Log>(), definition, projectService, alarmService, alarmDefinitionService, template);
            }

        }

        rulesEngine.fireRules();
    }
}

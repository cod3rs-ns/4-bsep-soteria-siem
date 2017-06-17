package bsep.sw.services;

import bsep.sw.domain.Alarm;
import bsep.sw.domain.AlarmDefinition;
import bsep.sw.domain.Project;
import bsep.sw.domain.User;
import bsep.sw.repositories.AlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AlarmService {

    private final AlarmRepository repository;

    @Autowired
    public AlarmService(final AlarmRepository repository) {
        this.repository = repository;
    }

    public Alarm save(final Alarm alarm) {
        return repository.save(alarm);
    }

    @Transactional(readOnly = true)
    public Alarm findOne(final Long id) {
        return repository.findOne(id);
    }

    public void delete(final Long id) {
        repository.delete(id);
    }

    public List<Alarm> findAllByProject(final Project project) {
        return repository.findAlarmsByDefinitionProject(project);
    }

    @Transactional(readOnly = true)
    public Alarm findOneByProjectAndId(final Project project, final Long alarmId) {
        return repository.findAlarmByDefinitionProjectAndId(project, alarmId);
    }

    @Transactional(readOnly = true)
    public Page<Alarm> findByUserAndStatus(final User user, final Boolean resolved, final Pageable pageable) {
        return repository.findAlarmsByDefinition_Project_Members_ContainingAndResolvedOrderByResolvedAtDesc(user, resolved, pageable);
    }

    @Transactional(readOnly = true)
    public List<Alarm> findAllByDefinition(final AlarmDefinition definition) {
        return repository.findAlarmsByDefinition(definition);
    }

}

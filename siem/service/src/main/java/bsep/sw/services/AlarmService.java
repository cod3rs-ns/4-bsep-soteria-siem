package bsep.sw.services;

import bsep.sw.domain.Alarm;
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
    public Page<Alarm> findAll(final Pageable pageable) {
        return repository.findAll(pageable);
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

    public Alarm findOneByProjectAndId(final Project project, final Long alarmId) {
        return repository.findAlarmByDefinitionProjectAndId(project, alarmId);
    }

    public Page<Alarm> findAllByUserAndStatus(final User user, final Boolean resolved, final Pageable pageable) {
        return repository.findAlarmsByDefinition_Project_Members_ContainingAndResolvedOrderByResolvedAtDesc(user, resolved, pageable);
    }

}

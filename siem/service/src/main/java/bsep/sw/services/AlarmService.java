package bsep.sw.services;

import bsep.sw.domain.*;
import bsep.sw.hateoas.reports.PieCollectionReport;
import bsep.sw.hateoas.reports.PieReport;
import bsep.sw.repositories.AlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    public PieCollectionReport getReportLevels(final Project project) {
        final ArrayList<PieReport> pieReports = new ArrayList<>();
        for (AlarmLevel level : AlarmLevel.values()) {
            final Integer val = repository.countAlarmsByDefinitionProjectAndLevel(project, level);
            pieReports.add(new PieReport(level.name(), val));
        }
        return new PieCollectionReport(pieReports);
    }

    public PieCollectionReport getReportResolved(final Project project) {
        final ArrayList<PieReport> pieReports = new ArrayList<>();
        final Integer resolved = repository.countAlarmsByDefinitionProjectAndResolved(project, true);
        pieReports.add(new PieReport("resolved", resolved));
        final Integer notResolved = repository.countAlarmsByDefinitionProjectAndResolved(project, false);
        pieReports.add(new PieReport("not-resolved", notResolved));

        return new PieCollectionReport(pieReports);
    }

}

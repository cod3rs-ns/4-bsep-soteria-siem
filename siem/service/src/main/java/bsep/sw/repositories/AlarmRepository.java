package bsep.sw.repositories;

import bsep.sw.domain.*;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    List<Alarm> findAlarmsByDefinitionProject(final Project project);

    Alarm findAlarmByDefinitionProjectAndId(final Project project, final Long alarmId);

    Page<Alarm> findAlarmsByDefinition_Project_Members_ContainingAndResolvedOrderByResolvedAtDesc(final User user, final Boolean resolved, final Pageable pageable);

    List<Alarm> findAlarmsByDefinition(final AlarmDefinition definition);

    Integer countAlarmsByDefinitionProjectAndLevel(final Project project, final AlarmLevel level);

    Integer countAlarmsByDefinitionProjectAndResolved(final Project project, final Boolean resolved);

    List<Alarm> findAlarmsByDefinitionProjectAndDefinition_DefinitionType_AndResolvedAndCreatedAtBetween(final Project project, final AlarmDefinitionType type, final Boolean resolved, final DateTime startTime, final DateTime endTime);

    List<Alarm> findAlarmsByDefinitionProjectAndDefinition_DefinitionType_AndLevelAndCreatedAtBetween(final Project project, final AlarmDefinitionType type, final AlarmLevel level, final DateTime startTime, final DateTime endTime);
}

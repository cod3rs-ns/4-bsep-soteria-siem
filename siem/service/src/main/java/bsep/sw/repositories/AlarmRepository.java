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

    Page<Alarm> findAlarmsByDefinition(final AlarmDefinition definition, final Pageable pageable);

    Integer countAlarmsByDefinitionProjectAndLevel(final Project project, final AlarmLevel level);

    Integer countAlarmsByDefinitionProjectAndResolved(final Project project, final Boolean resolved);

    List<Alarm> findAlarmsByDefinitionProjectAndResolvedAndCreatedAtBetween(final Project project, final Boolean resolved, final DateTime startTime, final DateTime endTime);

    List<Alarm> findAlarmsByDefinitionProjectAndLevelAndCreatedAtBetween(final Project project, final AlarmLevel level, final DateTime startTime, final DateTime endTime);

    List<Alarm> findAlarmsByDefinitionProjectAndDefinition_DefinitionTypeAndCreatedAtBetween(final Project project, final AlarmDefinitionType type, final DateTime startTime, final DateTime endTime);
}

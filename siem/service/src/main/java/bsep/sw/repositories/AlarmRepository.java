package bsep.sw.repositories;

import bsep.sw.domain.Alarm;
import bsep.sw.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    List<Alarm> findAlarmsByDefinitionProject(final Project project);

    Alarm findAlarmByDefinitionProjectAndId(final Project project, final Long alarmId);

}

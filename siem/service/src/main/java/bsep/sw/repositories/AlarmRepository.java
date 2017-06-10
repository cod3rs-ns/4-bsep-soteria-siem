package bsep.sw.repositories;

import bsep.sw.domain.Alarm;
import bsep.sw.domain.Project;
import bsep.sw.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    List<Alarm> findAlarmsByDefinitionProject(final Project project);

    Alarm findAlarmByDefinitionProjectAndId(final Project project, final Long alarmId);

    Page<Alarm> findAlarmsByDefinition_Project_Members_Containing(final User user, final Pageable pageable);
}

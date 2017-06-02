package bsep.sw.repositories;

import bsep.sw.domain.AlarmDefinition;
import bsep.sw.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlarmDefinitionRepository extends JpaRepository<AlarmDefinition, Long> {

    List<AlarmDefinition> findAlarmDefinitionsByProject(final Project project);

    AlarmDefinition findAlarmDefinitionByIdAndProject(final Long definitionId, final Project project);

}

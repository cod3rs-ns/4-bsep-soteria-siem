package bsep.sw.repositories;

import bsep.sw.domain.AlarmDefinition;
import bsep.sw.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmDefinitionRepository extends JpaRepository<AlarmDefinition, Long> {

    Page<AlarmDefinition> findAlarmDefinitionsByProject(final Project project, final Pageable pageable);

    List<AlarmDefinition> findAlarmDefinitionsByProject(final Project project);

    AlarmDefinition findAlarmDefinitionByIdAndProject(final Long definitionId, final Project project);

}

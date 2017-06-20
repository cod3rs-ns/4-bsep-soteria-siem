package bsep.sw.repositories;

import bsep.sw.domain.Agent;
import bsep.sw.domain.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {

    Page<Agent> findAgentsByProject(final Project project, final Pageable pageable);

    Agent findAgentByProjectAndId(final Project project, final Long alarmId);

}

package bsep.sw.repositories;

import bsep.sw.domain.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepository extends JpaRepository<Agent, Long> {

}

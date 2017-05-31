package bsep.sw.repositories;

import bsep.sw.domain.Project;
import bsep.sw.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findProjectByOwnerAndId(final User user, final Long id);
    
}

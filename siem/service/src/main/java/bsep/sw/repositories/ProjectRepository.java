package bsep.sw.repositories;

import bsep.sw.domain.Project;
import bsep.sw.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findProjectByOwnerAndId(final User user, final Long id);

    Project findProjectsByMembersContainingAndId(final User user, final Long id);

    List<Project> findProjectsByOwner(final User user);

    List<Project> findProjectsByMembersContaining(final User user);

}

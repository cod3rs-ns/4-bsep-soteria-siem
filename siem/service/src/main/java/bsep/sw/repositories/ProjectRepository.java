package bsep.sw.repositories;

import bsep.sw.domain.Project;
import bsep.sw.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findProjectByOwnerAndId(final User user, final Long id);

    Project findProjectsByMembersContainingAndId(final User user, final Long id);

    List<Project> findProjectsByOwner(final User user);

    List<Project> findProjectsByMembersContaining(final User user);

    List<Project> findProjectsByMembersContainingAndOwnerNot(final User user, final User owner);
}

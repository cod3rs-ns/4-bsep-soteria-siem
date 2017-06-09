package bsep.sw.repositories;

import bsep.sw.domain.Project;
import bsep.sw.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Project findProjectByOwnerAndId(final User user, final Long id);

    Project findProjectsByMembersContainingAndId(final User user, final Long id);

    Page<Project> findProjectsByOwner(final User user, final Pageable pageable);

    Page<Project> findProjectsByMembersContaining(final User user, final Pageable pageable);

    Page<Project> findProjectsByMembersContainingAndOwnerNot(final User user, final User owner, final Pageable pageable);
}

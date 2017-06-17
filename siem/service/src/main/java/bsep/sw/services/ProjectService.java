package bsep.sw.services;

import bsep.sw.domain.Project;
import bsep.sw.domain.User;
import bsep.sw.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProjectService {

    private final ProjectRepository repository;

    @Autowired
    public ProjectService(final ProjectRepository repository) {
        this.repository = repository;
    }

    public Project save(final Project project) {
        return repository.save(project);
    }

    @Transactional(readOnly = true)
    public Project findOne(final Long id) {
        return repository.findOne(id);
    }

    public Boolean delete(final User user, final Long id) {
        if (repository.findProjectByOwnerAndId(user, id) == null) {
            return false;
        } else {
            repository.delete(id);
            return true;
        }
    }

    @Transactional(readOnly = true)
    public Project findByOwnerAndId(final User user, final Long id) {
        return repository.findProjectByOwnerAndId(user, id);
    }

    @Transactional(readOnly = true)
    public Project findByMembershipAndId(final User user, final Long id) {
        return repository.findProjectsByMembersContainingAndId(user, id);
    }

    @Transactional(readOnly = true)
    public Page<Project> findOwned(final User user, final Pageable pageable) {
        return repository.findProjectsByOwner(user, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Project> findByMembership(final User user, final Boolean owner, final Pageable pageable) {
        if (owner) {
            return repository.findProjectsByMembersContaining(user, pageable);
        }
        return repository.findProjectsByMembersContainingAndOwnerNot(user, user, pageable);
    }

}

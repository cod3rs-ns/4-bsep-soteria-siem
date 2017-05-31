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
    public Page<Project> findAll(final Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Project findOne(final Long id) {
        return repository.findOne(id);
    }

    public void delete(final Long id) {
        repository.delete(id);
    }

    public Project findByUserAndId(final User user, final Long id) {
        return repository.findProjectByOwnerAndId(user, id);
    }
}

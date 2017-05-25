package bsep.sw.services;

import bsep.sw.domain.Project;
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

    public Project save(Project project) {
        return repository.save(project);
    }

    @Transactional(readOnly = true)
    public Page<Project> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Project findOne(Long id) {
        return repository.findOne(id);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

}
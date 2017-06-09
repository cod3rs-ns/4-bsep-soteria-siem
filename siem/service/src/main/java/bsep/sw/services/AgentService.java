package bsep.sw.services;

import bsep.sw.domain.Agent;
import bsep.sw.domain.Project;
import bsep.sw.repositories.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AgentService {

    private final AgentRepository repository;

    @Autowired
    public AgentService(final AgentRepository repository) {
        this.repository = repository;
    }

    public Agent save(final Agent agent) {
        return repository.save(agent);
    }

    @Transactional(readOnly = true)
    public Page<Agent> findAll(final Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Agent findOne(final Long id) {
        return repository.findOne(id);
    }

    public void delete(final Long id) {
        repository.delete(id);
    }

    public List<Agent> findAllByProject(final Project project, final Pageable pageable) {
        return repository.findAgentsByProject(project, pageable);
    }

    public Agent findOneByProjectAndId(final Project project, final Long agentId) {
        return repository.findAgentByProjectAndId(project, agentId);
    }

}

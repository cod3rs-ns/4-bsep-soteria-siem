package bsep.sw.services;

import bsep.sw.domain.Agent;
import bsep.sw.repositories.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void delete(Long id) {
        repository.delete(id);
    }

}

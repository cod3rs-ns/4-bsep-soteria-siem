package bsep.sw.services;

import bsep.sw.domain.Alarm;
import bsep.sw.repositories.AlarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AlarmService {

    private final AlarmRepository repository;

    @Autowired
    public AlarmService(final AlarmRepository repository) {
        this.repository = repository;
    }

    public Alarm save(Alarm alarm) {
        return repository.save(alarm);
    }

    @Transactional(readOnly = true)
    public Page<Alarm> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Alarm findOne(Long id) {
        return repository.findOne(id);
    }

    public void delete(Long id) {
        repository.delete(id);
    }

}
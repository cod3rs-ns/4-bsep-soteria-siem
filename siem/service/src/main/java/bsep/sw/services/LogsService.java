package bsep.sw.services;

import bsep.sw.domain.Log;
import bsep.sw.repositories.LogsRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LogsService {

    private final LogsRepository repository;
    private final MongoOperations operations;

    @Autowired
    public LogsService(final LogsRepository repository, final MongoOperations operations) {
        this.repository = repository;
        this.operations = operations;
    }

    public Log findOne(final String id) {
        return repository.findOne(id);
    }

    public Log save(final Log log) {
        return repository.save(log);
    }

    public List<Log> findByProjectAndTimeAfter(final Long projectId, final Long time) {
        return repository.findAllByProjectAndTimeAfter(projectId, time);
    }

    public List<Log> findByProject(final Long project, final Map<String, String[]> filters, final Integer limit, final Integer offset) {

        final Long from = new DateTime(filters.getOrDefault("from", new String[]{DateTime.now().minusYears(1).toString()})[0]).getMillis();
        final Long to = new DateTime(filters.getOrDefault("to", new String[]{DateTime.now().plusYears(1).toString()})[0]).getMillis();
        filters.remove("to");
        filters.remove("from");

        final Query query = new Query();
        query.addCriteria(Criteria.where("project").is(project)).limit(limit).skip(offset);
        query.addCriteria(Criteria.where("time").lt(to).gt(from));

        for (final String name : filters.keySet()) {
            final Criteria criteria = Criteria.where(name);
            criteria.regex(String.join("|", filters.get(name)));
            query.addCriteria(criteria);
        }

        return operations.find(query, Log.class, "logs");
    }
}

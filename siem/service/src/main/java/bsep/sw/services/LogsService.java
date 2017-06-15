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

    public List<Log> findByProject(final Long project, final Map<String, String[]> filters, final Integer limit, final Integer offset) {

        final DateTime from = new DateTime(filters.getOrDefault("from", new String[] {DateTime.now().minusYears(1).toString()})[0]);
        final DateTime to = new DateTime(filters.getOrDefault("to", new String[] {DateTime.now().plusYears(1).toString()})[0]);

        final Query query = new Query();
        query.addCriteria(Criteria.where("project").is(project)).limit(limit).skip(offset);

        // FIXME Add support for 'from' and 'to'
//        query.addCriteria(Criteria.where("time").lt(to).and("time").gt(from));

        for (final String name: filters.keySet()) {
            for (final String value: filters.get(name)) {
                query.addCriteria(Criteria.where(name).regex(value, "i"));
            }
        }

        return operations.find(query, Log.class, "logs");
    }

}

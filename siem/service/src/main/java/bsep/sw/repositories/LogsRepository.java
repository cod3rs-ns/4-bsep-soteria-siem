package bsep.sw.repositories;

import bsep.sw.domain.Log;
import org.joda.time.DateTime;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogsRepository extends MongoRepository<Log, String> {

    List<Log> findByProject(final Long project, final Pageable pageable);

    List<Log> findAllByProjectAndTimeAfter(final Long project, final DateTime dateTime);

}

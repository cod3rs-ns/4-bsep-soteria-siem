package bsep.sw.repositories;

import bsep.sw.domain.Log;
import bsep.sw.domain.LogLevel;
import bsep.sw.domain.Project;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogsRepository extends MongoRepository<Log, String> {

    List<Log> findAllByProjectAndTimeAfter(final Long project, final Long dateTime);

    List<Log> findAllByProjectAndLevelEqualsAndTimeBetween(final Long project, final LogLevel level, final Long timeStart, final Long timeEnd);

    List<Log> findAllByProjectAndInfo_HostEqualsAndTimeBetween(final Long project, final String host, final Long timeStart, final Long timeEnd);

    List<Log> findAllByProjectAndInfo_SourceEqualsAndTimeBetween(final Long project, final String source, final Long timeStart, final Long timeEnd);

}

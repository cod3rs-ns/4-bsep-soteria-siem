package bsep.sw.repositories;

import bsep.sw.domain.Log;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogsRepository extends MongoRepository <Log, Long> {

}

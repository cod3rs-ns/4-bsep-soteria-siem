package bsep.sw.repositories;

import bsep.sw.domain.AlarmedLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmedLogsRepository extends JpaRepository<AlarmedLogs, Long> {

    List<AlarmedLogs> findAllByAlarmDefinitionId(final Long alarmDefinitionId);

}

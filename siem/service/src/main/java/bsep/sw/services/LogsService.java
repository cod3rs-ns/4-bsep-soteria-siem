package bsep.sw.services;

import bsep.sw.domain.Log;
import bsep.sw.domain.LogLevel;
import bsep.sw.domain.PlatformType;
import bsep.sw.domain.Project;
import bsep.sw.hateoas.reports.*;
import bsep.sw.repositories.LogsRepository;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    public GlobalReport getReport(final Project project, final ReportRequest request) {
        final List<Log> logs = new ArrayList<>();
        switch (request.type) {
            case LOG_LEVEL:
                final LogLevel level;
                try {
                    level = LogLevel.valueOf(StringUtils.upperCase(request.value));
                } catch (final Exception ex) {
                    return null;
                }
                logs.addAll(repository.findAllByProjectAndLevelEqualsAndTimeBetween(
                        project.getId(),
                        level,
                        request.fromDate.getMillis(),
                        request.toDate.getMillis()));
                break;
            case LOG_PLATFORM:
                final PlatformType type;
                try {
                    type = PlatformType.valueOf(StringUtils.upperCase(request.value));
                } catch (final Exception ex) {
                    return null;
                }
                logs.addAll(repository.findAllByProjectAndInfo_PlatformEqualsAndTimeBetween(
                        project.getId(),
                        type,
                        request.fromDate.getMillis(),
                        request.toDate.getMillis()));
                break;
            case LOG_HOST:
                logs.addAll(repository.findAllByProjectAndInfo_HostEqualsAndTimeBetween(
                        project.getId(),
                        request.value,
                        request.fromDate.getMillis(),
                        request.toDate.getMillis()));
                break;
            case LOG_SOURCE:
                logs.addAll(repository.findAllByProjectAndInfo_SourceEqualsAndTimeBetween(
                        project.getId(),
                        request.value,
                        request.fromDate.getMillis(),
                        request.toDate.getMillis()));
                break;
            default:
                return null;
        }

        final Map<String, Integer> logsByDate = new TreeMap<>();
        for (final Log log : logs) {
            final String day = extractDayOnly(log.getTime());
            if (logsByDate.containsKey(day)) {
                Integer dailyLogs = logsByDate.get(day);
                dailyLogs += 1;
                logsByDate.put(day, dailyLogs);
            } else {
                Integer dailyLogs = 1;
                logsByDate.put(day, dailyLogs);
            }
        }
        final List<DailyReport> dailyReports = new ArrayList<>();
        for (final Map.Entry<String, Integer> entry : logsByDate.entrySet()) {
            dailyReports.add(new DailyReport(entry.getKey(), entry.getValue()));
        }

        return new GlobalReport(logs.size(), dailyReports);
    }

    private String extractDayOnly(final Long time) {
        final DateTime full = new DateTime(time);
        final StringBuilder sb = new StringBuilder();
        sb.append(full.getYear());
        sb.append("-");
        sb.append(full.getMonthOfYear());
        sb.append("-");
        sb.append(full.getDayOfMonth());
        return sb.toString();
    }

    public PieCollectionReport getReportLevels(final Project project) {
        final ArrayList<PieReport> pieReports = new ArrayList<>();
        for (LogLevel level : LogLevel.values()) {
            final Integer val = repository.countAllByProjectAndLevel(project.getId(), level);
            pieReports.add(new PieReport(level.name(), val));
        }
        return new PieCollectionReport(pieReports);
    }

    public PieCollectionReport getReportPlatforms(final Project project) {
        final ArrayList<PieReport> pieReports = new ArrayList<>();
        for (PlatformType type : PlatformType.values()) {
            final Integer val = repository.countAllByProjectAndInfo_Platform(project.getId(), type);
            pieReports.add(new PieReport(type.name(), val));
        }
        return new PieCollectionReport(pieReports);
    }

}

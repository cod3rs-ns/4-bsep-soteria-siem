package bsep.sw.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "alarmed_logs")
public class AlarmedLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @NotNull
    @Column(name = "all_alarm_definition_id", nullable = false, updatable = false)
    private Long alarmDefinitionId;

    @NotNull
    @Column(name = "all_log_id", nullable = false, updatable = false)
    private String logId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlarmDefinitionId() {
        return alarmDefinitionId;
    }

    public void setAlarmDefinitionId(Long alarmDefinitionId) {
        this.alarmDefinitionId = alarmDefinitionId;
    }

    public AlarmedLogs definition(Long alarmDefinitionId) {
        this.alarmDefinitionId = alarmDefinitionId;
        return this;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public AlarmedLogs log(String logId) {
        this.logId = logId;
        return this;
    }

    @Override
    public String toString() {
        return "AlarmedLogs{" +
                "id=" + id +
                ", alarmDefinitionId=" + alarmDefinitionId +
                ", logId='" + logId + '\'' +
                '}';
    }
    
}

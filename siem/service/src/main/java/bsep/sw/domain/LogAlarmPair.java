package bsep.sw.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "log_alarm_pairs")
public class LogAlarmPair {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lap_alarm_id")
    private Alarm alarm;

    @NotNull
    @Column(name = "lap_log_id", nullable = false, updatable = false)
    private String log;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LogAlarmPair id(Long id) {
        this.id = id;
        return this;
    }

    public Alarm getAlarm() {
        return alarm;
    }

    public void setAlarm(Alarm alarm) {
        this.alarm = alarm;
    }

    public LogAlarmPair alarm(Alarm alarm) {
        this.alarm = alarm;
        return this;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public LogAlarmPair log(String log) {
        this.log = log;
        return this;
    }

}

package bsep.sw.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "multi_rules")
public class MultiRule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @NotNull
    @Column(name = "mr_repetition_trigger", nullable = false)
    @Min(1)
    private Integer repetitionTrigger = 1;

    @NotNull
    @Column(name = "mr_interval", nullable = false)
    private Integer interval; // in seconds

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mr_definition_id")
    private AlarmDefinition definition;

    @OneToMany(mappedBy = "parentRule", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<SingleRule> singleRules = new HashSet<>(0);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MultiRule id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRepetitionTrigger() {
        return repetitionTrigger;
    }

    public void setRepetitionTrigger(Integer repetitionTrigger) {
        this.repetitionTrigger = repetitionTrigger;
    }

    public MultiRule repetitionTrigger(Integer repetitionTrigger) {
        this.repetitionTrigger = repetitionTrigger;
        return this;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public MultiRule interval(Integer interval) {
        this.interval = interval;
        return this;
    }

    public Set<SingleRule> getSingleRules() {
        return singleRules;
    }

    public void setSingleRules(Set<SingleRule> singleRules) {
        this.singleRules = singleRules;
    }

    public MultiRule singleRules(Set<SingleRule> singleRules) {
        this.singleRules = singleRules;
        return this;
    }

    public AlarmDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(AlarmDefinition definition) {
        this.definition = definition;
    }

    public MultiRule definition(AlarmDefinition definition) {
        this.definition = definition;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof MultiRule)) return false;

        MultiRule multiRule = (MultiRule) o;

        return new EqualsBuilder()
                .append(id, multiRule.id)
                .append(repetitionTrigger, multiRule.repetitionTrigger)
                .append(interval, multiRule.interval)
                .append(definition, multiRule.definition)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(repetitionTrigger)
                .append(interval)
                .append(definition)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "MultiRule{" +
                "id=" + id +
                ", repetitionTrigger=" + repetitionTrigger +
                ", interval=" + interval +
                ", definition=" + definition +
                ", singleRules=" + singleRules +
                '}';
    }

}

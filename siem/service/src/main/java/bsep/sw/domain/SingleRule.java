package bsep.sw.domain;


import bsep.sw.rule_engine.FieldType;
import bsep.sw.rule_engine.MethodType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "single_rules")
public class SingleRule implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @NotNull
    @Column(name = "sr_value", nullable = false)
    private String value;

    @NotNull
    @Column(name = "sr_field", nullable = false)
    @Enumerated(EnumType.STRING)
    private FieldType field;

    @NotNull
    @Column(name = "sr_method", nullable = false)
    @Enumerated(EnumType.STRING)
    private MethodType method;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sr_definition_id")
    private AlarmDefinition definition;

    // nullable in case where this rule is top level rule,
    // directly under AlarmDefinition
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sr_multi_rule_id")
    private MultiRule parentRule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SingleRule id(Long id) {
        this.id = id;
        return this;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public SingleRule value(String value) {
        this.value = value;
        return this;
    }

    public FieldType getField() {
        return field;
    }

    public void setField(FieldType field) {
        this.field = field;
    }

    public SingleRule field(FieldType field) {
        this.field = field;
        return this;
    }

    public MethodType getMethod() {
        return method;
    }

    public void setMethod(MethodType method) {
        this.method = method;
    }

    public SingleRule method(MethodType method) {
        this.method = method;
        return this;
    }

    public AlarmDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(AlarmDefinition definition) {
        this.definition = definition;
    }

    public SingleRule definition(AlarmDefinition definition) {
        this.definition = definition;
        return this;
    }

    public MultiRule getParentRule() {
        return parentRule;
    }

    public void setParentRule(MultiRule parentRule) {
        this.parentRule = parentRule;
    }

    public SingleRule parent(MultiRule multiRule) {
        this.parentRule = multiRule;
        return this;
    }

    @Override
    public String toString() {
        return "SingleRule{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", field=" + field +
                ", method=" + method +
                '}';
    }

}

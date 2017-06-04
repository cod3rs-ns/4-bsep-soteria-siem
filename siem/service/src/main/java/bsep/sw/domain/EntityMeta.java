package bsep.sw.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Should be base entity for all entities in our model. It contains all base fields which gives us more control of our
 * entities. It also simplifies and reduce code complexity of derived entity classes.
 */
@MappedSuperclass
public abstract class EntityMeta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "id", nullable = false, updatable = false, columnDefinition = "BIGINT UNSIGNED")
    private Long id;

    @Column(name = "version", nullable = false)
    @Version
    private Long version;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private DateTime createdAt = new DateTime();

    @Column(name = "created_by")
    private String createdBy;

    @NotNull
    @Column(name = "last_update", nullable = false)
    private DateTime lastUpdate = new DateTime();

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntityMeta id(Long id) {
        this.id = id;
        return this;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public EntityMeta version(Long version) {
        this.version = version;
        return this;
    }

    public DateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(DateTime createdAt) {
        this.createdAt = createdAt;
    }

    public EntityMeta createdAt(DateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public EntityMeta createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public DateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(DateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public EntityMeta lastUpdated(DateTime updatedAt) {
        this.lastUpdate = updatedAt;
        return this;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public EntityMeta updatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public EntityMeta active(Boolean active) {
        this.active = active;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof EntityMeta)) return false;

        EntityMeta that = (EntityMeta) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(version, that.version)
                .append(createdAt, that.createdAt)
                .append(createdBy, that.createdBy)
                .append(lastUpdate, that.lastUpdate)
                .append(updatedBy, that.updatedBy)
                .append(active, that.active)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(version)
                .append(createdAt)
                .append(createdBy)
                .append(lastUpdate)
                .append(updatedBy)
                .append(active)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "EntityMeta{" +
                "id=" + id +
                ", version=" + version +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", lastUpdate=" + lastUpdate +
                ", updatedBy='" + updatedBy + '\'' +
                ", active=" + active +
                '}';
    }
}

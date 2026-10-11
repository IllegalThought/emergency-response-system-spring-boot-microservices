package com.campusnaksha.incident.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.campusnaksha.incident.exception.InvalidIncidentStateException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "incidents")
public class Incident {

    @Id
    private UUID id;
    @Version
@Column(nullable = false)
private Long version;

    @Column(
            name = "reported_by_user_id",
            nullable = false
    )
    private UUID reportedByUserId;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private IncidentCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private IncidentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private IncidentPriority priority;

    @Column(
            nullable = false,
            precision = 9,
            scale = 6
    )
    private BigDecimal latitude;

    @Column(
            nullable = false,
            precision = 9,
            scale = 6
    )
    private BigDecimal longitude;

    @Column(
            name = "location_name",
            length = 200
    )
    private String locationName;

    @Column(
            name = "created_at",
            nullable = false
    )
    private Instant createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private Instant updatedAt;

    @Column(name = "resolved_at")
    private Instant resolvedAt;

    protected Incident() {
    }

    public Incident(
            UUID id,
            UUID reportedByUserId,
            String title,
            String description,
            IncidentCategory category,
            IncidentStatus status,
            IncidentPriority priority,
            BigDecimal latitude,
            BigDecimal longitude,
            String locationName,
            Instant createdAt,
            Instant updatedAt,
            Instant resolvedAt
    ) {
        this.id = id;
        this.reportedByUserId = reportedByUserId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.status = status;
        this.priority = priority;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationName = locationName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.resolvedAt = resolvedAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getReportedByUserId() {
        return reportedByUserId;
    }

    public String getTitle() {
        return title;
    }
    public Long getVersion() {
    return version;
}

    public String getDescription() {
        return description;
    }

    public IncidentCategory getCategory() {
        return category;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public IncidentPriority getPriority() {
        return priority;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }

   public void changeStatus(
        IncidentStatus newStatus,
        Instant now
) {

    if (newStatus == null) {

        throw new InvalidIncidentStateException(
                "Incident status cannot be null"
        );
    }

    /*
     * Idempotent operation:
     *
     * REPORTED -> REPORTED
     *
     * does nothing.
     */
    if (status == newStatus) {
        return;
    }

    if (!status.canTransitionTo(newStatus)) {

        throw new InvalidIncidentStateException(
                "Invalid incident status transition: "
                        + status
                        + " -> "
                        + newStatus
        );
    }

    this.status = newStatus;
    this.updatedAt = now;

    if (newStatus == IncidentStatus.RESOLVED) {
        this.resolvedAt = now;
    }
}

public void changePriority(
        IncidentPriority newPriority,
        Instant now
) {

    if (newPriority == null) {
        throw new IllegalArgumentException(
                "Incident priority cannot be null"
        );
    }

    if (this.priority == newPriority) {
        return;
    }

    this.priority = newPriority;
    this.updatedAt = now;
}
}
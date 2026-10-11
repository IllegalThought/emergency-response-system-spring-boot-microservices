package com.campusnaksha.incident.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "incident_status_history")
public class IncidentStatusHistory {

    @Id
    private UUID id;

    @Column(
            name = "incident_id",
            nullable = false
    )
    private UUID incidentId;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "previous_status",
            nullable = false,
            length = 30
    )
    private IncidentStatus previousStatus;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "new_status",
            nullable = false,
            length = 30
    )
    private IncidentStatus newStatus;

    @Column(
            name = "changed_by_user_id",
            nullable = false
    )
    private UUID changedByUserId;

    @Column(
            name = "changed_at",
            nullable = false
    )
    private Instant changedAt;

    @Column(length = 1000)
    private String note;

    protected IncidentStatusHistory() {
    }

    public IncidentStatusHistory(
            UUID id,
            UUID incidentId,
            IncidentStatus previousStatus,
            IncidentStatus newStatus,
            UUID changedByUserId,
            Instant changedAt,
            String note
    ) {
        this.id = id;
        this.incidentId = incidentId;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
        this.changedByUserId = changedByUserId;
        this.changedAt = changedAt;
        this.note = note;
    }

    public UUID getId() {
        return id;
    }

    public UUID getIncidentId() {
        return incidentId;
    }

    public IncidentStatus getPreviousStatus() {
        return previousStatus;
    }

    public IncidentStatus getNewStatus() {
        return newStatus;
    }

    public UUID getChangedByUserId() {
        return changedByUserId;
    }

    public Instant getChangedAt() {
        return changedAt;
    }

    public String getNote() {
        return note;
    }
}
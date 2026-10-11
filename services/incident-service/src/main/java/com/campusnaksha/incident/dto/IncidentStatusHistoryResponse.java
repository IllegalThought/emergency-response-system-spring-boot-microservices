package com.campusnaksha.incident.dto;

import java.time.Instant;
import java.util.UUID;

import com.campusnaksha.incident.entity.IncidentStatus;
import com.campusnaksha.incident.entity.IncidentStatusHistory;

public record IncidentStatusHistoryResponse(

        UUID id,

        UUID incidentId,

        IncidentStatus previousStatus,

        IncidentStatus newStatus,

        UUID changedByUserId,

        Instant changedAt,

        String note

) {

    public static IncidentStatusHistoryResponse from(
            IncidentStatusHistory history
    ) {

        return new IncidentStatusHistoryResponse(
                history.getId(),
                history.getIncidentId(),
                history.getPreviousStatus(),
                history.getNewStatus(),
                history.getChangedByUserId(),
                history.getChangedAt(),
                history.getNote()
        );
    }
}
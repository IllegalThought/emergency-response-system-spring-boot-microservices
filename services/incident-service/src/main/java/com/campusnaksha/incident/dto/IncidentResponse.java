package com.campusnaksha.incident.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.campusnaksha.incident.entity.Incident;
import com.campusnaksha.incident.entity.IncidentCategory;
import com.campusnaksha.incident.entity.IncidentPriority;
import com.campusnaksha.incident.entity.IncidentStatus;

public record IncidentResponse(

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

    public static IncidentResponse from(
            Incident incident
    ) {

        return new IncidentResponse(

                incident.getId(),

                incident.getReportedByUserId(),

                incident.getTitle(),

                incident.getDescription(),

                incident.getCategory(),

                incident.getStatus(),

                incident.getPriority(),

                incident.getLatitude(),

                incident.getLongitude(),

                incident.getLocationName(),

                incident.getCreatedAt(),

                incident.getUpdatedAt(),

                incident.getResolvedAt()
        );
    }
}
package com.campusnaksha.incident.dto;

import com.campusnaksha.incident.entity.IncidentStatus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateIncidentStatusRequest(

        @NotNull(message = "Status is required")
        IncidentStatus status,

        @Size(
                max = 1000,
                message = "Note must not exceed 1000 characters"
        )
        String note

) {

    public UpdateIncidentStatusRequest {

        if (note != null) {

            note = note.trim();

            if (note.isBlank()) {
                note = null;
            }
        }
    }
}
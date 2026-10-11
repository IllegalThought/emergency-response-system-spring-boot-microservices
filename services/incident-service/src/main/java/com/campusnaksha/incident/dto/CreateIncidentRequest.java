package com.campusnaksha.incident.dto;

import java.math.BigDecimal;

import com.campusnaksha.incident.entity.IncidentCategory;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateIncidentRequest(

        @NotBlank(message = "Title is required")
        @Size(
                min = 5,
                max = 150,
                message = "Title must be between 5 and 150 characters"
        )
        String title,

        @NotBlank(message = "Description is required")
        @Size(
                min = 10,
                max = 2000,
                message = "Description must be between 10 and 2000 characters"
        )
        String description,

        @NotNull(message = "Category is required")
        IncidentCategory category,

        @NotNull(message = "Latitude is required")
        @DecimalMin(
                value = "-90.0",
                message = "Latitude must be >= -90"
        )
        @DecimalMax(
                value = "90.0",
                message = "Latitude must be <= 90"
        )
        @Digits(
                integer = 3,
                fraction = 6,
                message = "Latitude must have at most 6 decimal places"
        )
        BigDecimal latitude,

        @NotNull(message = "Longitude is required")
        @DecimalMin(
                value = "-180.0",
                message = "Longitude must be >= -180"
        )
        @DecimalMax(
                value = "180.0",
                message = "Longitude must be <= 180"
        )
        @Digits(
                integer = 3,
                fraction = 6,
                message = "Longitude must have at most 6 decimal places"
        )
        BigDecimal longitude,

        @Size(
                max = 200,
                message = "Location name must not exceed 200 characters"
        )
        String locationName

) {

    public CreateIncidentRequest {

        title = normalizeRequired(title);
        description = normalizeRequired(description);
        locationName = normalizeOptional(locationName);
    }

    private static String normalizeRequired(String value) {

        return value == null
                ? null
                : value.trim();
    }

    private static String normalizeOptional(String value) {

        if (value == null) {
            return null;
        }

        String trimmed = value.trim();

        return trimmed.isBlank()
                ? null
                : trimmed;
    }
}
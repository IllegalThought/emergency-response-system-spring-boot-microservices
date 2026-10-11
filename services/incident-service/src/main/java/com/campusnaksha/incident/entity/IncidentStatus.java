package com.campusnaksha.incident.entity;

public enum IncidentStatus {

    REPORTED,
    ACKNOWLEDGED,
    IN_PROGRESS,
    RESOLVED,
    REJECTED;

    public boolean canTransitionTo(
            IncidentStatus next
    ) {

        return switch (this) {

            case REPORTED ->
                    next == ACKNOWLEDGED
                            || next == REJECTED;

            case ACKNOWLEDGED ->
                    next == IN_PROGRESS
                            || next == REJECTED;

            case IN_PROGRESS ->
                    next == RESOLVED;

            case RESOLVED, REJECTED ->
                    false;
        };
    }

    public boolean isFinal() {

        return this == RESOLVED
                || this == REJECTED;
    }
}
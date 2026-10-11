package com.campusnaksha.incident;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.campusnaksha.incident.entity.IncidentStatus;

class IncidentStatusTest {

    @Test
    void reportedCanBeAcknowledged() {

        assertTrue(
                IncidentStatus.REPORTED
                        .canTransitionTo(
                                IncidentStatus.ACKNOWLEDGED
                        )
        );
    }

    @Test
    void reportedCanBeRejected() {

        assertTrue(
                IncidentStatus.REPORTED
                        .canTransitionTo(
                                IncidentStatus.REJECTED
                        )
        );
    }

    @Test
    void acknowledgedCanBecomeInProgress() {

        assertTrue(
                IncidentStatus.ACKNOWLEDGED
                        .canTransitionTo(
                                IncidentStatus.IN_PROGRESS
                        )
        );
    }

    @Test
    void inProgressCanBecomeResolved() {

        assertTrue(
                IncidentStatus.IN_PROGRESS
                        .canTransitionTo(
                                IncidentStatus.RESOLVED
                        )
        );
    }

    @Test
    void resolvedCannotBeReopened() {

        assertFalse(
                IncidentStatus.RESOLVED
                        .canTransitionTo(
                                IncidentStatus.IN_PROGRESS
                        )
        );
    }

    @Test
    void rejectedCannotBeAcknowledged() {

        assertFalse(
                IncidentStatus.REJECTED
                        .canTransitionTo(
                                IncidentStatus.ACKNOWLEDGED
                        )
        );
    }
}
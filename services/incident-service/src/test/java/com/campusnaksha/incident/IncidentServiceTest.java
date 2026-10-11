package com.campusnaksha.incident;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.campusnaksha.incident.dto.CreateIncidentRequest;
import com.campusnaksha.incident.dto.IncidentResponse;
import com.campusnaksha.incident.dto.UpdateIncidentStatusRequest;
import com.campusnaksha.incident.entity.Incident;
import com.campusnaksha.incident.entity.IncidentCategory;
import com.campusnaksha.incident.entity.IncidentPriority;
import com.campusnaksha.incident.entity.IncidentStatus;
import com.campusnaksha.incident.exception.IncidentNotFoundException;
import com.campusnaksha.incident.exception.InvalidIncidentStateException;
import com.campusnaksha.incident.repository.IncidentRepository;
import com.campusnaksha.incident.repository.IncidentStatusHistoryRepository;
import com.campusnaksha.incident.service.IncidentService;

@ExtendWith(MockitoExtension.class)
class IncidentServiceTest {

    @Mock
    private IncidentRepository incidentRepository;

    @Mock
    private IncidentStatusHistoryRepository historyRepository;

    private IncidentService service;

    private final UUID reporterId =
            UUID.randomUUID();

    private final UUID adminId =
            UUID.randomUUID();

    @BeforeEach
    void setUp() {

        service = new IncidentService(
                incidentRepository,
                historyRepository
        );
    }

    @Test
    void createStartsIncidentAsReported() {

        CreateIncidentRequest request =
                new CreateIncidentRequest(
                        "Broken street light",
                        "Street light near library is not working.",
                        IncidentCategory.LIGHTING,
                        new BigDecimal("18.520400"),
                        new BigDecimal("73.856700"),
                        "Library Road"
                );

        when(
                incidentRepository.save(any(Incident.class))
        ).thenAnswer(invocation ->
                invocation.getArgument(0)
        );

        IncidentResponse response =
                service.create(
                        request,
                        reporterId
                );

        assertEquals(
                IncidentStatus.REPORTED,
                response.status()
        );

        assertEquals(
                IncidentPriority.MEDIUM,
                response.priority()
        );
    }

    @Test
    void updateStatusCreatesHistory() {

        UUID id = UUID.randomUUID();

        Incident incident =
                incident(
                        id,
                        IncidentStatus.REPORTED
                );

        when(
                incidentRepository.findById(id)
        ).thenReturn(Optional.of(incident));

        when(
                incidentRepository.saveAndFlush(incident)
        ).thenReturn(incident);

        IncidentResponse response =
                service.updateStatus(
                        id,
                        new UpdateIncidentStatusRequest(
                                IncidentStatus.ACKNOWLEDGED,
                                "Admin acknowledged the incident"
                        ),
                        adminId
                );

        assertEquals(
                IncidentStatus.ACKNOWLEDGED,
                response.status()
        );

        verify(historyRepository).save(any());
    }

    @Test
    void invalidTransitionThrowsException() {

        UUID id = UUID.randomUUID();

        Incident incident =
                incident(
                        id,
                        IncidentStatus.RESOLVED
                );

        when(
                incidentRepository.findById(id)
        ).thenReturn(Optional.of(incident));

        assertThrows(
                InvalidIncidentStateException.class,
                () ->
                        service.updateStatus(
                                id,
                                new UpdateIncidentStatusRequest(
                                        IncidentStatus.IN_PROGRESS,
                                        null
                                ),
                                adminId
                        )
        );

        verify(
                incidentRepository,
                never()
        ).saveAndFlush(any());

        verify(
                historyRepository,
                never()
        ).save(any());
    }

    @Test
    void sameStatusIsIdempotent() {

        UUID id = UUID.randomUUID();

        Incident incident =
                incident(
                        id,
                        IncidentStatus.REPORTED
                );

        when(
                incidentRepository.findById(id)
        ).thenReturn(Optional.of(incident));

        IncidentResponse response =
                service.updateStatus(
                        id,
                        new UpdateIncidentStatusRequest(
                                IncidentStatus.REPORTED,
                                "Retry"
                        ),
                        adminId
                );

        assertEquals(
                IncidentStatus.REPORTED,
                response.status()
        );

        verify(
                incidentRepository,
                never()
        ).saveAndFlush(any());

        verify(
                historyRepository,
                never()
        ).save(any());
    }

    @Test
    void missingIncidentThrowsNotFound() {

        UUID id = UUID.randomUUID();

        when(
                incidentRepository.findById(id)
        ).thenReturn(Optional.empty());

        assertThrows(
                IncidentNotFoundException.class,
                () ->
                        service.findById(id)
        );
    }

    private Incident incident(
            UUID id,
            IncidentStatus status
    ) {

        Instant now = Instant.now();

        return new Incident(
                id,
                reporterId,
                "Broken light",
                "A broken light near the library.",
                IncidentCategory.LIGHTING,
                status,
                IncidentPriority.MEDIUM,
                new BigDecimal("18.520400"),
                new BigDecimal("73.856700"),
                "Library Road",
                now,
                now,
                status == IncidentStatus.RESOLVED
                        ? now
                        : null
        );
    }
}
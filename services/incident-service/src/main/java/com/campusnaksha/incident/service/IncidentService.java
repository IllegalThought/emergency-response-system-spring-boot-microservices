package com.campusnaksha.incident.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campusnaksha.incident.dto.CreateIncidentRequest;
import com.campusnaksha.incident.dto.IncidentResponse;
import com.campusnaksha.incident.dto.IncidentStatusHistoryResponse;
import com.campusnaksha.incident.dto.UpdateIncidentPriorityRequest;
import com.campusnaksha.incident.dto.UpdateIncidentStatusRequest;
import com.campusnaksha.incident.entity.Incident;
import com.campusnaksha.incident.entity.IncidentCategory;
import com.campusnaksha.incident.entity.IncidentPriority;
import com.campusnaksha.incident.entity.IncidentStatus;
import com.campusnaksha.incident.entity.IncidentStatusHistory;
import com.campusnaksha.incident.exception.IncidentNotFoundException;
import com.campusnaksha.incident.repository.IncidentRepository;
import com.campusnaksha.incident.repository.IncidentStatusHistoryRepository;

@Service
public class IncidentService {

    private static final Logger log =
            LoggerFactory.getLogger(IncidentService.class);

    private final IncidentRepository incidentRepository;
    private final IncidentStatusHistoryRepository historyRepository;

    public IncidentService(
            IncidentRepository incidentRepository,
            IncidentStatusHistoryRepository historyRepository
    ) {
        this.incidentRepository = incidentRepository;
        this.historyRepository = historyRepository;
    }

    @Transactional
    public IncidentResponse create(
            CreateIncidentRequest request,
            UUID reportedByUserId
    ) {

        Instant now = Instant.now();

        Incident incident = new Incident(
                UUID.randomUUID(),
                reportedByUserId,
                request.title(),
                request.description(),
                request.category(),
                IncidentStatus.REPORTED,
                IncidentPriority.MEDIUM,
                request.latitude(),
                request.longitude(),
                request.locationName(),
                now,
                now,
                null
        );

        Incident saved =
                incidentRepository.save(incident);

        log.info(
                "Incident created: id={}, reporter={}, category={}",
                saved.getId(),
                reportedByUserId,
                saved.getCategory()
        );

        return IncidentResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<IncidentResponse> findAll(
            IncidentStatus status,
            IncidentCategory category
    ) {

        List<Incident> incidents;

        if (status != null && category != null) {

            incidents =
                    incidentRepository
                            .findAllByStatusAndCategory(
                                    status,
                                    category
                            );

        } else if (status != null) {

            incidents =
                    incidentRepository
                            .findAllByStatus(status);

        } else if (category != null) {

            incidents =
                    incidentRepository
                            .findAllByCategory(category);

        } else {

            incidents =
                    incidentRepository.findAll();
        }

        return incidents.stream()
                .map(IncidentResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public IncidentResponse findById(
            UUID id
    ) {

        Incident incident =
                incidentRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new IncidentNotFoundException(
                                        "Incident not found"
                                )
                        );

        return IncidentResponse.from(incident);
    }

    @Transactional(readOnly = true)
    public List<IncidentStatusHistoryResponse> findHistory(
            UUID incidentId
    ) {

        /*
         * Verify that the incident exists.
         *
         * Otherwise:
         *
         * GET /incidents/random-id/history
         *
         * would incorrectly return 200 [].
         */
        if (!incidentRepository.existsById(incidentId)) {

            throw new IncidentNotFoundException(
                    "Incident not found"
            );
        }

        return historyRepository
                .findAllByIncidentIdOrderByChangedAtAsc(
                        incidentId
                )
                .stream()
                .map(IncidentStatusHistoryResponse::from)
                .toList();
    }

    @Transactional
    public IncidentResponse updateStatus(
            UUID id,
            UpdateIncidentStatusRequest request,
            UUID changedByUserId
    ) {

        Incident incident =
                incidentRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new IncidentNotFoundException(
                                        "Incident not found"
                                )
                        );

        IncidentStatus previousStatus =
                incident.getStatus();

        /*
         * Idempotent retry.
         *
         * Do not create another history record.
         */
        if (previousStatus == request.status()) {

            log.info(
                    "Incident status update was idempotent: id={}, status={}",
                    id,
                    previousStatus
            );

            return IncidentResponse.from(incident);
        }

        Instant now = Instant.now();

        incident.changeStatus(
                request.status(),
                now
        );

        /*
         * saveAndFlush makes optimistic-lock failures
         * happen inside this transaction rather than
         * being deferred unnecessarily.
         */
        Incident saved =
                incidentRepository.saveAndFlush(incident);

        IncidentStatusHistory history =
                new IncidentStatusHistory(
                        UUID.randomUUID(),
                        saved.getId(),
                        previousStatus,
                        saved.getStatus(),
                        changedByUserId,
                        now,
                        request.note()
                );

        historyRepository.save(history);

        log.info(
                "Incident status changed: id={}, from={}, to={}, by={}",
                id,
                previousStatus,
                saved.getStatus(),
                changedByUserId
        );

        return IncidentResponse.from(saved);
    }

    @Transactional
    public IncidentResponse updatePriority(
            UUID id,
            UpdateIncidentPriorityRequest request
    ) {

        Incident incident =
                incidentRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new IncidentNotFoundException(
                                        "Incident not found"
                                )
                        );

        if (incident.getPriority() == request.priority()) {

            return IncidentResponse.from(incident);
        }

        Instant now = Instant.now();

        IncidentPriority previousPriority =
                incident.getPriority();

        incident.changePriority(
                request.priority(),
                now
        );

        Incident saved =
                incidentRepository.saveAndFlush(incident);

        log.info(
                "Incident priority changed: id={}, from={}, to={}",
                id,
                previousPriority,
                saved.getPriority()
        );

        return IncidentResponse.from(saved);
    }
}
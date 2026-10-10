package com.campusnaksha.incident.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campusnaksha.incident.entity.IncidentStatusHistory;

public interface IncidentStatusHistoryRepository
        extends JpaRepository<IncidentStatusHistory, UUID> {

    List<IncidentStatusHistory>
    findAllByIncidentIdOrderByChangedAtAsc(
            UUID incidentId
    );
}
package com.campusnaksha.incident.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.campusnaksha.incident.entity.Incident;
import com.campusnaksha.incident.entity.IncidentCategory;
import com.campusnaksha.incident.entity.IncidentStatus;

public interface IncidentRepository
        extends JpaRepository<Incident, UUID> {

    List<Incident> findAllByStatus(
            IncidentStatus status
    );

    List<Incident> findAllByCategory(
            IncidentCategory category
    );

    List<Incident> findAllByStatusAndCategory(
            IncidentStatus status,
            IncidentCategory category
    );
}
ALTER TABLE incidents
ADD CONSTRAINT chk_incidents_status
CHECK (
    status IN (
        'REPORTED',
        'ACKNOWLEDGED',
        'IN_PROGRESS',
        'RESOLVED',
        'REJECTED'
    )
);

ALTER TABLE incidents
ADD CONSTRAINT chk_incidents_category
CHECK (
    category IN (
        'INFRASTRUCTURE',
        'ELECTRICAL',
        'WATER',
        'SANITATION',
        'ROAD_DAMAGE',
        'SECURITY',
        'LIGHTING',
        'OTHER'
    )
);

ALTER TABLE incidents
ADD CONSTRAINT chk_incidents_priority
CHECK (
    priority IN (
        'LOW',
        'MEDIUM',
        'HIGH',
        'CRITICAL'
    )
);

ALTER TABLE incident_status_history
ADD CONSTRAINT chk_incident_history_previous_status
CHECK (
    previous_status IN (
        'REPORTED',
        'ACKNOWLEDGED',
        'IN_PROGRESS',
        'RESOLVED',
        'REJECTED'
    )
);

ALTER TABLE incident_status_history
ADD CONSTRAINT chk_incident_history_new_status
CHECK (
    new_status IN (
        'REPORTED',
        'ACKNOWLEDGED',
        'IN_PROGRESS',
        'RESOLVED',
        'REJECTED'
    )
);

ALTER TABLE incident_status_history
ADD CONSTRAINT chk_incident_history_status_change
CHECK (
    previous_status <> new_status
);
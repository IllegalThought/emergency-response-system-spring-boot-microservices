CREATE TABLE incidents (

    id UUID PRIMARY KEY,

    reported_by_user_id UUID NOT NULL,

    title VARCHAR(150) NOT NULL,

    description TEXT NOT NULL,

    category VARCHAR(30) NOT NULL,

    status VARCHAR(30) NOT NULL,

    priority VARCHAR(30) NOT NULL,

    latitude NUMERIC(9,6) NOT NULL,

    longitude NUMERIC(9,6) NOT NULL,

    location_name VARCHAR(200),

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    resolved_at TIMESTAMP WITH TIME ZONE,

    CONSTRAINT chk_incidents_latitude
        CHECK (latitude >= -90 AND latitude <= 90),

    CONSTRAINT chk_incidents_longitude
        CHECK (longitude >= -180 AND longitude <= 180)
);


CREATE INDEX idx_incidents_status
    ON incidents(status);


CREATE INDEX idx_incidents_category
    ON incidents(category);


CREATE INDEX idx_incidents_created_at
    ON incidents(created_at);


CREATE INDEX idx_incidents_reported_by
    ON incidents(reported_by_user_id);

CREATE TABLE incident_status_history (

    id UUID PRIMARY KEY,

    incident_id UUID NOT NULL,

    previous_status VARCHAR(30) NOT NULL,

    new_status VARCHAR(30) NOT NULL,

    changed_by_user_id UUID NOT NULL,

    changed_at TIMESTAMP WITH TIME ZONE NOT NULL,

    note VARCHAR(1000),

    CONSTRAINT fk_incident_status_history_incident
        FOREIGN KEY (incident_id)
        REFERENCES incidents(id)
        ON DELETE CASCADE
);


CREATE INDEX idx_incident_status_history_incident
    ON incident_status_history(incident_id);


CREATE INDEX idx_incident_status_history_changed_at
    ON incident_status_history(changed_at);
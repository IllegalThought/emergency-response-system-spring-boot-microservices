#!/usr/bin/env bash

set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

SERVICE="${1:-}"

if [[ -z "$SERVICE" ]]; then
    echo "Usage: ./scripts/run-service.sh <service-name>"
    echo
    echo "Available services:"
    echo "  service-registry"
    echo "  api-gateway"
    echo "  identity-service"
    echo "  incident-service"
    exit 1
fi

ENV_FILE="$ROOT_DIR/.env"

if [[ ! -f "$ENV_FILE" ]]; then
    echo "ERROR: .env file not found at:"
    echo "$ENV_FILE"
    exit 1
fi

# Load environment variables from .env
set -a
source "$ENV_FILE"
set +a

case "$SERVICE" in

    identity-service)
        POM="$ROOT_DIR/services/identity-service/pom.xml"
        ;;

    incident-service)
        POM="$ROOT_DIR/services/incident-service/pom.xml"
        ;;

    api-gateway)
        POM="$ROOT_DIR/infrastructure/api-gateway/pom.xml"
        ;;

    service-registry)
        POM="$ROOT_DIR/infrastructure/service-registry/pom.xml"
        ;;

    *)
        echo "ERROR: Unknown service: $SERVICE"
        exit 1
        ;;

esac

# JWT is required by Identity and Incident services
if [[ "$SERVICE" == "identity-service" || "$SERVICE" == "incident-service" ]]; then
    if [[ -z "${JWT_SECRET:-}" ]]; then
        echo "ERROR: JWT_SECRET is not set in .env"
        exit 1
    fi
fi

if [[ ! -f "$POM" ]]; then
    echo "ERROR: pom.xml not found:"
    echo "$POM"
    exit 1
fi

echo "Starting $SERVICE..."
echo "Using POM: $POM"

exec mvn -f "$POM" spring-boot:run
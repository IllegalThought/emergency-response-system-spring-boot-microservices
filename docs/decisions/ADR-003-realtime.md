# ADR-003: Real-Time Architecture

## Status

Accepted


## Decision

RabbitMQ will be used for asynchronous backend events.

WebSocket/STOMP will be used to deliver real-time events to frontend
clients.


## Example

Emergency Service

    ->

RabbitMQ

    ->

Notification Service

    ->

WebSocket/STOMP

    ->

Frontend
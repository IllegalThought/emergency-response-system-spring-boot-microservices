# Parking Service Design


# IoT

Campus Naksha will not use physical parking sensors.


# Parking Simulation

A backend simulation will generate parking occupancy changes.


Current architecture:

ParkingSimulationService

        ->

Parking Service

        ->

PostgreSQL

        ->

RabbitMQ

        ->

Notification Service

        ->

WebSocket

        ->

Frontend


# Future IoT Architecture

Future hardware can call the same parking APIs.

IR Sensor

    ->

Microcontroller

    ->

Parking API

    ->

Parking Service


This means physical IoT can later replace the simulator without redesigning
the Parking Service.
# Emergency Service Design


# Nearest Guard Assignment


When a student creates an SOS:


1. Validate the student's coordinates.

2. Create the SOS.

3. Set:

CREATED

4. Transition to:

SEARCHING_FOR_GUARD

5. Search Redis GEO for guards near the student.

6. Return guards ordered by distance.

Example:

Guard A: 120 metres

Guard B: 340 metres

Guard C: 580 metres


7. Check guards in nearest-first order.

For every guard:

If status is not AVAILABLE:

skip guard.


If status is AVAILABLE:

attempt atomic reservation.


8. If reservation succeeds:

Guard:

AVAILABLE -> RESERVED

SOS:

SEARCHING_FOR_GUARD -> GUARD_ASSIGNED


9. Publish:

sos.guard.assigned


10. If no eligible guards are available:

SOS:

SEARCHING_FOR_GUARD -> NO_GUARD_AVAILABLE
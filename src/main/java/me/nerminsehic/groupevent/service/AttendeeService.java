package me.nerminsehic.groupevent.service;

import me.nerminsehic.groupevent.entity.Attendee;

import java.util.Set;
import java.util.UUID;

public interface AttendeeService extends Service<Attendee, UUID> {

    Set<Attendee> findOrCreateAttendeesByEmail(Set<String> attendees);

    Set<Attendee> updateLastInvited(Set<Attendee> attendees);

    Attendee updateName(UUID attendeeId, String firstName, String lastName);
}

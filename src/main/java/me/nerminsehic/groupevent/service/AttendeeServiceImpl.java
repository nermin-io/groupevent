package me.nerminsehic.groupevent.service;

import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import me.nerminsehic.groupevent.entity.Attendee;
import me.nerminsehic.groupevent.exception.NotFoundException;
import me.nerminsehic.groupevent.repository.Attendees;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendeeServiceImpl implements AttendeeService {

    private final Attendees attendees;

    @Override
    public Optional<Attendee> findById(UUID id) {
        return attendees.findById(id);
    }

    @Override
    public List<Attendee> findAll() {
        return attendees.findAll();
    }

    @Override
    public Attendee create(Attendee attendee) {
        Attendee newAttendee = new Attendee(
                attendee.getEmailAddress()
        );

        return attendees.save(newAttendee);
    }

    @Override
    public void deleteById(UUID id) {
        attendees.deleteById(id);
    }

    @Override
    public void delete(Attendee attendee) {
        attendees.delete(attendee);
    }

    @Override
    public Set<Attendee> findOrCreateAttendeesByEmail(Set<String> emails) {
        Set<Attendee> foundAttendees = emails
                .stream()
                .map(email -> attendees.findByEmailAddress(email).orElseGet(() -> new Attendee(email)))
                .collect(Collectors.toSet());

        return Sets.newHashSet(attendees.saveAll(foundAttendees));
    }

    @Override
    public Set<Attendee> updateLastInvited(Set<Attendee> attendeesSet) {
        attendeesSet.forEach(attendee -> attendee.setLastInvited(Instant.now()));
        return Sets.newHashSet(attendees.saveAll(attendeesSet));
    }

    @Override
    public Attendee updateById(UUID id, Attendee attendee) {
        throw new UnsupportedOperationException("Cannot update an attendee");
    }

    @Override
    public Attendee updateName(UUID attendeeId, String firstName, String lastName) {
        Attendee attendee = findById(attendeeId)
                .orElseThrow(() -> new NotFoundException(Attendee.class, attendeeId));

        attendee.setFirstName(firstName);
        attendee.setLastName(lastName);

        return attendees.save(attendee);
    }
}

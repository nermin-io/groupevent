package me.nerminsehic.groupevent.service;

import com.github.javafaker.Faker;
import me.nerminsehic.groupevent.entity.*;
import me.nerminsehic.groupevent.exception.IllegalOperationException;
import me.nerminsehic.groupevent.exception.NotFoundException;
import me.nerminsehic.groupevent.repository.Events;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private Events events;

    @Mock
    private OrganiserService organiserService;

    @Mock
    private AddressService addressService;

    @Mock
    private AttendeeService attendeeService;

    @Mock
    private MailService mailService;

    @Mock
    private EventAccessTokenService eventAccessTokenService;

    @Captor
    private ArgumentCaptor<Set<Attendee>> attendeeArgumentCaptor;

    @Mock
    private Clock clock;

    private EventService underTest;
    private final Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        underTest = new EventServiceImpl(
                events,
                organiserService,
                addressService,
                attendeeService,
                mailService,
                eventAccessTokenService,
                clock
        );
    }

    @Test
    void findById_ItShould_FindEventById() {
        // given
        UUID organiserId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        // when
        underTest.findById(organiserId, eventId);

        // then
        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<Organiser> organiserArgumentCaptor = ArgumentCaptor.forClass(Organiser.class);
        verify(events).findByIdAndOrganiser(uuidArgumentCaptor.capture(), organiserArgumentCaptor.capture());

        assertThat(uuidArgumentCaptor.getValue()).isEqualTo(eventId);
        assertThat(organiserArgumentCaptor.getValue()).isEqualTo(organiser);
    }

    @Test
    void findAll_ItShould_FindAllEvents_GivenOrganiserId() {
        // given
        UUID organiserId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        // when
        underTest.findAll(organiserId);

        // then
        ArgumentCaptor<Organiser> organiserArgumentCaptor = ArgumentCaptor.forClass(Organiser.class);
        verify(events).findAllByOrganiser(organiserArgumentCaptor.capture());

        assertThat(organiserArgumentCaptor.getValue()).isEqualTo(organiser);
    }

    @Test
    void create_ItShould_CreateEventAndSendInvites_GivenAttendees() {
        // given
        UUID organiserId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();
        Address address = createTestAddress(organiser);
        Set<Attendee> attendees = createTestAttendeeSet();
        Event event = createTestEventWithAddressAndAttendees(organiser, address, attendees);
        String accessToken = "ACCESS_TOKEN";

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        given(addressService.findOrCreateAddress(organiserId, event.getAddress()))
                .willReturn(address);

        given(events.save(any(Event.class)))
                .willReturn(event);

        given(eventAccessTokenService.createToken(event))
                .willReturn(accessToken);

        // when
        Event result = underTest.create(organiserId, event, attendees);
        event.setStatus(EventStatus.PLANNED);
        event.setUpdatedAt(Instant.now(clock));
        event.setCreatedAt(Instant.now(clock));

        // then
        ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(events).save(eventArgumentCaptor.capture());
        assertThat(eventArgumentCaptor.getValue()).isEqualTo(event);

        verify(mailService).sendInvitesToAttendees(eventArgumentCaptor.capture());
        assertThat(eventArgumentCaptor.getValue()).isEqualTo(event);

        verify(mailService).sendEventConfirmationToOrganiser(eventArgumentCaptor.capture(), stringArgumentCaptor.capture());
        assertThat(eventArgumentCaptor.getValue()).isEqualTo(event);

        verify(attendeeService).updateLastInvited(attendeeArgumentCaptor.capture());
        assertThat(attendeeArgumentCaptor.getValue()).isEqualTo(attendees);
        assertThat(stringArgumentCaptor.getValue()).isEqualTo(accessToken);

        assertThat(result.getStatus()).isEqualTo(EventStatus.PLANNED);
    }

    @Test
    void updateById_ItShould_UpdateEventById_IfExists() {
        // given
        UUID organiserId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();

        Address address = createTestAddress(organiser);
        Event event = createTestEvent(organiser);
        Event newEvent = createTestEventWithAddress(organiser, address);

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        given(addressService.findOrCreateAddress(organiserId, newEvent.getAddress()))
                .willReturn(address);

        given(events.findByIdAndOrganiser(eventId, organiser))
                .willReturn(Optional.of(event));
        // when
        underTest.updateById(organiserId, eventId, newEvent);

        // then
        ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);
        verify(events).save(eventArgumentCaptor.capture());

        assertThat(eventArgumentCaptor.getValue()).isEqualTo(event);
        assertThat(event).isEqualTo(newEvent);
    }

    @Test
    void updateById_ItShouldThrow_NotFoundException_IfNotExists() {
        // given
        UUID organiserId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();

        Address address = createTestAddress(organiser);
        Event newEvent = createTestEventWithAddress(organiser, address);

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        given(addressService.findOrCreateAddress(organiserId, newEvent.getAddress()))
                .willReturn(address);

        given(events.findByIdAndOrganiser(eventId, organiser))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.updateById(organiserId, eventId, newEvent))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Cannot find");
    }

    @Test
    void cancel_ItShould_CancelEventAndNotifyAttendees_IfExists() {
        // given
        UUID organiserId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        String message = faker.lorem().characters();

        Organiser organiser = createTestOrganiser();
        Event event = createTestEvent(organiser);
        event.setStatus(EventStatus.PLANNED);

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        given(events.findByIdAndOrganiser(eventId, organiser))
                .willReturn(Optional.of(event));

        given(events.save(any(Event.class)))
                .willReturn(event);

        // when
        underTest.cancel(organiserId, eventId, message);

        // then
        assertThat(event.getStatus()).isEqualTo(EventStatus.CANCELLED);
        assertThat(event.getCancelMessage()).isEqualTo(message);

        ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);
        verify(events).save(eventArgumentCaptor.capture());
        assertThat(eventArgumentCaptor.getValue()).isEqualTo(event);

        verify(mailService).sendCancellationNoticeToAttendees(eventArgumentCaptor.capture());
        assertThat(eventArgumentCaptor.getValue()).isEqualTo(event);
    }

    @Test
    void cancel_ItShouldThrow_NotFoundException_IfNotExists() {
        // given
        UUID organiserId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        String message = faker.lorem().characters();

        Organiser organiser = createTestOrganiser();

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        given(events.findByIdAndOrganiser(eventId, organiser))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.cancel(organiserId, eventId, message))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Cannot find");
    }

    @Test
    void cancel_ItShouldThrow_IllegalOperationException_IfEventAlreadyCancelled() {
        // given
        UUID organiserId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        String message = faker.lorem().characters();

        Organiser organiser = createTestOrganiser();
        Event event = createTestEvent(organiser);
        event.setStatus(EventStatus.CANCELLED);

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        given(events.findByIdAndOrganiser(eventId, organiser))
                .willReturn(Optional.of(event));

        // when
        // then
        assertThatThrownBy(() -> underTest.cancel(organiserId, eventId, message))
                .isInstanceOf(IllegalOperationException.class)
                .hasMessageContaining("Cannot cancel event that is already cancelled");
    }

    @Test
    void reschedule_ItShould_RescheduleEventAndNotifyAttendees_IfExists() {
        // given
        UUID organiserId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();

        Event event = createTestEvent(organiser);
        event.setStatus(EventStatus.PLANNED);

        Event newEvent = new Event(
                organiser,
                null,
                Collections.emptySet(),
                null,
                null,
                LocalDate.now().plus(1, ChronoUnit.DAYS),
                LocalTime.now().plus(1, ChronoUnit.HOURS),
                LocalTime.now().plus(2, ChronoUnit.HOURS),
                "New Agenda"
        );

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        given(events.findByIdAndOrganiser(eventId, organiser))
                .willReturn(Optional.of(event));

        given(events.save(any(Event.class)))
                .willReturn(event);

        // when
        underTest.reschedule(organiserId, eventId, newEvent);

        // then
        assertThat(event.getStatus()).isEqualTo(EventStatus.RESCHEDULED);
        assertThat(event.getScheduledDate()).isEqualTo(newEvent.getScheduledDate());
        assertThat(event.getTimeFrom()).isEqualTo(newEvent.getTimeFrom());
        assertThat(event.getTimeTo()).isEqualTo(newEvent.getTimeTo());
        assertThat(event.getAgenda()).isEqualTo(newEvent.getAgenda());

        ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);

        verify(events).save(eventArgumentCaptor.capture());
        assertThat(eventArgumentCaptor.getValue()).isEqualTo(event);

        verify(mailService).sendRescheduledNoticeToAttendees(eventArgumentCaptor.capture());
        assertThat(eventArgumentCaptor.getValue()).isEqualTo(event);
    }

    @Test
    void reschedule_ItShouldThrow_NotFoundException_IfNotExists() {
        // given
        UUID organiserId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();

        Event event = createTestEvent(organiser);
        event.setStatus(EventStatus.PLANNED);

        Event newEvent = new Event(
                organiser,
                null,
                Collections.emptySet(),
                null,
                null,
                LocalDate.now().plus(1, ChronoUnit.DAYS),
                LocalTime.now().plus(1, ChronoUnit.HOURS),
                LocalTime.now().plus(2, ChronoUnit.HOURS),
                "New Agenda"
        );

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        given(events.findByIdAndOrganiser(eventId, organiser))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.reschedule(organiserId, eventId, newEvent))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Cannot find");
    }

    @Test
    void reschedule_ItShouldThrow_IllegalOperationException_IfEventAlreadyCancelled() {
        // given
        UUID organiserId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();

        Event event = createTestEvent(organiser);
        event.setStatus(EventStatus.CANCELLED);

        Event newEvent = new Event(
                organiser,
                null,
                Collections.emptySet(),
                null,
                null,
                LocalDate.now().plus(1, ChronoUnit.DAYS),
                LocalTime.now().plus(1, ChronoUnit.HOURS),
                LocalTime.now().plus(2, ChronoUnit.HOURS),
                "New Agenda"
        );

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        given(events.findByIdAndOrganiser(eventId, organiser))
                .willReturn(Optional.of(event));

        // when
        // then
        assertThatThrownBy(() -> underTest.reschedule(organiserId, eventId, newEvent))
                .isInstanceOf(IllegalOperationException.class)
                .hasMessageContaining("Cannot reschedule a cancelled event");
    }

    @Test
    void deleteById_ItShould_DeleteEventById_IfExists() {
        // given
        UUID organiserId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        Organiser organiser = createTestOrganiser();
        Event event = createTestEvent(organiser);

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        given(events.findByIdAndOrganiser(eventId, organiser))
                .willReturn(Optional.of(event));
        // when
        underTest.deleteById(organiserId, eventId);

        // then
        ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);
        verify(events).delete(eventArgumentCaptor.capture());

        assertThat(eventArgumentCaptor.getValue()).isEqualTo(event);
    }

    @Test
    void deleteById_ItShouldThrow_NotFoundException_IfNotExists() {
        // given
        UUID organiserId = UUID.randomUUID();
        UUID eventId = UUID.randomUUID();

        Organiser organiser = createTestOrganiser();

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        given(events.findByIdAndOrganiser(eventId, organiser))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.deleteById(organiserId, eventId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Cannot find");
    }

    @Test
    void delete_ItShould_DeleteEvent_IfExists() {
        // given
        UUID organiserId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();
        Event event = createTestEvent(organiser);

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        // when
        underTest.delete(organiserId, event);

        // then
        ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);
        verify(events).delete(eventArgumentCaptor.capture());

        assertThat(eventArgumentCaptor.getValue()).isEqualTo(event);
    }

    @Test
    void delete_ItShouldThrow_IllegalOperationException_IfOrganiserIsInvalid() {
        // given
        UUID organiserId = UUID.randomUUID();
        Organiser validOrganiser = createTestOrganiser();
        Organiser invalidOrganiser = createTestOrganiser();
        Event event = createTestEvent(validOrganiser);

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(invalidOrganiser);

        // when
        // then

        assertThatThrownBy(() -> underTest.delete(organiserId, event))
                .isInstanceOf(IllegalOperationException.class)
                .hasMessageContaining("Attempt to illegally delete an event failed");
    }

    private Organiser createTestOrganiser() {
        return new Organiser(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress()
        );
    }

    private Event createTestEventWithAddress(Organiser organiser, Address address) {
        Event event = new Event(
                organiser,
                address,
                Collections.emptySet(),
                faker.funnyName().name(),
                faker.lorem().characters(),
                LocalDate.now(),
                LocalTime.now(),
                LocalTime.now(),
                "Agenda"
        );

        event.setCreatedAt(Instant.now(clock));
        event.setUpdatedAt(Instant.now(clock));

        return event;
    }

    private Event createTestEventWithAddressAndAttendees(Organiser organiser, Address address, Set<Attendee> attendees) {
        Event event = new Event(
                organiser,
                address,
                attendees,
                faker.funnyName().name(),
                faker.lorem().characters(),
                LocalDate.now(),
                LocalTime.now(),
                LocalTime.now(),
                "Agenda"
        );

        event.setCreatedAt(Instant.now(clock));
        event.setUpdatedAt(Instant.now(clock));

        return event;
    }

    private Set<Attendee> createTestAttendeeSet() {
        return Stream.iterate(1, x -> x + 1)
                .limit(5)
                .map(x -> new Attendee(faker.internet().emailAddress()))
                .collect(Collectors.toSet());
    }

    private Address createTestAddress(Organiser organiser) {
        return new Address(
                organiser,
                faker.address().streetAddress(),
                faker.address().secondaryAddress(),
                faker.address().city(),
                faker.address().state(),
                faker.address().zipCode(),
                "Ring Bell"
        );
    }

    private Event createTestEvent(Organiser organiser) {
        Event event = new Event(
                organiser,
                null,
                Collections.emptySet(),
                faker.funnyName().name(),
                faker.lorem().characters(),
                LocalDate.now(),
                LocalTime.now(),
                LocalTime.now(),
                "Agenda"
        );

        event.setCreatedAt(Instant.now(clock));
        event.setUpdatedAt(Instant.now(clock));

        return event;
    }
}
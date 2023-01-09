package me.nerminsehic.groupevent.service;

import com.github.javafaker.Faker;
import me.nerminsehic.groupevent.entity.*;
import me.nerminsehic.groupevent.entity.key.InviteCompositeKey;
import me.nerminsehic.groupevent.exception.IllegalOperationException;
import me.nerminsehic.groupevent.exception.NotFoundException;
import me.nerminsehic.groupevent.repository.Invites;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class InviteServiceImplTest {

    @Mock
    private Invites invites;

    @Mock
    private AttendeeService attendeeService;

    @Mock
    private MailService mailService;

    @Mock
    private Clock clock;

    private InviteService underTest;
    private final Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        underTest = new InviteServiceImpl(invites, attendeeService, mailService, clock);
    }

    @Test
    void respondInvite_ItShould_SaveInviteAndNotifyOrganiser_GivenAttendeeAndEvent() {
        // given
        UUID eventId = UUID.randomUUID();
        UUID attendeeId = UUID.randomUUID();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        InviteResponse response = InviteResponse.GOING;
        String message = faker.backToTheFuture().quote();

        Invite invite = createTestInvite(EventStatus.PLANNED);
        given(invites.findById(new InviteCompositeKey(eventId, attendeeId)))
                .willReturn(Optional.of(invite));

        given(invites.save(invite))
                .willReturn(invite);

        // when
        underTest.respondInvite(eventId, attendeeId, firstName, lastName, response, message);

        // then

        // verify that invite is saved
        ArgumentCaptor<Invite> inviteArgumentCaptor = ArgumentCaptor.forClass(Invite.class);
        verify(invites).save(inviteArgumentCaptor.capture());
        assertThat(inviteArgumentCaptor.getValue()).isEqualTo(invite);

        // verify that organiser is notified
        ArgumentCaptor<Invite> persistedInviteArgumentCaptor = ArgumentCaptor.forClass(Invite.class);
        verify(mailService).sendAttendeeResponseToOrganiser(persistedInviteArgumentCaptor.capture());
        assertThat(persistedInviteArgumentCaptor.getValue()).isEqualTo(invite);

        // verify that attendee name is updated
        ArgumentCaptor<String> firstNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> lastNameCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(attendeeService).updateName(idCaptor.capture(), firstNameCaptor.capture(), lastNameCaptor.capture());

        assertThat(firstNameCaptor.getValue()).isEqualTo(firstName);
        assertThat(lastNameCaptor.getValue()).isEqualTo(lastName);
        assertThat(idCaptor.getValue()).isEqualTo(attendeeId);
    }

    @Test
    void respondInvite_ItShouldThrow_IllegalOperationException_WhenEventIsCancelled() {
        // given
        UUID eventId = UUID.randomUUID();
        UUID attendeeId = UUID.randomUUID();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        InviteResponse response = InviteResponse.GOING;
        String message = faker.backToTheFuture().quote();

        Invite invite = createTestInvite(EventStatus.CANCELLED);
        given(invites.findById(new InviteCompositeKey(eventId, attendeeId)))
                .willReturn(Optional.of(invite));

        // when
        // then
        assertThatThrownBy(() -> underTest.respondInvite(eventId, attendeeId, firstName, lastName, response, message))
                .isInstanceOf(IllegalOperationException.class)
                .hasMessageContaining("This event has been cancelled");
    }

    @Test
    void getInvite_ItShould_GetInvite_WhenExists() {
        // given
        UUID eventId = UUID.randomUUID();
        UUID attendeeId = UUID.randomUUID();

        Invite invite = createTestInvite(EventStatus.PLANNED);
        given(invites.findById(new InviteCompositeKey(eventId, attendeeId)))
                .willReturn(Optional.of(invite));

        // when
        Invite result = underTest.getInvite(eventId, attendeeId);

        // then
        assertThat(result).isEqualTo(invite);
    }

    @Test
    void getInvite_ItShouldThrow_NotFoundException_WhenNotExists() {
        // given
        UUID eventId = UUID.randomUUID();
        UUID attendeeId = UUID.randomUUID();
        Invite invite = createTestInvite(EventStatus.PLANNED);

        given(invites.findById(new InviteCompositeKey(eventId, attendeeId)))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getInvite(eventId, attendeeId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Cannot find");
    }

    private Invite createTestInvite(EventStatus status) {
        Attendee attendee = createTestAttendee();
        Event event = createTestEvent(attendee);
        event.setStatus(status);

        return new Invite(event, attendee);
    }

    private Attendee createTestAttendee() {
        return new Attendee(faker.internet().emailAddress());
    }

    private Event createTestEvent(Attendee attendee) {
        Organiser organiser = new Organiser(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress()
        );

        Address address = new Address(
                organiser,
                faker.address().streetAddress(),
                faker.address().secondaryAddress(),
                faker.address().city(),
                faker.address().state(),
                faker.address().zipCode(),
                "Ring Bell"
        );

        return new Event(
                organiser,
                address,
                Set.of(attendee),
                faker.funnyName().name(),
                faker.lorem().characters(),
                LocalDate.of(2023, 1, 1),
                LocalTime.now(),
                LocalTime.now().plus(2, ChronoUnit.HOURS),
                "Agenda"
        );
    }
}
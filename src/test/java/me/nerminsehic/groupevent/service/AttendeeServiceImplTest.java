package me.nerminsehic.groupevent.service;

import com.github.javafaker.Faker;
import com.google.common.collect.Sets;
import me.nerminsehic.groupevent.entity.Attendee;
import me.nerminsehic.groupevent.exception.UniqueConstraintException;
import me.nerminsehic.groupevent.repository.Attendees;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AttendeeServiceImplTest {

    @Mock
    private Attendees attendees;

    @Mock
    private Clock clock;

    private AttendeeService underTest;

    @Captor
    private ArgumentCaptor<Set<Attendee>> attendeeSetArgumentCaptor;

    private final Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        underTest = new AttendeeServiceImpl(attendees, clock);
    }

    @Test
    void itShould_FindAttendeeById() {
        // given
        UUID id = UUID.randomUUID();

        // when
        underTest.findById(id);

        // then
        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(attendees).findById(uuidArgumentCaptor.capture());

        UUID idArg = uuidArgumentCaptor.getValue();
        assertThat(idArg).isEqualTo(id);
    }

    @Test
    void itShould_FindAllAttendees() {
        // when
        underTest.findAll();

        // then
        verify(attendees).findAll();
    }

    @Test
    void itShould_CreateNewAttendee() {
        // given
        Attendee attendee = new Attendee(faker.internet().emailAddress());

        // when
        underTest.create(attendee);

        // then
        ArgumentCaptor<Attendee> attendeeArgumentCaptor = ArgumentCaptor.forClass(Attendee.class);
        verify(attendees).save(attendeeArgumentCaptor.capture());

        Attendee attendeeArg = attendeeArgumentCaptor.getValue();
        assertThat(attendeeArg).isEqualTo(attendee);
    }

    @Test
    void itShould_ThrowException_WhenDuplicateEmailIsUsed() {
        // given
        Attendee attendee = new Attendee(faker.internet().emailAddress());
        given(attendees.findByEmailAddress(attendee.getEmailAddress()))
                .willReturn(Optional.of(attendee));

        // when
        // then
        assertThatThrownBy(() -> underTest.create(attendee))
                .isInstanceOf(UniqueConstraintException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void itShould_DeleteAttendeeById() {
        // given
        UUID id = UUID.randomUUID();

        // when
        underTest.deleteById(id);

        // then
        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(attendees).deleteById(uuidArgumentCaptor.capture());

        UUID idArg = uuidArgumentCaptor.getValue();
        assertThat(idArg).isEqualTo(id);
    }

    @Test
    void itShould_DeleteAttendee() {
        // given
        Attendee attendee = new Attendee(faker.internet().emailAddress());

        // when
        underTest.delete(attendee);

        // then
        ArgumentCaptor<Attendee> attendeeArgumentCaptor = ArgumentCaptor.forClass(Attendee.class);
        verify(attendees).delete(attendeeArgumentCaptor.capture());

        Attendee attendeeArg = attendeeArgumentCaptor.getValue();
        assertThat(attendeeArg).isEqualTo(attendee);
    }

    @Test
    void itShould_FindAttendeesByEmail_WhenExists() {
        // given
        String email = faker.internet().emailAddress();
        Set<String> emails = Set.of(email);

        Attendee attendee = new Attendee(email);
        Set<Attendee> attendeeSet = Set.of(attendee);

        given(attendees.findByEmailAddress(email))
                .willReturn(Optional.of(attendee));

        // when
        underTest.findOrCreateAttendeesByEmail(emails);

        // then
        verify(attendees).saveAll(attendeeSetArgumentCaptor.capture());

        Set<Attendee> attendeeSetArg = attendeeSetArgumentCaptor.getValue();
        assertThat(attendeeSetArg).isEqualTo(attendeeSet);
    }

    @Test
    void itShould_CreateAttendeesByEmail_WhenNotExists() {
        String email = faker.internet().emailAddress();
        Set<String> emails = Set.of(email);

        Attendee attendee = new Attendee(email);
        Set<Attendee> attendeeSet = Sets.newHashSet(attendee);

        given(attendees.findByEmailAddress(email))
                .willReturn(Optional.empty());

        // when
        underTest.findOrCreateAttendeesByEmail(emails);

        // then
        verify(attendees).saveAll(attendeeSetArgumentCaptor.capture());

        Set<Attendee> attendeeSetArg = attendeeSetArgumentCaptor.getValue();
        assertThat(attendeeSetArg).isEqualTo(attendeeSet);
    }

    @Test
    void itShould_UpdateAttendeeLastInvited() {
        // given
        Attendee attendee = new Attendee(faker.internet().emailAddress());
        Set<Attendee> attendeeSet = Set.of(attendee);

        // when
        underTest.updateLastInvited(attendeeSet);

        // then
        verify(attendees).saveAll(attendeeSetArgumentCaptor.capture());

        attendee.setLastInvited(Instant.now(clock));
        Set<Attendee> attendeeSetArg = attendeeSetArgumentCaptor.getValue();
        assertThat(attendeeSetArg).isEqualTo(attendeeSet);
    }

    @Test
    void itShouldNot_UpdateAttendeeById() {
        // given
        UUID id = UUID.randomUUID();
        Attendee attendee = new Attendee(faker.internet().emailAddress());

        // then
        // when
        assertThatThrownBy(() -> underTest.updateById(id, attendee))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("Cannot update an attendee");
    }

    @Test
    void itShould_UpdateAttendeeName() {
        // given
        UUID id = UUID.randomUUID();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        given(underTest.findById(id))
                .willReturn(Optional.of(new Attendee(faker.internet().emailAddress())));

        // when
        underTest.updateName(id, firstName, lastName);

        // then
        ArgumentCaptor<Attendee> attendeeArgumentCaptor = ArgumentCaptor.forClass(Attendee.class);
        verify(attendees).save(attendeeArgumentCaptor.capture());

        Attendee attendeeArg = attendeeArgumentCaptor.getValue();
        assertThat(attendeeArg.getFirstName()).isEqualTo(firstName);
        assertThat(attendeeArg.getLastName()).isEqualTo(lastName);
    }
}
package me.nerminsehic.groupevent.service;

import com.github.javafaker.Faker;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.objects.MailSettings;
import com.sendgrid.helpers.mail.objects.TrackingSettings;
import me.nerminsehic.groupevent.entity.*;
import me.nerminsehic.groupevent.gmaps.Directions;
import me.nerminsehic.groupevent.gmaps.StaticMaps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SendgridMailServiceTest {

    @Mock
    private SendGrid sendgrid;

    @Mock
    private StaticMaps maps;

    @Mock
    private Directions directions;

    private MailService underTest;

    private final Faker faker = new Faker();
    private final MailSettings mailSettings = new MailSettings();
    private final TrackingSettings trackingSettings = new TrackingSettings();

    @BeforeEach
    void setUp() {
        underTest = new SendgridMailService(sendgrid, mailSettings, trackingSettings, maps, directions);
    }

    @Test
    void sendLinkToOrganiser_ItShould_UseSendGrid() throws IOException {
        // given
        Organiser organiser = createTestOrganiser();
        MagicLink link = new MagicLink(organiser);

        given(sendgrid.api(any(Request.class)))
                .willReturn(new Response());

        // when
        underTest.sendLinkToOrganiser(organiser, link);

        // then
        verify(sendgrid).api(any(Request.class));
    }

    @Test
    void sendEventConfirmationToOrganiser_ItShould_UseSendGrid() throws IOException {
        // given
        Organiser organiser = createTestOrganiser();
        Event event = createTestEvent(organiser);
        String accessToken = "ACCESS_TOKEN";

        given(sendgrid.api(any(Request.class)))
                .willReturn(new Response());

        // when
        underTest.sendEventConfirmationToOrganiser(event, accessToken);

        // then
        verify(sendgrid).api(any(Request.class));
    }

    @Test
    void sendInvitesToAttendees_ItShould_UseSendGrid() throws IOException {
        // given
        Organiser organiser = createTestOrganiser();
        Event event = createTestEvent(organiser);
        Address address = event.getAddress();

        given(sendgrid.api(any(Request.class)))
                .willReturn(new Response());

        // when
        underTest.sendInvitesToAttendees(event);

        // then
        ArgumentCaptor<Address> addressArgumentCaptor = ArgumentCaptor.forClass(Address.class);
        verify(sendgrid).api(any(Request.class));

        verify(directions).getDirectionsLink(addressArgumentCaptor.capture());
        assertThat(addressArgumentCaptor.getValue()).isEqualTo(address);

        verify(maps).create(addressArgumentCaptor.capture());
        assertThat(addressArgumentCaptor.getValue()).isEqualTo(address);
    }

    @Test
    void sendAttendeeResponseToOrganiser_ItShould_UseSendGrid() throws IOException {
        // given
        Organiser organiser = createTestOrganiser();
        Event event = createTestEvent(organiser);
        Invite invite = event.getInvites()
                .stream()
                .findFirst()
                .orElse(null);

        given(sendgrid.api(any(Request.class)))
                .willReturn(new Response());

        // when
        underTest.sendAttendeeResponseToOrganiser(invite);

        // then
        verify(sendgrid).api(any(Request.class));
    }

    @Test
    void sendRescheduledNoticeToAttendees_ItShould_UseSendGrid() throws IOException {
        // given
        Organiser organiser = createTestOrganiser();
        Event event = createTestEvent(organiser);
        Address address = event.getAddress();

        given(sendgrid.api(any(Request.class)))
                .willReturn(new Response());

        // when
        underTest.sendRescheduledNoticeToAttendees(event);

        // then
        ArgumentCaptor<Address> addressArgumentCaptor = ArgumentCaptor.forClass(Address.class);
        verify(sendgrid).api(any(Request.class));

        verify(directions).getDirectionsLink(addressArgumentCaptor.capture());
        assertThat(addressArgumentCaptor.getValue()).isEqualTo(address);

        verify(maps).create(addressArgumentCaptor.capture());
        assertThat(addressArgumentCaptor.getValue()).isEqualTo(address);
    }

    @Test
    void sendCancellationNoticeToAttendees_ItShould_UseSendGrid() throws IOException {
        // given
        Organiser organiser = createTestOrganiser();
        Event event = createTestEvent(organiser);

        given(sendgrid.api(any(Request.class)))
                .willReturn(new Response());

        // when
        underTest.sendCancellationNoticeToAttendees(event);

        // then
        verify(sendgrid).api(any(Request.class));
    }

    private Organiser createTestOrganiser() {
        return new Organiser(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress()
        );
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
        Address address = createTestAddress(organiser);
        return new Event(
                organiser,
                address,
                Set.of(
                        new Attendee(faker.internet().emailAddress())
                ),
                "Event Name",
                "Event Description",
                LocalDate.now(),
                LocalTime.now(),
                LocalTime.now(),
                "Agenda"
        );
    }
}
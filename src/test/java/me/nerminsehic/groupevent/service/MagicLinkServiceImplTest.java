package me.nerminsehic.groupevent.service;

import com.github.javafaker.Faker;
import me.nerminsehic.groupevent.entity.LinkStatus;
import me.nerminsehic.groupevent.entity.MagicLink;
import me.nerminsehic.groupevent.entity.Organiser;
import me.nerminsehic.groupevent.exception.LinkException;
import me.nerminsehic.groupevent.exception.NotFoundException;
import me.nerminsehic.groupevent.repository.MagicLinks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MagicLinkServiceImplTest {

    @Mock
    private MagicLinks magicLinks;

    @Mock
    private OrganiserService organiserService;

    @Mock
    private MailService mailService;

    private MagicLinkService underTest;
    private final Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        underTest = new MagicLinkServiceImpl(magicLinks, organiserService, mailService);
    }

    @Test
    void create_ItShould_CreateNewLinkAndSendEmail_GivenOrganiserId() {
        // given
        UUID organiserId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();
        MagicLink link = new MagicLink(organiser);

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        given(magicLinks.save(any(MagicLink.class)))
                .willReturn(link);

        // when
        underTest.create(organiserId);

        // then
        verify(magicLinks).save(any(MagicLink.class));

        ArgumentCaptor<Organiser> organiserArgumentCaptor = ArgumentCaptor.forClass(Organiser.class);
        ArgumentCaptor<MagicLink> magicLinkArgumentCaptor = ArgumentCaptor.forClass(MagicLink.class);
        verify(mailService).sendMagicLink(organiserArgumentCaptor.capture(), magicLinkArgumentCaptor.capture());

        assertThat(organiserArgumentCaptor.getValue()).isEqualTo(organiser);
        assertThat(magicLinkArgumentCaptor.getValue()).isEqualTo(link);
    }

    @Test
    void activate_ItShould_ActivateAndCloseLink_GivenOrganiserAndLinkId() {
        // given
        UUID organiserId = UUID.randomUUID();
        UUID linkId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();
        MagicLink link = new MagicLink(organiser);

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        given(magicLinks.findByIdAndOrganiser(linkId, organiser))
                .willReturn(Optional.of(link));
        // when
        underTest.activate(organiserId, linkId);

        // then
        ArgumentCaptor<MagicLink> magicLinkArgumentCaptor = ArgumentCaptor.forClass(MagicLink.class);
        verify(magicLinks).save(magicLinkArgumentCaptor.capture());

        MagicLink linkArg = magicLinkArgumentCaptor.getValue();
        assertThat(linkArg).isEqualTo(link);
        assertThat(linkArg.getStatus()).isEqualTo(LinkStatus.CLOSED);
    }

    @Test
    void activate_ItShouldThrow_NotFoundException_WhenNotExists() {
        // given
        UUID organiserId = UUID.randomUUID();
        UUID linkId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        given(magicLinks.findByIdAndOrganiser(linkId, organiser))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.activate(organiserId, linkId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Cannot find");
    }

    @Test
    void activate_ItShouldThrow_LinkException_WhenLinkIsExpired() {
        // given
        UUID organiserId = UUID.randomUUID();
        UUID linkId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();

        MagicLink link = new MagicLink(organiser);
        link.setExpiresAt(Instant.now().minus(1, ChronoUnit.HOURS));

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        given(magicLinks.findByIdAndOrganiser(linkId, organiser))
                .willReturn(Optional.of(link));

        // when
        // then
        assertThatThrownBy(() -> underTest.activate(organiserId, linkId))
                .isInstanceOf(LinkException.class)
                .hasMessageContaining("The link expired");
    }

    @Test
    void activate_ItShouldThrow_LinkException_WhenLinkAlreadyActivated() {
        // given
        UUID organiserId = UUID.randomUUID();
        UUID linkId = UUID.randomUUID();
        Organiser organiser = createTestOrganiser();

        MagicLink link = new MagicLink(organiser);
        link.setStatus(LinkStatus.CLOSED);

        given(organiserService.getOrganiserById(organiserId))
                .willReturn(organiser);

        given(magicLinks.findByIdAndOrganiser(linkId, organiser))
                .willReturn(Optional.of(link));

        // when
        // then
        assertThatThrownBy(() -> underTest.activate(organiserId, linkId))
                .isInstanceOf(LinkException.class)
                .hasMessageContaining("The link has already been activated");
    }

    private Organiser createTestOrganiser() {
        return new Organiser(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress()
        );
    }
}
package me.nerminsehic.groupevent.service;

import com.github.javafaker.Faker;
import me.nerminsehic.groupevent.entity.Organiser;
import me.nerminsehic.groupevent.exception.NotFoundException;
import me.nerminsehic.groupevent.exception.UniqueConstraintException;
import me.nerminsehic.groupevent.repository.Organisers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.util.Pair;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrganiserServiceImplTest {

    @Mock
    private Organisers organisers;

    @Mock
    private MagicLinkService magicLinkService;

    @Mock
    private Clock clock;

    private OrganiserService underTest;
    private final Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        underTest = new OrganiserServiceImpl(organisers, magicLinkService, clock);
    }

    @Test
    void findById_ItShould_FindOrganiserById() {
        // given
        UUID id = UUID.randomUUID();

        // when
        underTest.findById(id);

        // then
        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(organisers).findById(uuidArgumentCaptor.capture());

        UUID uuidArg = uuidArgumentCaptor.getValue();
        assertThat(uuidArg).isEqualTo(id);
    }

    @Test
    void findAll_ItShould_FindAllOrganisers() {
        // when
        underTest.findAll();

        // then
        verify(organisers).findAll();
    }

    @Test
    void create_ItShould_CreateNewOrganiser_WhenEmailNotExists() {
        // given
        String email = faker.internet().emailAddress();
        Organiser organiser = new Organiser(
                faker.name().firstName(),
                faker.name().lastName(),
                email
        );

        given(organisers.findByEmailAddress(email))
                .willReturn(Optional.empty());
        // when
        underTest.create(organiser);

        // then
        organiser.setUpdatedAt(Instant.now(clock));

        ArgumentCaptor<Organiser> organiserArgumentCaptor = ArgumentCaptor.forClass(Organiser.class);
        verify(organisers).save(organiserArgumentCaptor.capture());

        Organiser organiserArg = organiserArgumentCaptor.getValue();
        assertThat(organiserArg).isEqualTo(organiser);
    }

    @Test
    void create_ItShouldThrow_UniqueConstraintException_WhenEmailExists() {
        // given
        String email = faker.internet().emailAddress();
        Organiser organiser = new Organiser(
                faker.name().firstName(),
                faker.name().lastName(),
                email
        );

        given(organisers.findByEmailAddress(email))
                .willReturn(Optional.of(organiser));

        assertThatThrownBy(() -> underTest.create(organiser))
                .isInstanceOf(UniqueConstraintException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    void deleteById_ItShould_DeleteOrganiserById() {
        // given
        UUID id = UUID.randomUUID();

        // when
        underTest.deleteById(id);

        // then
        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(organisers).deleteById(uuidArgumentCaptor.capture());

        UUID uuidArg = uuidArgumentCaptor.getValue();
        assertThat(uuidArg).isEqualTo(id);
    }

    @Test
    void delete_ItShould_DeleteOrganiser() {
        // given
        Organiser organiser = new Organiser(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress()
        );

        // when
        underTest.delete(organiser);

        // then
        ArgumentCaptor<Organiser> organiserArgumentCaptor = ArgumentCaptor.forClass(Organiser.class);
        verify(organisers).delete(organiserArgumentCaptor.capture());

        Organiser organiserArg = organiserArgumentCaptor.getValue();
        assertThat(organiserArg).isEqualTo(organiser);
    }

    @Test
    void updateById_ItShould_UpdateOrganiserById_IfExists() {
        // given
        UUID id = UUID.randomUUID();
        Organiser currentOrganiser = new Organiser(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress()
        );
        Organiser newOrganiser = new Organiser(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress()
        );

        given(underTest.findById(id))
                .willReturn(Optional.of(currentOrganiser));

        // when
        underTest.updateById(id, newOrganiser);

        currentOrganiser.setFirstName(newOrganiser.getFirstName());
        currentOrganiser.setLastName(newOrganiser.getLastName());
        currentOrganiser.setEmailAddress(newOrganiser.getEmailAddress());
        currentOrganiser.setUpdatedAt(Instant.now(clock));

        // then
        ArgumentCaptor<Organiser> organiserArgumentCaptor = ArgumentCaptor.forClass(Organiser.class);
        verify(organisers).save(organiserArgumentCaptor.capture());

        Organiser organiserArg = organiserArgumentCaptor.getValue();
        assertThat(organiserArg).isEqualTo(currentOrganiser);
    }

    @Test
    void updateById_ItShouldThrow_NotFoundException_IfNotExists() {
        // given
        UUID id = UUID.randomUUID();
        Organiser newOrganiser = new Organiser(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress()
        );
        given(underTest.findById(id))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.updateById(id, newOrganiser))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Cannot find");
    }

    @Test
    void getOrganiserById_ItShould_GetOrganiserById_IfExists() {
        // given
        UUID id = UUID.randomUUID();
        Organiser organiser = new Organiser(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress()
        );

        given(underTest.findById(id))
                .willReturn(Optional.of(organiser));
        // when
        Organiser result = underTest.getOrganiserById(id);

        // then
        assertThat(result).isEqualTo(organiser);
    }

    @Test
    void getOrganiserById_ItShouldThrow_NotFoundException_IfNotExists() {
        // given
        UUID id = UUID.randomUUID();
        given(underTest.findById(id))
                .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getOrganiserById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Cannot find");
    }

    @Test
    void findOrCreateOrganiser_ItShould_FindOrganiser_IfExists() {
        // given
        String email = faker.internet().emailAddress();
        Organiser organiser = new Organiser(
                faker.name().firstName(),
                faker.name().lastName(),
                email
        );

        given(organisers.findByEmailAddress(email))
                .willReturn(Optional.of(organiser));

        // when
        Pair<Organiser, Boolean> result = underTest.findOrCreateOrganiser(organiser);

        // then
        assertThat(result.getFirst()).isEqualTo(organiser);
        assertThat(result.getSecond()).isFalse();
    }

    @Test
    void findOrCreateOrganiser_ItShould_CreateNewOrganiser_IfNotExists() {
        // given
        String email = faker.internet().emailAddress();
        Organiser organiser = new Organiser(
                faker.name().firstName(),
                faker.name().lastName(),
                email
        );

        given(organisers.findByEmailAddress(email))
                .willReturn(Optional.empty());

        given(organisers.save(organiser))
                .willReturn(organiser);

        // when
        Pair<Organiser, Boolean> result = underTest.findOrCreateOrganiser(organiser);
        organiser.setUpdatedAt(Instant.now(clock));

        // then
        ArgumentCaptor<Organiser> organiserArgumentCaptor = ArgumentCaptor.forClass(Organiser.class);
        verify(organisers).save(organiserArgumentCaptor.capture());

        Organiser organiserArg = organiserArgumentCaptor.getValue();
        assertThat(organiserArg).isEqualTo(organiser);
        assertThat(result.getFirst()).isEqualTo(organiser);
        assertThat(result.getSecond()).isTrue();
    }

    @Test
    void attemptLogin_ItShould_CreateLink_IfOrganiserExists() {
        // given
        String email = faker.internet().emailAddress();
        Organiser organiser = new Organiser(
                faker.name().firstName(),
                faker.name().lastName(),
                email
        );
        given(organisers.findByEmailAddress(email))
                .willReturn(Optional.of(organiser));

        // when
        boolean isCreated = underTest.attemptLogin(organiser);

        // then
        ArgumentCaptor<Organiser> organiserArgumentCaptor = ArgumentCaptor.forClass(Organiser.class);
        verify(magicLinkService).create(organiserArgumentCaptor.capture());

        assertThat(organiserArgumentCaptor.getValue()).isEqualTo(organiser);
        assertThat(isCreated).isFalse();
    }

    @Test
    void attemptLogin_ItShould_CreateLink_IfOrganiserNotExists() {
        // given
        String email = faker.internet().emailAddress();
        Organiser organiser = new Organiser(
                faker.name().firstName(),
                faker.name().lastName(),
                email
        );
        given(organisers.findByEmailAddress(email))
                .willReturn(Optional.empty());

        given(organisers.save(any(Organiser.class)))
                .willReturn(organiser);

        // when
        boolean isCreated = underTest.attemptLogin(organiser);

        // then
        verify(magicLinkService).create(any(Organiser.class));
        assertThat(isCreated).isTrue();
    }
}
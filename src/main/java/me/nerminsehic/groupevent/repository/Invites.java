package me.nerminsehic.groupevent.repository;

import me.nerminsehic.groupevent.entity.Invite;
import me.nerminsehic.groupevent.entity.key.InviteCompositeKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Invites extends JpaRepository<Invite, InviteCompositeKey> {}

package by.grsu.eventlink.repository;

import by.grsu.eventlink.entity.Invitation;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.Optional;

public interface InvitationRepository extends CrudRepository<Invitation, Long> {

    Optional<Invitation> getInvitationByEmail(String email);

    Boolean existsByEmailAndExpirationTimeAfter(String email, Date expirationTime);

    Optional<Invitation> getInvitationByEmailAndExpirationTimeAfter(String email, Date expirationTime);

}

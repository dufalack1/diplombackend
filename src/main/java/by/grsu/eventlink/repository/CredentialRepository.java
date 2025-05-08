package by.grsu.eventlink.repository;

import by.grsu.eventlink.entity.Credential;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CredentialRepository extends CrudRepository<Credential, Long> {

    @Query(
            value = """
            select crd.email 
            from credentials as crd
            where crd.id = :id
            """,
            nativeQuery = true
    )
    Optional<String> getCredentialEmailById(Long id);

    Boolean existsByEmail(String email);

    @EntityGraph(attributePaths = { "user", "roles" })
    Optional<Credential> getCredentialByEmail(String email);

    @Query(
            value = """
            select cr.email
            from credentials as cr
            left join users as us
            on us.credentials_id = cr.id
            where us.id = :userId
            """,
            nativeQuery = true
    )
    Optional<String> getCredentialEmailByUserId(Long userId);

}

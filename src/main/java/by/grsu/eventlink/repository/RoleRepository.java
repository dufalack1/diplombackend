package by.grsu.eventlink.repository;

import by.grsu.eventlink.entity.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {

    @EntityGraph(attributePaths = { "credentials" })
    Optional<Role> getRoleByTitle(String title);

}

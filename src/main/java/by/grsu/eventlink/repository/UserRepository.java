package by.grsu.eventlink.repository;

import by.grsu.eventlink.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    @Query(
            value = """
            select case when count(*)> 0
            then true else false end
            from users as urs
            left join credentials as crs
            on urs.credentials_id = crs.id
            where crs.email = :email
            """,
            nativeQuery = true
    )
    Boolean existsByEmail(String email);

    @EntityGraph(attributePaths = { "credentials" })
    Optional<User> getUserById(Long id);

    @Override
    @EntityGraph(attributePaths = { "likedCategories" })
    Optional<User> findById(Long aLong);

    @Query(
            value = """
            select us.id from users as us
            left join credentials as cr
            on us.credentials_id = cr.id
            where cr.email = :email
            """,
            nativeQuery = true
    )
    Optional<Long> getUserIdByEmail(String email);

    Boolean existsByDisplayedNameIgnoreCase(String displayedName);

    @Query(
            value = """
            select case when count(*)> 0
            then true else false end
            from users_categories as urs
            where urs.users_id = :userId
            and urs.categories_id = :categoryId
            """,
            nativeQuery = true
    )
    Boolean isUserLikedCategoryById(Long userId, Long categoryId);

    Optional<User> findByCredentials_Email(String credentials_email);

    List<User> findUsersByDisplayedNameContainingIgnoreCase(String displayedName, Pageable pageable);

    @Query(
            value = """
            select case when count(*)> 0
            then true else false end
            from users as usr
            left join credentials as crs
            on usr.credentials_id = crs.id
            where usr.displayed_name = :displayedName or
            usr.phone_number = :phoneNumber or crs.email = :email
            """,
            nativeQuery = true
    )
    Boolean existsByEmailOrDisplayedNameOrPhoneNumber(String email, String displayedName, String phoneNumber);

}

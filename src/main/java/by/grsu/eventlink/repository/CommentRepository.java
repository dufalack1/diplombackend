package by.grsu.eventlink.repository;

import by.grsu.eventlink.entity.Comment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    @Override
    @EntityGraph(attributePaths = { "author" })
    Optional<Comment> findById(Long id);

    @EntityGraph(attributePaths = { "author" })
    List<Comment> getCommentsByNode_Id(Long nodeId);

    @Query(
            value = """
            select case when count(*)> 0
            then true else false end
            from comments as com
            left join users u on u.id = com.users_id
            left join credentials c on c.id = u.credentials_id
            where com.id = :id
            and c.email = :email
            """,
            nativeQuery = true
    )
    Boolean isUserAuthoredByEmail(Long id, String email);

    Optional<Comment> getByMessageAndNode_IdAndAuthor_Id(String message, Long node_id, Long author_id);

}

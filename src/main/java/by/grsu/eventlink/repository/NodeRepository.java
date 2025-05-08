package by.grsu.eventlink.repository;

import by.grsu.eventlink.entity.Node;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface NodeRepository extends CrudRepository<Node, Long> {

    @EntityGraph(attributePaths = { "owner", "subCategory", "joinedUsers" })
    Optional<Node> getNodeById(Long id);

    @Query(
            value = """
            delete from users_nodes
            where nodes_id = :nodeId and users_id = :userId
            """,
            nativeQuery = true
    )
    @Modifying
    void removeJoinedUser(Long nodeId, Long userId);

    @Query(
            value = """
            select case when count(*)> 0
            then true else false end
            from users_nodes
            where users_id = :userId and nodes_id = :nodeId
            """,
            nativeQuery = true
    )
    Boolean isUserJoined(Long nodeId, Long userId);

    @Query(
            value = """
            select case when count(*)> 0
            then true else false end
            from nodes as nds
            left join users as usrs
            on nds.users_id = usrs.id
            left join credentials as creds
            on creds.id = usrs.credentials_id
            where creds.email = :email
            and nds.id = :nodeId
            """,
            nativeQuery = true
    )
    Boolean isOwnerByEmail(String email, Long nodeId);

    @Query(
            value = """
            select * from nodes as nds
            left join users_nodes un on nds.id = un.nodes_id
            where un.users_id = :userId
            """,
            nativeQuery = true
    )
    List<Node> getNodesUserJoined(Long userId);

    @Query(
            value = """
            select * from nodes as nds
            where nds.users_id = :userId
            """,
            nativeQuery = true
    )
    List<Node> getNodesOwnedByUser(Long userId);

    @Query(
            value = """
            select case when count(*)> 0
            then true else false end
            from nodes as nds
            left join users as ow on nds.users_id = ow.id
            left join credentials as ocr on ocr.id = ow.credentials_id
            left join users_nodes as unds on nds.id = unds.nodes_id
            left join users as usrs on unds.users_id = usrs.id
            left join credentials as creds on creds.id = usrs.credentials_id
            where (creds.email = :email
            and unds.nodes_id = :nodeId)
            or (ocr.email = :email
            and nds.id = :nodeId)
            """,
            nativeQuery = true
    )
    Boolean isJoinedOrOwnerByEmail(String email, Long nodeId);

    Page<Node> findNodesBySubCategory_Id(Long subCategory_id, Pageable pageable);

    Page<Node> findNodesBySubCategory_IdAndTitleContainingIgnoreCase(Long subCategory_id, String title,
                                                                     Pageable pageable);

    Page<Node> findNodesBySubCategory_IdAndDescriptionContainingIgnoreCase(Long subCategory_id, String description,
                                                                           Pageable pageable);

    Page<Node> findNodesByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description,
                                                                                     Pageable pageable);

}

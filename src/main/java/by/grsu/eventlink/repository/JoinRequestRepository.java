package by.grsu.eventlink.repository;

import by.grsu.eventlink.entity.JoinRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface JoinRequestRepository extends CrudRepository<JoinRequest, Long> {

    @EntityGraph(attributePaths = { "node" })
    List<JoinRequest> findAllByInvited_Id(Long invited_id);

    @EntityGraph(attributePaths = { "invited", "node" })
    Optional<JoinRequest> findByInvited_IdAndNode_Id(Long invited_id, Long node_id);

    @EntityGraph(attributePaths = { "invited", "node" })
    List<JoinRequest> findAllByNode_Owner_Id(Long node_owner_id);

    default List<JoinRequest> findAllByNodeOwnerId(Long nodeOwnerId) {
        return findAllByNode_Owner_Id(nodeOwnerId);
    }

    default Optional<JoinRequest> findByInvitedIdAndNodeId(Long invitedId, Long nodeId) {
        return findByInvited_IdAndNode_Id(invitedId, nodeId);
    }

    default List<JoinRequest> findAllByInvitedId(Long invitedId) {
        return findAllByInvited_Id(invitedId);
    }

}

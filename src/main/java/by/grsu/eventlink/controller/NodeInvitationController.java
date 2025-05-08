package by.grsu.eventlink.controller;

import by.grsu.eventlink.dto.nodeinvitation.NodeInvitationDto;
import by.grsu.eventlink.service.NodeInvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/requests")
public class NodeInvitationController {

    private final NodeInvitationService nodeInvitationService;

    @GetMapping("{userId}")
    @PreAuthorize("@accessManagerService.isAuthorisedAdmittedToUser(#userId, authentication)")
    public List<NodeInvitationDto> getInvitationsByUserId(@PathVariable Long userId) {
        return nodeInvitationService.getNodeInvitationsByUserId(userId);
    }

}

package by.grsu.eventlink.service.impl;

import by.grsu.eventlink.entity.enums.RoleHelper;
import by.grsu.eventlink.repository.CommentRepository;
import by.grsu.eventlink.repository.CredentialRepository;
import by.grsu.eventlink.repository.NodeRepository;
import by.grsu.eventlink.security.jwt.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service("accessManagerService")
public class AccessManagerServiceImpl {

    private final NodeRepository nodeRepository;

    private final CommentRepository commentRepository;

    private final CredentialRepository credentialRepository;

    public Boolean isAuthorisedAdmittedToComment(Long commentId, Authentication authentication) {
        if (isAllowedByRole(List.of(RoleHelper.MODERATOR, RoleHelper.ADMIN), authentication)) {
            return Boolean.TRUE;
        }

        if (isBlocked(authentication)) {
            return Boolean.FALSE;
        }

        return commentRepository.isUserAuthoredByEmail(commentId, authentication.getName());
    }

    public Boolean isAuthorisedAdmin(Authentication authentication) {
        return isAllowedByRole(RoleHelper.ADMIN, authentication);
    }

    public Boolean isAuthorisedModerator(Authentication authentication) {
        return isAllowedByRole(RoleHelper.MODERATOR, authentication);
    }

    public Boolean isAuthorisedAdmittedToCategory(Authentication authentication) {
        return isAllowedByRole(List.of(RoleHelper.MODERATOR, RoleHelper.ADMIN), authentication);
    }

    public Boolean isAuthorisedAdmittedToUser(Long userId, Authentication authentication) {
        if (isAllowedByRole(RoleHelper.ADMIN, authentication)) {
            return Boolean.TRUE;
        }

        if (isBlocked(authentication)) {
            return Boolean.FALSE;
        }
        final String currentUserEmail = credentialRepository.getCredentialEmailByUserId(userId)
                .orElse(Strings.EMPTY);
        log.info("Почта пользователя {}", authentication.getName());//anonymousUser
        log.info("Почта пользователя {}", currentUserEmail);

        return currentUserEmail.equals(authentication.getName());
    }

    public Boolean isAuthorisedAdmittedToNode(Long nodeId, Authentication authentication) {
        log.info("Checking access for node {} and user {}", nodeId, authentication.getName());
        if (isAllowedByRole(List.of(RoleHelper.MODERATOR, RoleHelper.ADMIN), authentication)) {
            return Boolean.TRUE;
        }

        if (isBlocked(authentication)) {
            return Boolean.FALSE;
        }

        return nodeRepository.isOwnerByEmail(authentication.getName(), nodeId);
    }

    private Boolean isAllowedByRole(RoleHelper allowedRole, Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals(velcro(allowedRole.getTitle())));
    }

    private Boolean isAllowedByRole(List<RoleHelper> allowedRoles, Authentication authentication) {
        List<String> allowed = allowedRoles.stream()
                .map(role -> velcro(role.getTitle()))
                .toList();

        return authentication.getAuthorities().stream()
                .anyMatch(role -> allowed.contains(role.getAuthority()));
    }

    private static String velcro(String roleTitle) {
        return SecurityUtils.ROLE_PREFIX + roleTitle;
    }

    private Boolean isBlocked(Authentication authentication) {
        return isAllowedByRole(RoleHelper.BLOCKED, authentication);
    }

}

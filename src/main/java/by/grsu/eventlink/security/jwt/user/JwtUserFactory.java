package by.grsu.eventlink.security.jwt.user;

import by.grsu.eventlink.entity.Credential;
import by.grsu.eventlink.entity.Role;
import by.grsu.eventlink.security.jwt.utils.SecurityUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtUserFactory {

    public static User jwtUserCreate(Credential credential) {
        return new User(
                credential.getEmail(),
                credential.getPassword(),
                mapToGrantedAuthority(new HashSet<>(credential.getRoles())));
    }

    public static List<GrantedAuthority> mapToGrantedAuthority(Set<Role> roleSet) {
        return roleSet.stream()
                .map(role ->
                        new SimpleGrantedAuthority(SecurityUtils.ROLE_PREFIX + role.getTitle()))
                .collect(Collectors.toList());

    }

}

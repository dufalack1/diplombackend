package by.grsu.eventlink.security.jwt.token;

import by.grsu.eventlink.configuration.properties.SecurityProperties;
import by.grsu.eventlink.entity.Role;
import by.grsu.eventlink.security.jwt.exception.AuthenticationFailed;
import by.grsu.eventlink.security.jwt.utils.SecurityUtils;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private String encodedSecretWord;

    private final SecurityProperties securityProperties;

    private final UserDetailsService userDetailsService;

    @PostConstruct
    protected void init() {
        encodedSecretWord = Base64.getEncoder().encodeToString(securityProperties.getSecretWord().getBytes());
    }

    public String createToken(String email, List<Role> roles) {

        Date expiration = Date.from(LocalDateTime.now()
                .plusSeconds(securityProperties.getTokenExpiration())
                .atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setSubject(email)
                .claim(SecurityUtils.ROLES_CLAIM, roles.stream().map(Role::getTitle).toList())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, encodedSecretWord)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, Strings.EMPTY, userDetails.getAuthorities());
    }

    public String getEmail(String token) {
        return Jwts.parser()
                .setSigningKey(encodedSecretWord)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader(SecurityUtils.AUTH_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(SecurityUtils.AUTH_TOKEN_PREFIX)) {

            return bearerToken.substring(SecurityUtils.AUTH_TOKEN_PREFIX.length());
        }

        return null;
    }
//public String resolveToken(HttpServletRequest req) {
//    // 1. Логируем все заголовки для отладки
//    log.info("Headers received:");
//    Collections.list(req.getHeaderNames())
//            .forEach(headerName -> log.info("{}: {}", headerName, req.getHeader(headerName)));
//
//    // 2. Получаем токен из заголовка
//    String bearerToken = req.getHeader(SecurityUtils.AUTH_HEADER);
//    log.info("Raw Authorization header: {}", bearerToken);
//
//    // 3. Проверяем наличие и формат токена
//    if (StringUtils.hasText(bearerToken)) {
//        // Удаляем лишние пробелы
//        bearerToken = bearerToken.trim();
//
//        // Проверяем префикс (без учёта регистра)
//        if (bearerToken.startsWith(SecurityUtils.AUTH_TOKEN_PREFIX)) {
//            String token = bearerToken.substring(SecurityUtils.AUTH_TOKEN_PREFIX.length());
//            log.info("Token extracted successfully");
//            return token;
//        } else {
//            log.warn("Invalid token prefix. Expected: {}", SecurityUtils.AUTH_TOKEN_PREFIX);
//        }
//    } else {
//        log.warn("Authorization header is missing or empty");
//    }
//
//    return null;
//}

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(encodedSecretWord).parseClaimsJws(token);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthenticationFailed();
        }
    }

}

package au.com.jc.authservice.config;

import au.com.jc.authservice.model.AuthUser;
import au.com.jc.authservice.repository.AuthUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Slf4j
@Configuration
@EnableMethodSecurity(prePostEnabled = false, jsr250Enabled = true)
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(CorsConfigurer::disable)
            .csrf(CsrfConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/actuator/health/**").permitAll()
                    .requestMatchers("/api/**").hasRole("USER")
                    .anyRequest().authenticated())
            .x509(x509 -> x509
                .subjectPrincipalRegex("CN=(.*?)(?:,|$)")
                .userDetailsService(userDetailsService()));
        return http.build();
    }

    @Autowired
    private AuthUserRepository authUserRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            if (StringUtils.isNotEmpty(username)) {
                List<AuthUser> authUser = authUserRepository.findByUsername(username);
                if (authUser.size() == 0) {
                    throw new UsernameNotFoundException("User not found!");
                }
                String authUsername = authUser
                        .stream()
                        .findFirst()
                        .get()
                        .getUsername();
                log.info("Username found as an authenticated user: {}", authUsername);
                return new User(authUsername,
                        "",
                        AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));
            }
            throw new UsernameNotFoundException("User not found!");
        };
    }

}

package sistema.login.seguridad;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/", 
                "/login", 
                "/register", 
                "/test-public",  // ← NUEVA RUTA DE PRUEBA
                "/css/**", 
                "/js/**", 
                "/images/**"
            ).permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")
            .loginProcessingUrl("/login")
            .usernameParameter("cedula")
            .passwordParameter("password")
            .defaultSuccessUrl("/reservas", true)
            .permitAll()
        )
        .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
        )
        .csrf(csrf -> csrf.disable());

    return http.build();
}
//       @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http
//             .authorizeHttpRequests(auth -> auth
//                 // PERMITIR TODO temporalmente para testing
//                 .anyRequest().permitAll()
//             )
//             .formLogin(form -> form
//                 .loginPage("/login")
//                 .loginProcessingUrl("/login")
//                 .usernameParameter("cedula")
//                 .passwordParameter("password")
//                 .defaultSuccessUrl("/reservas", true)
//             )
//             .logout(logout -> logout
//                 .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                 .logoutSuccessUrl("/login?logout")
//             )
//             .csrf(csrf -> csrf.disable());

//         return http.build();
//     }
// }
}
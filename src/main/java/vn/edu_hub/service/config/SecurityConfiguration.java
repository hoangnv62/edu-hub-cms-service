package vn.edu_hub.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;
import tech.jhipster.config.JHipsterProperties;
import vn.edu_hub.service.security.AuthoritiesConstants;
import vn.edu_hub.service.security.jwt.JWTConfigurer;
import vn.edu_hub.service.security.jwt.TokenProvider;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Import({SecurityProblemSupport.class})
public class SecurityConfiguration {
    private final JHipsterProperties jHipsterProperties;
    private final TokenProvider tokenProvider;
    private final SecurityProblemSupport securityProblemSupport;

    public SecurityConfiguration(JHipsterProperties jHipsterProperties, TokenProvider tokenProvider, SecurityProblemSupport securityProblemSupport) {
        this.jHipsterProperties = jHipsterProperties;
        this.tokenProvider = tokenProvider;
        this.securityProblemSupport = securityProblemSupport;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable) //được sử dụng để tắt bảo vệ CSRF (Cross-Site Request Forgery) trong ứng dụng Spring Security.
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(securityProblemSupport)
                        .accessDeniedHandler(securityProblemSupport)
                )
                .headers(header -> header
                        .addHeaderWriter(new StaticHeadersWriter("Content-Security-Policy", //thêm header CSP từ file application.yml dùng để chống tấn công XSS
                                jHipsterProperties.getSecurity().getContentSecurityPolicy()))
                        .referrerPolicy(referrer -> referrer
                                .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)) //Chỉ gửi origin (không path/query) khi cross-origin → bảo vệ referrer leak.
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                        // Ngăn clickjacking (iframe trang của bạn từ site khác).
                        // giống như gắn quản cáo shopee vào 1 hình ảnh nào đó, khi click vào hình ảnh thì thay vì xem hình ảnh thì lại chuyển đến trang shopee
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //Không tạo session → stateless hoàn toàn (JWT lưu ở client, không cookie).
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/authenticate").permitAll()
                        .requestMatchers("/api/admin/**").hasAnyAuthority(AuthoritiesConstants.ADMIN)
                        .requestMatchers("/api/users").permitAll()
                        .requestMatchers("/api/**").authenticated()
                )
                .with(securityConfigurerAdapter(), Customizer.withDefaults());
        return http.build();
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:3000","http://localhost:5173"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }
}

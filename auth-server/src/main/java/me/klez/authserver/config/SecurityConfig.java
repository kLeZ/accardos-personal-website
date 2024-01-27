/*
 * Copyright © 2024 Alessandro Accardo a.k.a. kLeZ <julius8774@gmail.com>
 * This file is part of AAccardo Personal WebSite.
 *
 * AAccardo Personal WebSite is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AAccardo Personal WebSite is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AAccardo Personal WebSite.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package me.klez.authserver.config;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import me.klez.authserver.filter.AuthenticationFailureHandlerImpl;
import me.klez.authserver.filter.AuthenticationSuccessHandlerImpl;
import me.klez.authserver.filter.PreloginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.*;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static me.klez.authserver.config.ApplicationProperties.*;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SecurityConfig {
	ApplicationProperties applicationProperties;

	@Bean
	@Order(1)
	SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
		OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
		http.getConfigurer(OAuth2AuthorizationServerConfigurer.class).oidc(Customizer.withDefaults());
		http.cors(Customizer.withDefaults())
				.csrf(Customizer.withDefaults())
				.exceptionHandling(exceptions -> exceptions.defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/login"), new MediaTypeRequestMatcher(MediaType.TEXT_HTML)))
				.oauth2ResourceServer(resourceServer -> resourceServer.jwt(Customizer.withDefaults()));
		return http.build();
	}

	@Bean
	@Order(2)
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		http.cors(Customizer.withDefaults())
				.csrf(Customizer.withDefaults())
				.authorizeHttpRequests(authorize -> authorize.requestMatchers(Stream.of(STATIC_RESOURCES, PUBLIC_URIS).flatMap(Stream::of).toArray(String[]::new))
						.permitAll()
						.anyRequest()
						.authenticated())
				.addFilterBefore(new PreloginFilter(), UsernamePasswordAuthenticationFilter.class)
				.formLogin(formLogin -> formLogin.loginPage("/login")
						.successHandler(new AuthenticationSuccessHandlerImpl())
						.failureHandler(new AuthenticationFailureHandlerImpl())
						.permitAll())
				.logout(logout -> logout.logoutUrl("/logout").clearAuthentication(true).invalidateHttpSession(true).deleteCookies(COOKIE_NAMES).permitAll());
		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(List.of("*"));
		configuration.setAllowedMethods(List.of("*"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		String idForEncode = "bcrypt";
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put(null, new BCryptPasswordEncoder(applicationProperties.getPasswordStrength()));
		encoders.put(idForEncode, new BCryptPasswordEncoder(applicationProperties.getPasswordStrength()));
		//noinspection deprecation
		encoders.put("noop", NoOpPasswordEncoder.getInstance());
		return new DelegatingPasswordEncoder(idForEncode, encoders);
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource() {
		byte[] key = applicationProperties.getSecretKey().getBytes(StandardCharsets.UTF_8);
		SecretKeySpec keySpec = new SecretKeySpec(key, applicationProperties.getSecretKeySpecAlgorithm());
		OctetSequenceKey jwk = new OctetSequenceKey.Builder(keySpec).algorithm(JWSAlgorithm.parse(applicationProperties.getJwsAlgorithm())).build();
		JWKSet jwkSet = new JWKSet(jwk);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	@Bean
	public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
		return new NimbusJwtEncoder(jwkSource);
	}

	@Bean
	public UserDetailsChecker userDetailsChecker() {
		return new AccountStatusUserDetailsChecker();
	}

	@Bean
	public LogoutHandler logoutHandler() {
		return new CompositeLogoutHandler(new CookieClearingLogoutHandler(COOKIE_NAMES), new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(Directive.ALL)), new SecurityContextLogoutHandler());
	}
}

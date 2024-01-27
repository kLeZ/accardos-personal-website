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
package me.klez.authserver.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import me.klez.authserver.config.ApplicationProperties;
import me.klez.authserver.model.Oauth2ClientRole;
import me.klez.authserver.model.Oauth2ClientRoleId;
import me.klez.authserver.repository.Oauth2ClientRoleRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenCustomizerServiceImpl implements OAuth2TokenCustomizer<JwtEncodingContext> {
	Oauth2ClientRoleRepository repository;
	ApplicationProperties applicationProperties;

	@Override
	public void customize(JwtEncodingContext context) {
		context.getJwsHeader().algorithm(applicationProperties.jwsAlgorithm());
		Authentication authentication = context.getPrincipal();
		Authentication authorizationGrant = context.getAuthorizationGrant();
		if (OAuth2TokenType.ACCESS_TOKEN.equals(context.getTokenType())) {
			if (context.getAuthorizationGrantType() == AuthorizationGrantType.AUTHORIZATION_CODE) {
				UserDetails principal = (UserDetails) authentication.getPrincipal();
				Set<GrantedAuthority> ofmAuthorities = repository.findByUsernameAndClientId(principal.getUsername(), authorizationGrant.getName())
						.stream()
						.map(Oauth2ClientRole::getId)
						.map(Oauth2ClientRoleId::getRoleCode)
						.map(SimpleGrantedAuthority::new)
						.collect(Collectors.toSet());
				Set<String> authorities = ofmAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
				context.getClaims().claim("authorities", authorities);
				context.getClaims().claims(claims -> claims.putAll(claimsMap(principal)));
			} else if (context.getAuthorizationGrantType() == AuthorizationGrantType.CLIENT_CREDENTIALS) {
				Set<Oauth2ClientRole> ofmAuthorities = repository.findById_ClientId(authentication.getName());
				Set<String> authorities = ofmAuthorities.stream()
						.map(Oauth2ClientRole::getId)
						.map(Oauth2ClientRoleId::getRoleCode)
						.collect(Collectors.toSet());
				context.getClaims().claim("authorities", authorities);
			}
		}
	}

	private Map<String, Object> claimsMap(UserDetails principal) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("user_id", principal.getId());
		claims.put("user_name", principal.getUsername());
		claims.put("name", principal.getName());
		claims.put("surname", principal.getSurname());
		claims.put("email", principal.getEmail());
		claims.put("telephone_number", principal.getTelephoneNumber());
		return claims.entrySet().parallelStream().filter(entry -> {
			if (entry.getValue() instanceof String s) {
				return StringUtils.isNotBlank(s);
			} else {
				return entry.getValue() != null;
			}
		}).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
}

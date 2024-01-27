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
package me.klez.authserver.repository;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import me.klez.authserver.model.OAuth2ClientDetails;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

@Repository
public class JpaRegisteredClientRepository implements RegisteredClientRepository {
	private final OAuth2ClientDetailsRepository repository;

	public JpaRegisteredClientRepository(OAuth2ClientDetailsRepository repository) {
		this.repository = repository;
	}

	private static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
		if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equalsIgnoreCase(authorizationGrantType)) {
			return AuthorizationGrantType.AUTHORIZATION_CODE;
		} else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equalsIgnoreCase(authorizationGrantType)) {
			return AuthorizationGrantType.CLIENT_CREDENTIALS;
		} else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equalsIgnoreCase(authorizationGrantType)) {
			return AuthorizationGrantType.REFRESH_TOKEN;
		} else if (AuthorizationGrantType.DEVICE_CODE.getValue().equalsIgnoreCase(authorizationGrantType)) {
			return AuthorizationGrantType.DEVICE_CODE;
		}
		return new AuthorizationGrantType(authorizationGrantType);
	}

	private static ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
		if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equalsIgnoreCase(clientAuthenticationMethod)) {
			return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
		} else if (ClientAuthenticationMethod.CLIENT_SECRET_JWT.getValue().equalsIgnoreCase(clientAuthenticationMethod)) {
			return ClientAuthenticationMethod.CLIENT_SECRET_JWT;
		} else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equalsIgnoreCase(clientAuthenticationMethod)) {
			return ClientAuthenticationMethod.CLIENT_SECRET_POST;
		} else if (ClientAuthenticationMethod.PRIVATE_KEY_JWT.getValue().equalsIgnoreCase(clientAuthenticationMethod)) {
			return ClientAuthenticationMethod.PRIVATE_KEY_JWT;
		} else if (ClientAuthenticationMethod.NONE.getValue().equalsIgnoreCase(clientAuthenticationMethod)) {
			return ClientAuthenticationMethod.NONE;
		}
		return new ClientAuthenticationMethod(clientAuthenticationMethod);
	}

	@Nonnull
	private static List<ClientAuthenticationMethod> resolveClientAuthenticationMethods(OAuth2ClientDetails entity) {
		return entity.getClientAuthenticationMethods().stream().map(JpaRegisteredClientRepository::resolveClientAuthenticationMethod).toList();
	}

	@Nonnull
	private static List<AuthorizationGrantType> resolveAuthorizationGrantTypes(OAuth2ClientDetails entity) {
		return entity.getAuthorizationGrantTypes().stream().map(JpaRegisteredClientRepository::resolveAuthorizationGrantType).toList();
	}

	@Nonnull
	private static TokenSettings buildTokenSettings(OAuth2ClientDetails entity) {
		return TokenSettings.builder()
				.authorizationCodeTimeToLive(Duration.ofMinutes(entity.getAuthorizationCodeValidity()))
				.accessTokenTimeToLive(Duration.ofMinutes(entity.getAccessTokenValidity()))
				.refreshTokenTimeToLive(Duration.ofMinutes(entity.getRefreshTokenValidity()))
				.build();
	}

	@Nonnull
	private static ClientSettings buildClientSettings(OAuth2ClientDetails entity) {
		return ClientSettings.builder().requireAuthorizationConsent(entity.getRequireConsent()).build();
	}

	@Nonnull
	private static <T> Consumer<Set<T>> addAllToSet(List<T> list) {
		return set -> set.addAll(list);
	}

	@Nullable
	private static LocalDateTime getLocalDateTime(Instant instant) {
		return Optional.ofNullable(instant).map(temporal -> LocalDateTime.ofInstant(temporal, ZoneOffset.UTC)).orElse(null);
	}

	/**
	 * Saves the registered client.
	 *
	 * <p>
	 * IMPORTANT: Sensitive information should be encoded externally from the implementation, e.g. {@link RegisteredClient#getClientSecret()}
	 *
	 * @param registeredClient the {@link RegisteredClient}
	 */
	@Override
	public void save(RegisteredClient registeredClient) {
		Optional<OAuth2ClientDetails> entity = repository.findById(registeredClient.getId());
		if (entity.isPresent()) {
			repository.save(toEntity(registeredClient, entity.get()));
		} else {
			repository.save(toEntity(registeredClient));
		}
	}

	/**
	 * Returns the registered client identified by the provided {@code id}, or {@code null} if not found.
	 *
	 * @param id the registration identifier
	 * @return the {@link RegisteredClient} if found, otherwise {@code null}
	 */
	@Override
	public RegisteredClient findById(String id) {
		return toModel(repository.findById(id));
	}

	/**
	 * Returns the registered client identified by the provided {@code clientId}, or {@code null} if not found.
	 *
	 * @param clientId the client identifier
	 * @return the {@link RegisteredClient} if found, otherwise {@code null}
	 */
	@Override
	public RegisteredClient findByClientId(String clientId) {
		return toModel(repository.findByClientId(clientId));
	}

	private OAuth2ClientDetails toEntity(RegisteredClient model) {
		OAuth2ClientDetails ret;
		if (model != null) {
			ret = toEntity(model, new OAuth2ClientDetails());
		} else {
			ret = null;
		}
		return ret;
	}

	private OAuth2ClientDetails toEntity(@Nonnull RegisteredClient model, @Nonnull OAuth2ClientDetails entity) {
		entity.setId(model.getId());
		entity.setClientId(model.getClientId());
		entity.setClientIdIssuedAt(getLocalDateTime(model.getClientIdIssuedAt()));
		entity.setClientSecret(model.getClientSecret());
		entity.setClientSecretExpiresAt(getLocalDateTime(model.getClientSecretExpiresAt()));
		entity.setClientName(model.getClientName());
		entity.setClientAuthenticationMethods(model.getClientAuthenticationMethods().stream().map(ClientAuthenticationMethod::getValue).toList());
		entity.setAuthorizationGrantTypes(model.getAuthorizationGrantTypes().stream().map(AuthorizationGrantType::getValue).toList());
		entity.setRedirectUris(model.getRedirectUris().stream().toList());
		entity.setPostLogoutRedirectUris(model.getPostLogoutRedirectUris().stream().toList());
		entity.setScopes(model.getScopes().stream().toList());
		entity.setRequireConsent(model.getClientSettings().isRequireAuthorizationConsent());
		entity.setAuthorizationCodeValidity(model.getTokenSettings().getAuthorizationCodeTimeToLive().toMinutes());
		entity.setAccessTokenValidity(model.getTokenSettings().getAccessTokenTimeToLive().toMinutes());
		entity.setRefreshTokenValidity(model.getTokenSettings().getRefreshTokenTimeToLive().toMinutes());
		return entity;
	}

	private RegisteredClient toModel(Optional<OAuth2ClientDetails> entity) {
		RegisteredClient ret;
		if (entity.isPresent()) {
			OAuth2ClientDetails clientDetails = entity.get();
			ret = RegisteredClient.withId(clientDetails.getId())
					.clientId(clientDetails.getClientId())
					.clientIdIssuedAt(getInstant(clientDetails.getClientIdIssuedAt()))
					.clientSecret(clientDetails.getClientSecret())
					.clientSecretExpiresAt(getInstant(clientDetails.getClientSecretExpiresAt()))
					.clientName(clientDetails.getClientName())
					.clientAuthenticationMethods(addAllToSet(resolveClientAuthenticationMethods(clientDetails)))
					.authorizationGrantTypes(addAllToSet(resolveAuthorizationGrantTypes(clientDetails)))
					.redirectUris(addAllToSet(clientDetails.getRedirectUris()))
					.postLogoutRedirectUris(addAllToSet(clientDetails.getPostLogoutRedirectUris()))
					.scopes(addAllToSet(clientDetails.getScopes()))
					.clientSettings(buildClientSettings(clientDetails))
					.tokenSettings(buildTokenSettings(clientDetails))
					.build();
		} else {
			ret = null;
		}
		return ret;
	}

	private Instant getInstant(LocalDateTime localDateTime) {
		return Optional.ofNullable(localDateTime).map(ldt -> ldt.toInstant(ZoneOffset.UTC)).orElse(null);
	}
}

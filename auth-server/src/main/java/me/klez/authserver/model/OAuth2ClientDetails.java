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
package me.klez.authserver.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "oauth2_client_details")
public class OAuth2ClientDetails {
	@Id
	@Column(name = "id", nullable = false, unique = true, length = 36)
	private String id;
	@Column(name = "client_id")
	private String clientId;
	@Column(name = "client_secret")
	private String clientSecret;
	@Column(name = "scopes")
	@Convert(converter = CsvTrimmedStringsConverter.class)
	private List<String> scopes;
	@Column(name = "authorization_grant_types")
	@Convert(converter = CsvTrimmedStringsConverter.class)
	private List<String> authorizationGrantTypes;
	@Column(name = "redirect_uris")
	@Convert(converter = CsvTrimmedStringsConverter.class)
	private List<String> redirectUris;
	@Column(name = "authorization_code_validity")
	private Long authorizationCodeValidity;
	@Column(name = "access_token_validity")
	private Long accessTokenValidity;
	@Column(name = "refresh_token_validity")
	private Long refreshTokenValidity;
	@Column(name = "require_consent")
	private Boolean requireConsent;
	@Column(name = "client_id_issued_at")
	private LocalDateTime clientIdIssuedAt;
	@Column(name = "client_secret_expires_at")
	private LocalDateTime clientSecretExpiresAt;
	@Column(name = "client_name")
	private String clientName;
	@Column(name = "client_authentication_methods")
	@Convert(converter = CsvTrimmedStringsConverter.class)
	private List<String> clientAuthenticationMethods;
	@Column(name = "post_logout_redirect_uris")
	@Convert(converter = CsvTrimmedStringsConverter.class)
	private List<String> postLogoutRedirectUris;
}

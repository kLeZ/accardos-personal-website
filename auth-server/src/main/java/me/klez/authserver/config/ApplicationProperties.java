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

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class ApplicationProperties {
	public static final String[] STATIC_RESOURCES = {"/webjars/**", "/images/**", "/css/**", "/assets/**", "/favicon.ico", "/error"};
	public static final String[] PUBLIC_URIS = {"/recoveryPassword", "/resetPassword/**", "/changePassword/**"};
	public static final String[] COOKIE_NAMES = {"token", "commessa"};
	@Value("${spring.authorization-server.password-encoder.strength}")
	private int passwordStrength;
	@Value("${spring.authorization-server.secret.key}")
	private String secretKey;
	@Value("${spring.authorization-server.secret.algorithm.secret-key-spec}")
	private String secretKeySpecAlgorithm;
	@Value("${spring.authorization-server.secret.algorithm.json-web-signature}")
	private String jwsAlgorithm;
	@Value("${spring.authorization-server.issuer}")
	private String issuer;
	@Value("${user.registration.mail.subject}")
	private String userRegistrationMailSubject;
	@Value("${user.registration.mail.body}")
	private String userRegistrationMailBody;
	@Value("${user.recoveryPassword.mail.subject}")
	private String userRecoveryPasswordMailSubject;
	@Value("${user.recoveryPassword.mail.body}")
	private String userRecoveryPasswordMailBody;
	@Value("${user.registration.token_temporary.validity_seconds}")
	private Integer tokenValiditySecondsForRegistration;
	@Value("${user.recoveryPassword.token_temporary.validity_seconds}")
	private Integer tokenValiditySecondsForRecoveryPassword;
	@Value("${user.logout.redirectTimeout}")
	private Integer logoutRedirectTimeout;

	public JwsAlgorithm jwsAlgorithm() {
		return jwsAlgorithm(getJwsAlgorithm());
	}

	private JwsAlgorithm jwsAlgorithm(String signingAlgorithm) {
		String name = signingAlgorithm.toUpperCase();
		JwsAlgorithm jwsAlgorithm = SignatureAlgorithm.from(name);
		if (jwsAlgorithm == null) {
			jwsAlgorithm = MacAlgorithm.from(name);
		}
		return jwsAlgorithm;
	}
}

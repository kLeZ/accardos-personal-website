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

import com.nimbusds.jose.util.Base64;
import me.klez.authserver.config.ApplicationProperties;
import me.klez.authserver.exception.*;
import me.klez.authserver.model.User;
import me.klez.authserver.repository.UserRepository;
import me.klez.authserver.util.Util;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
	private final MailService mailService;
	private final PasswordService passwordService;
	private final ApplicationProperties appProperties;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UserService(MailService mailService, PasswordService passwordService, ApplicationProperties appProperties, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.mailService = mailService;
		this.passwordService = passwordService;
		this.appProperties = appProperties;
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public void changePassword(String username, String currentPassword, String newPassword) throws ChangePasswordException {
		Optional<User> userFromUsername = userRepository.findByUsername(username);
		final User user = userFromUsername.orElseThrow(() -> new ChangePasswordException("Utente non trovato"));
		if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
			throw new ChangePasswordException("Password corrente errata");
		}
		passwordService.checkPassword(newPassword);
		String newHashedPwd = passwordEncoder.encode(newPassword);
		user.setPassword(newHashedPwd);
		userRepository.save(user);
		if (user.getEmailAddress() != null) {
			mailService.sendASynchronousMail(user.getEmailAddress(), "Notifica di Aggiornamento Password", "Buongiorno, è stato effettuato un aggiornamento della password. In caso non si sia a conoscenza dell'operazione, contattare un operatore.");
		}
	}

	public void checkTokenTemporary(String token) throws TokenNotFoundException, InvalidTokenException {
		Optional<User> tokenTemporary = userRepository.findByTemporaryToken(token);
		final User user = tokenTemporary.orElseThrow(() -> new TokenNotFoundException("Token non trovato"));
		final long secondsDiffBetweenDates = Util.getSecondsDiffBetweenDates(user.getTokenCreationDate(), LocalDateTime.now());
		long validityInSeconds = secondsDiffBetweenDates;
		if (token.startsWith("R_")) {
			validityInSeconds = appProperties.getTokenValiditySecondsForRegistration();
		} else if (token.startsWith("C_")) {
			validityInSeconds = appProperties.getTokenValiditySecondsForRecoveryPassword();
		}
		if (secondsDiffBetweenDates > validityInSeconds) {
			throw new InvalidTokenException("Token non valido");
		}
	}

	public void resetPassword(String newPassword, String token) throws UserNotFoundException, ChangePasswordException {
		passwordService.checkPassword(newPassword);
		Optional<User> userFromTokenTemporary = userRepository.findByTemporaryToken(token);
		final User user = userFromTokenTemporary.orElseThrow(() -> new UserNotFoundException("Utente non trovato"));
		user.setPassword(passwordEncoder.encode(newPassword));
		user.setResetPassword(false);
		user.setTemporaryToken(null);
		user.setTokenCreationDate(null);
		user.setLastModifiedBy(user.getUsername());
		userRepository.save(user);
	}

	public void recoveryPassword(String email) throws UserNotFoundException, MailSenderException {
		Optional<User> userByEmail = userRepository.findByEmailAddress(email.trim());
		final User user = userByEmail.orElseThrow(() -> new UserNotFoundException("Email non valida: utente non trovato"));
		final String encryptedTemporaryToken = "C_" + generateEncryptedTemporaryToken(user.getName(), user.getSurname());
		user.setResetPassword(true);
		user.setTemporaryToken(encryptedTemporaryToken);
		user.setTokenCreationDate(LocalDateTime.now());
		user.setLastModifiedBy(user.getUsername());
		userRepository.save(user);
		mailService.sendMail(user.getEmailAddress(), appProperties.getUserRecoveryPasswordMailSubject(), appProperties.getUserRecoveryPasswordMailBody() + encryptedTemporaryToken);
	}

	private String generateEncryptedTemporaryToken(String name, String surname) {
		String token = "%s_%s_%d".formatted(name, surname, Util.getRandomNumberInRange(100, 5000));
		if (userRepository.findByTemporaryToken(token).isPresent()) {
			generateEncryptedTemporaryToken(name, surname);
		}
		return Base64.encode(token).toString();
	}
}

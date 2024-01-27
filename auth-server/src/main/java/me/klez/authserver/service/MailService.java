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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.klez.authserver.exception.MailSenderException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
	private final JavaMailSender sender;

	public void sendASynchronousMail(String toEmail, String subject, String text) throws RuntimeException {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(toEmail);
		mail.setSubject(subject);
		mail.setText(text);
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			executor.submit(() -> {
				try {
					sender.send(mail);
					log.debug("Mail inviata con successo");
				} catch (Exception e) {
					log.error("Exception occur while send a mail : ", e);
				}
			});
		}
	}

	public void sendMail(String toEmail, String subject, String text) throws MailSenderException {
		try {
			SimpleMailMessage mail = getSimpleMailMessage(toEmail, subject, text);
			sender.send(mail);
			log.info("Mail inviata con successo");
		} catch (Exception e) {
			log.error("Invio Mail fallita : ", e);
			throw new MailSenderException("Invio Mail fallita all'indirizzo: [" + toEmail + "]");
		}
	}

	private SimpleMailMessage getSimpleMailMessage(String toEmail, String subject, String text) {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(toEmail);
		mail.setSubject(subject);
		mail.setText(text);
		return mail;
	}
}

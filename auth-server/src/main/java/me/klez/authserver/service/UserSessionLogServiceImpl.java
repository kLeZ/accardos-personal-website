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

import me.klez.authserver.model.UserSessionLog;
import me.klez.authserver.repository.UserSessionLogRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserSessionLogServiceImpl implements UserSessionLogService {
	private final UserSessionLogRepository logRepository;
	@Value("${user.lock.soft-lock-minutes}")
	private int softLockMinutes;
	@Value("${user.lock.max-attempts}")
	private int maxAttempts;

	public UserSessionLogServiceImpl(UserSessionLogRepository logRepository) {
		this.logRepository = logRepository;
	}

	@Override
	@Transactional
	public boolean checkSoftLockForUser(String username) {
		Optional<UserSessionLog> optLog = logRepository.findByUsername(username);
		if (optLog.isPresent()) {
			UserSessionLog log = optLog.get();
			Optional<LocalDateTime> lastFailedAttemptDate = Optional.ofNullable(log.getLastFailedAttemptDate());
			Duration durationBetween = Duration.between(lastFailedAttemptDate.orElse(LocalDateTime.now()), LocalDateTime.now());
			Duration softLockDuration = Duration.ofMinutes(softLockMinutes);
			if (lastFailedAttemptDate.isPresent() && log.getFailedAttemptsNum() != null && log.getFailedAttemptsNum() > maxAttempts) {
				if (durationBetween.compareTo(softLockDuration) <= 0) {
					return true;
				} else {
					logRepository.delete(log);
				}
			}
		}
		return false;
	}

	@Override
	@Transactional
	public void registerAuthentication(String username, boolean success) {
		Optional<UserSessionLog> optSession = logRepository.findByUsername(username);
		UserSessionLog session = optSession.orElseGet(() -> {
			var s = new UserSessionLog();
			s.setUsername(username);
			s.setFailedAttemptsNum(0L);
			return s;
		});
		if (!success) {
			session.setFailedAttemptsNum(session.getFailedAttemptsNum() + 1);
			session.setLastFailedAttemptDate(LocalDateTime.now());
		} else {
			session.setFailedAttemptsNum(0L);
		}
		logRepository.save(session);
	}

	@Override
	public UserSessionLog getByUsername(String username) {
		return logRepository.findByUsername(username).orElse(null);
	}
}

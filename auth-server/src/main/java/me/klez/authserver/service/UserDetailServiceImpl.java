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

import me.klez.authserver.model.User;
import me.klez.authserver.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
	private final UserRepository userRepository;
	private final UserDetailsChecker checker;

	public UserDetailServiceImpl(UserRepository userRepository, UserDetailsChecker checker) {
		this.userRepository = userRepository;
		this.checker = checker;
	}

	@Override
	public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		if (StringUtils.isBlank(userName)) {
			throw new UsernameNotFoundException("User name is empty");
		}
		User user = userRepository.findByUsername(userName).orElseThrow(() -> new UsernameNotFoundException("User not found"));
		UserDetails userDetails = new UserDetails(userName, user.getPassword(), new HashSet<>());
		userDetails.setId(user.getId());
		userDetails.setName(user.getName());
		userDetails.setSurname(user.getSurname());
		userDetails.setEmail(user.getEmailAddress());
		userDetails.setTelephoneNumber(user.getTelephoneNumber());
		userDetails.setResetPassword(user.getResetPassword());
		userDetails.setTokenTemporary(user.getTemporaryToken());
		userDetails.setTokenCreationDate(user.getTokenCreationDate());
		userDetails.setLastModified(user.getLastModified());
		userDetails.setLastModifiedBy(user.getLastModifiedBy());
		checker.check(userDetails);
		return userDetails;
	}
}

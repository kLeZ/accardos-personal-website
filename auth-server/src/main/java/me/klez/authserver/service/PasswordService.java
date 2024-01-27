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

import com.codahale.passpol.BreachDatabase;
import com.codahale.passpol.PasswordPolicy;
import me.klez.authserver.exception.ChangePasswordException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
	protected final Log logger = LogFactory.getLog(getClass());
	final PasswordPolicy policy = new PasswordPolicy(BreachDatabase.top100K(), 12, 70);

	public void checkPassword(String password) throws ChangePasswordException {
		if (password.isBlank()) {
			throw new ChangePasswordException("La nuova password non può essere vuota");
		}
		switch (policy.check(password)) {
			case BREACHED ->
					throw new ChangePasswordException("La nuova password è contenuta tra le 100000 password più comuni");
			case TOO_SHORT -> throw new ChangePasswordException("La nuova password deve contenere almeno 12 caratteri");
			case TOO_LONG ->
					throw new ChangePasswordException("La nuova password non può essere più lunga di 70 caratteri");
			case OK -> logger.info("Successfully changed password");
		}
	}
}

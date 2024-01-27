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

import me.klez.authserver.model.Oauth2ClientRole;
import me.klez.authserver.model.Oauth2ClientRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface Oauth2ClientRoleRepository extends JpaRepository<Oauth2ClientRole, Oauth2ClientRoleId> {
	Set<Oauth2ClientRole> findById_ClientId(String clientId);

	@Query(value = """
			select ocr.*
			from "user" u
			         inner join user_client uc on u.username = uc.username
			         inner join oauth2_client_details ocd on uc.client_id = ocd.client_id
			         inner join oauth2_client_role ocr on ocd.client_id = ocr.client_id
			         inner join role r on r.code = ocr.role_code
			where u.username = :username
			  and ocd.client_id = :clientId
			""", nativeQuery = true)
	Set<Oauth2ClientRole> findByUsernameAndClientId(String username, String clientId);
}

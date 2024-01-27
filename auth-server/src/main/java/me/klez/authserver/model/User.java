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
import org.hibernate.type.NumericBooleanConverter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "name")
	private String name;
	@Column(name = "surname")
	private String surname;
	@Column(name = "email_address")
	private String emailAddress;
	@Column(name = "telephone_number")
	private String telephoneNumber;
	@Column(name = "username")
	private String username;
	@Column(name = "password")
	private String password;
	@Column(name = "reset_password")
	@Convert(converter = NumericBooleanConverter.class)
	private Boolean resetPassword;
	@Column(name = "temporary_token")
	private String temporaryToken;
	@Column(name = "token_creation_date")
	private LocalDateTime tokenCreationDate;
	@Version
	@Column(name = "last_modified")
	private LocalDateTime lastModified;
	@Column(name = "last_modified_by")
	private String lastModifiedBy;
	@Column(name = "enabled")
	private Boolean enabled;
	@Column(name = "locked")
	private Boolean locked;
}

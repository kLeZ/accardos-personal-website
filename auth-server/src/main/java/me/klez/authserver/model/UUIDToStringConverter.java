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

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Converter
public class UUIDToStringConverter implements AttributeConverter<UUID, String> {
	/**
	 * Converts the value stored in the entity attribute into the data representation to be stored in the database.
	 *
	 * @param attribute the entity attribute value to be converted
	 * @return the converted data to be stored in the database column
	 */
	@Override
	public String convertToDatabaseColumn(UUID attribute) {
		return Optional.ofNullable(attribute).map(UUID::toString).orElse(null);
	}

	/**
	 * Converts the data stored in the database column into the value to be stored in the entity attribute. Note that it is the responsibility of the converter writer to
	 * specify the correct <code>dbData</code> type for the corresponding column for use by the JDBC driver: i.e., persistence providers are not expected to do such type
	 * conversion.
	 *
	 * @param dbData the data from the database column to be converted
	 * @return the converted value to be stored in the entity attribute
	 */
	@Override
	public UUID convertToEntityAttribute(String dbData) {
		return Optional.ofNullable(dbData).filter(StringUtils::isNotBlank).map(UUID::fromString).orElse(null);
	}
}

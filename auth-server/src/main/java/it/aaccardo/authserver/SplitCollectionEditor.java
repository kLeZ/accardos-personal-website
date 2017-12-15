//    Copyright © 2017 Alessandro Accardo a.k.a. kLeZ <julius8774@gmail.com>
//
//    This file is part of AAccardo Personal WebSite.
//
//    AAccardo Personal WebSite is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    AAccardo Personal WebSite is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with AAccardo Personal WebSite.  If not, see <http://www.gnu.org/licenses/>.
//
//
//
//
package it.aaccardo.authserver;

import java.util.Collection;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;

/**
 * Creates collections from a string.
 * If the string is empty or null, return an empty collection. Otherwise split by the given splitRegex and use the array.
 *
 * @author Moritz Schulze
 * @author klez
 */
@SuppressWarnings("rawtypes")
public class SplitCollectionEditor extends CustomCollectionEditor {

	private final Class<? extends Collection> collectionType;
	private final String splitRegex;

	public SplitCollectionEditor(Class<? extends Collection> collectionType, String splitRegex) {
		super(collectionType, true);
		this.collectionType = collectionType;
		this.splitRegex = splitRegex;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (text == null || text.isEmpty()) {
			super.setValue(super.createCollection(this.collectionType, 0));
		} else {
			super.setValue(text.split(splitRegex));
		}
	}
}

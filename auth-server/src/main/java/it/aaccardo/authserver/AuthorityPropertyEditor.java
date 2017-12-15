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

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Used to bind Strings to a {@link org.springframework.security.core.GrantedAuthority} when adding/editing a client.
 * <p>
 * Only implements {@link #getAsText()} and {@link #setAsText(String)}.
 * 
 * @author Moritz Schulze
 * @author klez
 */
public class AuthorityPropertyEditor implements PropertyEditor {
	private GrantedAuthority grantedAuthority;

	@Override
	public void setValue(Object value) {
		this.grantedAuthority = (GrantedAuthority) value;
	}

	@Override
	public Object getValue() {
		return grantedAuthority;
	}

	@Override
	public boolean isPaintable() {
		return false;
	}

	@Override
	public void paintValue(Graphics gfx, Rectangle box) {

	}

	@Override
	public String getJavaInitializationString() {
		return null;
	}

	@Override
	public String getAsText() {
		return grantedAuthority.getAuthority();
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (text != null && !text.isEmpty()) {
			this.grantedAuthority = new SimpleGrantedAuthority(text);
		}
	}

	@Override
	public String[] getTags() {
		return new String[0];
	}

	@Override
	public Component getCustomEditor() {
		return null;
	}

	@Override
	public boolean supportsCustomEditor() {
		return false;
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {

	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {

	}
}

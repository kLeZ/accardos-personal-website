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

package it.aaccardo.templatesprovider;

public class DynamicTemplateNotFoundException extends Exception {
	private static final long serialVersionUID = 2255629753793387793L;

	public DynamicTemplateNotFoundException(String templateKey) {
		super(String.format("Cannot find template '%s'", templateKey));
	}
}

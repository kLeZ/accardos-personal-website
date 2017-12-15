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

package it.aaccardo.webui.models;

import java.util.Date;

public class Curriculum {
	private String id;
	private String title;
	private Date creationDate;

	public Curriculum() {
	}

	public Curriculum(String title, Date creationDate) {
		this.title = title;
		this.creationDate = creationDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}

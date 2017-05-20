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

package it.aaccardo.curriculumprovider;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public class StubCurriculumRepository implements CurriculumRepository {

	@Override
	public List<Curriculum> getAllCurricula() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Curriculum getCurriculum(String number) {
		// TODO Auto-generated method stub
		return null;
	}

}

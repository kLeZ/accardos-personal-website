/*
 * Copyright © 2023 Alessandro Accardo a.k.a. kLeZ <julius8774@gmail.com>
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

package me.klez.curriculumprovider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class CurriculumController {
	protected Logger log = Logger.getLogger(CurriculumController.class.getName());

	@Autowired
	me.klez.curriculumprovider.CurriculumRepository repository;

	@RequestMapping(value = "/curricula", method = RequestMethod.GET)
	public List<me.klez.curriculumprovider.Curriculum> all() {
		return repository.findAll();
	}

	@RequestMapping(value = "/curricula/{title}", method = RequestMethod.GET)
	public me.klez.curriculumprovider.Curriculum byTitle(@PathVariable String title) throws me.klez.curriculumprovider.CurriculumNotFoundException {
		me.klez.curriculumprovider.Curriculum curriculum = repository.findByTitle(title);
		if (curriculum != null) {
			return curriculum;
		} else {
			throw new me.klez.curriculumprovider.CurriculumNotFoundException(title);
		}
	}

	@RequestMapping(value = "/curricula/new", method = RequestMethod.POST)
	public me.klez.curriculumprovider.Curriculum create(@RequestBody me.klez.curriculumprovider.Curriculum newCurriculum) {
		newCurriculum.setId(null);
		return repository.save(newCurriculum);
	}

	@RequestMapping(value = "/curricula/{id}", method = RequestMethod.PUT)
	public me.klez.curriculumprovider.Curriculum update(@PathVariable String id, @RequestBody me.klez.curriculumprovider.Curriculum updatedCurriculum) {
		updatedCurriculum.setId(id);
		return repository.save(updatedCurriculum);
	}

	@RequestMapping(value = "/curricula/{id}", method = RequestMethod.DELETE)
	public void remove(@PathVariable String id) {
		repository.deleteById(id);
	}
}

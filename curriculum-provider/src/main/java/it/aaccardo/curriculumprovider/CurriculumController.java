/*
 * Copyright © 2018 Alessandro Accardo a.k.a. kLeZ <julius8774@gmail.com>
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

package it.aaccardo.curriculumprovider;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurriculumController {
	protected Logger log = Logger.getLogger(CurriculumController.class.getName());

	@Autowired
	CurriculumRepository repository;

	@RequestMapping(value = "/curricula", method = RequestMethod.GET)
	public List<Curriculum> all() {
		return repository.findAll();
	}

	@RequestMapping(value = "/curricula/{title}", method = RequestMethod.GET)
	public Curriculum byTitle(@PathVariable String title) throws CurriculumNotFoundException {
		Curriculum curriculum = repository.findByTitle(title);
		if (curriculum != null) {
			return curriculum;
		} else {
			throw new CurriculumNotFoundException(title);
		}
	}

	@RequestMapping(value = "/curricula/new", method = RequestMethod.POST)
	public Curriculum create(@RequestBody Curriculum newCurriculum) {
		newCurriculum.setId(null);
		return repository.save(newCurriculum);
	}

	@RequestMapping(value = "/curricula/{id}", method = RequestMethod.PUT)
	public Curriculum update(@PathVariable String id, @RequestBody Curriculum updatedCurriculum) {
		updatedCurriculum.setId(id);
		return repository.save(updatedCurriculum);
	}

	@RequestMapping(value = "/curricula/{id}", method = RequestMethod.DELETE)
	public void remove(@PathVariable String id) {
		repository.delete(id);
	}
}

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

package it.aaccardo.pagesprovider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class PagesController {
	protected Logger log = Logger.getLogger(PagesController.class.getName());

	@Autowired
	PagesRepository repository;

	@RequestMapping(value = "/pages", method = RequestMethod.GET)
	public List<Page> all() {
		return repository.findAll();
	}

	@RequestMapping(value = "/pages/{title}", method = RequestMethod.GET)
	public Page byTitle(@PathVariable String title) throws PageNotFoundException {
		Page page = repository.findByTitle(title);
		if (page != null) {
			return page;
		} else {
			throw new PageNotFoundException(title);
		}
	}

	@RequestMapping(value = "/pages/new", method = RequestMethod.POST)
	public Page create(@RequestBody Page newPage) {
		newPage.setId(null);
		return repository.save(newPage);
	}

	@RequestMapping(value = "/pages/{id}", method = RequestMethod.PUT)
	public Page update(@PathVariable String id, @RequestBody Page updatedPage) {
		updatedPage.setId(id);
		return repository.save(updatedPage);
	}

	@RequestMapping(value = "/pages/{id}", method = RequestMethod.DELETE)
	public void remove(@PathVariable String id) {
		repository.deleteById(id);
	}
}

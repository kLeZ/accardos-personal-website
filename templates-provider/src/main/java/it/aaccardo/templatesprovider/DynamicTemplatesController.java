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

package it.aaccardo.templatesprovider;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DynamicTemplatesController {
	protected Logger log = Logger.getLogger(DynamicTemplatesController.class.getName());

	@Autowired
	DynamicTemplateRepository repository;

	@RequestMapping(value = "/templates", method = RequestMethod.GET)
	public List<DynamicTemplate> all() {
		return repository.getObjects();
	}

	@RequestMapping(value = "/templates/{key}", method = RequestMethod.GET)
	public DynamicTemplate get(@PathVariable String key) throws DynamicTemplateNotFoundException {
		DynamicTemplate template = new DynamicTemplate();
		template.setId(key);
		DynamicTemplate ret = repository.get(template);
		if (ret != null) {
			return ret;
		} else {
			throw new DynamicTemplateNotFoundException(key);
		}
	}

	@RequestMapping(value = "/templates/new", method = RequestMethod.PUT)
	public @ResponseBody void create(@RequestBody DynamicTemplate newTemplate) throws DuplicateTemplateException {
		repository.put(newTemplate);
	}

	@RequestMapping(value = "/templates/{key}", method = RequestMethod.POST)
	public void update(@PathVariable String key, @RequestBody DynamicTemplate updatedTemplate) {
		updatedTemplate.setId(key);
		repository.put(updatedTemplate);
	}

	@RequestMapping(value = "/templates/{key}", method = RequestMethod.DELETE)
	public void remove(@PathVariable String key) {
		DynamicTemplate template = new DynamicTemplate();
		template.setId(key);
		repository.delete(template);
	}
}

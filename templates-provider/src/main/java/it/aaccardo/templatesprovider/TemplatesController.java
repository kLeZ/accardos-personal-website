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

package me.klez.templatesprovider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
public class TemplatesController {
	protected Logger log = Logger.getLogger(TemplatesController.class.getName());

	@Autowired
	me.klez.templatesprovider.TemplateRepository repository;

	@RequestMapping(value = "/templates", method = RequestMethod.GET)
	public List<Template> all() {
		return repository.getObjects();
	}

	@RequestMapping(value = "/templates/{key}", method = RequestMethod.GET)
	public Template get(@PathVariable String key) throws me.klez.templatesprovider.TemplateNotFoundException {
		Template template = new Template();
		template.setId(key);
		Template ret = repository.get(template);
		if (ret != null) {
			return ret;
		} else {
			throw new me.klez.templatesprovider.TemplateNotFoundException(key);
		}
	}

	@RequestMapping(value = "/templates/new", method = RequestMethod.PUT)
	public @ResponseBody
	void create(@RequestBody Template newTemplate) {
		repository.put(newTemplate);
	}

	@RequestMapping(value = "/templates/{key}", method = RequestMethod.POST)
	public void update(@PathVariable String key, @RequestBody Template updatedTemplate) {
		updatedTemplate.setId(key);
		repository.put(updatedTemplate);
	}

	@RequestMapping(value = "/templates/{key}", method = RequestMethod.DELETE)
	public void remove(@PathVariable String key) {
		Template template = new Template();
		template.setId(key);
		repository.delete(template);
	}
}

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

package it.aaccardo.webui.clients;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.aaccardo.webui.models.DynamicTemplate;

@FeignClient("http://templates")
public interface DynamicTemplateSubscriber {
	@RequestMapping(value = "/templates", method = RequestMethod.GET)
	List<DynamicTemplate> all();

	@RequestMapping(value = "/templates/{key}", method = RequestMethod.GET)
	DynamicTemplate get(@PathVariable("key") String key);

	@RequestMapping(value = "/templates/new", method = RequestMethod.POST)
	DynamicTemplate create(@RequestBody DynamicTemplate newTemplate);

	@RequestMapping(value = "/templates/{key}", method = RequestMethod.PUT)
	DynamicTemplate update(@PathVariable("key") String key, @RequestBody DynamicTemplate updatedTemplate);

	@RequestMapping(value = "/templates/{key}", method = RequestMethod.DELETE)
	void remove(@PathVariable("key") String key);
}

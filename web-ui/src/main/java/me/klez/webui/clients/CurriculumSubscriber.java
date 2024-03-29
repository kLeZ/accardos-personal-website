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

package me.klez.webui.clients;

import me.klez.webui.models.Curriculum;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("curriculum")
public interface CurriculumSubscriber {
	@RequestMapping(value = "/curricula", method = RequestMethod.GET)
	List<Curriculum> all();

	@RequestMapping(value = "/curricula/{title}", method = RequestMethod.GET)
	Curriculum byTitle(@PathVariable(name = "title") String title);

	@RequestMapping(value = "/curricula/new", method = RequestMethod.POST)
	Curriculum create(@RequestBody Curriculum newCurriculum);

	@RequestMapping(value = "/curricula/{id}", method = RequestMethod.PUT)
	Curriculum update(@PathVariable(name = "id") String id, @RequestBody Curriculum updatedCurriculum);

	@RequestMapping(value = "/curricula/{id}", method = RequestMethod.DELETE)
	void remove(@PathVariable(name = "id") String id);
}

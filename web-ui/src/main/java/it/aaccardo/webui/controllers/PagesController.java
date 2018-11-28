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

package it.aaccardo.webui.controllers;

import it.aaccardo.webui.clients.PagesSubscriber;
import it.aaccardo.webui.models.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class PagesController {
	@Autowired
	private PagesSubscriber pages;

	@RequestMapping("**/pages/{template}")
	public ModelAndView pages(@PathVariable String template, final HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();

		String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		UriTemplate uritemplate = new UriTemplate("{value}/pages/{template}");
		boolean isTemplateMatched = uritemplate.matches(restOfTheUrl);
		if (isTemplateMatched) {
			Map<String, String> matchTemplate = uritemplate.match(restOfTheUrl);
			String value = matchTemplate.get("value");
			if (!value.isEmpty()) {
				//mv.addObject("pageTitle", String.format("%s » %s", value.substring(1).replace("/", " » "), template));
				template = String.format("%s_%s", value.substring(1).replace('/', '-'), template);
			}
		}
		Page page = pages.byTitle(template);
		mv.addObject("pageTitle", page.getTitle());
		mv.addObject("templateId", template);
		mv.setViewName("dyn-page");
		return mv;
	}

}

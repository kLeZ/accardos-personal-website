/*
 * Copyright © 2024 Alessandro Accardo a.k.a. kLeZ <julius8774@gmail.com>
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

package me.klez.webui.controllers;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import me.klez.webui.clients.PagesSubscriber;
import me.klez.webui.models.NewPage;
import me.klez.webui.models.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriTemplate;

import java.util.Map;

@Slf4j
@Controller
public class PagesController {

	@Autowired
	private PagesSubscriber pages;

	private String getPath(final HttpServletRequest request) {
		String ret = "";
		String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		UriTemplate uritemplate = new UriTemplate("{value}/pages/{template}");
		boolean isTemplateMatched = uritemplate.matches(restOfTheUrl);
		if (isTemplateMatched) {
			Map<String, String> matchTemplate = uritemplate.match(restOfTheUrl);
			ret = matchTemplate.get("value");
		}
		return ret;
	}

	@RequestMapping("**/pages/{template}")
	public ModelAndView getPage(@PathVariable String template, final HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		String pageTitle = "";
		String value = getPath(request);
		if (!value.isEmpty()) {
			pageTitle = String.format("%s » %s", value.substring(1).replace("/", " » "), template);
			template = String.format("%s_%s", value.substring(1).replace('/', '-'), template);
		}
		try {
			Page page = pages.byTitle(template);
			pageTitle = page.getTitle();
		} catch (Exception e) {
			log.error("Page not found", e);
		}
		mv.addObject("pageTitle", pageTitle);
		mv.addObject("templateId", template);
		mv.setViewName("dyn-page");
		return mv;
	}

	@RequestMapping(value = "**/pages/new", method = RequestMethod.GET)
	public ModelAndView newPage() {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("pages/new-page");
		return mv;
	}

	@RequestMapping(value = "**/pages/new", method = RequestMethod.POST)
	public String saveNewPage(@ModelAttribute NewPage page, final HttpServletRequest request) {
		String path = getPath(request);
		String template = page.getTemplate();
		log.info(page.toString());
		return String.format("redirect:%s/pages/%s", path, template);
	}
}

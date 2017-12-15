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

package it.aaccardo.webui.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import it.aaccardo.webui.clients.CurriculumSubscriber;

@Controller
public class CurriculumController {

	@Autowired
	CurriculumSubscriber cv;

	@RequestMapping("/cv")
	public ModelAndView all() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("curricula", cv.all());
		mv.setViewName("curriculum");
		return mv;
	}

	@RequestMapping(path = "/cv/detail")
	public ModelAndView get(@RequestParam String key) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("templateId", "cv-detail");
		mv.addObject("pageTitle", String.format("Curriculum Vitae: %s", key));
		mv.addObject("cv", cv.byTitle(key));
		mv.setViewName("dyn-page");
		return mv;
	}
}

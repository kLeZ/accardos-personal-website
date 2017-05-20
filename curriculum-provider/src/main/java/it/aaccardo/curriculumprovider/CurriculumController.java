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

package it.aaccardo.curriculumprovider;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurriculumController {
	protected Logger logger = Logger.getLogger(CurriculumController.class.getName());

	@Autowired
	CurriculumRepository curriculumRepository;

	@RequestMapping("/curricula")
	public Curriculum[] all() {
		logger.info("curriculum-provider all() invoked");
		List<Curriculum> curricula = curriculumRepository.getAllCurricula();
		logger.info("curriculum-provider all() found: " + curricula.size());
		return curricula.toArray(new Curriculum[curricula.size()]);
	}

	@RequestMapping("/curricula/{id}")
	public Curriculum byId(@PathVariable("id") String id) {
		logger.info("curriculum-provider byId() invoked: " + id);
		Curriculum curriculum = curriculumRepository.getCurriculum(id);
		logger.info("curriculum-provider byId() found: " + curriculum);
		return curriculum;
	}
}

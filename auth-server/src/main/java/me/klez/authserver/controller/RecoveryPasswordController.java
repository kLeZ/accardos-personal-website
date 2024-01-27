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
package me.klez.authserver.controller;

import lombok.extern.slf4j.Slf4j;
import me.klez.authserver.exception.UserNotFoundException;
import me.klez.authserver.service.UserService;
import me.klez.authserver.util.Util;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class RecoveryPasswordController {
	private final UserService userService;

	public RecoveryPasswordController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/recoveryPassword")
	public String recoveryPassword(Model model) {
		model.addAttribute("email", "");
		return "recovery-password";
	}

	@PostMapping("/recoveryPassword")
	public String recoveryPassword(Model model, @RequestParam("email") String email) {
		try {
			if (email.isEmpty()) {
				model.addAttribute("result", "KO");
				model.addAttribute("error", "Inserire un indirizzo email");
			} else if (!Util.isValidEmailAddress(email)) {
				model.addAttribute("result", "KO");
				model.addAttribute("error", "Indirizzo email non valido");
			} else {
				userService.recoveryPassword(email.trim());
				model.addAttribute("result", "OK");
			}
		} catch (UserNotFoundException unfe) {
			model.addAttribute("result", "KO");
			model.addAttribute("error", unfe.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			model.addAttribute("result", "KO");
			model.addAttribute("error", "Errore generico");
		}
		model.addAttribute("email", email);
		return "recovery-password";
	}
}

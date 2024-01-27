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
import me.klez.authserver.dto.ChangePasswordInputDTO;
import me.klez.authserver.exception.ChangePasswordException;
import me.klez.authserver.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Slf4j
@Controller
public class ChangePasswordController {
	private final UserService userService;

	public ChangePasswordController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/changePassword/{username:.+}")
	public String changePasswordInput(Principal principal, Model model, @PathVariable String username) {
		model.addAttribute("input", new ChangePasswordInputDTO());
		model.addAttribute("username", username);
		return "change-password";
	}

	@PostMapping("/changePassword")
	public String changePassword(Principal principal, Model model, @ModelAttribute ChangePasswordInputDTO input) {
		try {
			if (input.getCurrentPassword().isEmpty()) {
				model.addAttribute("result", "KO");
				model.addAttribute("error", "Inserire la password corrente");
			} else if (input.getNewPassword().isEmpty()) {
				model.addAttribute("result", "KO");
				model.addAttribute("error", "Inserire la nuova password");
			} else if (input.getConfirmPassword().isEmpty()) {
				model.addAttribute("result", "KO");
				model.addAttribute("error", "Inserire la conferma della nuova password");
			} else if (!input.getNewPassword().equals(input.getConfirmPassword())) {
				model.addAttribute("result", "KO");
				model.addAttribute("error", "La nuova password non coincide con la conferma");
			} else {
				userService.changePassword(principal.getName(), input.getCurrentPassword(), input.getNewPassword());
				model.addAttribute("result", "OK");
			}
		} catch (ChangePasswordException cpe) {
			model.addAttribute("result", "KO");
			model.addAttribute("error", cpe.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			model.addAttribute("result", "KO");
			model.addAttribute("error", "Errore generico");
		}
		model.addAttribute("input", input);
		return "change-password";
	}
}

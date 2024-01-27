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

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import me.klez.authserver.config.ApplicationProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LogoutController {
	private final ApplicationProperties applicationProperties;
	private final LogoutHandler logoutHandler;

	public LogoutController(ApplicationProperties applicationProperties, LogoutHandler logoutHandler) {
		this.applicationProperties = applicationProperties;
		this.logoutHandler = logoutHandler;
	}

	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication, Model model,
	                     @RequestParam(value = "redirect_uri", required = false) final String redirectUri) {
		final String redirectUrl;
		if (redirectUri == null || StringUtils.isBlank(redirectUri)) {
			redirectUrl = applicationProperties.getIssuer();
		} else {
			redirectUrl = redirectUri;
		}
		model.addAttribute("redirectUrl", redirectUrl);
		model.addAttribute("timeout", applicationProperties.getLogoutRedirectTimeout());
		logoutHandler.logout(request, response, authentication);
		return "logout";
	}
}

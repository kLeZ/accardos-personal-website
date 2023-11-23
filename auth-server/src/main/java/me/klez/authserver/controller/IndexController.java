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
package me.klez.authserver.controller;

import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Moritz Schulze
 * @author klez
 */
@Controller
public class IndexController {

	private final JdbcClientDetailsService clientDetailsService;
	private final ApprovalStore approvalStore;
	private final TokenStore tokenStore;

	public IndexController(JdbcClientDetailsService clientDetailsService, ApprovalStore approvalStore,
			TokenStore tokenStore) {
		this.clientDetailsService = clientDetailsService;
		this.approvalStore = approvalStore;
		this.tokenStore = tokenStore;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView root(Map<String, Object> model, Principal principal) {
		List<Approval> approvals = clientDetailsService.listClientDetails().stream()
				.map((clientDetail) -> approvalStore.getApprovals(principal.getName(), clientDetail.getClientId()))
				.flatMap(Collection::stream).collect(Collectors.toList());
		model.put("approvals", approvals);
		model.put("clientDetails", clientDetailsService.listClientDetails());
		return new ModelAndView("index", model);
	}

	@RequestMapping(value = "/approval/revoke", method = RequestMethod.POST)
	public String revokeApproval(@ModelAttribute Approval approval) {
		approvalStore.revokeApprovals(Collections.singletonList(approval));
		tokenStore.findTokensByClientIdAndUserName(approval.getClientId(), approval.getUserId())
				.forEach(tokenStore::removeAccessToken);
		return "redirect:/";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage() {
		return "login";
	}
}

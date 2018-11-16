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

package it.aaccardo.webui;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import com.google.common.collect.Sets;

import it.aaccardo.webui.clients.DynamicTemplateSubscriber;
import it.aaccardo.webui.models.DynamicTemplate;

public class DynamicTemplateResolver extends TemplateResolver {

	@Autowired
	DynamicTemplateSubscriber templates;

	private final static String PREFIX = "dyn:";

	public DynamicTemplateResolver() {
		setResourceResolver(new DynamicResourceResolver());
		setResolvablePatterns(Sets.newHashSet(PREFIX + "*"));
	}

	@Override
	protected String computeResourceName(TemplateProcessingParameters params) {
		String templateName = params.getTemplateName();
		return templateName.substring(PREFIX.length());
	}

	private class DynamicResourceResolver implements IResourceResolver {

		@Override
		public InputStream getResourceAsStream(TemplateProcessingParameters params, String resourceName) {
			//DynamicTemplate template = templates.get(resourceName);
			DynamicTemplate template = new DynamicTemplate();
			template.setPublished(true);
			template.setId(resourceName);
			template.setContent(String.format("<h1>Hello %s!</h1>", resourceName));
			if (template != null) {
				return new ByteArrayInputStream(template.getContent().getBytes());
			}
			return null;
		}

		@Override
		public String getName() {
			return "dynResourceResolver";
		}
	}
}

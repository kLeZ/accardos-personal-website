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

import it.aaccardo.webui.clients.TemplatesSubscriber;
import it.aaccardo.webui.models.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.cache.ICacheEntryValidity;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.AbstractTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;

import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Map;

@Component
public class TemplateResolver extends AbstractTemplateResolver {
	public static final Logger log = LoggerFactory.getLogger(TemplateResolver.class);
	private final static String PREFIX = "dyn:";

	@Autowired
	TemplatesSubscriber templates;

	public TemplateResolver() {
		log.info("TemplateResolver ctor()");
		HashSet<String> patterns = new HashSet<>();
		patterns.add(PREFIX + "*");
		setResolvablePatterns(patterns);
	}

	@Override
	protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate, String template, Map<String, Object> templateResolutionAttributes) {
		log.info(String.format("computeTemplateResource { template: %s, ownerTemplate: %s }", template, ownerTemplate));
		return new DynamicTemplateResource(template.substring(PREFIX.length()));
	}

	@Override
	protected TemplateMode computeTemplateMode(IEngineConfiguration configuration, String ownerTemplate, String template, Map<String, Object> templateResolutionAttributes) {
		log.info(String.format("computeTemplateMode { template: %s, ownerTemplate: %s }", template, ownerTemplate));
		return TemplateMode.HTML;
	}

	@Override
	protected ICacheEntryValidity computeValidity(IEngineConfiguration configuration, String ownerTemplate, String template, Map<String, Object> templateResolutionAttributes) {
		log.info(String.format("computeValidity { template: %s, ownerTemplate: %s }", template, ownerTemplate));
		return new ICacheEntryValidity() {
			@Override
			public boolean isCacheable() {
				return false;
			}

			@Override
			public boolean isCacheStillValid() {
				return false;
			}
		};
	}

	private class DynamicTemplateResource implements ITemplateResource {
		String baseName;

		DynamicTemplateResource(String template) {
			this.baseName = template;
		}

		@Override
		public String getDescription() {
			return "";
		}

		@Override
		public String getBaseName() {
			return baseName;
		}

		@Override
		public boolean exists() {
			return true;
		}

		@Override
		public Reader reader() {
			log.info(String.format("DynamicTemplateResource.render(%s)", this.getBaseName()));
			StringReader ret = null;
			if (templates != null) {
				try {
					Template template = templates.get(this.getBaseName());
					if (template != null) {
						ret = new StringReader(template.getContent());
					}
				} catch (Exception e) {
					log.error("There was a problem with loading the template from the source", e);
				}
			}
			// Gli fanno schifo i template vuoti, deve sempre esserci qualcosa dentro.
			return ret == null ? new StringReader(String.format("<h1>%s</h1>", this.getBaseName())) : ret;
		}

		@Override
		public ITemplateResource relative(String relativeLocation) {
			return this;
		}
	}
}

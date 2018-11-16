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

package it.aaccardo.templatesprovider;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class DynamicTemplateRepository implements RedisRepository<DynamicTemplate> {

	@Autowired
	private RedisTemplate<String, DynamicTemplate> redisTemplate;

	public RedisTemplate<String, DynamicTemplate> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, DynamicTemplate> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void put(DynamicTemplate obj) {
		redisTemplate.opsForHash().put(obj.getObjectKey(), obj.getKey(), obj);
	}

	@Override
	public DynamicTemplate get(DynamicTemplate key) {
		return (DynamicTemplate) redisTemplate.opsForHash().get(key.getObjectKey(), key.getKey());
	}

	@Override
	public void delete(DynamicTemplate key) {
		redisTemplate.opsForHash().delete(key.getObjectKey(), key.getKey());
	}

	@Override
	public List<DynamicTemplate> getObjects() {
		List<DynamicTemplate> templates = new ArrayList<>();
		for (Object template : redisTemplate.opsForHash().values(DynamicTemplate.OBJECT_KEY)) {
			templates.add((DynamicTemplate) template);
		}
		return templates;
	}
}

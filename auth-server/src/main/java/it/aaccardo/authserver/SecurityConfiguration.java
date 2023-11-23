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
package me.klez.authserver;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author Moritz Schulze
 * @author klez
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	/*
	 * drop table if exists `users`;
	 * drop table if exists `authorities`;
	 * CREATE TABLE `users` (
	 *   `USERNAME` VARCHAR(50) BINARY NOT NULL,
	 *   `PASSWORD` VARCHAR(500) NOT NULL,
	 *   `ENABLED` boolean not null,
	 *   PRIMARY KEY (`USERNAME`)
	 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	 * CREATE TABLE `authorities` (
	 *   `USERNAME` VARCHAR(20) NOT NULL,
	 *   `AUTHORITY` VARCHAR(20) NOT NULL)
	 *   ENGINE=InnoDB;
	 */

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public JdbcUserDetailsManager userDetailsManager() {
		return new JdbcUserDetailsManager();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/webjars/**", "/css/**", "/js/**", "/img/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.authorizeRequests()
			.antMatchers("/login", "/logout.do").permitAll()
			.antMatchers("/**").authenticated()
		.and()
		.formLogin()
			.loginProcessingUrl("/login.do")
			.usernameParameter("name")
			.loginPage("/login")
		.and()
		.logout()
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout.do"));
		// @formatter:on
	}
}

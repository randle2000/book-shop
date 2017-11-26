package com.sln.bshop.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.sln.bshop.service.impl.UserSecurityService;
import com.sln.bshop.utility.SecurityUtility;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    DataSource dataSource;

	@Autowired
	private UserSecurityService userSecurityService;
	
	private BCryptPasswordEncoder passwordEncoder() {
		return SecurityUtility.passwordEncoder();
	}
	
	public static final String[] PUBLIC_MATCHERS = {
			"/css/**",
			"/js/**",
			"/image/**",
			"/",
			"/newUser",
			"/forgotPassword",
			"/login",
			"/fonts/**",
			"/bookshelf",
			"/bookDetail",
			"/hours",
			"/faq",
			"/searchByCategory",
			"/searchBook"
	};
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()	// rules are applied in the order given
				.antMatchers("/admin/**").hasRole("ADMIN")	// can also use regexMatches()
				.antMatchers(PUBLIC_MATCHERS).permitAll()
				.anyRequest().authenticated()
				.and()
//			.requiresChannel()	// https
//				.antMatchers(PUBLIC_MATCHERS).requiresInsecure()
//				.anyRequest().requiresSecure()
//				.and()
			//.csrf().disable()		// Thymeleaf adds hidden _csrf field to the form automatically
			//.cors().disable()
			.formLogin()
				.usernameParameter("email")
				.passwordParameter("password")
				.failureUrl("/login?error")
				.defaultSuccessUrl("/")
				.loginPage("/login").permitAll()
				.and()
			.logout()
				.deleteCookies("JSESSIONID")
				.logoutUrl("/logout") 
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/?logout").deleteCookies("remember-me").permitAll()
				.and()
			.rememberMe()
				.rememberMeParameter("remember-me")
				.tokenRepository(persistentTokenRepository())
				.tokenValiditySeconds(7 * 24 * 60 * 60); // in seconds
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(userSecurityService)
			.passwordEncoder(passwordEncoder());
	}
	
	// for remember-me
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepositoryImpl = new JdbcTokenRepositoryImpl();
		tokenRepositoryImpl.setDataSource(dataSource);
		return tokenRepositoryImpl;
	}
	
}

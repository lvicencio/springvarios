package com.udemy.spring.app;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
/*import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;*/
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.udemy.spring.app.auth.handler.LoginSuccessHandler;
import com.udemy.spring.app.service.JpaUserDetailsService;

@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{

	
	@Autowired
	private LoginSuccessHandler successHandler;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	
	
	@Autowired
	private JpaUserDetailsService userDetailService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	
		http.authorizeRequests().antMatchers("/","/css/**", "/js/**", "/images/**", "/listar**", "/api/**").permitAll()
				/*  proteccion a traves de rutas, lo cambio para reemplazar por proteccion por controllador
				 * .antMatchers("/ver/**").hasAnyRole("USER", "ADMIN")
				 * .antMatchers("/uploads/**").hasAnyRole("USER")
				 * .antMatchers("/form/**").hasAnyRole("ADMIN")
				 * .antMatchers("/eliminar/**").hasAnyRole("ADMIN")
				 * .antMatchers("/factura/**").hasAnyRole("ADMIN")
				 */
		.anyRequest().authenticated()
		.and()
		.formLogin()
			.successHandler(successHandler)
			.loginPage("/login").permitAll()
		.and()
		.logout().permitAll()
		.and()
		.exceptionHandling().accessDeniedPage("/error_403");
		
	}




	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception{
		
		builder.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
		
	}
	
}

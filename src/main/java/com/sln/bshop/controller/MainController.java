package com.sln.bshop.controller;

import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.sln.bshop.utility.MailConstructor;
import com.sln.bshop.domain.User;
import com.sln.bshop.domain.security.PasswordResetToken;
import com.sln.bshop.domain.security.Role;
import com.sln.bshop.service.UserService;
import com.sln.bshop.service.impl.UserSecurityService;
import com.sln.bshop.utility.SecurityUtility;
import com.sln.bshop.validator.EmailValidator;

@Controller
public class MainController {
		
	private static final Logger LOG = LoggerFactory.getLogger(MainController.class);
	
	@Autowired
	UserService userService;
	
	@Autowired
	UserSecurityService userSecurityService;
	
	@Autowired
	EmailValidator emailValidator;
	
	@Autowired
	JavaMailSender mailSender;
	
	@Autowired
	MailConstructor mailConstructor;
	
	private void doManualLogin(String username) {
		UserDetails userDetails = userSecurityService.loadUserByUsername(username);
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),	userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/hours")
	public String hours() {
		return "hours";
	}
	
	@RequestMapping("/faq")
	public String faq() {
		return "faq";
	}
	
	@RequestMapping("/login")
	public String myAccount(Model model) {
		model.addAttribute("classActiveLogin", true);
		return "myAccount";
	}
	
	@RequestMapping(value="/newUser", method=RequestMethod.POST)
	public String newUserPost(HttpServletRequest request, @RequestParam("email") String email, Model model) throws Exception {
		model.addAttribute("classActiveNewAccount", true);
		model.addAttribute("email", email);
		
		if (!emailValidator.valid(email)) {
			model.addAttribute("invalidEmail", true);
			return "myAccount";
		}
		
		if (userService.findByEmail(email) != null) {
			model.addAttribute("emailExists", true);
			return "myAccount";
		}
		
		String password = SecurityUtility.randomPassword();
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		Role role = new Role();
		role.setRoleId(1);
		role.setName("ROLE_USER");
		User user = userService.createUser(email, encryptedPassword, role);
		
		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);
		
		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		SimpleMailMessage emailMessage = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);
		LOG.info("Sending email: {}", emailMessage);
		mailSender.send(emailMessage);
		
		model.addAttribute("emailSent", true);
		return "myAccount";
	}

	// this will be requested when user clicks on a link sent to him by email upon initial registration
	@RequestMapping("/newUser")
	public String newUser(Locale locale, @RequestParam("token") String token, Model model) {
		PasswordResetToken passToken = userService.getPasswordResetToken(token);
		if (passToken == null) {
			//String message = "Invalid Token.";
			return "redirect:/badRequest";
		}
		User user = passToken.getUser();
		String email = user.getEmail();
		
		doManualLogin(email);
		
		return "redirect:/myProfile";
	}
	
	@RequestMapping("/forgotPassword")
	public String forgetPassword(HttpServletRequest request, @RequestParam("email") String email, Model model) {
		
		User user = userService.findByEmail(email);
		if (user == null) {
			model.addAttribute("emailNotExist", true);
			model.addAttribute("classActiveForgotPassword", true);
			return "myAccount";
		}
		
		String password = SecurityUtility.randomPassword();
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		user.setPassword(encryptedPassword);
		userService.save(user);
		
		String token = UUID.randomUUID().toString();
		userService.createPasswordResetTokenForUser(user, token);
		
		String appUrl = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password);
		LOG.info("Sending email: {}", newEmail);
		mailSender.send(newEmail);
		
		model.addAttribute("forgotPasswordEmailSent", "true");
		model.addAttribute("classActiveForgotPassword", true);
		return "myAccount";
	}
	
	
}
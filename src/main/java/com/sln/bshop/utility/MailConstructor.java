package com.sln.bshop.utility;

import java.util.Locale;

//import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.sln.bshop.domain.Order;
import com.sln.bshop.domain.User;

@Component
public class MailConstructor {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	public SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, User user, String password) {
		String url = contextPath + "/newUser?token=" + token;
		String message = "Hello there,\n\n" +
				"You received this email because you have requested a new password at SLN's Book Shop\n" + 
				"Please click on this link to verify your email: " + url +
				"\n\nYour current password is: \n" + password +
				"\n\nYou can change it in your My Profile menu\n" +
				"If you did not request this, then disregard this email please.\n\n" +
				"Yours,\n" +
				"SLN's Book Shop\n";
		SimpleMailMessage email = new SimpleMailMessage();
		email.setTo(user.getEmail());
		email.setSubject("SLN's bookstore registration password. Please verify your email");
		email.setText(message);
		email.setFrom(env.getProperty("support.email"));
		return email;	
	}

	public MimeMessagePreparator constructOrderConfirmationEmail (User user, Order order, Locale locale) {
		Context context = new Context();
		context.setVariable("order", order);
		context.setVariable("user", user);
		context.setVariable("cartItemList", order.getCartItemList());
		String text = templateEngine.process("orderConfirmationEmailTemplate", context);
		
		MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
				email.setTo(user.getEmail());
				email.setSubject("Order Confirmation - "+order.getId());
				email.setText(text, true);
				//email.setFrom(new InternetAddress("aaa@gmail.com"));
				email.setFrom(env.getProperty("support.email"));
			}
		};
		
		return messagePreparator;
	}
}

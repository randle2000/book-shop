package com.sln.bshop.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import com.sln.bshop.domain.security.Role;
import com.sln.bshop.domain.ShoppingCart;
import com.sln.bshop.domain.User;
import com.sln.bshop.domain.UserBilling;
import com.sln.bshop.domain.UserPayment;
import com.sln.bshop.domain.UserShipping;
import com.sln.bshop.domain.security.PasswordResetToken;
import com.sln.bshop.domain.security.UserRole;
import com.sln.bshop.repository.PasswordResetTokenRepository;
import com.sln.bshop.repository.RoleRepository;
import com.sln.bshop.repository.UserPaymentRepository;
import com.sln.bshop.repository.UserRepository;
import com.sln.bshop.repository.UserShippingRepository;
import com.sln.bshop.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	UserPaymentRepository userPaymentRepository;
	
	@Autowired
	UserShippingRepository userShippingRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public void detachUser(User user) {
        entityManager.detach(user);
    }

	@Override
	public PasswordResetToken getPasswordResetToken(String token) {
		return passwordResetTokenRepository.findByToken(token);
	}

	@Override
	public void createPasswordResetTokenForUser(User user, String token) {
		final PasswordResetToken myToken = new PasswordResetToken(token, user); 
		passwordResetTokenRepository.save(myToken);
	}
	
	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	@Override
	public User findById(Long id) {
		return userRepository.findOne(id);
	}
	
	@Override
	@Transactional
	public User createUser(User user, Set<UserRole> userRoles) {
		User localUser = userRepository.findByEmail(user.getEmail());
		
		if (localUser != null) {
			LOG.info("user {} already exists. Nothing will be done.", user.getEmail());
		} else {
			for (UserRole ur : userRoles) {
				ur.setUser(user);	// this is probably not necessary
				roleRepository.save(ur.getRole());	// roles need to be saved because they are not marked CascadeType.ALL in UserRole
			}
			user.getUserRoles().addAll(userRoles);
			
			ShoppingCart shoppingCart = new ShoppingCart();
			shoppingCart.setUser(user);
			user.setShoppingCart(shoppingCart);
			
			user.setUserShippingList(new ArrayList<UserShipping>());
			user.setUserPaymentList(new ArrayList<UserPayment>());
			
			localUser = userRepository.save(user);
		}
		return localUser;
	}
	
	@Override
	public User createUser(String email, String password, Role role) {
		User user = new User();
		user.setEmail(email);
		user.setPassword(password);
		
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(user, role));
		return createUser(user, userRoles);
	}

	@Override
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	public User save(User user) {
		return userRepository.save(user);
	}
	
	@Override
	@Transactional
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	public void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user) {
		userPaymentRepository.setAllDefaultPayment(false, user);	// remove 'default' flag from all other payments
		userPayment.setUser(user);
		userPayment.setUserBilling(userBilling);
		userPayment.setDefaultPayment(true);
		userBilling.setUserPayment(userPayment);
		user.getUserPaymentList().add(userPayment);
		save(user);
	}
	
	@Override
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	public void setUserDefaultPayment(Long userPaymentId, User user) {
		List<UserPayment> userPaymentList = user.getUserPaymentList();
		
		userPaymentList.forEach(up -> {
			up.setDefaultPayment(up.getId().equals(userPaymentId));
			userPaymentRepository.save(up);
		});
	}
	
	@Override
	@Transactional
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	public void updateUserShipping(UserShipping userShipping, User user) {
		userShippingRepository.setAllDefaultShipping(false, user);	// remove 'default' flag from all other shippings
		userShipping.setUser(user);
		userShipping.setUserShippingDefault(true);
		user.getUserShippingList().add(userShipping);
		save(user);
	}
	
	@Override
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	public void setUserDefaultShipping(Long userShippingId, User user) {
		List<UserShipping> userShippingList = user.getUserShippingList();
		
		userShippingList.forEach(us -> {
			us.setUserShippingDefault(us.getId().equals(userShippingId));
			userShippingRepository.save(us);
		});
	}
	
	@Override
	@Transactional
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	public void removeUserPayment(User user, UserPayment userPayment) {
		//userPaymentRepository.delete(userPayment);
		user.getUserPaymentList().remove(userPayment);
		//userRepository.save(user);
		// if this card was Default, then set next credit card as Default
		if (userPayment.isDefaultPayment()) { 
			if (user.getUserPaymentList().listIterator().hasNext()) {
				Long nextPaymentId = user.getUserPaymentList().listIterator().next().getId();
				setUserDefaultPayment(nextPaymentId, user);
			}
		}
	}
	
	@Override
	@Transactional
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	public void removeUserShipping(User user, UserShipping userShipping) {
		//userShippingRepository.delete(userShipping);
		user.getUserShippingList().remove(userShipping);
		// if this address was Default, then set next address as Default
		if (userShipping.isUserShippingDefault()) { 
			if (user.getUserShippingList().listIterator().hasNext()) {
				Long nextShippingId = user.getUserShippingList().listIterator().next().getId();
				setUserDefaultShipping(nextShippingId, user);
			}
		}
	}
	
	@Override
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	public UserShipping getDefaultUserShipping(User user) {
		for (UserShipping userShipping : user.getUserShippingList()) {
			if (userShipping.isUserShippingDefault()) 
				return userShipping; 
		}
		return null;
	}
	
	@Override
	@Secured({"ROLE_USER", "ROLE_ADMIN"})
	public UserPayment getDefaultUserPayment(User user) {
		for (UserPayment userPayment : user.getUserPaymentList()) {
			if (userPayment.isDefaultPayment()) 
				return userPayment; 
		}
		return null;
	}

}

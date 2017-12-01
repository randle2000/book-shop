package com.sln.bshop.repository;

import java.util.Date;
import java.util.stream.Stream;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.sln.bshop.domain.User;
import com.sln.bshop.domain.security.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
	
	// Tried to implement cache support with EhCache as explained in Chapter 13 of "Spring in Action 4th Edition" book
	// It works but if cached entity has reference to other entity with Lazy initialization, the you'll have LazyInitializationException.
	// It seems that Spring's cache it's not suitable for hibernate entities, you have to use Hibernate 2nd level cache instead. 
	// This article explains it: https://stackoverflow.com/a/36168024/6094091
	@Cacheable("bshopCache")
	PasswordResetToken findByToken(String token);
	
	PasswordResetToken findByUser(User user);
	
	Stream<PasswordResetToken> findAllByExpiryDateLessThan(Date now);
	
	@Modifying
	@Query("delete from PasswordResetToken t where t.expiryDate <= ?1")
	void deleteAllExpiredSince(Date now);
	

}

package org.fullstack.dheeraj.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.fullstack.dheeraj.models.Contact;
import org.fullstack.dheeraj.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

	User findByUsername(String username);
	
	@Query("Select c from Contact c where c.username like %:username%")
	List<Contact> searchByUsername(@Param("username") String username);
	
	@Query("Select u from User u where u.phoneNumber = :phoneNumber")
	User findByPhoneNumber(@Param("phoneNumber") String phoneNumber);
	
	Boolean existsByPhoneNumber(String phoneNumber);
	
	@Transactional
	@Modifying
	@Query("UPDATE Spam t set t.spamCount = t.spamCount + 1 WHERE t.phoneNumber = :phoneNumber")
	int updateSpamCount(@Param("phoneNumber") String phoneNumber);
	
	
	
	
	
}

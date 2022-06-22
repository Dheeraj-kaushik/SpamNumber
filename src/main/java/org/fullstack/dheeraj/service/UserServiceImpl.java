package org.fullstack.dheeraj.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.fullstack.dheeraj.Exceptions.RecordNotFoundException;
import org.fullstack.dheeraj.dto.SearchResultDto;
import org.fullstack.dheeraj.models.User;
import org.fullstack.dheeraj.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {

	@Autowired
	UserRepository userRepository;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private HttpSession httpSession;

	
	public User loadUserByUsername(String username) throws Exception {
		User user = userRepository.findByUsername(username);
		if(user==null) {
			throw new Exception("no user found with username "+username);
		}else {
			return user;
		}
	}
	
	// Save user Details in repository
	public User save(User user) {
		return userRepository.save(user);
	}
	
	//Check if user exists through Phone Number
	public boolean userExistsByPhoneNumber(String phoneNumber) {
		return userRepository.existsByPhoneNumber(phoneNumber);
	}
	
	
	//Fetch user by phone number
	public User findUserByContact(String contact) throws Exception {
		User user = userRepository.findByPhoneNumber(contact);
		if(user==null) {
			throw new Exception("User Not Found with contact: " + contact);
		}
		return user;
	}
	
	//Update spam count of a contact
	public int markSpam(String phoneNumber) {
		return userRepository.updateSpamCount(phoneNumber);
	}
	
	//Insert new contact in spam list
	@Transactional
	public void addToSpam(String phoneNumber, String name) throws RecordNotFoundException {
		entityManager.createNativeQuery("INSERT INTO Spam (phoneNumber, spamCount) VALUES (?1,?2)").setParameter(1, phoneNumber)
				.setParameter(2, 1).executeUpdate();

		entityManager.createNativeQuery("INSERT INTO Contacts (phoneNumber, name) VALUES (?1,?2)").setParameter(1, phoneNumber)
				.setParameter(2, name).executeUpdate();

	}
	
	//Check if contact exists in spam list
	@Transactional
	public int checkSpamByPhoneNumber(String phoneNumber) throws RecordNotFoundException {
		List<String> s = entityManager.createQuery("SELECT s.phoneNumber FROM Spam s where s.phoneNumber=?1", String.class)
				.setParameter(1, phoneNumber).getResultList();
		return s.size();

	}
	
	//Search contact by name in global database
	@Transactional
	public List<SearchResultDto> searchByName(String username) throws RecordNotFoundException {
		List<Object[]> results = entityManager.createQuery(
				"SELECT c.username,c.phoneNumber,s.spamCount from Contact c left join  Spam s on c.phoneNumber=s.phoneNumber where c.username like CONCAT('%',?1,'%') ",
				Object[].class).setParameter(1, username).getResultList();
		List<SearchResultDto> list = new ArrayList<>();

		for (Object[] row : results) {
			SearchResultDto search = new SearchResultDto();
			search.setUsername((String) row[0]);
			search.setPhoneNumber((String) row[1]);
			if (null != row[2])
				search.setSpamcount(row[2].toString());
			else
				search.setSpamcount("0");
			list.add(search);
		}
		return list;

	}
	
	//Search user by name in registered users
	@Transactional
	public List<SearchResultDto> searchByNameRegistered(String username) throws RecordNotFoundException {
		List<Object[]> results = entityManager.createQuery(
				"SELECT u.username,u.phoneNumber,u.email,s.spamCount from User u left join  Spam s on u.phoneNumber=s.phoneNumber where u.username like CONCAT('%',?1,'%')",
				Object[].class).setParameter(1, username).getResultList();

		if (results.isEmpty()) {
			throw new RecordNotFoundException("No User with Name: " + username + " was found");
		}
		List<SearchResultDto> list = new ArrayList<>();

		for (Object[] row : results) {
			SearchResultDto search = new SearchResultDto();
			search.setUsername((String) row[0]);
			search.setPhoneNumber((String) row[1]);
			search.setSpamcount((String) row[2]);

			list.add(search);
		}
		return list;

	}

	//Search user by phone Number from registered users
	@Transactional
	public List<SearchResultDto> searchByContactRegistered(String phoneNumber) throws RecordNotFoundException {
		List<Object[]> results = entityManager.createQuery(
				"SELECT u.username,u.phoneNumber,s.spamCount from User u left join  Spam s on u.phoneNumber=s.phoneNumber where u.phoneNumber like CONCAT('%',?1,'%') ",
				Object[].class).setParameter(1, phoneNumber).getResultList();
		if (results.isEmpty()) {
			throw new RecordNotFoundException("User with Contact Number: " + phoneNumber + " not found");
		}
		List<SearchResultDto> list = new ArrayList<>();

		for (Object[] row : results) {
			SearchResultDto search = new SearchResultDto();
			search.setUsername((String) row[0]);
			search.setPhoneNumber((String) row[1]);
			if(null!=row[2])
			search.setSpamcount(row[2].toString());
			else
			search.setSpamcount("0");	
			list.add(search);
		}
		return list;

	}
	
	//Find contact by phone number in global database
	@Transactional
	public List<SearchResultDto> searchByPhoneNumberInGlobal(String phoneNumber) throws RecordNotFoundException {
		List<Object[]> results = entityManager.createQuery(
				"SELECT c.username,c.phoneNumber,s.spamCount from Contact c left join  Spam s on c.phoneNumber=s.phoneNumber where c.phoneNumber like CONCAT('%',?1,'%') ",
				Object[].class).setParameter(1, phoneNumber).getResultList();

		if (results.isEmpty()) {
			throw new RecordNotFoundException("Contact Number: " + phoneNumber + " not found");
		}
		List<SearchResultDto> list = new ArrayList<>();

		for (Object[] row : results) {
			SearchResultDto search = new SearchResultDto();
			search.setUsername((String) row[0]);
			search.setPhoneNumber((String) row[1]);
			if (null != row[2])
				search.setSpamcount(row[2].toString());
			else
				search.setSpamcount("0");
			list.add(search);
		}
		return list;

	}
	
	//Get All details of contact if present in contact list
	@Transactional
	public List<SearchResultDto> getContactDetails(String phoneNumber) throws RecordNotFoundException {

		List<Object[]> results = entityManager.createQuery(
				"SELECT u.username,u.phoneNumber,s.spamCount,u.email from User u left join  Spam s on u.phoneNumber=s.phoneNumber where u.phoneNumber like CONCAT('%',?1,'%') ",
				Object[].class).setParameter(1, phoneNumber).getResultList();

		List<SearchResultDto> list = new ArrayList<>();
		if (results.isEmpty()) {
			throw new RecordNotFoundException("Contact Number: " + phoneNumber + " not found");
		}
		for (Object[] row : results) {
			SearchResultDto search = new SearchResultDto();
			search.setUsername((String) row[0]);
			search.setPhoneNumber((String) row[1]);
			if(null!=row[2])
			search.setSpamcount((String) row[2]);
			else
				search.setSpamcount("0");
			
			search.setEmail((String) row[2]);
			

			list.add(search);
		}
		return list;

	}
	
	
	
	
	
	
	
	
	
	
	
}

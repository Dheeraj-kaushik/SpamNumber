package org.fullstack.dheeraj.controller;

import org.fullstack.dheeraj.Exceptions.RecordNotFoundException;
import org.fullstack.dheeraj.models.User;
import org.fullstack.dheeraj.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/search")
@CrossOrigin
@RestController
public class SearchController {

	@Autowired
	private UserServiceImpl userDetailsService;
	
	//Search person by phone Number
	@RequestMapping(value = "/searchByPhoneNumber/{phoneNumber}", method = RequestMethod.GET)
	public ResponseEntity<?> searchByPhoneNumber(@PathVariable String phoneNumber) throws RecordNotFoundException {
		if (userDetailsService.userExistsByPhoneNumber(phoneNumber)) {
			return ResponseEntity.ok(userDetailsService.searchByContactRegistered(phoneNumber));
		} else {
			return ResponseEntity.ok(userDetailsService.searchByPhoneNumberInGlobal(phoneNumber));
		}
	}
	
	 //Search person by name
	@RequestMapping(value = "/searchByName/{username}", method = RequestMethod.GET)
	public ResponseEntity<?> searchByName(@PathVariable String username) throws RecordNotFoundException {
		if (userDetailsService.searchByName(username).isEmpty()) {
			return ResponseEntity.ok(userDetailsService.searchByNameRegistered(username));
		} else {
			return ResponseEntity.ok(userDetailsService.searchByName(username));
		}
	}
	
	//Full Details of contact by phone number
	@RequestMapping(value = "/details/{contact}", method = RequestMethod.GET)
	public ResponseEntity<?> getDetailsByContact(@PathVariable String phoneNumber) throws RecordNotFoundException {
		if (userDetailsService.userExistsByPhoneNumber(phoneNumber)) {
			return ResponseEntity.ok(userDetailsService.getContactDetails(phoneNumber));
		} else {
			return ResponseEntity.ok(userDetailsService.searchByPhoneNumberInGlobal(phoneNumber));
		}
	}
	
	//Save user
	@RequestMapping(value="/saveUser", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody User user) throws Exception{
		User tempUser = userDetailsService.save(user);
		if(tempUser==null) {
			throw new Exception("user not created");
		}else {
			return ResponseEntity.ok(tempUser);
		}
	}
	
	
	
	
}

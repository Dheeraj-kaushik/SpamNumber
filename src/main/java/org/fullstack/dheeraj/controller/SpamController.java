package org.fullstack.dheeraj.controller;

import org.fullstack.dheeraj.Exceptions.RecordNotFoundException;
import org.fullstack.dheeraj.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RequestMapping("/spam")
@RestController
public class SpamController {

	@Autowired
	UserServiceImpl userDetailsService;
	
	//Add number and name to spam list
	@RequestMapping(value = "/markSpam/{phoneNumber}", method = RequestMethod.POST)
	public ResponseEntity<?> addToSpam(@PathVariable String phoneNumber, @RequestParam String name)
			throws RecordNotFoundException {

		if (userDetailsService.checkSpamByPhoneNumber(phoneNumber) > 0) {
			return ResponseEntity.ok(userDetailsService.markSpam(phoneNumber));
		} else {
			userDetailsService.addToSpam(phoneNumber, name);
			return new ResponseEntity<>("Spam Added!!", HttpStatus.CREATED);
		}

	}

	
	
}

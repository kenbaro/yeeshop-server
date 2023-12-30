package com.yeeshop.yeeserver.app.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yeeshop.yeeserver.app.form.ContactForm;
import com.yeeshop.yeeserver.domain.dto.contact.ContactDto;
import com.yeeshop.yeeserver.domain.service.IContactService;

/**
 * API for control Home Page.
 * @author Thai Duy Bao.
 *
 * since 2023
 */
@RestController
@RequestMapping("/customer")
@CrossOrigin("*")
public class ContactApi {

	@Autowired 
	private ModelMapper mapper;
	
	@Autowired
	private IContactService contactService;
	
	@GetMapping("/contact")
	public ResponseEntity<?> index() {
		
		// Data Transfer Object for Contact API.
		ContactDto contactDto = new ContactDto();
		
		// set Contact Information
		this.contactService.getShopContact(contactDto);
		
		// map Data into Form;
		ContactForm contactForm = this.mapper.map(contactDto, ContactForm.class);
		
		return new ResponseEntity<>(contactForm, HttpStatus.OK);
	}
}

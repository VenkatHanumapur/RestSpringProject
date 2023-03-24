package com.learing.restproject;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.learing.restproject.exception.UserNotFoundException;
import com.learing.restproject.user.User;
import com.learing.restproject.user.persistence.UserDao;

import jakarta.validation.Valid;

@RestController
public class UserResource {

	@Autowired
	private UserDao userDao;


	@GetMapping("/users")
	public List<User> retrieveAllUsers() {
		return userDao.findAll();
	}

	@PostMapping("/users")
	public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
		User savedUser = userDao.save(user);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping("/users/{id}")
	public EntityModel<User> retrieveUser(@PathVariable int id) {
		
		User user = userDao.findById(id);
		
		EntityModel<User> entityModel = EntityModel.of(user);

		List<User> retrieveAllUsers = WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUsers();
		WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(retrieveAllUsers);
		entityModel.add(linkTo.withRel("all_users"));
		if(user==null) {
			throw new UserNotFoundException("id : "+id+"  not found");
		}
		return entityModel;
		
	}
	@DeleteMapping("/users/{id}")
	public void deleteUser(@PathVariable int id) {
		userDao.deleteById(id);
	}
	
	

}

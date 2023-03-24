package com.learing.restproject;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
import com.learing.restproject.user.Post;
import com.learing.restproject.user.User;
import com.learing.restproject.user.persistence.PostsJpaRepository;
import com.learing.restproject.user.persistence.UserJpaRepository;

import jakarta.validation.Valid;

@RestController
public class UserJpaResource {


    @Autowired
	private UserJpaRepository userJpaRepository;
    
    @Autowired
    private PostsJpaRepository postsJpaRepository;

	@GetMapping("/jpa/users")
	public List<User> retrieveAllUsers() {
		return userJpaRepository.findAll();
	}

	@PostMapping("/jpa/users")
	public ResponseEntity<User> addUser(@Valid @RequestBody User user) {
		User savedUser = userJpaRepository.save(user);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedUser.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping("/jpa/users/{id}")
	public EntityModel<User> retrieveUser(@PathVariable int id) {
		
		Optional<User> user = userJpaRepository.findById(id);
		if(user.isEmpty()) {
			throw new UserNotFoundException("id : "+id+"  not found");
		}
		
		EntityModel<User> entityModel = EntityModel.of(user.get());

		List<User> retrieveAllUsers = WebMvcLinkBuilder.methodOn(this.getClass()).retrieveAllUsers();
		WebMvcLinkBuilder linkTo = WebMvcLinkBuilder.linkTo(retrieveAllUsers);
		entityModel.add(linkTo.withRel("all_users"));
		
		return entityModel;
		
	}
	@GetMapping(path="/jpa/users", params="name")
	public List<User> retrieveUserByName(@Param("name") String name) {
		
		return userJpaRepository.findByName(name);
	}
	
	
	@DeleteMapping("/jpa/users/{id}")
	public void deleteUser(@PathVariable int id) {
		userJpaRepository.deleteById(id);
	}
	
	@GetMapping("/jpa/users/{id}/posts")
	public List<Post> retrievePostsByUser(@PathVariable int id) {
		Optional<User> user = userJpaRepository.findById(id);
		if(user.isEmpty()) {
			throw new UserNotFoundException("User with ID : "+id+" not found");
		}		
	  return	user.get().getPosts();		
	}
	
	@PostMapping("/jpa/users/{id}/posts")
	public ResponseEntity<Post> addPosts(@PathVariable int id,@RequestBody Post post) {
		Optional<User> user = userJpaRepository.findById(id);
		if(user.isEmpty())
			throw new UserNotFoundException("User with ID : "+id +" not found ");
		post.setUser(user.get());
		Post savedPost = postsJpaRepository.save(post);
		URI uri=ServletUriComponentsBuilder.fromCurrentRequest().path("/{postid}").buildAndExpand(savedPost.getId()).toUri();
		return  ResponseEntity.created(uri).build();		
		
	}
	
	@GetMapping("/jpa/users/{id}/posts/{postid}")
	public EntityModel<Post> retrieveByPostId(@PathVariable int id,@PathVariable int postid) {
		Optional<Post> post = postsJpaRepository.findById(postid);
		if(post.isEmpty())
			throw new PostNotFoundException("Post with ID :"+id+" not Found");
		List<Post> retrievePostsByUser = WebMvcLinkBuilder.methodOn(this.getClass()).retrievePostsByUser(id);
		WebMvcLinkBuilder link = WebMvcLinkBuilder.linkTo(retrievePostsByUser);
		EntityModel<Post> entityModel = EntityModel.of(post.get());
		return entityModel.add(link.withRel("all_posts"));
		
	}
	@DeleteMapping("/jpa/users/{id}/posts/{postid}")
	public void deletePost(@PathVariable int id,@PathVariable int postid) {
		postsJpaRepository.deleteById(postid);		
	}
}

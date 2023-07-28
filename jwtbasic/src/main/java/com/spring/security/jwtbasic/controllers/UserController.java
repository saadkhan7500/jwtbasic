package com.spring.security.jwtbasic.controllers;

import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.spring.security.jwtbasic.dto.UserDto;
import com.spring.security.jwtbasic.entities.User;
import com.spring.security.jwtbasic.exceptions.MissingTokenException;
import com.spring.security.jwtbasic.services.UserService;
import com.spring.security.jwtbasic.utils.JwtTokenProvider;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.micrometer.common.util.StringUtils;

@RestController
public class UserController {

	@Autowired
	private JwtTokenProvider jwtUtil;

	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UserDto userDto) {
		User user = userService.findByUsernameAndPassword(userDto.getUsername(), userDto.getPassword());

		if (user == null || !user.getUsername().equals(userDto.getUsername())
				&& user.getPassword().equals(userDto.getPassword())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
		}
		return jwtUtil.generateToken(user.getUsername());
	}

	@PostMapping("/save-user")
	public ResponseEntity<Object> saveUser(@RequestHeader(name = "Authorization", required = false) String token,
			@RequestBody User user) {

		System.out.println(token);
		if (StringUtils.isBlank(token) || !token.startsWith("Bearer ")) {
			throw new MissingTokenException("Authorization token is missing or invalid");
		}

		try {
			jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));
			userService.saveUser(user);
			User savedUser = userService.saveUser(user);
			return ResponseEntity.ok(savedUser); // Return the user details in the response body
		}

		catch (ExpiredJwtException e) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired. Please log in again.");
		}

		catch (JwtException e) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token. Please log in again.");
		}

		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while saving the user.");
		}
	}

	@GetMapping("/get-user/{id}")
	public ResponseEntity<?> getUser(@RequestHeader(name = "Authorization", required = false) String token,
			@PathVariable int id) {

		if (StringUtils.isBlank(token)) {
			throw new MissingTokenException("Authorization token is missing.");
		}

		try {
			String t=jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));
			System.out.println(t);
			User user = userService.getUser(id);
			return ResponseEntity.ok(user);
		} catch (ExpiredJwtException e) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired. Please log in again.");
		}

		catch (JwtException e) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token. Please log in again.");
		}

		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while saving the user.");
		}

	}

	@GetMapping("/get-users")
	public ResponseEntity<?> getUsers(@RequestHeader(name = "Authorization", required = false) String token) {

		if (StringUtils.isBlank(token)) {
			throw new MissingTokenException("Authorization token is missing.");
		}
		
		try {
			jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));
			List<User> user = userService.getUsers();
			return ResponseEntity.ok(user);
		} catch (ExpiredJwtException e) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired. Please log in again.");
		}

		catch (JwtException e) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token. Please log in again.");
		}

		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while saving the user.");
		}
	}
	
	@PutMapping("/update-user")
	public ResponseEntity<?> updateUser(
			                            @RequestHeader("Authorization")String token, 
			                            @PathVariable int id, 
			                            @RequestBody User user)
	{
		if (StringUtils.isBlank(token)) {
			throw new MissingTokenException("Authorization token is missing.");
		}
		
		try {
			jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));
			User updatedUser = userService.updateUser(id,user);
			return ResponseEntity.ok(updatedUser);
		} catch (ExpiredJwtException e) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired. Please log in again.");
		}

		catch (JwtException e) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token. Please log in again.");
		}

		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while saving the user.");
		}
	}
	
	@DeleteMapping("/delete-user/{id}")
	public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String token,@PathVariable int id)
	{
		if (StringUtils.isBlank(token)) {
			throw new MissingTokenException("Authorization token is missing.");
		}
		
		try {
			jwtUtil.getUsernameFromToken(token.replace("Bearer ", ""));
			userService.deleteUser(id);
			return ResponseEntity.status(HttpStatus.OK).body("user has been deleted");
		} catch (ExpiredJwtException e) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token has expired. Please log in again.");
		}

		catch (JwtException e) {

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token. Please log in again.");
		}

		catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while saving the user.");
		}
	}
	
	
}

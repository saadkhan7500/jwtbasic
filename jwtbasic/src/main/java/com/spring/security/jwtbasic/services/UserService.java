package com.spring.security.jwtbasic.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.security.jwtbasic.dto.UserDto;
import com.spring.security.jwtbasic.entities.User;
import com.spring.security.jwtbasic.repositories.UserRepo;

@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;
	
	
	public User saveUser(User user)
	{
		return userRepo.save(user);
	}
	
	public User getUser(int id)
	{
		return userRepo.findById(id);
	}
	
	public List<User> getUsers()
	{
		return (List<User>) userRepo.findAll();
	}
	
	public User updateUser(int id , User user)
	{
		User existingUser=userRepo.findById(id);
		if(existingUser !=null)
		{
			existingUser.setName(user.getName());
			existingUser.setEmail(user.getEmail());
			existingUser.setPhone(user.getPhone());
			existingUser.setAddress(user.getAddress());
			existingUser.setUsername(user.getUsername());
			existingUser.setPassword(user.getPassword());
		}
		return userRepo.save(existingUser);
	}
	
	public User findByUsernameAndPassword(String username, String password)
	{
		return userRepo.findByUsernameAndPassword(username, password);
		
	}
	public void deleteUser(int id)
	{
		userRepo.deleteById(id);
	}
}

package com.spring.security.jwtbasic.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.spring.security.jwtbasic.entities.User;

@Repository
public interface UserRepo extends CrudRepository<User, Integer> {
	
	User findById(int id);
	
	User findByUsernameAndPassword(String username , String password);

	User findByUsername(String username);
	
}

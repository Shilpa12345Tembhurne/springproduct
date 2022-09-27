package com.springboot.first.demo.user;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;


 
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

	  @Autowired private UserRepository repo;
	     
	    @Test
	    public void testCreateUser()
	    {
	       PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	       String rawPassword="john123";
	       String encodedPassword=passwordEncoder.encode(rawPassword);
	       
	       User newUser=new User("john@gmail.com",encodedPassword);
	       User savedUser=repo.save(newUser);
	       
	    assertThat(savedUser).isNotNull();
	    assertThat(savedUser.getId()).isGreaterThan(0);
	      
	       
	       
	        
	    }
}

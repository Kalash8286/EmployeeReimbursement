package com.example.services.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dto.ReimbursementTypeDTO;
import com.example.entities.ReimbursementType;
import com.example.entities.Users;
import com.example.repositories.ReimbursementTypeRepo;
import com.example.repositories.UsersRepo;
import com.example.services.ReimbursementTypeService;

/**
 * Service class for Users
 * 
 * @auther Kalash Vishwakarma
 */
@Service
public class UserServiceImpl {

	UsersRepo userRepo;;

	@Autowired
	public UserServiceImpl(UsersRepo userRepo) {
		super();
		this.userRepo = userRepo;
	}

	/**
	 * Retrieves all users
	 *
	 * @return List of users
	 */
	// Get all users
	public Users getUser(String username, String password) {
		List<Users> userList = (List<Users>) userRepo.findAll();

		userList = userList.stream().filter(user -> user.getUsername().equals(username))
				.filter(user -> user.getPassword().equals(password)).collect(Collectors.toList());

		if (userList.size() == 0) {
			throw new RuntimeException("Invalid User");
		}
		return userList.get(0);

	}

}

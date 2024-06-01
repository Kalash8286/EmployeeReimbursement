package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entities.Users;
import com.example.services.Impl.UserServiceImpl;

import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Controller class for handling users Manages endpoints related to Users
 *
 * @RestController indicates that this class is a Spring REST controller.
 * @RequestMapping specifies the base URL path for all endpoints in this
 *                 controller. In this case, all endpoints under this controller
 *                 will start with "/api/reimbursements".
 */
@Tag(name = "Users", description = "Handling User Requests" )
@RestController
@RequestMapping("api/reimbursements")
@CrossOrigin(origins = "http://localhost:4200")
public class UsersController {

	private UserServiceImpl userService;

	/**
	 * Constructor for the controller.
	 *
	 * @param userService an instance of the UserServiceImple. Autowired by Spring
	 *                    to inject the service implementation.
	 */
	@Autowired
	public UsersController(UserServiceImpl userService) {
		super();
		this.userService = userService;
	}

	/**
	 * Retrieves all users
	 *
	 * @return ResponseEntity containing a list of Users or a BAD_REQUEST status if
	 *         not found.
	 */
	@PostMapping("users")
	public ResponseEntity<Users> getUsers(@RequestBody Users inputUser) {

		
		Users user = userService.getUser(inputUser.getUsername(), inputUser.getPassword());

		if (user.getUsername().equals(inputUser.getUsername()) && user.getPassword().equals(inputUser.getPassword())) {

			return new ResponseEntity<Users>(user, HttpStatus.OK);

		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

	}
}

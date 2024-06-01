package com.example.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.entities.Users;

public interface UsersRepo extends CrudRepository<Users, Integer>{

}

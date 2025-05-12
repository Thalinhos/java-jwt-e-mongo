package com.example.demo.Repository;

// import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.Model.User;

public interface UserRepository extends MongoRepository<User, String> {
    // User findByUserName(String userName);
    // Optional<User> findById(String id);
    // void deleteById(String id);
    // void deleteByUserName(String userName);

}

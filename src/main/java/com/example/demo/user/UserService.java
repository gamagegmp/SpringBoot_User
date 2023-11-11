package com.example.demo.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public void addNewUser(User user) {
        Optional<User> userOptional = userRepository
                .findUserByEmail(user.getEmail());
        if(userOptional.isPresent()) {
            throw new IllegalStateException("Email is Already Taken!");
        }
        userRepository.save(user);
    }

    public void deleteUser(Integer userId) {
        Boolean exists = userRepository.existsById(userId);
        if(!exists) {
            throw new IllegalStateException("User with id: " + userId + " does not exist");
        }
        userRepository.deleteById(userId);
    }

    @Transactional
    public void updateUser(Integer userId, User user) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User with id: " + userId + " does not exist"));

        if(user.getFirstName() != null &&
                user.getFirstName().length() > 0 &&
                !Objects.equals(existingUser.getFirstName(), user.getFirstName())) {
            existingUser.setFirstName(user.getFirstName());
        }

        if(user.getLastName() != null &&
                user.getLastName().length() > 0 &&
                !Objects.equals(existingUser.getLastName(), user.getLastName())) {
            existingUser.setLastName(user.getLastName());
        }

        if(user.getAddress() != null &&
                user.getAddress().length() > 0 &&
                !Objects.equals(existingUser.getAddress(), user.getAddress())) {
            existingUser.setAddress(user.getAddress());
        }
    }
}

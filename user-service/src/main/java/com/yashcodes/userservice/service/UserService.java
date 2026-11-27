package com.yashcodes.userservice.service;

import com.yashcodes.userservice.dto.UserRequest;
import com.yashcodes.userservice.dto.UserResponse;
import com.yashcodes.userservice.entity.User;
import com.yashcodes.userservice.exception.DuplicateEmailException;
import com.yashcodes.userservice.exception.UserNotFoundException;
import com.yashcodes.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Locale;


@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //create User
    @Transactional
    public UserResponse createUser(UserRequest request) {
        String email = normalizeEmail(request.email());
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("User already exist with this email: " + email);
        }
        User user = new User();
        user.setName(request.name().trim());
        user.setEmail(email);
        user.setPhone(request.phone());

        User savedUser = userRepository.save(user);
        return mapToResponse(savedUser);
    }

    //get all user
    public List<UserResponse> getAllUser() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    //get user by id
    public UserResponse getUSerById(Long id) {
        User user = userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        return mapToResponse(user);
    }

    // Update User
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not Found with id: " + id));
        String email = normalizeEmail(request.email());
        if (userRepository.existsByEmailAndIdNot(email, id)) {
            throw new DuplicateEmailException("Email already used by another user: " + email);
        }
        user.setName(request.name().trim());
        user.setEmail(email);
        user.setPhone(request.phone());
        User updateUser = userRepository.save(user);
        return mapToResponse(updateUser);
    }

    // Delete User
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException("User not Found with id: " + id));

        userRepository.delete(user);
    }

    private UserResponse mapToResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone()
        );
    }

    private String normalizeEmail(String email) {
        return email.toLowerCase(Locale.ROOT);
    }


}

package com.example.demo.service;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.exceptions.UsernameAlreadyExistsException;
import com.example.demo.repository.UserRepository;
import com.example.demo.validator.UserRegisterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service("userService")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public void saveUser(UserRegisterValidator userRegisterValidator) {
        User foundedUser = userRepository.findByUserName(userRegisterValidator.getUserName());

        if(foundedUser != null) {
            throw new UsernameAlreadyExistsException("Username '" + foundedUser.getUserName() + "' already exists");
        }

        Role[] roles = new Role[]{new Role("USER")};

        User user = new User();

        user.setUserName(userRegisterValidator.getUserName());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setMatchingPassword("");
        user.setFirstName(userRegisterValidator.getFirstName());
        user.setLastName(userRegisterValidator.getLastName());
        user.setEmail(userRegisterValidator.getEmail());
        user.setActive(true);
        user.setRoles(Arrays.asList(roles));

        userRepository.save(user);
    }

    public User getUserByPasswordAndUsername(String password, String userName) {
        return userRepository.getUserByPasswordAndUserName(password, userName);
    }
}

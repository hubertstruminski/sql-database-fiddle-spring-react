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
import java.util.Date;

@Service("userService")
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public User saveUser(UserRegisterValidator userRegisterValidator) {
        User foundedUser = userRepository.findByUserName(userRegisterValidator.getUserName());

        if(foundedUser != null) {
            throw new UsernameAlreadyExistsException("Username '" + foundedUser.getUserName() + "' already exists");
        }

        Role[] roles = new Role[]{new Role("USER")};

        User user = new User();

        user.setUserName(userRegisterValidator.getUserName());
        user.setPassword(bCryptPasswordEncoder.encode(userRegisterValidator.getPassword()));
        user.setMatchingPassword("");
        user.setFirstName(userRegisterValidator.getFirstName());
        user.setLastName(userRegisterValidator.getLastName());
        user.setEmail(userRegisterValidator.getEmail());
        user.setActive(true);
        user.setCreateAt(new Date());
        user.setRoles(Arrays.asList(roles));

        return userRepository.save(user);
    }

    public User getUserByPasswordAndUsername(String password, String userName) {
        return userRepository.getUserByPasswordAndUserName(password, userName);
    }

    public User converFromUserValidator(UserRegisterValidator userValidator) {
        User user = new User();

        user.setUserName(userValidator.getUserName());
        user.setPassword(userValidator.getPassword());
        user.setMatchingPassword(userValidator.getMatchingPassword());
        user.setFirstName(userValidator.getFirstName());
        user.setLastName(userValidator.getLastName());
        user.setEmail(userValidator.getEmail());
        user.setCreateAt(new Date());

        return user;
    }
}

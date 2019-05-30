package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.UserService;
import com.example.demo.service.MapErrorValidator;
import com.example.demo.validator.UserRegisterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Controller
public class UserController {

    @Autowired
    private MapErrorValidator mapErrorValidator;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRegisterValidator userRegisterValidator;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterValidator userValidator, BindingResult result) {
        userRegisterValidator.validate(userValidator, result);

        ResponseEntity<?> errorMap = mapErrorValidator.validateToMap(result);

        if(errorMap != null) {
            return errorMap;
        }

        User newUser = userService.saveUser(userValidator);
        return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
    }
}

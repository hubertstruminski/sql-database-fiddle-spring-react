package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.exceptions.InvalidLoginResponse;
import com.example.demo.payload.JwtLoginSuccessResponse;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.UserService;
import com.example.demo.service.MapErrorValidator;
import com.example.demo.validator.UserRegisterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

import static com.example.demo.security.SecurityConstants.TOKEN_PREFIX;

@Controller
@CrossOrigin
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

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody InvalidLoginResponse invalidLoginResponse, BindingResult result) {
        ResponseEntity<?> errorMap = mapErrorValidator.validateToMap(result);

        if(errorMap != null) {
            return errorMap;
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(invalidLoginResponse.getUserName(), invalidLoginResponse.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX + tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtLoginSuccessResponse(true, jwt));
    }

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

    @GetMapping("/message")
    public ResponseEntity<?> passWelcomeRegistrationMessage() {
        Boolean isRegisteredUser = true;
        return new ResponseEntity<Boolean>(isRegisteredUser, HttpStatus.OK);
    }
}

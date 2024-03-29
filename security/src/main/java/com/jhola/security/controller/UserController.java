package com.jhola.security.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jhola.security.configuration.JwtTokenProvider;
import com.jhola.security.configuration.SecurityConstants;
import com.jhola.security.dto.JWTLoginSucessReponse;
import com.jhola.security.dto.LoginRequest;
import com.jhola.security.dto.UserDTO;
import com.jhola.security.model.UserEntity;
import com.jhola.security.service.UserService;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {


    @Autowired
    private UserService userService;


    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

   
    	
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = SecurityConstants.TOKEN_PREFIX + tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTLoginSucessReponse(true, jwt));
    }
    
    

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
        
    	UserEntity newUser = userService.saveUser(userDTO);
        
        return new ResponseEntity<UserEntity>(newUser, HttpStatus.CREATED);
    }
}

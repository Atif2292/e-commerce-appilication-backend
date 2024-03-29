package com.jhola.security.service;


import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.jhola.security.dto.UserDTO;
import com.jhola.security.exception.UsernameAlreadyExistsException;
import com.jhola.security.model.UserEntity;
import com.jhola.security.repository.UserRepository;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserEntity saveUser (UserDTO newUser){

        try{
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            
            //Username has to be unique (exception)
            newUser.setUsername(newUser.getUsername());
            
            // Make sure that password and confirmPassword match
            // We don't persist or show the confirmPassword
            newUser.setConfirmPassword("");
            
            UserEntity user = modelMapper.map(newUser, UserEntity.class);
            
            return userRepository.save(user);

        }catch (Exception e){
            throw new UsernameAlreadyExistsException("Username '"+newUser.getUsername()+"' already exists");
        }

    }



}

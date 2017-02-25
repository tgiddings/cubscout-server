package com.robocubs4205.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by trevor on 2/13/17.
 */
@RestController
public class UserController {
    private UserRepository userRepository;
    public UserController(@Autowired UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @RequestMapping(value = "/user/{user}",method = RequestMethod.GET)
    User get(@PathVariable User user){
        if(user==null) throw new ResourceNotFoundException();
        return user;
    }
    @RequestMapping(value = "/user/{user}",method = RequestMethod.PUT)
    User edit(@PathVariable User user, @RequestBody User newUser){
        if(user ==null) throw new ResourceNotFoundException();
        newUser.setId(user.getId());
        userRepository.save(newUser);
        return newUser;
    }
    @RequestMapping(value = "/users",method = RequestMethod.GET)
    List<User> getAll(){
        return userRepository.findAll();
    }
    @RequestMapping(value = "/user",method = RequestMethod.POST)
    User create(@RequestBody User newUser){
        userRepository.save(newUser);
        return newUser;
    }
}

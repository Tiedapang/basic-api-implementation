package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    List<User> userList = new ArrayList<User>();
    @Autowired
    UserRepository userRepository;
    @PostMapping("/user")
    public ResponseEntity addUser(@RequestBody @Valid User user) throws JsonProcessingException {
        if(!userList.contains(user)){
            userList.add(user);
        }
        return ResponseEntity.created(null)
                .header("index",String.valueOf(userList.size()-1)).build();
    }
    @GetMapping("/user")
    public ResponseEntity getUserList(){
        return ResponseEntity.ok(userList);
    }
    @PostMapping("/user/dataBase")
    public ResponseEntity addUserOnDataBase(@RequestBody @Valid User user) throws JsonProcessingException {
        UserPO userPO = new UserPO();
        userPO.setName(user.getName());
        userPO.setGender(user.getGender());
        userPO.setAge(user.getAge());
        userPO.setEmail(user.getEmail());
        userPO.setPhone(user.getPhone());
        userPO.setVoteNmu(user.getVoteNmu());
        userRepository.save(userPO);
        return ResponseEntity.ok(userPO);
    }
    @GetMapping("/user/{userID}")
    public ResponseEntity getUserByIndex(@PathVariable int userID){
        Optional<UserPO> userPO = userRepository.findById(userID);
        return ResponseEntity.ok(userPO);
    }

}

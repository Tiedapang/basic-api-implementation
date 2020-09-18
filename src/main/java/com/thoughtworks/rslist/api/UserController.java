package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @PostMapping("/user")
    public ResponseEntity addUser(@RequestBody @Valid User user) throws JsonProcessingException {
        UserPO userPo = userRepository.save(UserPO.builder().name(user.getName()).voteNmu(user.getVoteNmu())
                .age(user.getAge()).email(user.getEmail()).gender(user.getGender())
                .phone(user.getPhone()).build());
        return ResponseEntity.created(null)
                .header("index",String.valueOf(userPo.getId())).build();
    }
    @GetMapping("/user")
    public ResponseEntity getUserList(){
        List<UserPO> userPOS = userRepository.findAll();
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity userExceptionHandler(MethodArgumentNotValidException e){
        Error error = new Error();
        error.setError("invalid user");
        return ResponseEntity.badRequest().body(error);
    }
    @PostMapping("/user/add")
    public ResponseEntity addUserOnDataBase(@RequestBody @Valid User user) throws JsonProcessingException {
        UserPO userPO = new UserPO();
        userPO.setName(user.getName());
        userPO.setAge(user.getAge());
        userPO.setGender(user.getGender());
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
    @DeleteMapping("/user/{id}")
    public ResponseEntity deleteRsEvent(@PathVariable int  id) throws JsonProcessingException {
        if(userRepository.findById(id).isPresent()){
            userRepository.deleteById(id);
        }
        return ResponseEntity.ok().build();
    }


}

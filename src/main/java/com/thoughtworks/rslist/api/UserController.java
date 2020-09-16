package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
@RestController
public class UserController {
    List<User> userList = new ArrayList<User>();
    @PostMapping("/user")
    public ResponseEntity addUser(@RequestBody @Valid User user) throws JsonProcessingException {
        userList.add(user);
        return ResponseEntity.created(null)
                .header("index",String.valueOf(userList.size()-1)).build();
    }
    @GetMapping("/user")
    public ResponseEntity getUserList(){
        return ResponseEntity.ok(userList);
    }


}

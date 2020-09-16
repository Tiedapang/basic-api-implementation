package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity userExceptionHandler(MethodArgumentNotValidException e){
        Error error = new Error();
        error.setError("invalid user");
        return ResponseEntity.badRequest().body(error);
    }


}

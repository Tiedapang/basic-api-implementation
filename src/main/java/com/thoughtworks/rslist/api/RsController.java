package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class RsController {
  private List<RsEvent> rsList =initRsEventList();
  UserController userController = new UserController();
  private List<RsEvent> initRsEventList() {
    User user = new User("xiaowang","female",19,"a@thoughtworks.com","18888888888");
    List<RsEvent> rsEventList = new ArrayList<>();
    rsEventList.add(new RsEvent("猪肉涨价了","经济",user));
    rsEventList.add(new RsEvent("小学生放假了","社会时事",user));
    rsEventList.add(new RsEvent("特朗普辞职了","政治",user));
    return rsEventList;
  }


  @GetMapping("/rs/{index}")
  public ResponseEntity getOneRSEvent(@PathVariable int index) throws JsonProcessingException {
    if(index <= 0 || index>rsList.size()-1){
      throw new RsEventNotValidException("invalid index");
    }
    return ResponseEntity.ok(rsList.get(index - 1));
  }


  @GetMapping("/rs/list")
  public ResponseEntity getList(@RequestParam(required = false) Integer start,@RequestParam(required = false) Integer end) throws JsonProcessingException {
    if(start == null || end == null){
      return ResponseEntity.ok(rsList);
    }else if(start < 0 || end>rsList.size()-1){
      throw new RsEventNotValidException("invalid request param");
    }
    return ResponseEntity.ok(rsList.subList(start - 1,end));
  }
  @PostMapping("/rs/addEvent")
  public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent  rsEvent) throws JsonProcessingException {
    User user = rsEvent.getUser();
    if(isExistUserName(user)){
      rsList.add(rsEvent);
    }else{
      userController.addUser(user);
      rsList.add(rsEvent);
    }
  return ResponseEntity.created(null).header("index",String.valueOf(rsList.size()-1)).build();

  }

  private boolean isExistUserName(User user) {
    return userController.userList.contains(user);
  }

  @DeleteMapping("/rs/event")
  public ResponseEntity deleteRsEvent(@RequestBody String  deleteID) throws JsonProcessingException {
    rsList.remove(Integer.parseInt(deleteID)-1);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/rs/updateEvent")
  public ResponseEntity updateRsEvent(@RequestBody Map params) throws JsonProcessingException {
    int id = (int) params.get("id");
    String eventName = (String) params.get("eventName");
    if(eventName!=null){
      rsList.get(id-1).setEventName(eventName);
    }
    String keyWord = (String) params.get("keyWord");
    if(keyWord!=null){
      rsList.get(id-1).setKeyWord(keyWord);
    }
    return ResponseEntity.created(null).header("index",String.valueOf(id)).build();
  }
  @GetMapping("/get/users")
  public ResponseEntity getAllUsers() throws JsonProcessingException {
    return ResponseEntity.ok(userController.userList);
  }
  @ExceptionHandler(RsEventNotValidException.class)
  public ResponseEntity rsExceptionHandler(RsEventNotValidException e){
    Error error = new Error();
    error.setError(e.getMessage());
    return ResponseEntity.badRequest().body(error);
  }

}

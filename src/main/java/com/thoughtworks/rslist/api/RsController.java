package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.springframework.http.HttpStatus;
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
  public ResponseEntity getOneRSEvent(@PathVariable int index){

    return ResponseEntity.ok(rsList.get(index - 1));
  }
  @GetMapping("/rs/list")
  public ResponseEntity getList(@RequestParam(required = false) Integer start,@RequestParam(required = false) Integer end){
    if(start == null || end == null){
      return ResponseEntity.ok(rsList);
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

}

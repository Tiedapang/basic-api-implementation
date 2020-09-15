package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class RsController {
  private List<RsEvent> rsList =initRsEventList();

  private List<RsEvent> initRsEventList() {
    List<RsEvent> rsEventList = new ArrayList<>();
    rsEventList.add(new RsEvent("猪肉涨价了","经济"));
    rsEventList.add(new RsEvent("小学生放假了","社会时事"));
    rsEventList.add(new RsEvent("特朗普辞职了","政治"));
    return rsEventList;
  }


  @GetMapping("/rs/{index}")
  public RsEvent getOneRSEvent(@PathVariable int index){
    return rsList.get(index - 1);
  }
  @GetMapping("/rs/list")
  public List<RsEvent> getList(@RequestParam(required = false) Integer start,@RequestParam(required = false) Integer end){
    if(start == null || end == null){
      return rsList;
    }
    return rsList.subList(start - 1,end);
  }
  @PostMapping("/rs/addEvent")
  public void addRsEvent(@RequestBody String  rsEvent) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    RsEvent event = objectMapper.readValue(rsEvent, RsEvent.class);
    rsList.add(event);
  }

  @PostMapping("/rs/deleteEvent")
  public void deleteRsEvent(@RequestBody String  deleteID) throws JsonProcessingException {
    rsList.remove(Integer.parseInt(deleteID)-1);
  }

  @PostMapping("/rs/updateEvent")
  public void updateRsEvent(@RequestBody Map params) throws JsonProcessingException {
    int id = (int) params.get("id");
    String eventName = (String) params.get("eventName");
    if(eventName!=null){
      rsList.get(id-1).setEventName(eventName);
    }
    String keyWord = (String) params.get("keyWord");
    if(keyWord!=null){
      rsList.get(id-1).setKeyWord(keyWord);
    }
  }

}

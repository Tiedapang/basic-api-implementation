package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
}

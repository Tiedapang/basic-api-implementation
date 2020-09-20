package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;


import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class RsController {

  private Logger logger = LoggerFactory.getLogger(RsController.class);
  UserController userController = new UserController();
  @Autowired
  UserRepository userRepository;
  @Autowired
  RsEventRepository rsEventRepository;
  @Autowired
  VoteRepository voteRepository;



  @GetMapping("/rs/{id}")
  public ResponseEntity getOneRSEvent(@PathVariable int id) throws JsonProcessingException {

    if(id <= 0 ){
      throw new RsEventNotValidException("invalid index");
    }
    Optional<RsEventPO> rsEventPO =  rsEventRepository.findById(id);
    if(rsEventPO.isPresent()){
      RsEvent rsEvent = new RsEvent(rsEventPO.get().getEventName(),rsEventPO.get().getKeyWord(),rsEventPO.get().getVoteNum());
      return ResponseEntity.ok(rsEvent);
    }else{
      throw new RsEventNotValidException("invalid index");
    }

  }


  @GetMapping("/rs/list")
  public ResponseEntity getList() throws JsonProcessingException {
    List<RsEventPO> rsEventPOS = rsEventRepository.findAll();
    List<RsEvent> rsEvents = new ArrayList<RsEvent>();
    for(int i = 0; i < rsEventPOS.size() ;i ++){
      rsEvents.add(new RsEvent(rsEventPOS.get(i).getEventName(),rsEventPOS.get(i).getKeyWord(),rsEventPOS.get(i).getVoteNum()));
    }
    return ResponseEntity.ok(rsEvents);
  }

  @PostMapping("/rs")
  public ResponseEntity addRsEvent(@RequestBody @Valid RsEvent  rsEvent) throws JsonProcessingException {
    Optional<UserPO> userPO = userRepository.findById(rsEvent.getUserId());
    if(userPO.isPresent()){
      rsEventRepository.save(RsEventPO.builder().eventName(rsEvent.getEventName()).keyWord(rsEvent.getKeyWord()).userPO(userPO.get()).build());
      return ResponseEntity.created(null).build();
    }else{
      return ResponseEntity.badRequest().build();
    }


  }



  @DeleteMapping("/rs")
  public ResponseEntity deleteRsEvent(@RequestBody String  deleteID) throws JsonProcessingException {
    rsEventRepository.deleteById(Integer.parseInt(deleteID));
    return ResponseEntity.ok().build();
  }

  @PatchMapping("/rs/{rsEventId}")
  public ResponseEntity should_update_rsEvent(@PathVariable int rsEventId, @RequestBody @Valid RsEvent rsEvent) {
    RsEventPO rsEventPO = rsEventRepository.findById(rsEventId).get();
    if (rsEventPO.getUserPO().getId() == rsEvent.getUserId()) {
      if (rsEvent.getEventName() != null) {
        rsEventPO.setEventName(rsEvent.getEventName());
      }
      if (rsEvent.getKeyWord() != null) {
        rsEventPO.setKeyWord(rsEvent.getKeyWord());
      }
      rsEventRepository.save(rsEventPO);
      return ResponseEntity.ok().build();
    }else {
      return ResponseEntity.badRequest().build();
    }

  }

  @ExceptionHandler({RsEventNotValidException.class,MethodArgumentNotValidException.class})
  public ResponseEntity rsExceptionHandler(Exception e){
    String errorMsg ;
    if(e instanceof MethodArgumentNotValidException){
      errorMsg = "invalid param";
    }else{
      errorMsg = e.getMessage();
    }
    logger.error(errorMsg);
    Error error = new Error();
    error.setError(errorMsg);
    return ResponseEntity.badRequest().body(error);
  }

  @PostMapping("/rs/vote/{rsEventId}")
  public ResponseEntity voteForRsEvent(@PathVariable Integer rsEventId,@RequestBody String jsonString) throws JsonProcessingException {
      ObjectMapper objectMapper = new ObjectMapper();
      Map voteMap = objectMapper.readValue(jsonString,Map.class);
      Vote vote = Vote.builder().voteNum((int)voteMap.get("voteNum")).voteTime((LocalDateTime)voteMap.get("voteTime")).userId(((int)voteMap.get("userId"))).build();

      Optional<RsEventPO> rsEventPO = rsEventRepository.findById(rsEventId);
    if(rsEventPO.isPresent()){
      UserPO userPO = userRepository.findById(vote.getUserId()).get();
      if(userPO.getVoteNmu()>vote.getVoteNum()){
        userPO.setVoteNmu(userPO.getVoteNmu()-vote.getVoteNum());
        userRepository.save(userPO);
        rsEventPO.get().setVoteNum(rsEventPO.get().getVoteNum()+vote.getVoteNum());
        rsEventRepository.save(rsEventPO.get());
        voteRepository.save(VotePO.builder().voteNum(vote.getVoteNum()).voteTime(vote.getVoteTime()).build());
        return ResponseEntity.ok().build();
      }
    }
    return ResponseEntity.ok().build();

  }


}

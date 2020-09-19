package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class VoteController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    VoteRepository voteRepository;

    @GetMapping("/findVote")
    public ResponseEntity<List<Vote>> findVoteByStartTimeAndEndTime(@RequestParam String startTime, @RequestParam String endTime) {
        LocalDateTime startTimeLocal = forMateStringToLcalTimeDate(startTime);
        LocalDateTime endTimeLocal = forMateStringToLcalTimeDate(endTime);
        List<VotePO> votePOS = voteRepository.findAllFromStartTimeToEndTime(startTimeLocal,endTimeLocal);
        List<Vote> votes = votePOS.stream()
                .map(item -> Vote.builder()
                        .rsEventId(item.getRsEvent().getId())
                        .userId(item.getUserPO().getId())
                        .voteNum(item.getVoteNum())
                        .build()
                ).collect(Collectors.toList());
        return ResponseEntity.ok(votes);


    }

    private LocalDateTime forMateStringToLcalTimeDate(String time) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(time,dateTimeFormatter);
        return localDateTime;
    }


}

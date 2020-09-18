package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VoteControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    VoteRepository voteRepository;
    UserPO userPO;
    RsEventPO rsEventPO;
    VotePO votePO;
    @BeforeEach
    public void setUp(){
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        voteRepository.deleteAll();

    }
    @Test
    public void should_add_Vote_on_datBase(){
        userPO = userRepository.save(UserPO.builder().email("a@b.com").age(19).gender("female")
                .phone("18888888888").name("ann").voteNmu(10).build());
        rsEventPO = rsEventRepository.save(RsEventPO.builder().eventName("testVoteController")
                .keyWord("test").userPO(userPO).build());
        votePO = VotePO.builder().rsEvent(rsEventPO).voteNum(5).userPo(userPO).voteTime(LocalDateTime.now()).build();
        VotePO newVotePo = voteRepository.save(votePO);
        assertEquals(5,newVotePo.getVoteNum());

    }


}
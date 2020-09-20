package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.domain.Vote;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RsControllerTest {
    @Autowired
    MockMvc mocMvc ;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    UserPO userPo;
    RsEventPO rsEventPO1;
    RsEventPO rsEventPO2;
    @BeforeEach
    public void setUp(){
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        userPo = UserPO.builder().email("aaa@b.com").age(25).gender("female")
                .phone("18888888888").name("wangMing").voteNmu(10).build();
        userRepository.save(userPo);
        rsEventPO1 = RsEventPO.builder().eventName("testForGet1").keyWord("ok").userPO(userPo).build();
        rsEventRepository.save(rsEventPO1);
        rsEventPO2 = RsEventPO.builder().eventName("testForGet2").keyWord("ok").userPO(userPo).build();
        rsEventRepository.save(rsEventPO2);
    }
    @Order(1)
    @Test
    public void should_get_one_rs_event() throws Exception {

        mocMvc.perform(get("/rs/"+rsEventPO1.getId()))
                .andExpect(jsonPath("$.eventName",is("testForGet1")))
                .andExpect(jsonPath("$.keyWord",is("ok")))
                .andExpect(status().isOk());
    }
    @Order(2)
    @Test
    public void should_get_rs_event_between() throws Exception {
        mocMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].eventName",is("testForGet1")))
                .andExpect(jsonPath("$[0].keyWord",is("ok")))
                .andExpect(jsonPath("$[1].eventName",is("testForGet2")))
                .andExpect(jsonPath("$[1].keyWord",is("ok")))
                .andExpect(status().isOk());

    }
    @Order(3)
    @Test
    public void should_add_rs_event_when_user_exist() throws Exception {
        UserPO saveUser = userRepository.save(UserPO.builder().email("aaa@b.com").age(25).gender("female")
                .phone("18888888888").name("thj").voteNmu(10).build());
        String jsonString = "{\"eventName\":\"updateDataBase\",\"keyWord\":\"opera\",\"userId\": " + saveUser.getId() + "}";
        mocMvc.perform(post("/rs").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<RsEventPO> all = rsEventRepository.findAll();
        assertNotNull(all);
        assertEquals(1,all.size());

    }
    @Order(4)
    @Test
    public void should_add_rs_event_when_user_not_exist() throws Exception {
        String jsonString = "{\"eventName\":\"updateDataBase\",\"opera\":\"经济\",\"userId\": 300}";
        mocMvc.perform(post("/rs").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Order(4)
    @Test
    public void should_delete_rs_event_by_id() throws Exception {
        int deleteId = rsEventPO1.getId();
        mocMvc.perform(delete("/rs").content(String.valueOf(deleteId)))
                .andExpect(status().isOk());
        Optional<RsEventPO> newRsEvent = rsEventRepository.findById(deleteId);
        assertEquals(false, newRsEvent.isPresent());


    }
    @Order(5)
    @Test
    public void should_update_rsEvent_when_rsEventId_match_userId() throws Exception {
        UserPO userPo = UserPO.builder().email("aaa@b.com").age(25).gender("female")
                .phone("18888888888").name("wangMing").voteNmu(10).build();
        userRepository.save(userPo);
        RsEventPO rsPO = RsEventPO.builder().eventName("adadfsf").keyWord("sdsfd").userPO(userPo).build();
        rsEventRepository.save(rsPO);
        String jsonString = "{\"eventName\":\"aderrrrrr\",\"keyWord\":\"sdddf\",\"userId\": " + userPo.getId() + "}";
        mocMvc.perform(patch("/rs/{rsEventId}",String.valueOf(rsPO.getId()))
                .content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        RsEventPO newRsEvent = rsEventRepository.findById(rsPO.getId()).get();
        assertEquals("aderrrrrr", newRsEvent.getEventName());
        assertEquals("sdddf", newRsEvent.getKeyWord());
    }



    @Order(10)
    @Test
    public void should_check_vote_num_to_judge_user_can_vote() throws Exception {

        UserPO userPo = UserPO.builder().email("aaa@b.com").age(25).gender("female")
                .phone("18888888888").name("wangMing").voteNmu(10).build();
        userRepository.save(userPo);
        RsEventPO rsPO = RsEventPO.builder().eventName("adadfsf").keyWord("sdsfd").userPO(userPo).build();
        rsEventRepository.save(rsPO);
        String jsonString = "{\"voteNum\":5,\"voteTime\":"+LocalDateTime.now()+",\"userId\": " + userPo.getId() + "}";
        mocMvc.perform(post("/rs/vote/"+rsPO.getId())
                .content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

}
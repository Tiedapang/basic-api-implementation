package com.thoughtworks.rslist.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    @BeforeEach
    public void setUp(){
        userRepository.deleteAll();
        rsEventRepository.deleteAll();

    }

    @Order(1)
    @Test
    public void should_register_user() throws Exception {
        User user = new User("thj","female",20,"abc@163.com","18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        List<UserPO> userPOS =userRepository.findAll();
        assertEquals(1,userPOS.size());
        assertEquals("thj",userPOS.get(0).getName());
    }
    @Order(2)
    @Test
    public void name_shoule_less_than_8() throws Exception {
        User user = new User("tiehuijie1","female",20,"abc@163.com","1234567891");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Order(3)
    @Test
    public void age_shoule_between_18_and_100() throws Exception {
        User user = new User("thj","female",13,"abc@163.com","18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Order(4)
    @Test
    public void email_shoule_suit_fomate() throws Exception {
        User user = new User("thj","female",20,"abc163.com","1234567891");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Order(5)
    @Test
    public void phone_shoule_suit_fomate() throws Exception {
        User user = new User("thj","female",20,"abc@163.com","1567891");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Order(6)
    @Test
    public void should_throws_method_argument_not_valid_exception() throws Exception {
        User user = new User("thjdfdgdgdfgg","female",20,"abc@163.com","1567891");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("error",is("invalid user")));
    }

    @Test
    @Order(7)
    void should_add_user_into_database() throws Exception {
        UserPO userPo = UserPO.builder().email("aaa@b.com").age(25).gender("female")
                .phone("18888888888").name("thj").voteNmu(10).build();
        UserPO save = userRepository.save(userPo);
        assertEquals(userPo,save);
    }
    @Order(8)
    @Test
    public void should_get_user_by_id_from_dataBase() throws Exception {
        int userID = 8;
        mockMvc.perform(get("/user/"+userID)) .andExpect(status().isOk());

    }
    @Order(9)
    @Test
    public void should_delete_user_by_id() throws Exception {
        UserPO userPo = UserPO.builder().email("aaa@b.com").age(25).gender("female")
                .phone("18888888888").name("thj").voteNmu(10).build();
        userRepository.save(userPo);
        RsEventPO rsPO = RsEventPO.builder().eventName("adadfsf").keyWord("sdsfd").userPO(userPo).build();
        rsEventRepository.save(rsPO);
        mockMvc.perform(delete("/user/{id}",userPo.getId()))
                .andExpect(status().isOk());
        assertEquals(0,userRepository.findAll().size());
        assertEquals(0,rsEventRepository.findAll().size());
    }
}
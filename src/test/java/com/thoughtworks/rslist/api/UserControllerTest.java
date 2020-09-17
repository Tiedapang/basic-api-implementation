package com.thoughtworks.rslist.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.UserRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
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
    @Order(1)
    @Test
    public void should_register_user() throws Exception {
        User user = new User("thj","female",20,"abc@163.com","18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("index","0"));
        mockMvc.perform(get("/user"))
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].user_name",is("thj")))
                .andExpect(jsonPath("$[0].user_gender",is("female")))
                .andExpect(jsonPath("$[0].user_age",is(20)))
                .andExpect(jsonPath("$[0].user_email",is("abc@163.com")))
                .andExpect(jsonPath("$[0].user_phone",is("18888888888")))
                .andExpect(status().isOk());
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
        User user = new User("thj","female",13,"abc@163.com","1234567891");
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
    public void should_register_user_by_dataBase() throws Exception {
        User user = new User("thj","female",20,"abc@163.com","18888888888");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/user/dataBase").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<UserPO> allUsers = userRepository.findAll();
        assertEquals(1,allUsers.size());
        assertEquals("thj",allUsers.get(0).getName());
        assertEquals("female",allUsers.get(0).getGender());
        assertEquals(20,allUsers.get(0).getAge());
        assertEquals("abc@163.com",allUsers.get(0).getEmail());
        assertEquals("18888888888",allUsers.get(0).getPhone());
    }


}
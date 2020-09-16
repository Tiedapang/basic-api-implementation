package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
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

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RsControllerTest {
    @Autowired
    MockMvc mocMvc ;
    @Order(1)
    @Test
    public void should_get_one_rs_event() throws Exception {

        mocMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName",is("猪肉涨价了")))
                .andExpect(jsonPath("$.keyWord",is("经济")))
                .andExpect(jsonPath("$",not(hasKey("user"))))
                .andExpect(status().isOk());
        mocMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName",is("小学生放假了")))
                .andExpect(jsonPath("$.keyWord",is("社会时事")))
                .andExpect(jsonPath("$",not(hasKey("user"))))
                .andExpect(status().isOk());
        mocMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName",is("特朗普辞职了")))
                .andExpect(jsonPath("$.keyWord",is("政治")))
                .andExpect(jsonPath("$",not(hasKey("user"))))
                .andExpect(status().isOk());
    }
    @Order(2)
    @Test
    public void should_get_rs_event_between() throws Exception {
        mocMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].eventName",is("猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyWord",is("经济")))
                .andExpect(jsonPath("$[0]",not(hasKey("user"))))
                .andExpect(jsonPath("$[1].eventName",is("小学生放假了")))
                .andExpect(jsonPath("$[1].keyWord",is("社会时事")))
                .andExpect(jsonPath("$[1]",not(hasKey("user"))))
                .andExpect(status().isOk());

    }
    @Order(3)
    @Test
    public void should_add_rs_event() throws Exception {
        User user = new User("xiaowang","female",19,"a@thoughtworks.com","18888888888");
        RsEvent rsEvent = new RsEvent("股市跌了","财经",user);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mocMvc.perform(post("/rs/addEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("index","3"));
        mocMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$",hasSize(4)))
                .andExpect(jsonPath("$[0].eventName",is("猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyWord",is("经济")))
                .andExpect(jsonPath("$[1].eventName",is("小学生放假了")))
                .andExpect(jsonPath("$[1].keyWord",is("社会时事")))
                .andExpect(jsonPath("$[2].eventName",is("特朗普辞职了")))
                .andExpect(jsonPath("$[2].keyWord",is("政治")))
                .andExpect(jsonPath("$[3].eventName",is("股市跌了")))
                .andExpect(jsonPath("$[3].keyWord",is("财经")))
                .andExpect(status().isOk());
    }
    @Order(4)
    @Test
    public void should_delete_rs_event_by_id() throws Exception {
        String deleteId = "1" ;
        mocMvc.perform(delete("/rs/event").content(deleteId))
                .andExpect(status().isCreated());
        mocMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].eventName",is("小学生放假了")))
                .andExpect(jsonPath("$[0].keyWord",is("社会时事")))
                .andExpect(jsonPath("$[1].eventName",is("特朗普辞职了")))
                .andExpect(jsonPath("$[1].keyWord",is("政治")))
                .andExpect(status().isOk());
    }
    @Order(5)
    @Test
    public void should_update_eventList_by_conditions() throws Exception {
        JSONObject jsonString = new JSONObject();
        jsonString.put("id",1);
        jsonString.put("eventName","这是修改后的名称");
        jsonString.put("keyWord","修改");
        mocMvc.perform(post("/rs/updateEvent").content(jsonString.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("index","1"));
        mocMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].eventName",is("这是修改后的名称")))
                .andExpect(jsonPath("$[0].keyWord",is("修改")))
                .andExpect(jsonPath("$[1].eventName",is("小学生放假了")))
                .andExpect(jsonPath("$[1].keyWord",is("社会时事")))
                .andExpect(jsonPath("$[2].eventName",is("特朗普辞职了")))
                .andExpect(jsonPath("$[2].keyWord",is("政治")))
                .andExpect(status().isOk());
    }
    @Order(6)
    @Test
    public void should_update_eventList_by_only_change_eventName() throws Exception {
        JSONObject jsonString = new JSONObject();
        jsonString.put("id",1);
        jsonString.put("eventName","这是修改后的名称");
        mocMvc.perform(post("/rs/updateEvent").content(jsonString.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("index","1"));
        mocMvc.perform(get("/rs/list"))
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].eventName",is("这是修改后的名称")))
                .andExpect(jsonPath("$[0].keyWord",is("经济")))
                .andExpect(jsonPath("$[1].eventName",is("小学生放假了")))
                .andExpect(jsonPath("$[1].keyWord",is("社会时事")))
                .andExpect(jsonPath("$[2].eventName",is("特朗普辞职了")))
                .andExpect(jsonPath("$[2].keyWord",is("政治")))
                .andExpect(status().isOk());
    }


}
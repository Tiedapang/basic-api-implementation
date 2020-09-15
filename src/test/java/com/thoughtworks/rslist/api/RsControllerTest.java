package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.domain.RsEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RsControllerTest {
    @Autowired
    MockMvc mocMvc ;

    @Test
    public void should_get_one_rs_event() throws Exception {
        mocMvc.perform(get("/rs/1"))
                .andExpect(jsonPath("$.eventName",is("猪肉涨价了")))
                .andExpect(jsonPath("$.keyWord",is("经济")))
                .andExpect(status().isOk());
        mocMvc.perform(get("/rs/2"))
                .andExpect(jsonPath("$.eventName",is("小学生放假了")))
                .andExpect(jsonPath("$.keyWord",is("社会时事")))
                .andExpect(status().isOk());
        mocMvc.perform(get("/rs/3"))
                .andExpect(jsonPath("$.eventName",is("特朗普辞职了")))
                .andExpect(jsonPath("$.keyWord",is("政治")))
                .andExpect(status().isOk());
    }
    @Test
    public void should_get_rs_event_between() throws Exception {
        mocMvc.perform(get("/rs/list?start=1&end=2"))
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].eventName",is("猪肉涨价了")))
                .andExpect(jsonPath("$[0].keyWord",is("经济")))
                .andExpect(jsonPath("$[1].eventName",is("小学生放假了")))
                .andExpect(jsonPath("$[1].keyWord",is("社会时事")))
                .andExpect(status().isOk());
    }
    @Test
    public void should_add_rs_event() throws Exception {
        RsEvent rsEvent = new RsEvent("股市跌了","财经");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(rsEvent);
        mocMvc.perform(post("/rs/addEvent").content(jsonString).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import java.util.logging.Logger;

public class RsEvent {
    private String eventName;
    private String keyWord;
    @JsonIgnore
    private int userId;
    private int voteNum;
    public RsEvent(){

    }
    public RsEvent(String eventName, String keyWord, int voteNum) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.voteNum = voteNum;
    }
    public RsEvent(String eventName, String keyWord, int userId, int voteNum) {
        this.eventName = eventName;
        this.keyWord = keyWord;
        this.userId = userId;
        this.voteNum = voteNum;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }
}

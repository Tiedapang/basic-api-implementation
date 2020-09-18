package com.thoughtworks.rslist.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "user")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPO {

    @Id
    @GeneratedValue()
    private int id;
    private String name;
    private Integer  age;
    private String gender;
    private String email;
    private String phone;
    private int voteNmu;
    @OneToMany(cascade = CascadeType.REMOVE , mappedBy = "userPO")
    private List<RsEventPO> rsEventPOs;
    @OneToMany(cascade = CascadeType.REMOVE , mappedBy = "userPo")
    private List<VotePO> votePOS;

//    public UserPO(){}
//
//    public UserPO(String name, Integer age, String gender, String email, String phone,int voteNmu) {
//        this.name = name;
//        this.age = age;
//        this.gender = gender;
//        this.email = email;
//        this.phone = phone;
//        this.voteNmu = voteNmu;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public Integer getAge() {
//        return age;
//    }
//
//    public void setAge(Integer age) {
//        this.age = age;
//    }
//
//    public String getGender() {
//        return gender;
//    }
//
//    public void setGender(String gender) {
//        this.gender = gender;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public int getVoteNmu() {
//        return voteNmu;
//    }
//
//    public void setVoteNmu(int voteNmu) {
//        this.voteNmu = voteNmu;
//    }
//
//    public List<RsEventPO> getRsEventPOs() {
//        return rsEventPOs;
//    }
//
//    public void setRsEventPOs(List<RsEventPO> rsEventPOs) {
//        this.rsEventPOs = rsEventPOs;
//    }
}

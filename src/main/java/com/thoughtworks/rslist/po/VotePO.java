package com.thoughtworks.rslist.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "vote")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VotePO {
    @Id
    @GeneratedValue()
    private int id;
    private int voteNum;
    private LocalDateTime voteTime;
    @ManyToOne
    @JoinColumn(name = "rs_event_id")
    private RsEventPO rsEvent;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserPO userPO;

}

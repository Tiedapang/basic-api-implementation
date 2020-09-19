package com.thoughtworks.rslist.repository;



import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface VoteRepository extends PagingAndSortingRepository<VotePO,Integer> {
        @Override
        List<VotePO> findAll();
        @Query(value = "select v from VotePO v where v.voteTime >:startTimeLocal and v.voteTime < :endTimeLocal")
        List<VotePO> findAllFromStartTimeToEndTime(LocalDateTime startTimeLocal, LocalDateTime endTimeLocal);
}

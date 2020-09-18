package com.thoughtworks.rslist.repository;



import com.thoughtworks.rslist.po.RsEventPO;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.po.VotePO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface VoteRepository extends CrudRepository<VotePO,Integer> {
        @Override
        List<VotePO> findAll();

}

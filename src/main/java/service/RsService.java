package service;

import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VoteRepository;

public class RsService {
    private RsEventRepository rsEventRepository;
    private UserRepository userRepository;
    private VoteRepository voteRepository;

    public RsService(RsEventRepository rsEventRepository, UserRepository userRepository, VoteRepository voteRepository) {
        this.rsEventRepository = rsEventRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public RsService() {
    }
}

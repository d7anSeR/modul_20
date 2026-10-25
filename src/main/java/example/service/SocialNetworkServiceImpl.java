package example.service;

import example.model.SocialNetwork;
import example.repository.SocialNetworkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SocialNetworkServiceImpl implements SocialNetworkService {

    @Autowired
    private SocialNetworkRepository socialNetworkRepository;

    @Override
    public void createSocialNetwork(SocialNetwork socialNetwork) {
        socialNetworkRepository.save(socialNetwork);
    }

    @Override
    public Optional<SocialNetwork> getSocialNetwork(Long id) {
        return socialNetworkRepository.findById(id);
    }

    @Override
    public List<SocialNetwork> getAllSocialNetworks() {
        return socialNetworkRepository.findAll();
    }

    @Override
    public boolean deleteSocialNetwork(Long id) {
        return socialNetworkRepository.deleteById(id);
    }
}
